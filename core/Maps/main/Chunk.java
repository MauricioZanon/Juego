package main;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Entity;

import components.PositionComponent;
import factories.EntityFactory;
import world.World;

public class Chunk{
	
	protected Tile[][] chunkMap = new Tile[World.CHUNK_SIZE][World.CHUNK_SIZE];
	
	protected int gx = 0;
	protected int gy = 0;
	protected int gz = 0;
	
	protected Set<Entity> npcList = new HashSet<>();
	protected Set<Entity> featureList = new HashSet<>();
	
	public Chunk() {}
	
	public Chunk(String chunkCoord, String chunkString) {
    	int[] coords = Arrays.stream(chunkCoord.split(":")).mapToInt(Integer::parseInt).toArray();
    	gx = coords[0];
    	gy = coords[1];
    	gz = coords[2];
    	String[] tileStrings = chunkString.split("/");
    	for(int i = 0; i < tileStrings.length; i++) {
    		String[] entitiesStrings = tileStrings[i].split("-");
    		int[] pos = Arrays.stream(entitiesStrings[0].split(":")).mapToInt(Integer::parseInt).toArray();
    		Tile t = new Tile(new PositionComponent(pos));
    		for(int j = 1; j < entitiesStrings.length; j++) {
    			t.put(EntityFactory.create(Integer.parseInt(entitiesStrings[j])));
    		}
    		chunkMap[pos[0]%chunkMap.length][pos[1]%chunkMap[0].length] = t;
    	}
    }
	
	protected void fillLevel(Consumer<Tile> createNewTerrain){
		int size = World.CHUNK_SIZE;
		chunkMap = new Tile[size][size];
		int x0 = gx*chunkMap.length;
		int y0 = gy*chunkMap.length;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				chunkMap[x][y] = new Tile(new PositionComponent(x0 + x, y0 + y, gz));
				createNewTerrain.accept(chunkMap[x][y]);
			}
		} 
	}  
	
	protected void buildLevel() {};
	
	public void addNPC(Tile tile, Entity npc){
		tile.put(npc);
		npcList.add(npc);
	}
	
	public void removeNPC(Entity npc){
		npcList.remove(npc);
		npc.getComponent(PositionComponent.class).getTile().remove(npc);
	}
	
	public void addFeature(Tile tile, Entity feature){
		tile.put(feature);
		featureList.add(feature);
	}
	
	public boolean isBorder(Tile tile){
		int X = tile.getPos().getLx();
		int Y = tile.getPos().getLy();
		return X == 0 || X == chunkMap.length - 1 || Y == 0 || Y == chunkMap[0].length - 1;
	}
	
	public Set<Entity> getEntities(Predicate<Entity> cond){
		Set<Entity> result = new HashSet<>();
		for(int x = 0; x < chunkMap.length; x++) {
			for(int y = 0; y < chunkMap[0].length; y++) {
				result.addAll(chunkMap[x][y].getEntities(cond));
			}
		}
		return result;
	}
	
	public Set<Entity> getNpcList() {
		return npcList;
	}

	public Set<Entity> getFeaturesLocations() {
		return featureList;
	}

	public Tile[][] getChunkMap() {
		return chunkMap;
	}
	
	public String getPosAsString() {
		return gx + ":" + gy + ":" + gz;
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
