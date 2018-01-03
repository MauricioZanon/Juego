package main;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import RNG.RNG;
import XPReader.REXLoader;

public abstract class RoomFactory {
	
	private static Set<Blueprint> dungeonRooms = new HashSet<>();
	private static Set<Blueprint> dungeonStartingRooms = new HashSet<>();
	private static Set<Blueprint> houses = new HashSet<>();
	
	private static final String DUNGEON_ROOMS_PATH = "../core/assets/Rooms and buildings/Dungeon rooms";
	private static final String DUNGEON_STARTING_ROOMS_PATH = "../core/assets/Rooms and buildings/Dungeon starting rooms";
	private static final String HOUSES_PATH = "../core/assets/Rooms and buildings/Houses";
	
	static{
		try {
			loadRooms(dungeonRooms, DUNGEON_ROOMS_PATH);
			loadRooms(dungeonStartingRooms, DUNGEON_STARTING_ROOMS_PATH);
			loadRooms(houses, HOUSES_PATH);
		} catch (IOException e) {e.printStackTrace();}
	}

	private static void loadRooms(Set<Blueprint> rooms, String roomsPath) throws IOException {
		File[] roomFiles = new File(roomsPath).listFiles();
		for(File f : roomFiles) {
			rooms.add(new Blueprint(REXLoader.laod(f.getAbsolutePath())));
		}
	}
	
	public static Blueprint createDungeonRoomBlueprint(){
		Blueprint room = RNG.getRandom(dungeonRooms);
		for(int i = 0; i < RNG.nextInt(4); i++){
			room.rotate();
		}
		return room;
	}

	public static Blueprint createDungeonStartingRoomBlueprint(){
		Blueprint room = RNG.getRandom(dungeonStartingRooms);
		for(int i = 0; i < RNG.nextInt(4); i++){
			room.rotate();
		}
		return room;
	}

	public static Blueprint createHouseBlueprint(){
		Blueprint room = RNG.getRandom(houses);
		for(int i = 0; i < RNG.nextInt(4); i++){
			room.rotate();
		}
		return room;
	}
}
