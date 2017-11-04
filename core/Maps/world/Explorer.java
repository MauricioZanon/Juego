package world;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static com.mygdx.juego.Juego.world;

import components.PositionComponent;
import main.Chunk;
import main.EmptyMap;
import main.Tile;

//TODO cambiar nombre
public class Explorer {
	
	public static Chunk getChunk(int x, int y, int z){
		try{
			return world.getMap()[x][y][z];
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
	
	public static Tile getTile(int x, int y, int z) {
		return getTile(x/world.CHUNK_SIZE, y/world.CHUNK_SIZE, z, x%world.CHUNK_SIZE, y%world.CHUNK_SIZE);
	}
	
	public static Tile getTile(int gx, int gy, int gz, int lx, int ly){
		Chunk chunk = world.getMap()[gx][gy][gz];
		if(chunk == null){
			world.getMap()[gx][gy][gz] = new EmptyMap(gx, gy, gz);
			chunk = world.getMap()[gx][gy][gz];
		}
		Tile tile = chunk.getChunkMap()[lx][ly];
		if(tile == null){
			chunk.getChunkMap()[lx][ly] = new Tile(new PositionComponent(gx*world.CHUNK_SIZE + lx, gy*world.CHUNK_SIZE + ly, gz));
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
		int x = tile.getPos().coord[0];
		int y = tile.getPos().coord[1];
		int z = tile.getPos().coord[2];
		
		Set<Tile> tiles = new HashSet<Tile>();
		Tile evaluatedTile;
		try {
			evaluatedTile = getTile(x + 1, y, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x - 1, y, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x, y + 1, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x, y - 1, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		
		return tiles;
	}

	public static Set<Tile> getDiagonalTiles(Tile tile, Predicate<Tile> cond) {
		int x = tile.getPos().coord[0];
		int y = tile.getPos().coord[1];
		int z = tile.getPos().coord[2];
		
		Set<Tile> tiles = new HashSet<Tile>();
		Tile evaluatedTile;
		try {
			evaluatedTile = getTile(x + 1, y + 1, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x - 1, y - 1, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x - 1, y + 1, z);
			if(evaluatedTile != null && cond.test(evaluatedTile))
				tiles.add(evaluatedTile);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
		try {
			evaluatedTile = getTile(x + 1, y - 1, z);
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
		int x = tile.getPos().coord[0];
		int y = tile.getPos().coord[1];
		int z = tile.getPos().coord[2];
		HashSet<Tile> list = new HashSet<Tile>();
		for(int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				try {
					PositionComponent evalPos = new PositionComponent(x+i, y+j, z);
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
		return end.coord[0] - start.coord[0];
	}
	
	public static int getDistanceInY(PositionComponent start, PositionComponent end){
		return start.coord[1] - end.coord[1];
	}
	
	public static double getDistance(PositionComponent start, PositionComponent end){
		double dx = getDistanceInX(start, end);
		double dy = getDistanceInY(start, end);
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public static ArrayList<Tile> getStraigthLine(PositionComponent start, PositionComponent end){
		int dx = getDistanceInX(start, end);
		int dy = getDistanceInY(start, end);

		int sx = dx > 0 ? 1 : -1;
		int sy = dy < 0 ? 1 : -1;
		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		
		int err = dx - dy;
		
		ArrayList<Tile> line = new ArrayList<Tile>();
		PositionComponent linePos = start.clone();
		
		double lineLength = getDistance(start, end);
		while (lineLength > getDistance(start, linePos)) {
			line.add(getTile(linePos));
			 
			int e2 = 2 * err;
			
			if (e2 > -dy) {
				err = err - dy;
				linePos.coord[0] += sx;
			}
			if (e2 < dx) {
				err = err + dx;
				linePos.coord[1] += sy;
			}
		}
		line.add(getTile(end));
		
		return line;
	}
	
	/**
	 * Usa flood fill solo en los tiles transitables y develve el primero que encuentra con la condicion dada
	 * @param origin el origen de la busqueda
	 * @param cond
	 * @return
	 */
	public static Tile getClosestTile(Tile origin, Predicate<Tile> cond){
		Set<Tile> possibleTiles = getAdjacentTiles(origin, t -> t.isTransitable());
		for(int i = 0; i < world.CHUNK_SIZE; i++){
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
		return null;
	}
	
	public static Tile getClosestTile(Tile origin, Set<Tile> tiles) {
		Tile closest = null;
		double shortestDistance = 1000;
		
		for(Tile tile : tiles) {
			double distance = getDistance(origin.getPos(), tile.getPos());
			if(distance < shortestDistance) {
				closest = tile;
				shortestDistance = distance;
			}
		}
		return closest;
		
	}
}
