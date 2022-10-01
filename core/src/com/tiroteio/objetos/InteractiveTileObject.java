package com.tiroteio.objetos;

import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Setters.Bones;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tiroteio.Tiroteio;
import com.tiroteio.screens.PlayScreen;

public abstract class InteractiveTileObject {
	
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Polygon poly;
	protected Ellipse circulo;
	protected Fixture fixtureRetangulo;
	protected Fixture fixtureCirculo;
	protected Fixture fixturePoligono;
	public PlayScreen screen;
	
	public InteractiveTileObject(Rectangle bounds, Polygon poly, Ellipse circulo, PlayScreen screen) {

		this.world = screen.getWorld();
		this.map = screen.getMap();
		
		this.bounds = bounds;
		this.poly = poly;
		this.circulo = circulo;
		this.screen = screen;
		
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		 if(bounds!=null) {
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((bounds.getX()+bounds.getWidth()/2)/Tiroteio.PPM, (bounds.getY()+bounds.getHeight()/2)/Tiroteio.PPM);
		
			body = world.createBody(bdef);
		
			shape.setAsBox(bounds.getWidth()/2/Tiroteio.PPM, bounds.getHeight()/2/Tiroteio.PPM);
			fdef.shape = shape;
			fixtureRetangulo = body.createFixture(fdef);
		 }
		 
		 if(poly!=null) {
			 bdef.type = BodyDef.BodyType.StaticBody;
				
			 bdef.position.set((poly.getX())/Tiroteio.PPM, (poly.getY())/Tiroteio.PPM);
			
			 body = world.createBody(bdef);
			
		     float [] linha = poly.getVertices();
				
			 for(int i = 0; i<linha.length;i++) {
				 linha[i] = linha[i] /Tiroteio.PPM;
			 }
			 
			 shape.set(linha);
			 fdef.shape = shape;
			 fixtureRetangulo = body.createFixture(fdef);
		 }
		 
		 if(circulo!=null) {
			 
			CircleShape cir = new CircleShape();
					
			bdef.type = BodyDef.BodyType.StaticBody;
					
			bdef.position.set((circulo.x+circulo.width/2)/Tiroteio.PPM, (circulo.y+circulo.height/2)/Tiroteio.PPM);
					
			body = world.createBody(bdef);
					
			cir.setRadius(circulo.height/2/Tiroteio.PPM);
					
			fdef.shape = cir;
			fixtureRetangulo = body.createFixture(fdef);
		 }
	}
	
	public abstract void onBodyHit();
	
	public void setCategoriFilter(short filterBit) {
	
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		
		if(fixtureRetangulo != null)
			fixtureRetangulo.setFilterData(filter);
		if(fixtureCirculo != null)
			fixtureCirculo.setFilterData(filter);
		if(fixturePoligono != null)
			fixturePoligono.setFilterData(filter);
		
	}
	
	protected void setUser(Object classe) {
		if(fixtureRetangulo != null)
			fixtureRetangulo.setUserData(classe);
		if(fixtureCirculo != null)
			fixtureCirculo.setUserData(classe);
		if(fixturePoligono != null)
			fixturePoligono.setUserData(classe);
	}

	public Rectangle getBounds() {
		if(bounds != null) {
			return bounds;
		}
		else if (poly != null) {
			return poly.getBoundingRectangle();
		}
		else {
			
			Rectangle ret = new Rectangle(circulo.x, circulo.y, circulo.width, circulo.height);
			
			return ret;
		}
	}
}
