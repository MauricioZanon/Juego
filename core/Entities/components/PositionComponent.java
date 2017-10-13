package components;

import com.badlogic.ashley.core.Component;
import com.mygdx.juego.Juego;

import cosas.Tile;
import world.Direction;
import world.Explorer;
import world.World;

//TODO eliminar Constructor, hacer que el engine cree un position component y darle un array de coordenadas
public class PositionComponent implements Cloneable, Component{
	
	/**
	 * Coordinates:
	 * 0 - Global X
	 * 1 - Global Y
	 * 2 - Global Z
	 * 3 - Local X
	 * 4 - Local Y
	 */
	private int[] coord = new int[5];

	public PositionComponent(int gx, int gy, int gz, int lx, int ly) {
		setGx(gx);
		setGy(gy);
		setGz(gz);
		setLx(lx);
		setLy(ly);
	}
	
	public void setCoord(int[] c) {
		setGx(c[0]);
		setGy(c[1]);
		setGz(c[2]);
		setLx(c[3]);
		setLy(c[4]);
	}
	
	public PositionComponent() {
		//Constructor vacío para que el engine pueda instanciar esta clase
	}

	public int getGx() {
		return coord[0];
	}
	public void setGx(int newGx) {
		coord[0] = newGx;
	}
	public int getGy() {
		return coord[1];
	}
	public void setGy(int newGy) {
		coord[1] = newGy;
	}
	public int getGz() {
		return coord[2];
	}
	public void setGz(int newGz) {
		coord[2] = newGz;
	}
	public int getLx() {
		return coord[3];
	}
	public void setLx(int newLx) {
		if(newLx >= World.CHUNK_SIZE){
			setLx(newLx - World.CHUNK_SIZE);
			if(coord[0] + 1 < World.getMap().length)
				coord[0]++;
		}else if(newLx < 0){
			setLx(World.CHUNK_SIZE + newLx);
			if(coord[0] - 1 >= 0)
				coord[0]--;
		}else{
			coord[3] = newLx;
		}
	}
	public int getLy() {
		return coord[4];
	}
	public void setLy(int newLy) {
		if(newLy >= World.CHUNK_SIZE){
			setLy(newLy - World.CHUNK_SIZE);
			if(coord[1] + 1 >= World.getMap()[0].length)
				return;
			coord[1]++;
		}else if(newLy < 0){
			setLy(World.CHUNK_SIZE + newLy);
			if(coord[1] - 1 < 0)
				return;
			coord[1]--;
		}else{
			coord[4] = newLy;
		}
	}
	
	public Tile getTile(){
		return Explorer.getTile(this);
	}
	
	@Override
	public String toString(){
		return "GX: " + coord[0] + " GY: " + coord[1] + " GZ: " + coord[2] + " LX: " + coord[3] + " LY: " + coord[4];
	}
	
	@Override
	public boolean equals(Object p){
		if(p == null) return false;
		PositionComponent pos = (PositionComponent) p;
		return coord[0] == pos.getGx() && coord[1] == pos.getGy() && coord[2] == pos.getGz() && coord[3] == pos.getLx() && coord[4] == pos.getLy();
	}

	public void move(Direction dir) {
		coord[3] += dir.movX;
		coord[4] += dir.movY;
		
	}
	
	@Override
	public PositionComponent clone(){
		PositionComponent newComp = Juego.ENGINE.createComponent(PositionComponent.class);
		newComp.setGx(coord[0]);
		newComp.setGy(coord[1]);
		newComp.setGz(coord[2]);
		newComp.setLx(coord[3]);
		newComp.setLy(coord[4]);
		
		return newComp;
	}
	
}
