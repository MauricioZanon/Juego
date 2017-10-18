package forest;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Type;
import factories.FeatureFactory;
import factories.TerrainFactory;
import main.Chunk;
import main.Tile;
import world.World;

public class ForestLevel extends Chunk{
	
	public ForestLevel(int posX, int posY){
		globalPosX = posX;
		globalPosY = posY;
		
		Entity grassFloor = TerrainFactory.get("grass floor");
		Consumer<Tile> createGrassFloor = t -> t.put(grassFloor);
		fillLevel(createGrassFloor);
		
		buildLevel();
	}
	
	@Override
	protected void buildLevel() {
		for(int i = 0; i < RNG.nextGaussian(World.CHUNK_SIZE / 2, World.CHUNK_SIZE / 10); i++){
			Tile tile = RNG.getRandom(chunkMap, t -> t.isTransitable() && t.get(Type.FEATURE) == null);
			
			Entity feature = FeatureFactory.get("tree");
			
			addFeature(tile, feature);
		}
	}
	

}
