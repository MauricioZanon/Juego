package world;

import components.PositionComponent;
import cosas.Tile;

public enum Direction {
	
	N(0, 1),
	NE(1, 1),
	E(1, 0),
	SE(1, -1),
	S(0, -1),
	SW(-1, -1),
	W(-1, 0),
	NW(-1, 1);
	
	public int movX;
	public int movY;
	
	Direction(int x, int y){
		movX = x;
		movY = y;
	}
	
	public static Direction get(int x, int y){
		if(x == 0 && y == -1) return N;
		else if(x == -1 && y == -1) return NE;
		else if(x == -1 && y == 0) return E;
		else if(x == -1 && y == 1) return SE;
		else if(x == 0 && y == 1) return S;
		else if(x == 1 && y == 1) return SW;
		else if(x == 1 && y == 0) return W;
		else return NW;
	}
	
	public static Direction get(Tile t1, Tile t2){
		int dx = 0; 
		int dy = 0;
		
		if(t1.getPos().getGx() < t2.getPos().getGx()) dx = -1;
		else if(t1.getPos().getGx() > t2.getPos().getGx()) dx = 1;
		else dx = t1.getPos().getLx() - t2 .getPos().getLx();
		
		if(t1.getPos().getGy() < t2.getPos().getGy()) dy = -1;
		else if(t1.getPos().getGy() > t2.getPos().getGy()) dy = 1;
		else dy = t1.getPos().getLy() - t2 .getPos().getLy();
		
		return get(dx, dy);
	}
	
	public static Direction get(PositionComponent p1, PositionComponent p2){
		return get(p1.getTile(), p2.getTile());
	}
	
	public static Direction getOpossite(Direction dir){
		int x = dir.movX;
		int y = dir.movY;
		
		if(x == 0 && y == 1) return S;
		else if(x == 1 && y == 1) return SW;
		else if(x == 1 && y == 0) return W;
		else if(x == 1 && y == -1) return NW;
		else if(x == 0 && y == -1) return N;
		else if(x == -1 && y == -1) return NE;
		else if(x == -1 && y == 0) return E;
		else return SE;
	}
}
