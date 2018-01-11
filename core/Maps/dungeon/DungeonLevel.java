package dungeon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import factories.FeatureFactory;
import factories.TerrainFactory;
import main.Tile;

public abstract class DungeonLevel {
	
	protected Set<Room> rooms = new HashSet<>();
	protected Set<Tile> doors = new HashSet<>();
	protected PositionComponent upStair = null;
	protected PositionComponent downStair = null;
	protected ArrayList<Tile> availableAnchors = new ArrayList<>();
	
	protected final Entity FLOOR = TerrainFactory.get("concrete floor");
	protected final Entity WALL = TerrainFactory.get("concrete wall");
	
	protected boolean validLevel = true;
	
	protected void putDoors() {
		for(Tile tile : doors) {
			if(tile.isEmpty()) {
				tile.put(FeatureFactory.createFeature("door"));
			}
		}
	}
	
	protected void putStairs() {
		if(upStair != null) {
			Entity stair = FeatureFactory.createFeature("stair");
			stair.add(upStair);
			Mappers.graphMap.get(stair).ASCII = "<";
			upStair.getTile().put(stair);
		}
		Room room = RNG.getRandom(rooms, r -> r.getDoorTiles().size() == 1);
		downStair = RNG.getRandom(room.getFloorTiles()).getPos();
		Entity stair = FeatureFactory.createFeature("stair");
		stair.add(downStair);
		Mappers.graphMap.get(stair).ASCII = ">";
		downStair.getTile().put(stair);
	}
	
	public Set<Room> getRooms() {
		return rooms;
	}

	public Set<Tile> getDoors() {
		return doors;
	}

	public PositionComponent getUpStair() {
		return upStair;
	}

	public PositionComponent getDownStair() {
		return downStair;
	}

	public boolean isValidLevel() {
		return validLevel;
	}

}
