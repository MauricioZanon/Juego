package world;

import java.util.HashSet;

import RNG.Noise;
import RNG.RNG;
import cave.Cave;
import cave.Cave.CaveSize;
import components.PositionComponent;
import dungeon.DungeonBuilder;
import dungeon.DungeonBuilder.DungeonSize;
import dungeon.DungeonBuilder.DungeonType;
import field.FieldLevel;
import forest.ForestLevel;
import main.Chunk;
import main.Location;
import mountain.MountainLevel;
import village.Village;

public class World {
	
	public final int WIDTH = 15;
	public final int HEIGHT = 15;
	public final int DEPTH = 10;
	
	public static final int CHUNK_SIZE = 25;
	
	private Chunk[][][] map = new Chunk[WIDTH][HEIGHT][DEPTH];;
	private float[][] elevationMap;
	private HashSet<Location> locations = new HashSet<>();
	
	public void initialize(){
//		WorldSaver.createSaveFile();
		long time = System.currentTimeMillis();
		
		createOverworld();
		
		createLocations();
		
		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
	}
	
	private void createOverworld() {
		elevationMap = Noise.generatePerlinNoise(WIDTH, HEIGHT, 3);
		Noise.print(elevationMap);
		
		int villages = 0;
		int fields = 0;
		int forests = 0;
		int mountains = 0;
		
		for(int x = 0; x < WIDTH; x++){
			for(int y = 0; y < HEIGHT; y++){
				int elevation = (int) (elevationMap[x][y]*10);
				switch(elevation) {
				case 1:
				case 2:
				case 3:
				case 4: //campo, desierto, planicie, aldea, etc
					map[x][y][0] = new FieldLevel(x, y);
					fields++;
					break;
				case 5:
				case 6: //bosque, jungla, pantano, etc
					map[x][y][0] = new ForestLevel(x, y);
					forests++;
					break;
				case 7:
				case 8:
				default: //montaña, meseta, volcan, etc
					map[x][y][0] = new MountainLevel(x, y);
					mountains++;
					break;
				}
			}
		}
		
		System.out.println("villages " + villages + " fields " + fields + " forests " + forests + " mountains " + mountains);
	}
	
	private void createLocations() {
		new Village(new PositionComponent(200, 200, 0));
//		DungeonBuilder.createDungeon(new PositionComponent(200, 200, 0), DungeonType.REGULAR, DungeonSize.TINY);
		new Cave(new PositionComponent(250, 250, 0), RNG.getRandom(CaveSize.values()));

	}

	public Chunk[][][] getMap() {
		return map;
	}
	
	public HashSet<Location> getLocations() {
		return locations;
	}
	
}
