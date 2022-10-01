package com.tiroteio.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tiroteio.Tiroteio;
import com.tiroteio.enemys.John;
import com.tiroteio.objetos.Arbusto;
import com.tiroteio.objetos.Objeto;
import com.tiroteio.objetos.Solo;
import com.tiroteio.screens.PlayScreen;

public class B2WorldCreator {
	
	private Array<John> jonhs;
	private Array<Solo> solo;
	private Array<Objeto> objeto;
	private Array<Arbusto> arbusto;
	
	public B2WorldCreator(PlayScreen screen) {
		
			TiledMap map = screen.getMap();
			jonhs = new Array<John>();
			
			solo = new Array<>();
			objeto = new Array<>();
			arbusto = new Array<>();
			
			for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
				     
				if(object instanceof RectangleMapObject) {
					Rectangle rect = ((RectangleMapObject) object).getRectangle();
					solo.add(new Solo(rect,null,null,screen));
				}
				else if(object instanceof PolygonMapObject) {
					Polygon poly = ((PolygonMapObject) object).getPolygon();
					solo.add(new Solo(null,poly,null,screen));
				}
				else if(object instanceof EllipseMapObject) {
					Ellipse circulo = ((EllipseMapObject) object).getEllipse();
					solo.add(new Solo(null,null,circulo,screen));
				}
			}
				
			//objetos
			for(MapObject object:map.getLayers().get(5).getObjects()) {
					
				if(object instanceof RectangleMapObject) {
					Rectangle rect = ((RectangleMapObject) object).getRectangle();
					objeto.add(new Objeto(rect,null,null,screen));
				}
				else if(object instanceof PolygonMapObject) {
					Polygon poly = ((PolygonMapObject) object).getPolygon();
					objeto.add(new Objeto(null,poly,null,screen));
				}
				else if(object instanceof EllipseMapObject) {
					Ellipse circulo = ((EllipseMapObject) object).getEllipse();
					objeto.add(new Objeto(null,null,circulo,screen));
				}
					
			}
				
			//arbustos
			for(MapObject object:map.getLayers().get(6).getObjects()) {
					
				if(object instanceof RectangleMapObject) {
					Rectangle rect = ((RectangleMapObject) object).getRectangle();
					arbusto.add(new Arbusto(rect,null,null,screen));
				}
				else if(object instanceof PolygonMapObject) {
					Polygon poly = ((PolygonMapObject) object).getPolygon();
					arbusto.add(new Arbusto(null,poly,null,screen));
				}
				else if(object instanceof EllipseMapObject) {
					Ellipse circulo = ((EllipseMapObject) object).getEllipse();
					arbusto.add(new Arbusto(null,null,circulo,screen));
				}
			}
				
			for(MapObject object:map.getLayers().get(7).getObjects()) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
					
				jonhs.add(new John(screen, rect.getX() / Tiroteio.PPM, rect.getY() / Tiroteio.PPM));
					
			}		
	}

	public Array<John> getJonhs() {
		return jonhs;
	}

	public Array<Solo> getSolo() {
		return solo;
	}

	public Array<Objeto> getObjeto() {
		return objeto;
	}

	public Array<Arbusto> getArbusto() {
		return arbusto;
	}
	
}
