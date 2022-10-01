package com.tiroteio.tools;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.tiroteio.Tiroteio;
import com.tiroteio.balas.Bala;
import com.tiroteio.enemys.Enemy;
import com.tiroteio.lek13.Lek13;
import com.tiroteio.objetos.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		if(fixA.getUserData() == "corpo" || fixB.getUserData() == "corpo" ) {
			Fixture corpo = fixA.getUserData() == "corpo" ? fixA : fixB;
			Fixture object  = corpo == fixA ? fixB : fixA;
			
			if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
				((InteractiveTileObject)object.getUserData()).onBodyHit();
			}
			
		}
		
		switch (cDef) {
		//Inimigo e bala_player
		case Tiroteio.ENEMY_BIT | Tiroteio.BALA_PLAYER_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_PLAYER_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
				((Enemy)fixB.getUserData()).hit();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
				((Enemy)fixA.getUserData()).hit();
			}
			break;
			
		//objeto e bala_player
		case Tiroteio.OBJETO_BIT | Tiroteio.BALA_PLAYER_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_PLAYER_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
			}
			break;

		//solo e bala_player
		case Tiroteio.SOLO_BIT | Tiroteio.BALA_PLAYER_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_PLAYER_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
			}
			break;
		//Inimigo e bala_player
		case Tiroteio.ENEMY_BIT | Tiroteio.OBJETO_BIT :
			
			if(fixA.getFilterData().categoryBits == Tiroteio.ENEMY_BIT) {
				((Enemy)fixA.getUserData()).setContato(((InteractiveTileObject)fixB.getUserData()).getBounds());
			}
			else {
				((Enemy)fixB.getUserData()).setContato(((InteractiveTileObject)fixA.getUserData()).getBounds());
			}
			break;
			
		//objeto e bala_enemy
		case Tiroteio.OBJETO_BIT | Tiroteio.BALA_ENEMY_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_ENEMY_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
			}
			break;

			//bala_inimigo lek13
		case Tiroteio.BALA_ENEMY_BIT | Tiroteio.LEK_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_ENEMY_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
				((Lek13)fixB.getUserData()).hit();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
				((Lek13)fixA.getUserData()).hit();
			}
			break;
			
		//solo e bala_enemy
		case Tiroteio.SOLO_BIT | Tiroteio.BALA_ENEMY_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.BALA_ENEMY_BIT) {
				((Bala)fixA.getUserData()).setToDestroy();
			}
			else {
				((Bala)fixB.getUserData()).setToDestroy();
			}
			break;	
		//Inimigo e solo
		case Tiroteio.ENEMY_BIT | Tiroteio.SOLO_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.ENEMY_BIT) {
				((Enemy)fixA.getUserData()).setContato(((InteractiveTileObject)fixB.getUserData()).getBounds());
			}
			else {
				((Enemy)fixB.getUserData()).setContato(((InteractiveTileObject)fixA.getUserData()).getBounds());
			}
			break;
			
		//Inimigo e solo
		case Tiroteio.LEK_BIT | Tiroteio.ARBUSTO_BIT:
			if(fixA.getFilterData().categoryBits == Tiroteio.ARBUSTO_BIT) {
				((InteractiveTileObject)fixA.getUserData()).onBodyHit();
			}
			else {
				((InteractiveTileObject)fixB.getUserData()).onBodyHit();
			}
			break;
			
		//Inimigo e objeto
		case Tiroteio.ENEMY_BIT | Tiroteio.ENEMY_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.ENEMY_BIT) {
				//((Enemy)fixA.getUserData()).contato = true ;
				//((Enemy)fixB.getUserData()).contato = true ;
				
				//((Enemy)fixA.getUserData()).reveseVelocity(true, false);
				//((Enemy)fixB.getUserData()).reveseVelocity(true, false);
				
			}
			break;
			
		default:
			break;
		}		
	}

	@Override
	public void endContact(Contact contact) {
		
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		switch (cDef) {
		//visao do inimigo e objeto
		case Tiroteio.ENEMY_BIT | Tiroteio.OBJETO_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.ENEMY_BIT) {
				((Enemy)fixA.getUserData()).contato = 0 ;
			}
			else {
				((Enemy)fixB.getUserData()).contato = 0 ;
				//((Enemy)fixB.getUserData()).reveseVelocity(true, false);
			}
			break;
		//visao do inimigo e objeto
		case Tiroteio.ENEMY_BIT | Tiroteio.SOLO_BIT :
			if(fixA.getFilterData().categoryBits == Tiroteio.ENEMY_BIT) {
				((Enemy)fixA.getUserData()).contato = 0;
			}
			else {
				((Enemy)fixB.getUserData()).contato = 0;
			}
			break;
			
		}
		
	}
		

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
