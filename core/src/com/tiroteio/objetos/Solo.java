package com.tiroteio.objetos;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.tiroteio.Tiroteio;
import com.tiroteio.screens.PlayScreen;

public class Solo extends InteractiveTileObject {

	public Solo(Rectangle bounds, Polygon poly, Ellipse circulo,PlayScreen screen) {
		super(bounds,poly,circulo,screen);
		
		setUser(this);
		
		setCategoriFilter(Tiroteio.SOLO_BIT);
		
	}

	@Override
	public void onBodyHit() {
	}

}
