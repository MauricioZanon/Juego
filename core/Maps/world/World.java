package world;

import java.util.HashSet;

import RNG.Noise;
import RNG.RNG;
import cave.Cave;
import cave.Cave.CaveSize;
import components.PositionComponent;
import dungeon.DungeonBuilder;
import field.FieldLevel;
import forest.ForestLevel;
import main.Chunk;
import main.Location;
import mountain.MountainLevel;
import village.VillageLevel;

public abstract class World {
	
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int DEPTH = 10;
	
	public static final int CHUNK_SIZE = 25;
	
	private static Chunk[][][] map;
	private static float[][] elevation;
	private static HashSet<Location> locations = new HashSet<>();
	
	public static void initialize(){
		long time = System.currentTimeMillis();
		elevation = Noise.generatePerlinNoise(WIDTH, HEIGHT, 5);
		map = new Chunk[WIDTH][HEIGHT][DEPTH];
		
		for(int x = 0; x < WIDTH; x++){
			for(int y = 0; y < HEIGHT; y++){
				float h = elevation[x][y];
				if(h < 0.2) // lago, oceano, rio, chasm, etc
            		map[x][y][0] = new VillageLevel(x, y);
            	else if(h > 0.6 && h < 0.7) //bosque, jungla, pantano, etc
            		map[x][y][0] = new ForestLevel(x, y);
            	else if(h < 0.6) //campo, desierto, planicia, aldea, etc
            		map[x][y][0] = new FieldLevel(x, y);
            	else //montaña, meseta, volcan, etc
            		map[x][y][0] = new MountainLevel(x, y);
			}
		}
		new Cave(new PositionComponent(5, 5, 0, 5, 5), RNG.getRandom(CaveSize.values()));
//		createDungeons();
		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
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
