package eventSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;
import com.mygdx.juego.StateLoader;

import components.Mappers;
import components.PositionComponent;
import factories.EntityFactory;
import main.Chunk;
import main.Tile;
import world.Direction;
import world.World;

public abstract class Map {
	
	private static LinkedHashMap<String, Chunk> chunksInMemory = new LinkedHashMap<>();
	
	private static Chunk[][] mapInChunks = new Chunk[5][5];
	private static Tile[][] mapInTiles = new Tile[World.CHUNK_SIZE*5][World.CHUNK_SIZE*5];
	private static int zLevel = 0;
	private static int chunkSize = World.CHUNK_SIZE;
	
	public static void refresh(){
		PositionComponent playerPos = Mappers.posMap.get(Juego.player);
		Chunk center = mapInChunks[2][2];
		if(center == null || playerPos.getGx() != center.getGx() || playerPos.getGy() != center.getGy() || playerPos.getGz() != center.getGz()) {
			refreshMap();
			
			HashSet<Entity> npcs = new HashSet<>();
			for (int lx = 0; lx < mapInChunks.length; lx++){
				for(int ly = 0; ly < mapInChunks[0].length; ly++){
					npcs.addAll(mapInChunks[lx][ly].getEntities(e -> Mappers.timedMap.has(e)));
				}
			}
			npcs.add(Juego.player);
			Juego.ENGINE.getSystem(EventSystem.class).setTimedEntities(npcs);
		}
	}
	
	private static void refreshMap() {
		PositionComponent playerPos = Mappers.posMap.get(Juego.player);
		int gx0 = playerPos.getGx();
		int gy0 = playerPos.getGy();
		int gz0 = playerPos.getGz();
		
		zLevel = gz0;
		
		for(int gx = 0; gx < mapInChunks.length; gx++) {
			for(int gy = 0; gy < mapInChunks[0].length; gy++) {
				Chunk chunk;
				String posString = (gx0 - 2 + gx) + ":" + (gy0 - 2 + gy) + ":" + gz0;
				if(chunksInMemory.keySet().contains(posString)) {
					chunk = chunksInMemory.get(posString);
				}else {
					chunk = StateLoader.load(posString);
					chunksInMemory.put(posString, chunk);
				}
				mapInChunks[gx][gy] = chunk;
				Tile[][] chunkMap = chunk.getChunkMap();
				for(int lx = 0; lx < chunkSize; lx++) {
					for(int ly = 0; ly < chunkSize; ly++) {
						mapInTiles[lx + gx*chunkSize][ly + gy*chunkSize] = chunkMap[lx][ly];
					}
				}
			}
		}
	}
	
	public static Chunk getChunk(int x, int y, int z){
		String posString = x + ":" + y + ":" + z;
		Chunk chunk = chunksInMemory.get(posString);
		if(chunk == null) {
			chunk = StateLoader.load(posString);
			chunksInMemory.put(posString, chunk);
		}
		return chunk;
	}
	
	public static Chunk getChunk(Tile tile) {
		int[] coord = tile.getPos().coord;
		return getChunk(coord[0], coord[1], coord[2]);
	}
	
	public static Tile getTile(int x, int y, int z) {
		if(z == zLevel) {
			try {
				int x0 = mapInTiles[0][0].getPos().coord[0];
				int y0 = mapInTiles[0][0].getPos().coord[1];
				return mapInTiles[x - x0][y - y0];
			}catch(ArrayIndexOutOfBoundsException | NullPointerException e) {}
		}
		int gx;
		int gy;
		int lx;
		int ly;
		if(x >= 0) {
			gx = x/chunkSize;
			lx = x%chunkSize;
		}else {
			gx = x/chunkSize - 1;
			lx = chunkSize - Math.abs(x%chunkSize);
			if(lx % chunkSize == 0) {
				gx++;
				lx =0;
			}
		}
		if(y >= 0) {
			gy = y/chunkSize;
			ly = y%chunkSize;
		}else {
			gy = y/chunkSize - 1;
			ly = chunkSize - Math.abs(y%chunkSize);
			if(ly % chunkSize == 0) {
				gy++;
				ly =0;
			}
		}
		Chunk chunk = getChunk(gx, gy, z);
		return chunk.getChunkMap()[lx][ly];
	}
	
	public static PositionComponent getPosition(PositionComponent oldPos, Direction dir) {
		int[] coord = oldPos.coord;
		return getTile(coord[0] + dir.movX, coord[1] + dir.movY, coord[2]).getPos();
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

	//TODO estos mismos métodos pero sin pedir condiciones
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
	
	public static Set<Tile> getCircundatingAreaAsSet(int radius, Tile tile, boolean isRound){
		int x = tile.getPos().coord[0];
		int y = tile.getPos().coord[1];
		int z = tile.getPos().coord[2];
		HashSet<Tile> list = new HashSet<Tile>();
		for(int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				try {
					PositionComponent evalPos = new PositionComponent(x+i, y+j, z);
					if(!isRound || getDistance(tile.getPos(), evalPos) <= radius){
						Tile t = getTile(x+i, y+j, z);
						list.add(t);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		return list;
	}

	public static Tile[][] getCircundatingAreaAsArray(int radius, Tile tile, boolean isRound){
		int x = tile.getPos().coord[0];
		int y = tile.getPos().coord[1];
		int z = tile.getPos().coord[2];
		Tile[][] area = new Tile[(radius*2)+1][(radius*2)+1];
		for (int i = -radius; i <= radius; i++){
			for (int j = -radius; j <= radius; j++){
				try {
					PositionComponent evalPos = new PositionComponent(x+i, y+j, z);
					if(!isRound || getDistance(tile.getPos(), evalPos) <= radius){
						area[i+radius][j+radius] = getTile(x+i, y+j, z);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		return area;
	}
	
	public static Set<Tile> getSquareAreaAsSet(PositionComponent pos, int width, int height) {
		int x0 = pos.coord[0];
		int y0 = pos.coord[1];
		int z = pos.coord[2];
		
		Set<Tile> area = new HashSet<>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				area.add(getTile(x0+x, y0+y, z));
			}
		}
		return area;
	}
	
	public static Tile[][] getSquareAreaAsArray(PositionComponent pos, int width, int height) {
		int x0 = pos.coord[0];
		int y0 = pos.coord[1];
		int z = pos.coord[2];
		
		Tile[][] area = new Tile[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				area[x][y] = getTile(x0+x, y0+y, z);
			}
		}
		return area;
	}
	
	public static double getDistance(PositionComponent start, PositionComponent end){
		double dx = end.coord[0] - start.coord[0];
		double dy = start.coord[1] - end.coord[1];
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	// Bresenham's line algorithm
	// https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
	public static ArrayList<Tile> getStraigthLine(PositionComponent start, PositionComponent end){
		return getStraigthLine(start, end, mapInTiles);
	}
	
	public static ArrayList<Tile> getStraigthLine(PositionComponent start, PositionComponent end, Tile[][] area){
		return getStraigthLine(start, end, area, t -> true);
	}
	
	public static ArrayList<Tile> getStraigthLine(PositionComponent start, PositionComponent end, Tile[][] area, Predicate<Tile> cond){
		int dx = end.coord[0] - start.coord[0];
		int dy = start.coord[1] - end.coord[1];

		int sx = dx > 0 ? 1 : -1; // slope X
		int sy = dy < 0 ? 1 : -1; // slope Y
		
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		
		int err = dx - dy;
		
		ArrayList<Tile> line = new ArrayList<Tile>();
		PositionComponent posInLine = start.clone();
		
		int[] startCoord = start.coord;
		int startXinArea;
		int startYinArea;
		if(area[0][0] == null) {
			startXinArea = area.length/2;
			startYinArea = area[0].length/2;
		}else {
			startXinArea = startCoord[0] - area[0][0].getPos().coord[0];
			startYinArea = startCoord[1] - area[0][0].getPos().coord[1];
		}
		double lineLength = getDistance(start, end);
		do {
			int[] coord = posInLine.coord;
			line.add(area[Math.abs(startXinArea - startCoord[0] + posInLine.coord[0])][Math.abs(startYinArea - startCoord[1] + posInLine.coord[1])]);
			 
			int e2 = 2 * err;
			
			if (e2 > -dy) {
				err -= dy;
				coord[0] += sx;
			}
			if (e2 < dx) {
				err += dx;
				coord[1] += sy;
			}
		} while (cond.test(posInLine.getTile()) && lineLength >= getDistance(start, posInLine));
		return line;
	}
	
	/**
	 * Usa flood fill solo en los tiles transitables y devuelve el primero que encuentra con la condicion dada
	 * @param origin el origen de la busqueda
	 * @param cond
	 * @return
	 */
	public static Tile getClosestTile(Tile origin, Predicate<Tile> cond){
		Set<Tile> possibleTiles = getAdjacentTiles(origin, t -> t.isTransitable());
		for(int i = 0; i < chunkSize; i++){
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
	
	public static Chunk[][] getMapInChunks() {
		return mapInChunks;
	}

	public static Tile[][] getMapInTiles() {
		return mapInTiles;
	}

	public static LinkedHashMap<String, Chunk> getChunksInMemory() {
		return chunksInMemory;
	}
}
