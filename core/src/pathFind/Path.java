package pathFind;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.juego.Juego;

import components.GraphicsComponent;
import components.PositionComponent;
import factories.TerrainFactory;
import main.Tile;

public class Path {
	
	private LinkedList<PositionComponent> path = new LinkedList<>();
	
	public void add(PositionComponent pos){
		path.add(0, pos);
	}
	
	public PositionComponent getNext(){
		return path.getFirst();
	}
	
	public void advance(){
		path.removeFirst();
	}
	
	public boolean isEnded(){
		return path.isEmpty();
	}
	
	public int getDistance(){
		return path.size();
	}

	public boolean contains(Tile tile) {
		for(PositionComponent t : path){
			if (t.getTile().equals(tile)){
				return true;
			}
		}
		return false;
	}
	
	public void showPath() {
		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "P";
		gc.frontColor = Color.RED;
		gc.backColor = Color.BLACK;
		gc.renderPriority = 0;
		path.forEach(p -> p.getTile().put(TerrainFactory.get("dirt floor")));
	}

}
