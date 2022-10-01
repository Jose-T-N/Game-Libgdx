package com.tiroteio.enemys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.tiroteio.Tiroteio;
import com.tiroteio.balas.Bala;
import com.tiroteio.screens.PlayScreen;

public class John extends Enemy {

	public John(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		
		frames = new Array<>();
		//sprite correndo
		TextureRegion texture = new TextureRegion(new Texture("sprites/john/john_run.png"));
		for(int i = 0;i<10;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/10)*i
					,0,
					texture.getTexture().getWidth()/10,
					texture.getTexture().getHeight()));
		}
		
		enemyRun = new Animation<>(0.1f, frames);
		
		frames.clear();
		
		//sprite parado
		texture = new TextureRegion(new Texture("sprites/john/john_idle.png"));
		for(int i = 0;i<5;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/5)*i
					,0,
					texture.getTexture().getWidth()/5,
					texture.getTexture().getHeight()));
		}
		enemyStand = new Animation<>(0.4f, frames);
		
		frames.clear();
		
		//sprite pulando
		texture = new TextureRegion(new Texture("sprites/john/john_jump.png"));
		for(int i = 0;i<9;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/9)*i
					,0,
					texture.getTexture().getWidth()/9,
					texture.getTexture().getHeight()));
		}
		
		enemyJump = new Animation<>(0.4f, frames);
		
		frames.clear();
		
		//sprite morto
		texture = new TextureRegion(new Texture("sprites/john/john_dead.png"));
		for(int i = 0;i<2;i++) {
			frames.add(new TextureRegion(texture,(texture.getTexture().getWidth()/2)*i
					,0,
					texture.getTexture().getWidth()/2,
					texture.getTexture().getHeight()));
		}
		
		enemyDead = new Animation<>(0.4f, frames);
		frames.clear();
		
		setBounds(0, 0, 26/Tiroteio.PPM, 42/Tiroteio.PPM);
		stateTimer = 0;
		
		life = 1f;
		
		milesegundo = 0;
		segundos = 0;
		
	}

	@Override
	public void update(float dt) {
		if(setToDestroy && !destroyed) {
			
			//if bodyDestroy for igual a true ele destroi o b2body
			//isso é só para o jogo não tenter destruir o b2body mais de uma vez
			if(bodyDestroy)
				wolrd.destroyBody(b2body);
				
			setRegion(enemyDead.getKeyFrame(stateTimer,true));
			
			//verifica qual o lado que o inigo esta
			//e vira a esprite para o lado atual
			if(!runningRigth && !isFlipX()) {
				flip(true, false);
			}
			//faz a auma do inigo subir
			setPosition(getX(),getY()+dt);
			
			bodyDestroy = false;
			
			if(getY()>100/Tiroteio.PPM) {
				destroyed = true;
			}
			
			for (Bala bala : tiros) {
				if(!bala.isDestroyed()) {
					bala.setToDestroy();
					bala.update(dt);
				}
				
			}
			passos.stop();
			
		}
		else{
			
			//b2body.setLinearVelocity(velocity);
			
			//configura o b2body para o lado certo
			if(runningRigth) {
				setPosition(b2body.getPosition().x - getWidth() / 2,
						b2body.getPosition().y - getHeight() / 2 + 18 / Tiroteio.PPM );
			}
			else {
				setPosition(b2body.getPosition().x - getWidth() / 2 ,
						b2body.getPosition().y - getHeight() / 2 + 18 / Tiroteio.PPM );
			}
		
			setRegion(getFrames(dt));
			
			if(b2body.getPosition().y < 0) {
				setToDestroy = true;
				//configura a variavel bodyDestroy para que o corpo possa ser estruido
				bodyDestroy = true;
			}
			
			if(life <= 0) {
				setToDestroy = true;
				//configura a variavel bodyDestroy para que o corpo possa ser estruido
				bodyDestroy = true;
			}
			
			//ativa o personagem qunado o player chega perto
			if(getX() < screen.getPlayer().getX() + 224/Tiroteio.PPM)
				b2body.setActive(true);
			
			//muda a diração 
			if(screen.getPlayer().b2body.getPosition().x > b2body.getPosition().x) {
				runningRigth = true;
			}
			else {
				runningRigth = false;
			}
			
			//atira 
			if(b2body.isActive()){
				tiro(dt);
			}
			
			//update bala
			for (Bala bala : tiros) {
				if(!bala.isDestroyed())
					bala.update(dt);
				
				if(bala.isDestroyed()) {
					tiros.removeValue(bala, true);
				}
			}
		}
	}
	
	@Override
	public void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = wolrd.createBody(bdef);
	
		fdef = new FixtureDef();
		shape = new PolygonShape();
	
		Vector2 [] vertices = new Vector2[4];

		vertices[0] = new Vector2(-9 / Tiroteio.PPM, 35 / Tiroteio.PPM);
		vertices[1] = new Vector2(6 / Tiroteio.PPM, 35 / Tiroteio.PPM);
		vertices[2] = new Vector2(-9 / Tiroteio.PPM, -4 / Tiroteio.PPM);
		vertices[3] = new Vector2(6 / Tiroteio.PPM, -4 / Tiroteio.PPM);
		
		shape.set(vertices);
		fdef.shape = shape;
		
		fdef.filter.categoryBits = Tiroteio.ENEMY_BIT;
		fdef.filter.maskBits = Tiroteio.OBJETO_BIT|
				Tiroteio.SOLO_BIT|
				Tiroteio.LEK_BIT|
				Tiroteio.BALA_PLAYER_BIT;
		
		b2body.createFixture(fdef).setUserData(this);
		
	}

	@Override
	public void hit() {
		life -= 1f; 
		
	}
	
}
