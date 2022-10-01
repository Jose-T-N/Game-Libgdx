package com.tiroteio.objetos;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.tiroteio.Tiroteio;
import com.tiroteio.screens.PlayScreen;

public class Arbusto extends InteractiveTileObject {

	public static Sound item_achado;
	
	public Arbusto(Rectangle bounds, Polygon poly, Ellipse circulo,PlayScreen screen) {
		super(bounds,poly,circulo,screen);
		
		setUser(this);
		
		setCategoriFilter(Tiroteio.ARBUSTO_BIT);
		
		 item_achado = Tiroteio.manager.get("audio/sounds/item_achado.ogg", Sound.class);
		
	}
	
	@Override
	public void onBodyHit() {
		setCategoriFilter(Tiroteio.DESTOYED_BIT);
		item_achado.play(0.1f);
	}

}
