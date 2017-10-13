package mountain;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import cosas.Chunk;
import cosas.Tile;
import fatories.TerrainFactory;

public class MountainLevel extends Chunk{
	
	private final float BOULDER_DENSITY = 0.3f;
	
	public MountainLevel(int posX, int posY){
		globalPosX = posX;
		globalPosY = posY;
		Entity floor = TerrainFactory.get("dirt floor");
		Consumer<Tile> createDirtFloor = t -> t.put(floor);
		fillLevel(createDirtFloor);
		buildLevel();
	}

	@Override
	protected void buildLevel() {
		int boulders = (int) (chunkMap.length * chunkMap[0].length * BOULDER_DENSITY);
		for(int i = 0; i < boulders; i++){
//			Tile tile = RNG.getRandom(chunkMap, t -> t.isTransitable() && t.get(Type.FEATURE) == null);
//			addFeature(tile, new Boulder(tile.getPos()));
		}
	}

}
