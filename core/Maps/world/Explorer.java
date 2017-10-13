package world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import components.PositionComponent;
import cosas.Chunk;
import cosas.EmptyMap;
import cosas.Tile;

//TODO cambiar nombre
public class Explorer {
	
	public static Chunk getChunk(int x, int y, int z){
		try{
			return World.getMap()[x][y][z];
		}catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}
	
	public static Chunk getChunk(PositionComponent pos){
		return getChunk(pos.getGx(), pos.getGy(), pos.getGz());
	}
	
	public static Tile getTile(PositionComponent p){
		Tile tile = getTile(p.getGx(), p.getGy(), p.getGz(), p.getLx(), p.getLy());
		return tile == null ? new Tile(p) : tile;
	}
	
	public static Tile getTile(int gx, int gy, int gz, int lx, int ly){
		if(lx < 0){
			gx--;
			lx = World.CHUNK_SIZE - 1;
		}else if(lx >= World.CHUNK_SIZE){
			gx++;
			lx = 0;
		}
		if(ly < 0){
			gy--;
			ly = World.CHUNK_SIZE - 1;
		}else if(ly >= World.CHUNK_SIZE){
			gy++;
			ly = 0;
		}
		Chunk chunk = World.getMap()[gx][gy][gz];
		if(chunk == null){
			World.getMap()[gx][gy][gz] = new EmptyMap(gx, gy, gz);
			chunk = World.getMap()[gx][gy][gz];
		}
		Tile tile = chunk.getChunkMap()[lx][ly];
		if(tile == null){
			chunk.getChunkMap()[lx][ly] = new Tile(new PositionComponent(gx, gy, gz, lx, ly));
			tile = chunk.getChunkMap()[lx][ly];
		}
		return tile;
	}
	
	public static PositionComponent getPosition(int gx, int gy, int gz, int lx, int ly){
		return getTile(gx, gy, gz, lx, ly).getPos();
	}
	
	public static PositionComponent getPosition(PositionComponent oldPos, Direction dir) {
		PositionComponent newPos = oldPos.clone();
		newPos.move(dir);
		return newPos;
	}
	
	public static Set<Tile> getOrthogonalTiles(Tile tile, Predicate<Tile> cond) {
		int lx = tile.getPos().getLx();
		int ly = tile.getPos().getLy();
		
		int gx = tile.getPos().getGx();
		int gy = tile.getPos().getGy();
		int gz = tile.getPos().getGz();
		
		Set<Tile> tiles = new HashSet<Tile>();
		Tile evaluatedTile;
		try {
			evaluatedTile = getTile(gx, gy, gz, lx + 1, ly);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx - 1, ly);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx, ly + 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx, ly - 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		
		return tiles;
	}

	public static Set<Tile> getDiagonalTiles(Tile tile, Predicate<Tile> cond) {
		int lx = tile.getPos().getLx();
		int ly = tile.getPos().getLy();
		
		int gx = tile.getPos().getGx();
		int gy = tile.getPos().getGy();
		int gz = tile.getPos().getGz();
		
		Set<Tile> tiles = new HashSet<Tile>();
		Tile evaluatedTile;
		try {
			evaluatedTile = getTile(gx, gy, gz, lx + 1, ly + 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx - 1, ly - 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx - 1, ly + 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(gx, gy, gz, lx + 1, ly - 1);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		
		return tiles;
	}

	public static Set<Tile> getAdjacentTiles(Tile tile, Predicate<Tile> cond) {
		Set<Tile> tiles = getOrthogonalTiles(tile, cond);
		tiles.addAll(getDiagonalTiles(tile, cond));
		return tiles;
	}
	
	public static int countOrthogonalAdjacency(Tile tile, Predicate<Tile> cond){
		return getOrthogonalTiles(tile, cond).size();
	}
	
	public static int countDiagonalAdjacency(Tile tile, Predicate<Tile> cond){
		return getDiagonalTiles(tile, cond).size();
	}
	
	public static int countAdjacency(Tile tile, Predicate<Tile> cond){
		return countOrthogonalAdjacency(tile, cond) + countDiagonalAdjacency(tile, cond);
	}
	
	public static boolean isAdjacent(Tile tile, Predicate<Tile> cond){
		return countAdjacency(tile, cond) != 0;
	}
	
	public static boolean isOrthogonallyAdjacent(Tile tile, Predicate<Tile> cond){
		return countOrthogonalAdjacency(tile, cond) != 0;
	}

	public static boolean isDiagonallyAdjacent(Tile tile, Predicate<Tile> cond){
		return countDiagonalAdjacency(tile, cond) != 0;
	}
	
	public static Set<Tile> getCircundatingArea(int radius, Tile tile, boolean isRound){
		int gx0 = tile.getPos().getGx();
		int gy0 = tile.getPos().getGy();
		int gz0 = tile.getPos().getGz();
		int lx0 = tile.getPos().getLx();
		int ly0 = tile.getPos().getLy();
		HashSet<Tile> list = new HashSet<Tile>();
		PositionComponent evalPos;
		for(int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				evalPos = new PositionComponent(gx0, gy0, gz0, lx0, ly0);
				try {
					evalPos.setLx(lx0 + i);
					evalPos.setLy(ly0 + j);
					if(!isRound || getDistance(tile.getPos(), evalPos) <= radius){
						Tile t = getTile(evalPos);
						list.add(t);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		return list;
	}
	
	public static int getDistanceInX(PositionComponent start, PositionComponent end){
		if(start.getGx() == end.getGx()){
			return end.getLx() - start.getLx();
		}else{
			if(start.getGx() < end.getGx()){
				return World.CHUNK_SIZE - start.getLx() + end.getLx();
			}else{
				return -(World.CHUNK_SIZE - end.getLx() + start.getLx());
			}
		}
	}
	
	public static int getDistanceInY(PositionComponent start, PositionComponent end){
		if(start.getGy() == end.getGy()){
			return start.getLy() - end.getLy();
		}else{
			if(start.getGy() < end.getGy()){
				return start.getLy() - end.getLy() - World.CHUNK_SIZE;
			}else{
				return (World.CHUNK_SIZE - end.getLy()) + start.getLy() + (start.getGy() - end.getGy() - 1) * World.CHUNK_SIZE;
			}
		}
	}
	
	public static double getDistance(PositionComponent start, PositionComponent end){
		double dx = getDistanceInX(start, end);
		double dy = getDistanceInY(start, end);
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	/*
	 * Bresenham's line algorithm
 		https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	 */
	public static ArrayList<Tile> getStraigthLine(PositionComponent start, PositionComponent end){
		int dx = getDistanceInX(start, end);
		int dy = getDistanceInY(start, end);
		
		int sx = dx > 0 ? 1 : -1;
		int sy = dy < 0 ? 1 : -1;
		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		
		int err = dx - dy;
		
		ArrayList<Tile> linea = new ArrayList<Tile>();
		PositionComponent linePos = new PositionComponent(start.getGx(), start.getGy(), start.getGz(), start.getLx(), start.getLy());
		
		while (!linePos.equals(end)) {
			linea.add(getTile(linePos));
			 
			int e2 = 2 * err;
			
			if (e2 > -dy) {
				err = err - dy;
				linePos.setLx(linePos.getLx() + sx);
			}
			if (e2 < dx) {
				err = err + dx;
				linePos.setLy(linePos.getLy() + sy);
			}
		}
		linea.add(getTile(end));
		
		return linea;
	}
	
	public static Tile getNearbyTile(Tile origin, Predicate<Tile> cond){
		Set<Tile> possibleTiles = getAdjacentTiles(origin, t -> t.isTransitable());
		while(true){
			Set<Tile> newSet = new HashSet<>();
			for(Tile tile : possibleTiles){
				if(cond.test(tile)){
					return tile;
				}
				newSet.addAll(getAdjacentTiles(tile, t -> t.isTransitable()));
			}
			newSet.removeAll(possibleTiles);
			possibleTiles = newSet;
		}
	}
}
