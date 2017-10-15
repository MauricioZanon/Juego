package field;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import fatories.TerrainFactory;
import main.Chunk;
import main.Tile;

public class FieldLevel extends Chunk{
	
	public FieldLevel(int posX, int posY){
		globalPosX = posX;
		globalPosY = posY;
		Entity grassFloor = TerrainFactory.get("grass floor");
		Consumer<Tile> createGrassFloor = t -> t.put(grassFloor);
		fillLevel(createGrassFloor);
		buildLevel();
	}

	@Override
	protected void buildLevel() {
		
	}

}
