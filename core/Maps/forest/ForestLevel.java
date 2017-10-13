package forest;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Type;
import cosas.Chunk;
import cosas.Tile;
import fatories.TerrainFactory;

public class ForestLevel extends Chunk{
	
	public ForestLevel(int posX, int posY){
		globalPosX = posX;
		globalPosY = posY;
		
		Entity grassFloor = TerrainFactory.get("grass floor");
		Consumer<Tile> createGrassFloor = t -> t.put(grassFloor);
		fillLevel(createGrassFloor);
		
		buildLevel();
		createHouses();
	}
	
	@Override
	protected void buildLevel() {
		for(int i = 0; i < RNG.nextInt(400, 600); i++){
//			Tile tile = RNG.getRandom(chunkMap, t -> t.isTransitable() && t.get(Type.FEATURE) == null);
//			
//			Entity feature = RNG.nextInt(100) < 50 ? new Tree() : new Bush();
//			
//			addFeature(tile, feature);
		}
	}
	
	private void createHouses(){
		Entity houseWall = TerrainFactory.get("wooden wall");
		Entity houseFloor = TerrainFactory.get("wooden floor");
		
		int sizeX = RNG.nextInt(5, 11);
		int sizeY = RNG.nextInt(5, 11);
		int cornerX = RNG.nextInt(getChunkMap().length - 20) + 10;
		int cornerY = RNG.nextInt(getChunkMap().length - 20) + 10;
		
		Set<Tile> walls = new HashSet<>();
		
		for(int i = 0; i < sizeX; i++){
			for(int j = 0; j < sizeY; j++){
				Tile tile = getChunkMap()[cornerX + i][cornerY + j];
				if(i == 0 || j == 0 || i == sizeX - 1 || j == sizeY - 1){
					tile.put(houseWall);
					walls.add(tile);
				}else{
					tile.put(houseFloor);
				}
				tile.remove(Type.FEATURE);
			}
		}
//		Tile doorTile = RNG.getRandom(walls, t -> World.getAdjacentTiles(t, ti -> ti.getTerrain().getName().equals("wooden floor")).size() > 1);
//		doorTile.setFeature(new Door());
//		doorTile.put(houseFloor);
	}

}
