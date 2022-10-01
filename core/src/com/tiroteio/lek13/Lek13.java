package com.tiroteio.lek13;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tiroteio.Tiroteio;
import com.tiroteio.balas.Bala;
import com.tiroteio.screens.PlayScreen;

public class Lek13 extends Sprite implements Disposable {

	public enum State{FALLING,JUMPING,SHOOTING_JUMPING,STANDING,RUNNING,SHOOTING,SHOOTING_RUNNING,DEAD}
	
	public World world;
	public Body b2body;
	public State currentState;
	public State priviosState;
	private TextureRegion texture;
	private Animation<TextureRegion> lekRun;
	private Animation<TextureRegion> lekJump;
	private Animation<TextureRegion> lekStand;
	private Animation<TextureRegion> lekShooting;
	private Animation<TextureRegion> lekShooting_running;
	private Animation<TextureRegion> lekShooting_jumping;
	private float stateTimer;
	private boolean runningRigth;
	private Sound passosSom;
	private Sound tiroSom;
	private Sound quedaSom;
	private AssetManager manager;
	private boolean ligaSomPassos;
	public Array<Bala> tiros; 
	private PlayScreen screen;
	private Vector2 pos;
	private boolean atirando;
	private float tempo = 0;
	private float espera = 0;
	private int vida; 
	private int  VIDA_INICIAL = 20 ; 
	private boolean setToDestroy;
	private boolean bodyDestroy;
	private boolean destroyed;
	private BodyDef bdef;
	private int contPulo = 0;
	
	public Lek13(PlayScreen screen) {
		
		super(new TextureRegion(new Texture("sprites/pp/Idle.png"),0,0, 128/4,48));
		
		this.screen = screen;
		
		this.world = screen.getWorld();
		
		currentState = State.STANDING;
		priviosState = State.STANDING;
		stateTimer = 0;
		runningRigth = true;
		
		Array <TextureRegion> frames = new Array<TextureRegion>();
		
		//Animação parado
		texture = new TextureRegion(new Texture("sprites/pp/Idle.png"));
		for(int i = 0;i<4;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/4)*i
					,0,
					texture.getTexture().getWidth()/4,
					texture.getTexture().getHeight()));
		}
		
		lekStand = new Animation<>(0.1f, frames);
		frames.clear();
		
		//Animação Correndo
		texture = new TextureRegion(new Texture("sprites/pp/Running.png"));
		for(int i = 0;i<6;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/6)*i
					,0,
					texture.getTexture().getWidth()/6,
					texture.getTexture().getHeight()));
		}
		
		lekRun = new Animation<>(0.1f, frames);
		frames.clear();
		
		//Animação pulando
		texture = new TextureRegion(new Texture("sprites/pp/Jump.png"));
		for(int i = 0;i<1;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/1)*i
					,0,
					texture.getTexture().getWidth()/1,
					texture.getTexture().getHeight()));
		}
		
		lekJump = new Animation<>(0.1f, frames);
		frames.clear();
		
		//Animação atirando
		texture = new TextureRegion(new Texture("sprites/pp/Shooting.png"));
		for(int i = 0;i<3;i++) {
		frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/3)*i
					,0,
					texture.getTexture().getWidth()/3,
					texture.getTexture().getHeight()));
		}
		//Animação atirando e andando
		lekShooting = new Animation<>(0.01f, frames);
		frames.clear();
		
		texture = new TextureRegion(new Texture("sprites/pp/Shooting_running.png"));
		for(int i = 0;i<6;i++) {
		frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/6)*i
					,0,
					texture.getTexture().getWidth()/6,
					texture.getTexture().getHeight()));
		}
		
		lekShooting_running = new Animation<>(0.1f, frames);
		frames.clear();
		
		//Animação atirando e punado
		texture = new TextureRegion(new Texture("sprites/pp/Shooting_jumping.png"));
		for(int i = 0;i<3;i++) {
		frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/3)*i
					,0,
					texture.getTexture().getWidth()/3,
					texture.getTexture().getHeight()));
		}
		
		lekShooting_jumping = new Animation<>(0.1f, frames);
		frames.clear();
		
		defineLek13();
		
		//array dos balas
		tiros = new Array<Bala>();
		
		texture = new TextureRegion(getTexture(),32*6,24,32,48);
		setBounds(0, 0, 32/Tiroteio.PPM, 48/Tiroteio.PPM);
		setRegion(texture);
		
		//caregando sons
		manager = new AssetManager();
		manager.load("audio/sounds/passos.ogg", Sound.class);
		manager.load("audio/sounds/tiro.ogg", Sound.class);
		manager.load("audio/sounds/queda.ogg", Sound.class);
		manager.finishLoading();
		
		//configurando sons
		passosSom = manager.get("audio/sounds/passos.ogg", Sound.class);
		tiroSom = manager.get("audio/sounds/tiro.ogg", Sound.class);
		quedaSom = manager.get("audio/sounds/queda.ogg", Sound.class);
		ligaSomPassos = true;
		
		//posição da onde vai sair a bala
		pos = new Vector2(b2body.getPosition().x + 22 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
		
		vida = VIDA_INICIAL;
		screen.getHud().setVida((int)(vida*100f)/VIDA_INICIAL);
		
	}
	
	//update
	public void update(float dt) {
		//quando lek morer
		if(setToDestroy && !destroyed) {
			
				//if bodyDestroy for igual a true ele destroi o b2body
				//isso é só para o jogo não tenter destruir o b2body mais de uma vez
				if(bodyDestroy)
					world.destroyBody(b2body);
				
				//verifica qual o lado que o inigo esta
				//e vira a esprite para o lado atual
				if(!runningRigth && !isFlipX()) {
					flip(true, false);
				}
				bodyDestroy = false;
				destroyed = true;
				passosSom.stop();
				
		}
		//em quanto estiver vivo
		else {
			//configura a posição do b2body e acordo com o lado
			if(runningRigth) {
				setPosition(b2body.getPosition().x - getWidth() / 2,
						b2body.getPosition().y - getHeight() / 2 + 20 / Tiroteio.PPM );
				
				pos.set(b2body.getPosition().x + 18 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
				
			}
			else {
				setPosition(b2body.getPosition().x - getWidth() / 2 - 3 / Tiroteio.PPM ,
						b2body.getPosition().y - getHeight() / 2 + 20 / Tiroteio.PPM );
				
				pos.set(b2body.getPosition().x - 18 / Tiroteio.PPM, b2body.getPosition().y + 12 / Tiroteio.PPM);
			}
			//pra lek não sair da tela
			
			//atualisa as balas
			for (Bala bala : tiros) {
				if(!bala.isDestroyed())
					bala.update(dt);
				
				if(bala.isDestroyed()) {
					tiros.removeValue(bala, true);
				}
			}
			
			setRegion(getFrames(dt));
			}
		
			if(b2body.getPosition().y<0) {
				setToDestroy = true;
				bodyDestroy = true;
			}
	}
	
	//TextureRegion
	public TextureRegion getFrames(float dt) {
		currentState = getState(); 
		
		TextureRegion region;
		switch (currentState) {
		case JUMPING:
			region = (TextureRegion) lekJump.getKeyFrame(stateTimer,true);
			break;
		case SHOOTING_JUMPING:
			region = (TextureRegion) lekShooting_jumping.getKeyFrame(stateTimer,true);
			break;
		case RUNNING:
			region = (TextureRegion) lekRun.getKeyFrame(stateTimer,true);
			//liga o efeito sonoro de passos 
			//é só para não liga o tempo todo
			if(ligaSomPassos) {
				
				if(priviosState == State.SHOOTING_RUNNING)
					passosSom.stop();
				
				passosSom.loop(0.4f);
				ligaSomPassos = false;
			}
			break;
		case SHOOTING:
			region = (TextureRegion) lekShooting.getKeyFrame(stateTimer,true);
			break;
		case SHOOTING_RUNNING:
			region = (TextureRegion) lekShooting_running.getKeyFrame(stateTimer,true);
			//liga o efeito sonoro de passos 
			//é só para não liga o tempo todo
			if(ligaSomPassos) {
				if(priviosState == State.RUNNING)
					passosSom.stop();
				
				passosSom.loop(0.4f);
				ligaSomPassos = false;
			}
			break;
			
		default:
			
			if(priviosState == State.JUMPING || priviosState == State.SHOOTING_JUMPING)
				quedaSom.play(0.4f);
				
			region = (TextureRegion) lekStand.getKeyFrame(stateTimer,true);
			
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
		
		//desliga o efeito sonoro de passos
		if(((currentState  != State.RUNNING && currentState  != State.SHOOTING_RUNNING))&& !ligaSomPassos){
			passosSom.stop();
			ligaSomPassos = true;
		}
			
		stateTimer = currentState == priviosState ? stateTimer + dt : 0;
		priviosState = currentState;
		
		if(getState() != State.JUMPING &&  getState() != State.SHOOTING_JUMPING)
			contPulo = 0;
		
		return region;
		
	}
	
	
	//getState
	public State getState() {
		if(vida <= 0) 
			return State.DEAD;
		if(b2body.getLinearVelocity().y > 0 && !atirando || (b2body.getLinearVelocity().y  < 0 && (priviosState == State.JUMPING || priviosState == State.SHOOTING_JUMPING) && !atirando) )
			return State.JUMPING;
		else if(b2body.getLinearVelocity().y > 0 && atirando || (b2body.getLinearVelocity().y  < 0 && (priviosState == State.JUMPING || priviosState == State.SHOOTING_JUMPING) && atirando) )
			return State.SHOOTING_JUMPING;
		else if(b2body.getLinearVelocity().x < -0.1 && !atirando || b2body.getLinearVelocity().x > 0.1 && !atirando)
			return State.RUNNING;
		else if(b2body.getLinearVelocity().x < -0.1 && atirando  || b2body.getLinearVelocity().x > 0.1 && atirando)
			return State.SHOOTING_RUNNING;
		else if(atirando)
			return State.SHOOTING;
		else 
			return State.STANDING;
	}
	//tiro
	public void tiro() {
		
		tiros.add(new Bala(screen, pos.x, pos.y, runningRigth ? true : false,Tiroteio.BALA_PLAYER_BIT));
		tiroSom.play(0.1f);
		
		/*tempo += 0.01f;
		if(espera <= tempo) {
			tiros.add(new Bala(screen, pos.x, pos.y, runningRigth ? true : false,Tiroteio.BALA_PLAYER_BIT));
			espera = tempo+0.10f;
			
				tiroSom.play(0.1f);
			
			atirando = true;
		}*/
		
	}
	//paraTiro
	public void paraTiro() {	
		atirando = false;
	}
	
	//pulo
	public void pulo() {
		if(contPulo == 0) {
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
			contPulo++;
		}
	}
	
	//defineLek13
	private void defineLek13() {
			
			bdef = new BodyDef();
			bdef.position.set(200/Tiroteio.PPM, 32/Tiroteio.PPM);
			bdef.type = BodyDef.BodyType.DynamicBody;
			b2body = world.createBody(bdef);
		
			FixtureDef fdef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
		
			Vector2 [] vertices = new Vector2[4];
			
			vertices[0] = new Vector2(-9 / Tiroteio.PPM, 35 / Tiroteio.PPM);
			vertices[1] = new Vector2(6 / Tiroteio.PPM, 35 / Tiroteio.PPM);
			vertices[2] = new Vector2(-9 / Tiroteio.PPM, -4 / Tiroteio.PPM);
			vertices[3] = new Vector2(6 / Tiroteio.PPM, -4 / Tiroteio.PPM);
		
			shape.set(vertices);
			fdef.shape = shape;
			
			fdef.filter.categoryBits = Tiroteio.LEK_BIT;
			fdef.filter.maskBits = Tiroteio.ARBUSTO_BIT |
					Tiroteio.OBJETO_BIT|
					Tiroteio.SOLO_BIT|
					Tiroteio.ENEMY_BIT|
					Tiroteio.BALA_ENEMY_BIT;
			
			b2body.createFixture(fdef).setUserData(this);
			
			Vector2 [] vertices2 = new Vector2[4];
			
			vertices2[0] = new Vector2(-11 / Tiroteio.PPM, 37 / Tiroteio.PPM);
			vertices2[1] = new Vector2(8 / Tiroteio.PPM, 37 / Tiroteio.PPM);
			vertices2[2] = new Vector2(-11 / Tiroteio.PPM, -6 / Tiroteio.PPM);
			vertices2[3] = new Vector2(8 / Tiroteio.PPM, -6 / Tiroteio.PPM);
			
			shape.set(vertices2);
			fdef.shape = shape;
			
			fdef.isSensor = true;
			
			b2body.createFixture(fdef).setUserData(this);
	}
	
	public void hit() {
		vida -= 1;
		screen.getHud().setVida((int)(vida*100f)/VIDA_INICIAL);
		if(vida <= 0) {
			setToDestroy = true;
			bodyDestroy = true;
		}
	}
	
	//draw
	@Override
	public void draw(Batch batch) {
		if(!destroyed) {
			super.draw(batch);
		}
		//desenha as balas
		for (Bala bala : tiros) {
			if(!bala.isDestroyed())
				bala.draw(batch);
		}
		
	}
	
	public float getVida() {
		return vida;
	}

	
	//dispose
	@Override
	public void dispose() {
		manager.dispose();
		tiroSom.dispose();
		passosSom.dispose();
		quedaSom.dispose();
	}

}