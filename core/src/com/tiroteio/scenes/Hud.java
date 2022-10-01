package com.tiroteio.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tiroteio.Tiroteio;
import com.tiroteio.screens.PlayScreen;

public class Hud implements Disposable {

	public Stage stage;
	private Viewport viewport;
	
	private Integer wolrdTimer;
	private Integer vida;
	
	Label contDanwLabel;
	Label scoreLabel;
	Label timeLabel;
	Label levelLabel;
	Label WorldLabel; 
	Label lek13Label;
	
	public Hud(SpriteBatch sb,PlayScreen screem) {
		wolrdTimer = 300;
		vida = 0;
		
		viewport = new FitViewport(Tiroteio.V_WIDTH,Tiroteio.V_HEIGTH, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		Table table = new Table();
		table.top();
		
		table.setFillParent(true);
		
		contDanwLabel = new Label(String.format("%03d", wolrdTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel =  new Label(String.format("%03d", vida)+"%", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE)); 
		levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE)); 
		WorldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		lek13Label = new Label("LEK13", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
	    
		table.add(lek13Label).expandX().padTop(10);
		table.add(WorldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(contDanwLabel).expandX();
		
		stage.addActor(table);
		
	}
	
	public Integer getVida() {
		return vida;
	}

	public void setVida(Integer mudaVida) {
		this.scoreLabel.setText(String.format("%03d", mudaVida)+"%");
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
	
}
