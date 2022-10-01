package com.tiroteio.enemys;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tiroteio.Tiroteio;
import com.tiroteio.balas.Bala;
import com.tiroteio.screens.PlayScreen;

public abstract class Enemy extends Sprite implements Disposable{
	
	public enum State{FALLING,JUMPING,STANDING,RUNNING}
    protected World wolrd;
    protected PlayScreen screen;
    public Body  b2body;
    public State currentState;
	public State priviosState;
	protected float stateTimer;
	protected Animation<TextureRegion> enemyRun;
	protected Animation<TextureRegion> enemyJump;
	protected Animation<TextureRegion> enemyStand;
	protected Animation<TextureRegion> enemyDead;
	protected Array<TextureRegion> frames;
	protected Sound passos;
	protected boolean ligaSom;
	protected boolean runningRigth;
	protected AssetManager manager;
	protected boolean setToDestroy;
	protected boolean bodyDestroy;
	protected boolean destroyed;
	protected FixtureDef fdef;
    protected Vector2 velocity;
    protected float life ;
    public int contato;
    protected PolygonShape shape;
    public Array<Bala> tiros; 
    protected Vector2 pos;
    protected float milesegundo;
    protected float segundos;
    protected Sound tiroSom;
    protected int primeiroTiro;
    
	public Enemy(PlayScreen screen,float x,float y) {
		this.wolrd = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		
		defineEnemy();
		manager = new AssetManager();
		manager.load("audio/sounds/passos.ogg", Sound.class);
		manager.finishLoading();
		
		passos = manager.get("audio/sounds/passos.ogg", Sound.class);
		ligaSom = true;
		setToDestroy = false;
		bodyDestroy = false;
		destroyed = false;
		
		velocity = new Vector2(-1f,-2f);
		
		b2body.setActive(false);
		
		pos = new Vector2(b2body.getPosition().x + 22 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
		tiros = new Array<Bala>();
		
		contato = 0;
		
		manager = new AssetManager();
		manager.load("audio/sounds/tiro.ogg", Sound.class);
		manager.finishLoading();
		
		//configurando sons
		tiroSom = manager.get("audio/sounds/tiro.ogg", Sound.class);
		
		primeiroTiro = 0;
		
	}
	
	public TextureRegion getFrames(float dt) {
		currentState = getState(); 
		
		TextureRegion region;
		switch (currentState) {
		case JUMPING:
			region = (TextureRegion) enemyJump.getKeyFrame(stateTimer,true);
			break;
		case RUNNING:
			region = (TextureRegion) enemyRun.getKeyFrame(stateTimer,true);
			
			if(ligaSom&&b2body.isActive()) {
				passos.loop(0.4f);
				ligaSom = false;
			}
			break;
		case FALLING:
		case STANDING:
			
		default:
			region = (TextureRegion) enemyStand.getKeyFrame(stateTimer,true);
			
			break;
		}
		
		if((b2body.getLinearVelocity().x < -0.1 || !runningRigth) && (!region.isFlipX())) {
			region.flip(true, false);
			runningRigth = false;
			
		}
		if((b2body.getLinearVelocity().x > 0.1 || runningRigth) && (region.isFlipX())) {
			region.flip(true, false);
			runningRigth = true;
			
		}
		
		stateTimer = currentState == priviosState ? stateTimer + dt : 0;
		priviosState = currentState;
		
		if(b2body.getLinearVelocity().x > -0.1 && b2body.getLinearVelocity().x < 0.1 || currentState == State.JUMPING) {
			passos.stop();
			ligaSom = true;
		}
		
		return region;
		
	}
	
	public State getState() {
		if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y  < 0 && priviosState == State.JUMPING) )
			return State.JUMPING;
		else if(b2body.getLinearVelocity().y < -2)
			return State.FALLING;
		else if(b2body.getLinearVelocity().x < 0. || b2body.getLinearVelocity().x > 0.1 )
			return State.RUNNING;
		else 
			return State.STANDING;
	}
	
	public abstract void update(float dt);
	
	public abstract void defineEnemy();
	
	public abstract void hit();
	
	public void setContato(Rectangle r1) {                         
		
		if((r1.y / Tiroteio.PPM) + (r1.height / Tiroteio.PPM) > b2body.getPosition().y) {      
		    //interceção na esquerda do inimigo e direita do objeto +
			
			if(r1.x / Tiroteio.PPM < b2body.getPosition().x)
				contato = 1;
			
			if(r1.x / Tiroteio.PPM > b2body.getPosition().x)
				contato = 2;
		}
	}
	
	public void tiro( float dt ) {
		
		milesegundo += dt;
		//posiciona a bala
		if(runningRigth) {
			pos.set(b2body.getPosition().x + 18 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
			
		}
		else {
			pos.set(b2body.getPosition().x - 18 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
		}
		//faz um delay entre os tiros
		if(milesegundo >= 1)
			segundos++;
			
		if(segundos>30f||primeiroTiro==0) {
			tiros.add(new Bala(screen, pos.x, pos.y, runningRigth ? true : false,Tiroteio.BALA_ENEMY_BIT));
			tiroSom.play(0.09f);
			segundos=0;
			primeiroTiro++;
		}
		
	}	
	
	@Override
	public void draw(Batch batch) {
		if(!destroyed)
			super.draw(batch);
		
		for (Bala bala : tiros) {
			if(!bala.isDestroyed())
				bala.draw(batch);
		}
		
	}
	
	@Override
	public void dispose() {
		manager.dispose();
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
}
