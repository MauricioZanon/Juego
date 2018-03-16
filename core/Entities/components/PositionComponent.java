package components;

import com.badlogic.ashley.core.Component;
import com.mygdx.juego.Juego;

import eventSystem.Map;
import main.Chunk;
import main.Tile;

public class PositionComponent implements Cloneable, Component{
	
	public int[] coord;

	public int getGx() {
		return coord[0] / Chunk.SIZE;
	}
	public int getLx() {
		return coord[0] % Chunk.SIZE;
	}
	public int getGy() {
		return coord[1] / Chunk.SIZE;
	}
	public int getLy() {
		return coord[1] % Chunk.SIZE;
	}
	public int getGz() {
		return coord[2];
	}
	
	public Tile getTile(){
		return Map.getTile(coord[0], coord[1], coord[2]);
	}
	
	@Override
	public String toString(){
		return coord[0] + ":" + coord[1] + ":" + coord[2];
	}
	
	@Override
	public boolean equals(Object o) {
		int[] otherCoords = ((PositionComponent)o).coord;
		return coord[0] == otherCoords[0] && coord[1] == otherCoords[1] && coord[2] == otherCoords[2];
	}
	
	@Override
	public PositionComponent clone(){
		PositionComponent newComp = Juego.ENGINE.createComponent(PositionComponent.class);
		newComp.coord = new int[] {coord[0], coord[1], coord[2]};
		return newComp;
	}

}
