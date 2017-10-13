package cosas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import RNG.RNG;

public abstract class RoomFactory {
	
	private static Set<Blueprint> dungeonRooms = new HashSet<>();
	private static Set<Blueprint> dungeonStartingRooms = new HashSet<>();
	
	private static final String DUNGEON_ROOMS_PATH = "../core/assets/Rooms and buildings/Dungeon rooms.txt";
	private static final String DUNGEON_STARTING_ROOMS_PATH = "../core/assets/Rooms and buildings/Dungeon starting rooms.txt";
	
	static{
		try {
			loadRooms(dungeonRooms, DUNGEON_ROOMS_PATH);
			loadRooms(dungeonStartingRooms, DUNGEON_STARTING_ROOMS_PATH);
		} catch (IOException e) {e.printStackTrace();}
	}

	private static void loadRooms(Set<Blueprint> rooms, String roomsPath) throws IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(roomsPath));
		
		String line;
		String[][] newRoom = null;
		int roomLineNumber = 0;
		while((line = in.readLine()) != null){
			if(line.contains("size")){
				String[] roomSize = line.split(" ");
				newRoom = new String[Integer.parseInt(roomSize[1])][Integer.parseInt(roomSize[2])];
			}
			else if(line.length() > 1){
				String[] chars = line.split("");
				for(int i = 0; i < chars.length; i++){
					newRoom[i][roomLineNumber] = chars[i];
				}
				roomLineNumber++;
			}
			else{
				rooms.add(new Blueprint(newRoom));
				newRoom = null;
				roomLineNumber = 0;
			}
		}
		in.close();
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
	
}
