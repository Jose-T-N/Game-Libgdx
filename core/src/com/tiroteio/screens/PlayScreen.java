package com.tiroteio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tiroteio.Tiroteio;
import com.tiroteio.enemys.Enemy;
import com.tiroteio.lek13.Lek13;
import com.tiroteio.scenes.Hud;
import com.tiroteio.tools.B2WorldCreator;
import com.tiroteio.tools.WorldContactListener;

public class PlayScreen implements Screen {

	private Tiroteio game;
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private Hud hud;
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private World world;
	private Box2DDebugRenderer b2dr;
	private Lek13 player;
	private B2WorldCreator creator;
	
	public PlayScreen(Tiroteio game) {
		
		this.game = game;
		
		gamecam = new OrthographicCamera();
		gamePort = new FitViewport(Tiroteio.V_WIDTH /Tiroteio.PPM , Tiroteio.V_HEIGTH /Tiroteio.PPM, gamecam);
		
		hud = new Hud(game.batch,this);
		
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("stage1/mapa.tmx");
		renderer = new OrthogonalTiledMapRenderer(map,1/Tiroteio.PPM);
		
		gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);
	
		world = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();
		
		player = new Lek13(this);
		
		world.setContactListener(new WorldContactListener());
		
		creator = new B2WorldCreator(this); 
		
	 }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	private void handleInput(float dt) {
		if(Gdx.input.isKeyJustPressed(Keys.Z))
			player.pulo();
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
		if(Gdx.input.isKeyPressed(Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
			player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);		
		if(Gdx.input.isKeyJustPressed(Keys.X))
			player.tiro();
		else if(!Gdx.input.isKeyPressed(Keys.X))
			player.paraTiro();	
		
}
	
	public void update(float dt) {
		handleInput(dt);
		
		world.step(1/60f, 6, 2);
		player.update(dt);
		
		for(Enemy enemy:creator.getJonhs()) {
			if(!enemy.isDestroyed()) {
				enemy.update(dt);
			}
		}
		if(player.getState() != player.getState().DEAD && player.b2body.getPosition().x>200/Tiroteio.PPM)
			gamecam.position.x = player.b2body.getPosition().x;	
		
		gamecam.update();
		renderer.setView(gamecam);
		
	}

	@Override
	public void render(float delta) {
		
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		b2dr.render(world, gamecam.combined);
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		
		for(Enemy enemy:creator.getJonhs()) {
			if(!enemy.isDestroyed())
				enemy.draw(game.batch);
		}
		
		game.batch.end();
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	

	public World getWorld() {
		return world;
	}

	
	public Lek13 getPlayer() {
		return player;
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);

	}
	
	public Hud getHud() {
		return hud;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	
	public B2WorldCreator getCreator() {
		return creator;
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
		player.dispose();
	}

}
