package world;

import java.util.HashSet;

import RNG.RNG;
import cave.Cave;
import cave.Cave.CaveSize;
import components.PositionComponent;
import cosas.Chunk;
import cosas.EmptyMap;
import cosas.Location;
import dungeon.DungeonBuilder;
import village.VillageLevel;

public abstract class World {
	
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	private static final int DEPTH = 10;
	
	public static final int CHUNK_SIZE = 100;
	
	private static Chunk[][][] map;
//	private static float[][] elevation;
	private static HashSet<Location> locations = new HashSet<>();
	
	public static void initialize(){
		long time = System.currentTimeMillis();
//		elevation = Noise.generatePerlinNoise(WIDTH, HEIGHT, 5);
		map = new Chunk[WIDTH][HEIGHT][DEPTH];
		
		createEmptyMap();
		
		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				map[x][y][0] = new VillageLevel(x, y, 0);
			}
		}
		new Cave(new PositionComponent(5, 5, 0, 5, 5), RNG.getRandom(CaveSize.values()));
//		createDungeons();
		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
	}

	private static void createEmptyMap() {
		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				for(int z = 0; z < map[0][0].length; z++){
					map[x][y][z] = new EmptyMap(x, y, z);
				}
			}
		}
	}

	private static void createDungeons() {
		DungeonBuilder.createDungeon(new PositionComponent(5, 5, 0, 5, 5));
	}

	public static Chunk[][][] getMap() {
		return map;
	}
	
	
	
	public static HashSet<Location> getLocations() {
		return locations;
	}
	
}
