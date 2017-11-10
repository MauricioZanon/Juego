package world;

import java.util.HashSet;

import RNG.Noise;
import components.PositionComponent;
import dungeon.DungeonBuilder;
import field.FieldLevel;
import forest.ForestLevel;
import main.Chunk;
import main.Location;
import mountain.MountainLevel;
import village.VillageLevel;

public class World {
	
	private final int WIDTH = 10;
	private final int HEIGHT = 10;
	private final int DEPTH = 10;
	
	public final int CHUNK_SIZE = 50;
	
	private Chunk[][][] map;
	private float[][] elevation;
	private HashSet<Location> locations = new HashSet<>();
	
	public void initialize(){
		long time = System.currentTimeMillis();
		
		createOverworld();
		
//		new Cave(new PositionComponent(5, 5, 0, 5, 5), RNG.getRandom(CaveSize.values()));
		createDungeons();
		map[5][5][0] = new VillageLevel(5, 5);

		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
	}
	
	private void createOverworld() {
		elevation = Noise.generatePerlinNoise(WIDTH, HEIGHT, 5);
		Noise.print(elevation);
		map = new Chunk[WIDTH][HEIGHT][DEPTH];
		
		for(int x = 0; x < WIDTH; x++){
			for(int y = 0; y < HEIGHT; y++){
				int h = (int) (elevation[x][y]*10);
				switch(h) {
					case 1:
					case 2: //lago, oceano, rio, chasm, etc
						map[x][y][0] = new VillageLevel(x, y);
						break;
					case 3:
					case 4:
					case 5:
					case 6: //campo, desierto, planicie, aldea, etc
						map[x][y][0] = new FieldLevel(x, y);
						break;
					case 7:
					case 8: //bosque, jungla, pantano, etc
						map[x][y][0] = new ForestLevel(x, y);
						break;
					default: //montaña, meseta, volcan, etc
						map[x][y][0] = new MountainLevel(x, y);
						break;
				}
			}
		}
	}

	private void createDungeons() {
		DungeonBuilder.createDungeon(new PositionComponent(200, 200, 0));
	}

	public Chunk[][][] getMap() {
		return map;
	}
	
	
	
	public HashSet<Location> getLocations() {
		return locations;
	}
	
}
