package com.tiroteio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tiroteio.screens.PlayScreen;

public class Tiroteio extends Game {
	public SpriteBatch batch;
	
	public static final int V_WIDTH = 400;
	public static final int V_HEIGTH = 208;
	public static final float PPM = 100 ; 
	
	public static final short SOLO_BIT = 1;
	public static final short LEK_BIT = 2;
	public static final short OBJETO_BIT = 4;
	public static final short ARBUSTO_BIT = 8;
	public static final short DESTOYED_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short BALA_PLAYER_BIT = 64;
	public static final short BALA_ENEMY_BIT = 128;
	public static final short VISAO_BIT = 256;
	
	public static AssetManager manager;
	public static Music music;
	public static Sound tiro;
	private PlayScreen screen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		manager = new AssetManager();
		manager.load("audio/music/sem_a_planta.mp3", Music.class);
		manager.load("audio/sounds/passos.ogg", Sound.class);
		manager.load("audio/sounds/item_achado.ogg", Sound.class);
		manager.load("audio/sounds/tiro.ogg", Sound.class);
		manager.finishLoading();
		
		tiro = manager.get("audio/sounds/tiro.ogg", Sound.class);
		
		screen = new PlayScreen(this);
		setScreen(screen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
		screen.dispose();
	}
}
