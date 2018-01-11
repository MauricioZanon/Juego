package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import RNG.RNG;
import XPReader.REXLoader;
import world.Direction;

public abstract class RoomFactory {
	
	private static HashMap<String, Set<Blueprint>> roomLists = new HashMap<>();
	
	private static final String ROOMS_PATH = "../core/assets/Rooms and buildings";
	
	static{
		try {
			File[] roomDirectories = new File(ROOMS_PATH).listFiles();
			for(File dir : roomDirectories) {
				loadRooms(dir);
			}
		} catch (IOException e) {e.printStackTrace();}
	}

	private static void loadRooms(File roomsPath) throws IOException {
		File[] roomFiles = roomsPath.listFiles();
		String directoryName = roomsPath.getName();
		roomLists.put(directoryName, new HashSet<Blueprint>());
		for(File f : roomFiles) {
			roomLists.get(directoryName).add(new Blueprint(REXLoader.laod(f.getAbsolutePath())));
		}
	}
	
	public static Blueprint createRoom(String type, Direction dir) {
		Blueprint room = RNG.getRandom(roomLists.get(type));
		room.rotate(dir);
		return room;
	}
	
	//TODO: hacer que roten las primeras habitaciones de los dungeons
	public static Blueprint createRoom(String type) {
		return RNG.getRandom(roomLists.get(type));
	}
	
	public static void main(String[] args) {
		for(Set<Blueprint> list : roomLists.values()) {
			list.forEach(b -> System.out.println(b));
		}
	}
}
