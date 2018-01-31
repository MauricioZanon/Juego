package main;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import components.PositionComponent;
import world.World;

public abstract class Chunk{
	
	protected Tile[][] chunkMap = new Tile[World.CHUNK_SIZE][World.CHUNK_SIZE];
	
	protected int globalPosX = 0;
	protected int globalPosY = 0;
	protected int globalPosZ = 0;
	
	protected Set<Entity> npcList = new HashSet<>();
	protected Set<Entity> featureList = new HashSet<>();
	
	protected void fillLevel(Consumer<Tile> createNewTerrain){
		int size = World.CHUNK_SIZE;
		chunkMap = new Tile[size][size];
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				chunkMap[x][y] = new Tile(new PositionComponent(globalPosX*chunkMap.length + x, globalPosY*chunkMap.length + y, globalPosZ));
				createNewTerrain.accept(chunkMap[x][y]);
			}
		} 
	}  
	
	protected abstract void buildLevel();
	
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
	
	public Set<Entity> getNpcList() {
		return npcList;
	}

	public Set<Entity> getFeaturesLocations() {
		return featureList;
	}

	public Tile[][] getChunkMap() {
		return chunkMap;
	}

	public int getGlobalPosX() {
		return globalPosX;
	}

	public int getGlobalPosY() {
		return globalPosY;
	}
	
	public int getGlobalPosZ() {
		return globalPosZ;
	}

}
