package cave;

import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.PositionComponent;
import components.Type;
import fatories.TerrainFactory;
import main.Tile;
import world.Explorer;

public class Miner {
	
	private static int diggedTiles = 0;
	private PositionComponent position;
	private static final Entity DIRT_FLOOR = TerrainFactory.get("dirt floor");
	
	private Set<Tile> floorTiles;
	
	public Miner(PositionComponent pos, Set<Tile> floorTiles) {
		position = pos;
		this.floorTiles = floorTiles;
		pos.getTile().put(DIRT_FLOOR);
		floorTiles.add(pos.getTile());
	}
	
	public void dig() {
		Set<Tile> validTiles = Explorer.getOrthogonalTiles(position.getTile(), t -> t.get(Type.TERRAIN) == null);
		Tile tile = null;
		if(validTiles.isEmpty()) {
			for(Tile floor : floorTiles) {
				if(Explorer.isAdjacent(floor, t -> !t.isTransitable())){
					tile = floor;
					break;
				}
			}
		}
		else {
			tile = RNG.getRandom(validTiles);
		}
		tile.put(DIRT_FLOOR);
		floorTiles.add(tile);
		position = tile.getPos();
		diggedTiles++;
	}

	public PositionComponent getPosition() {
		return position;
	}

	public static int getDiggedTiles() {
		return diggedTiles;
	}

}
