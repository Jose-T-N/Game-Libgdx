package com.tiroteio.balas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tiroteio.Tiroteio;
import com.tiroteio.screens.PlayScreen;

public class Bala extends Sprite implements Disposable {

	    private PlayScreen screen;
	    private  World world;
	    private Array<TextureRegion> frames;
	    private Animation<TextureRegion> fireAnimation;
	    private float stateTime;
	    private boolean destroyed;
	    private boolean setToDestroy;
	    private boolean fireRight;
	    private Body b2body;
	    
	    public Bala(PlayScreen screen, float x, float y, boolean fireRight,short filtro){
	        this.fireRight = fireRight; 
	        this.screen = screen;
	        this.world = screen.getWorld();
	        
	        frames = new Array<TextureRegion>();
	        
	        TextureRegion texture = new TextureRegion(new Texture("texturas/balas/bala_metralhadora.png"));
			for(int i = 0;i<1;i++) {
				frames.add(texture);
			}
			
			fireAnimation = new Animation<TextureRegion>(0.2f, frames);
	        setRegion(new Texture("texturas/balas/bala_metralhadora.png"));
	        
	        if(!fireRight) {
				flip(true, false);
			}
	        
			setBounds(x, y, 11 / Tiroteio.PPM , 10 / Tiroteio.PPM);
	        defineFireBall(filtro);
	        
	        setToDestroy = false;
	        destroyed = false;
			
	    }

	    public void defineFireBall(short filtro){
	        BodyDef bdef = new BodyDef();
	        bdef.position.set(getX(), getY());
	        bdef.type = BodyDef.BodyType.DynamicBody;
	        if(!world.isLocked())
	        	b2body = world.createBody(bdef);
	        
	        FixtureDef fdef = new FixtureDef();
	        CircleShape shape = new CircleShape();
	        shape.setRadius(3 / Tiroteio.PPM);
	        fdef.filter.categoryBits = filtro;
			fdef.filter.maskBits = Tiroteio.OBJETO_BIT|
					Tiroteio.SOLO_BIT|
					Tiroteio.LEK_BIT|
					Tiroteio.ENEMY_BIT;
			
	        fdef.shape = shape;
	        fdef.restitution = 1;
	        fdef.friction = 0;
	        fdef.density = 0.5f;
	        b2body.createFixture(fdef).setUserData(this);
	    }

	    public void update(float dt){
	        stateTime += dt;
	        
	        if(setToDestroy && !destroyed) {
	        	 if(!world.isLocked())
	        		 world.destroyBody(b2body);
	        	destroyed = true;
	        }
	        else {
	        	setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		        /*if((stateTime > 3 || setToDestroy) && !destroyed) {
		            world.destroyBody(b2body);
		            destroyed = true;
		        }*/
		        
		        b2body.setLinearVelocity(new Vector2(fireRight ? 5 : -5, 0.2f));
		        /*if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
		             setToDestroy();*/
	        }
	    }

	    public void setToDestroy(){
	        setToDestroy = true;
	    }

	    public boolean isDestroyed(){
	        return destroyed;
	    }

		@Override
		public void dispose() {
			
			
		}

	}
