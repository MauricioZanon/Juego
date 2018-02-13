package cave;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.PositionComponent;
import components.Type;
import eventSystem.Map;
import factories.TerrainFactory;
import main.Tile;

public class Miner {
	
	private PositionComponent position;
	private static final Entity DIRT_FLOOR = TerrainFactory.get("dirt floor");
	protected static Set<Tile> floorTiles = new HashSet<>();
	
	protected boolean activated = true;
	
	public Miner(Tile startingTile, Tile[][] caveArea) {
		position = startingTile.getPos();
		startingTile.put(DIRT_FLOOR);
		floorTiles.add(startingTile);
	}
	
	public void dig() {
		Set<Tile> validTiles = Map.getOrthogonalTiles(position.getTile(), t -> t.get(Type.TERRAIN) == null);
		if(!validTiles.isEmpty()) {
			Tile tile = RNG.getRandom(validTiles);
			tile.put(DIRT_FLOOR);
			position = tile.getPos();
			floorTiles.add(tile);
		}else {
			activated = false;
		}
	}
	
	public Miner reproduce(Tile[][] caveArea) {
		
		Tile tile = RNG.getRandom(floorTiles, t -> Map.isOrthogonallyAdjacent(t, ti -> ti.get(Type.TERRAIN) == null));
		if(tile == null) {
			return null;
		}else {
			return new Miner(tile, caveArea);
		}
	}

	public PositionComponent getPosition() {
		return position;
	}

	public static int getDiggedTiles() {
		return floorTiles.size();
	}

}
