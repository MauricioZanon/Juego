package dungeon;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import FOV.VisionCalculator;
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
	private LinkedList<Tile> tilesForExpantion = new LinkedList<>();
	
	private final Entity FLOOR = TerrainFactory.get("concrete floor");
	private final Entity WALL = TerrainFactory.get("concrete wall");
	
	private boolean validLevel = true;
	
	
	public DungeonLevel(PositionComponent startingPos, DungeonType type, DungeonSize size){
		Blueprint firstRoom = RoomFactory.createDungeonStartingRoomBlueprint();
		PositionComponent correctedPos = firstRoom.correctPosition(startingPos);
		createNewRoom(firstRoom, startingPos, correctedPos);
		
		int requestedRooms = size.roomQuantity;
		int attempts = 0;
		
		while(!tilesForExpantion.isEmpty() && rooms.size() < requestedRooms && attempts++ < 3000){
			PositionComponent pos = tilesForExpantion.removeFirst().getPos();//RNG.getRandom(tilesForExpantion).getPos();
			Blueprint bp = RoomFactory.createDungeonRoomBlueprint();
			
			Set<Tile> emptyPositions = Explorer.getOrthogonalTiles(pos.getTile(), t -> t.get(Type.TERRAIN) == null);
			if(emptyPositions.isEmpty()) {
				attempts++;
				continue;
			}
			PositionComponent emptyPos = RNG.getRandom(emptyPositions).getPos();
			Direction anchorDir = Direction.get(pos, emptyPos);
			PositionComponent correctedPos2 = bp.correctPosition(pos, anchorDir);
			
			createNewRoom(bp, pos, correctedPos2);
		}
		
		if(attempts >= 3000){
			validLevel = false;
			discard();
			return;
		}
		createLoops();
		putStairs();
		putEnemies();
		putItems();
	}
	
	private void createNewRoom(Blueprint bp, PositionComponent startingPos, PositionComponent correctedPos) {
		Set<Tile> roomTiles = new HashSet<>();
		Set<Tile> doorTiles = new HashSet<>();
		Tile upStairTile = null;
		Tile downStairTile = null;
		Set<Tile> newExpansionTiles = new HashSet<>();
		
		String[][] blueprintArray = bp.getArray();
		for(int x = 0; x < blueprintArray.length; x++){
			for(int y = 0; y < blueprintArray[0].length; y++){
				Tile tile = correctedPos.getTile();
				String ASCII = blueprintArray[x][y];
				
				if(ASCII.equals(".")){
					if(!validTile(tile)) {
						return;
					}
					roomTiles.add(tile);
				}else if (ASCII.equals("u")){
					newExpansionTiles.add(tile);
				}else if(ASCII.equals("+")){
					roomTiles.add(tile);
					doorTiles.add(tile);
				}else if(ASCII.equals(">")){
					roomTiles.add(tile);
					downStairTile = tile;
				}else if(ASCII.equals("<")){
					roomTiles.add(tile);
					upStairTile = tile;
				}
				correctedPos.setLy(correctedPos.getLy() - 1);
			}
			correctedPos.setLy(correctedPos.getLy() + blueprintArray[0].length);
			correctedPos.setLx(correctedPos.getLx() + 1);
		}
		
		roomTiles.add(startingPos.getTile());
		tilesForExpantion.addAll(newExpansionTiles);
		tilesForExpantion.remove(startingPos.getTile());
		buildRoom(roomTiles);
		rooms.add(new Room(roomTiles));
		if(!doorTiles.isEmpty()){
			doors.addAll(doorTiles);
		}
		doors.add(startingPos.getTile());
		if(downStairTile != null && downStair == null){
			downStair = downStairTile.getPos();
		}
		if(upStairTile != null && upStair == null){
			upStair = startingPos;
		}
		
		
	}
	
	private boolean validTile(Tile tile){
		return tile.get(Type.TERRAIN) == null || tilesForExpantion.contains(tile);
	}
	
	private void buildRoom(Set<Tile> roomTiles){
		roomTiles.forEach(t -> t.put(FLOOR));
		Set<Tile> wallTiles = new HashSet<>();
		for(Tile tile : roomTiles){
			wallTiles.addAll(Explorer.getAdjacentTiles(tile, t -> t.get(Type.TERRAIN) == null));
		}
		wallTiles.forEach(t -> t.put(WALL));
		
	}
	
	//FIXME: muy pocos loops y casi siempre se hacen al lado de otra puerta
	private void createLoops() {
		for(Tile tile : tilesForExpantion) {
			Object[] tiles = Explorer.getOrthogonalTiles(tile, t -> FLOOR.equals(t.get(Type.TERRAIN))).toArray();
			if(tiles.length == 2 && PathFinder.getWalkableDistance((Tile)tiles[0], (Tile)tiles[1]) > 20){
//				Door door = new Door();
//				tile.setFeature(door);
				tile.put(FLOOR);
			}
		}
	}
	
	private void putStairs() {
		if(upStair != null) {
			Entity stair = FeatureFactory.get("stair");
			stair.add(upStair);
			Mappers.graphMap.get(stair).ASCII = "<";
			upStair.getTile().put(stair);
		}
		if(downStair != null) {
			Entity stair = FeatureFactory.get("stair");
			stair.add(downStair);
			Mappers.graphMap.get(stair).ASCII = ">";
			downStair.getTile().put(stair);
		}
		else {
			Room room = RNG.getRandom(rooms);//, r -> r.getDoorTiles().size() == 1);
			downStair = RNG.getRandom(room.getFloorTiles()).getPos();
			Entity stair = FeatureFactory.get("stair");
			stair.add(downStair);
			Mappers.graphMap.get(stair).ASCII = ">";
			downStair.getTile().put(stair);
		}
	}
	
	private void putEnemies() {
		int quantity = RNG.nextGaussian(rooms.size(), rooms.size()/3);
		Set<Tile> availableTiles = new HashSet<>();
		rooms.forEach(r -> availableTiles.addAll(r.getFloorTiles()));
		while(quantity > 0) {
			Entity npc = NPCFactory.createNPC();
			Tile tile = RNG.getRandom(availableTiles, t -> t.get(Type.FEATURE) == null);
			npc.add(tile.getPos().clone());
			tile.put(npc);
			VisionCalculator.calculateVision(npc);
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
	
	private void discard() {
		for(Room room : rooms) {
			Set<Tile> tiles = room.getFloorTiles();
			for(Tile tile : tiles) {
				tile.remove(Type.TERRAIN);
				tile.remove(Type.FEATURE);
			}
		}
	}

	public boolean isValidLevel() {
		return validLevel;
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
	
}
