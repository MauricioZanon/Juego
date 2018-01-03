package dungeon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import dungeon.DungeonBuilder.DungeonSize;
import dungeon.DungeonBuilder.DungeonType;
import factories.FeatureFactory;
import factories.ItemFactory;
import factories.NPCFactory;
import factories.TerrainFactory;
import main.Blueprint;
import main.RoomFactory;
import main.Tile;
import pathFind.PathFinder;
import world.Direction;
import world.Explorer;

public class DungeonLevel {
	
	private Set<Room> rooms = new HashSet<>();
	private Set<Tile> doors = new HashSet<>();
	private PositionComponent upStair = null;
	private PositionComponent downStair = null;
	private ArrayList<Tile> availableAnchors = new ArrayList<>();
	
	private final Entity FLOOR = TerrainFactory.get("concrete floor");
	private final Entity WALL = TerrainFactory.get("concrete wall");
	
	private boolean validLevel = true;
	
	public DungeonLevel(PositionComponent exitStairPos, DungeonType type, DungeonSize size) {
		int requestedRooms = size.roomQuantity;
		
		createFirstRoom(exitStairPos);
		
		while(rooms.size() < requestedRooms) {
			PositionComponent anchorPos = RNG.getRandom(availableAnchors).getPos();
			createRoom(anchorPos);
		}
		
		createLoops();
		putDoors();
		putStairs();
		putEnemies();
		putItems();
	}

	/**
	 * Crea la primer habitación del nivel
	 * @param exitStairPos: El lugar en el que debe ir la escalera al nivel superior
	 */
	private void createFirstRoom(PositionComponent exitStairPos) {
		Blueprint bp = RoomFactory.createDungeonStartingRoomBlueprint();
		int[] startingPosCorrection = bp.getStairsAnchor();
		PositionComponent startingPos = exitStairPos.clone();
		startingPos.coord[0] -= startingPosCorrection[0];
		startingPos.coord[1] -= startingPosCorrection[1];
		
		buildRoom(startingPos, null, bp);
	}
	
	/**
	 * Crea una habitación genérica en el nivel
	 * @param anchorPos: Es la posición en el nivel a la que estará unida la nueva habitación
	 */
	private void createRoom(PositionComponent anchorPos) {
		Blueprint bp = RoomFactory.createDungeonRoomBlueprint();
		Tile emptyTile = RNG.getRandom(Explorer.getOrthogonalTiles(anchorPos.getTile(), t -> t.get(Type.TERRAIN) == null));
		if(emptyTile == null) return;
		Direction bpDirection = Direction.get(anchorPos, emptyTile.getPos());
		List<Integer[]> posibleAnchors = bp.getAnchors(bpDirection);
		if(posibleAnchors.isEmpty()) return;
		Integer[] bpAnchor = RNG.getRandom(posibleAnchors);
		PositionComponent startingPos = anchorPos.clone();
		
		startingPos.coord[0] -= bpAnchor[0];
		startingPos.coord[1] -= bpAnchor[1];
		
		buildRoom(startingPos, anchorPos.getTile(), bp);
	}


	private void buildRoom(PositionComponent startingPos, Tile entranceTile, Blueprint bp) {
		Set<Tile> roomTiles = new HashSet<>();
		Set<Tile> doorTiles = new HashSet<>();
		Tile upStairTile = null;
		Tile downStairTile = null;
		Set<Tile> newAnchorTiles = new HashSet<>();
		
		char[][] bpArray = bp.getArray();
		for(int i = 0; i < bpArray.length; i++) {
			for(int j = 0; j < bpArray[0].length; j++) {
				Tile tile = Explorer.getTile(startingPos.coord[0] + i, startingPos.coord[1] + j, startingPos.coord[2]);
				char symbol = bpArray[i][j];
				
				switch(symbol) {
				case '.':
					roomTiles.add(tile);
					break;
				case 'u':
					newAnchorTiles.add(tile);
					break;
				case '+':
					roomTiles.add(tile);
					doorTiles.add(tile);
					break;
				case '>':
					roomTiles.add(tile);
					downStairTile = tile;
					break;
				case '<':
					roomTiles.add(tile);
					upStairTile = tile;
					break;
				}
			}
		}
		
		if(isValidRoom(roomTiles)) {
			buildRoom(roomTiles);
			rooms.add(new Room(roomTiles));
			doors.addAll(doorTiles);
			if(entranceTile != null) doors.add(entranceTile);
			upStair = upStairTile == null ? upStair: upStairTile.getPos();
			downStair = downStairTile == null ? downStair : downStairTile.getPos();
			availableAnchors.addAll(newAnchorTiles);
			availableAnchors.remove(entranceTile);
		}
		
	}
	
	private boolean isValidRoom(Set<Tile> roomTiles) {
		for(Tile tile : roomTiles) {
			if(tile.get(Type.TERRAIN) != null) return false;
		}
		return true;
	}

	private void buildRoom(Set<Tile> roomTiles) {
		for(Tile floorTile : roomTiles) {
			floorTile.put(FLOOR);
			Explorer.getAdjacentTiles(floorTile, t -> t.get(Type.TERRAIN) == null).forEach(t -> t.put(WALL));
		}
	}
	
	//FIXME: no crea loops
	private void createLoops() {
		for(Tile tile : availableAnchors) {
			Object[] tiles = Explorer.getOrthogonalTiles(tile, t -> FLOOR.equals(t.get(Type.TERRAIN))).toArray();
			if(tiles.length == 2 && PathFinder.getWalkableDistance((Tile)tiles[0], (Tile)tiles[1]) > 20){
				doors.add(tile);
			}
		}
	}
	
	private void putDoors() {
		for(Tile tile : doors) {
			if(tile.isEmpty()) {
				tile.put(FLOOR);
				tile.put(FeatureFactory.createFeature("door"));
			}
		}
	}
	
	private void putStairs() {
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
	
	private void putEnemies() {
		int quantity = 5;//RNG.nextGaussian(rooms.size()/2, rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		while(quantity > 0) {
			Entity npc = NPCFactory.createNPC();
			Tile tile = RNG.getRandom(availableTiles, t -> t.get(Type.FEATURE) == null);
			npc.add(tile.getPos().clone());
			tile.put(npc);
			quantity--;
		}
	}
	
	private void putItems() {
		int quantity = RNG.nextGaussian(rooms.size(), rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		while(quantity > 0) {
			Entity item = ItemFactory.createRandomItem();
			Tile tile = RNG.getRandom(availableTiles, t -> t.get(Type.FEATURE) == null);
			tile.put(item);
			quantity--;
		}
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
