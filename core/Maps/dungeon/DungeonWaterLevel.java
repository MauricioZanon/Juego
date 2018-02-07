package dungeon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import dungeon.DungeonBuilder.DungeonSize;
import eventSystem.Map;
import factories.ItemFactory;
import factories.NPCFactory;
import factories.TerrainFactory;
import main.Blueprint;
import main.RoomFactory;
import main.Tile;
import world.Direction;

public class DungeonWaterLevel extends DungeonLevel{
	
	public DungeonWaterLevel(PositionComponent exitStairPos, DungeonSize size) {
		int requestedRooms = size.roomQuantity;
		
		while(rooms.isEmpty()) {
			createFirstRoom(exitStairPos);
		}
		
		while(rooms.size() < requestedRooms) {
			if(availableAnchors.isEmpty()) {
				validLevel = false;
				return;
			}
			PositionComponent anchorPos = RNG.getRandom(availableAnchors).getPos();
			createRoom(anchorPos);
		}
		
		putDoors();
		putStairs();
		putEnemies();
		putItems();
		putPuddles();
		putDeepWater();
	}
	
	private void createFirstRoom(PositionComponent exitStairPos) {
		Blueprint bp = RoomFactory.createRoom("Dungeon starting rooms");
		int[] startingPosCorrection = bp.getStairsAnchor();
		PositionComponent startingPos = exitStairPos.clone();
		startingPos.coord[0] -= startingPosCorrection[0];
		startingPos.coord[1] -= startingPosCorrection[1];
		
		buildRoom(startingPos, null, bp);
	}
	
	private void createRoom(PositionComponent anchorPos) {
		Tile emptyTile = RNG.getRandom(Map.getOrthogonalTiles(anchorPos.getTile(), t -> t.get(Type.TERRAIN) == null));
		if(emptyTile == null) return;
		Direction bpDirection = Direction.get(anchorPos, emptyTile.getPos());
		Blueprint bp;
		if(RNG.nextInt(100) < 90) {
			bp = RoomFactory.createRoom("Dungeon rooms", bpDirection);
		}else {
			bp = RoomFactory.createRoom("Dungeon water rooms", bpDirection);
		}
		List<Integer[]> posibleAnchors = bp.getAnchors(bpDirection);
		Integer[] bpAnchor = RNG.getRandom(posibleAnchors);
		
		PositionComponent startingPos = anchorPos.clone();
		startingPos.coord[0] -= bpAnchor[0];
		startingPos.coord[1] -= bpAnchor[1];
		
		buildRoom(startingPos, anchorPos.getTile(), bp);
	}


	private void buildRoom(PositionComponent startingPos, Tile entranceTile, Blueprint bp) {
		Set<Tile> roomTiles = new HashSet<>();
		Set<Tile> waterTiles = new HashSet<>();
		Set<Tile> doorTiles = new HashSet<>();
		Tile upStairTile = null;
		Tile downStairTile = null;
		Set<Tile> newAnchorTiles = new HashSet<>();
		
		char[][] bpArray = bp.getArray();
		for(int i = 0; i < bpArray.length; i++) {
			for(int j = 0; j < bpArray[0].length; j++) {
				Tile tile = Map.getTile(startingPos.coord[0] + i, startingPos.coord[1] + j, startingPos.coord[2]);
				
				char symbol = bpArray[i][j];
				switch(symbol) {
				case '.':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					break;
				case '÷':
					if(!isValidTile(tile)) return;
					waterTiles.add(tile);
					break;
				case 'u':
					newAnchorTiles.add(tile);
					break;
				case '+':
					roomTiles.add(tile);
					doorTiles.add(tile);
					break;
				case '>':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					downStairTile = tile;
					break;
				case '<':
					if(!isValidTile(tile)) return;
					roomTiles.add(tile);
					upStairTile = tile;
					break;
				}
			}
		}
		
		if(entranceTile != null) {
			roomTiles.add(entranceTile);
			doors.add(entranceTile);
		}
		buildRoom(roomTiles);
		roomTiles.addAll(waterTiles);
		rooms.add(new Room(roomTiles));
		doors.addAll(doorTiles);
		upStair = upStairTile == null ? upStair: upStairTile.getPos();
		downStair = downStairTile == null ? downStair : downStairTile.getPos();
		availableAnchors.addAll(newAnchorTiles);
		availableAnchors.remove(entranceTile);
		putDeepWater(waterTiles);
	}
	
	private void buildRoom(Set<Tile> roomTiles) {
		for(Tile floorTile : roomTiles) {
			floorTile.put(FLOOR);
			Map.getAdjacentTiles(floorTile, t -> t.get(Type.TERRAIN) == null).forEach(t -> t.put(WALL));
		}
	}
	
	private void putDeepWater(Set<Tile> waterTiles) {
		for(Tile floorTile : waterTiles) {
			floorTile.put(TerrainFactory.get("deep water"));
			Map.getAdjacentTiles(floorTile, t -> t.get(Type.TERRAIN) == null).forEach(t -> t.put(WALL));
		}
	}
	
	private void putEnemies() {
		int quantity = RNG.nextGaussian(rooms.size()/2, rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		Predicate<Tile> isValidTile = t -> Mappers.descMap.get(t.get(Type.TERRAIN)).name.equals("concrete floor") && t.get(Type.FEATURE) == null;
		while(quantity > 0) {
			Entity npc = NPCFactory.createNPC();
			Tile tile = RNG.getRandom(availableTiles, isValidTile);
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
	
	private void putPuddles() {
		Entity shallowWater = TerrainFactory.get("shallow water");
		Predicate<Tile> isConcreteFloor = t -> Mappers.descMap.get(t.get(Type.TERRAIN)).name.equals("concrete floor");
		for(int i = 0; i < rooms.size()*4; i++) {
			Tile initialTile = RNG.getRandom(RNG.getRandom(rooms).getFloorTiles());
			initialTile.put(shallowWater);
			int spreadChance = 90;
			while(RNG.nextInt(100) < spreadChance) {
				initialTile = RNG.getRandom(Map.getOrthogonalTiles(initialTile, isConcreteFloor));
				if(initialTile == null) break;
				initialTile.put(shallowWater);
				spreadChance -= 5;
			}
		}
	}
	
	private void putDeepWater() {
		Entity shallowWater = TerrainFactory.get("deep water");
		Predicate<Tile> isConcreteFloor = t -> Mappers.descMap.get(t.get(Type.TERRAIN)).name.equals("concrete floor");
		for(int i = 0; i < rooms.size()*3; i++) {
			Tile initialTile = RNG.getRandom(RNG.getRandom(rooms).getFloorTiles());
			initialTile.put(shallowWater);
			int spreadChance = 90;
			while(RNG.nextInt(100) < spreadChance) {
				initialTile = RNG.getRandom(Map.getOrthogonalTiles(initialTile, isConcreteFloor));
				if(initialTile == null) break;
				initialTile.put(shallowWater);
				spreadChance -= 3;
			}
		}
	}
	
}
