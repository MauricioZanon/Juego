package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import components.PositionComponent;
import factories.EntityFactory;
import world.World;

public class Chunk{
	
	protected Tile[][] chunkMap = new Tile[World.CHUNK_SIZE][World.CHUNK_SIZE];
	
	protected int gx = 0;
	protected int gy = 0;
	protected int gz = 0;
	
	public Chunk() {}
	
	public Chunk(String chunkCoord, String chunkString) {
		int chunkSize = World.CHUNK_SIZE;
    	int[] coords = Arrays.stream(chunkCoord.split(":")).mapToInt(Integer::parseInt).toArray();
    	gx = coords[0];
    	gy = coords[1];
    	gz = coords[2];
    	
    	fillLevel(t -> {});
    	
    	String[] tileStrings = chunkString.split("/");
    	for(int i = 0; i < tileStrings.length; i++) {
    		String[] entitiesStrings = tileStrings[i].split(",");
    		
    		PositionComponent tilePos = Juego.ENGINE.createComponent(PositionComponent.class);
    		int tileX = i/chunkSize;
    		int tileY = i%chunkSize;
    		tilePos.coord = new int[] {gx*chunkSize + tileX, gy*chunkSize + tileY, gz};
    		Tile t = new Tile(tilePos);
    		for(int j = 1; j < entitiesStrings.length; j++) {
    			t.put(EntityFactory.create(Integer.parseInt(entitiesStrings[j])));
    		}
    		chunkMap[tileX][tileY] = t;
    	}
    }
	
	protected void fillLevel(Consumer<Tile> createNewTerrain){
		int size = World.CHUNK_SIZE;
		chunkMap = new Tile[size][size];
		int x0 = gx*chunkMap.length;
		int y0 = gy*chunkMap.length;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				PositionComponent tilePos = Juego.ENGINE.createComponent(PositionComponent.class);
				tilePos.coord = new int[] {x0+x, y0+y, gz};
				chunkMap[x][y] = new Tile(tilePos);
				createNewTerrain.accept(chunkMap[x][y]);
			}
		} 
	}  
	
	protected void buildLevel() {};
	
	public Set<Entity> getEntities(Predicate<Entity> cond){
		Set<Entity> result = new HashSet<>();
		for(int x = 0; x < chunkMap.length; x++) {
			for(int y = 0; y < chunkMap[0].length; y++) {
				result.addAll(chunkMap[x][y].getEntities(cond));
			}
		}
		return result;
	}
	
	public Tile[][] getChunkMap() {
		return chunkMap;
	}
	
	public String getPosAsString() {
		return gx + ":" + gy + ":" + gz;
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		for(int x = 0; x < chunkMap.length; x++) {
			for(int y = 0; y < chunkMap[0].length; y++) {
				Tile tile = chunkMap[x][y];
				if(tile != null) {
					chunkMap[x][y].serialize(sb);
				}
			}
		}
		
		return sb.toString();
	}

	public int getGx() {
		return gx;
	}

	public int getGy() {
		return gy;
	}
	
	public int getGz() {
		return gz;
	}

}
