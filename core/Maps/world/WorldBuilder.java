package world;

import com.mygdx.juego.Juego;

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
import mountain.MountainLevel;
import village.Village;

public class WorldBuilder {
	
	private static String name = "world";
	private static int[][] initialMap = new int[1000][1000];
	
	public static boolean isBuilding;
	
	public void initialize(){
		long time = System.currentTimeMillis();
		
		isBuilding = true;
		createOverworld();
		createLocations();
		isBuilding = false;
		
		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
		
	}
	
	private static void createOverworld() {
		float[][] elevationMap = Noise.generatePerlinNoise(1000, 1000, 3);
//		float[][] temperatureMap = Noise.generatePerlinNoise(1000, 1000, 3);
		
		for(int x = 0; x < elevationMap.length; x++){
			for(int y = 0; y < elevationMap[0].length; y++){
				float elevation = elevationMap[x][y];
				if(elevation <= 0.5f) {
					initialMap[x][y] = 1; // fields
				}
				else if(elevation <= 0.65f) {
					initialMap[x][y] = 2; // forests
				}
				else{
					initialMap[x][y] = 3; // mountains
				}
			}
		}
	}
	
	private void createLocations() {
		PositionComponent pos00 = Juego.ENGINE.createComponent(PositionComponent.class);
		pos00.coord = new int[] {0,0,0};
		new Village(pos00);
		DungeonBuilder.createDungeon(pos00, RNG.getRandom(DungeonType.values()), RNG.getRandom(DungeonSize.values()));
		pos00.coord = new int[] {100, 100, 0};
		new Cave(pos00, RNG.getRandom(CaveSize.values()));
	}

	public static String getName() {
		return name;
	}
	
	public static Chunk createOverworldChunk(int x, int y) {
		int center = initialMap.length / 2;
		int elevation = initialMap[center + x][center + y];
		if(elevation == 1) {
			return new FieldLevel(x, y);
		}
		else if(elevation == 2) {
			return new ForestLevel(x, y);
		}
		else{
			return new MountainLevel(x, y);
		}
	}

}
