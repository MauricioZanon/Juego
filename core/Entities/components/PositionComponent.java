package components;

import com.badlogic.ashley.core.Component;
import static com.mygdx.juego.Juego.world;
import com.mygdx.juego.Juego;

import main.Tile;
import world.Direction;
import world.Explorer;

public class PositionComponent implements Cloneable, Component{
	
	public int[] coord = new int[3];

	//TODO eliminar Constructor, hacer que el engine cree un position component y darle un array de coordenadas
	public PositionComponent(int x, int y, int z) {
		coord[0] = x;
		coord[1] = y;
		coord[2] = z;
	}
	
	public PositionComponent() {
		//Constructor vacío para que el engine pueda instanciar esta clase
	
	}
	public int getGx() {
		return coord[0] / world.CHUNK_SIZE;
	}
	public int getLx() {
		return coord[0] % world.CHUNK_SIZE;
	}
	public int getGy() {
		return coord[1] / world.CHUNK_SIZE;
	}
	public int getLy() {
		return coord[1] % world.CHUNK_SIZE;
	}
	public int getGz() {
		return coord[2];
	}
	
	public Tile getTile(){
		return Explorer.getTile(this);
	}
	
	@Override
	public String toString(){
		return "GX: " + getGx() + " GY: " + getGy() + " GZ: " + coord[2] + " LX: " + getLx() + " LY: " + getLy();
	}
	
	@Override
	public boolean equals(Object p){
		if(p == null) return false;
		int[] pos = ((PositionComponent) p).coord;
		return coord[0] == pos[0] && coord[1] == pos[1] && coord[2] == pos[2];
	}

	public void move(Direction dir) {
		coord[0] += dir.movX;
		coord[1] += dir.movY;
		
	}
	
	@Override
	public PositionComponent clone(){
		PositionComponent newComp = Juego.ENGINE.createComponent(PositionComponent.class);
		newComp.coord[0] = coord[0];
		newComp.coord[1] = coord[1];
		newComp.coord[2] = coord[2];
		return newComp;
	}
	
}
