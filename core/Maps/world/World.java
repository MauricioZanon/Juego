package world;

import com.mygdx.juego.StateSaver;

import RNG.Noise;
import RNG.RNG;
import cave.Cave;
import cave.Cave.CaveSize;
import components.PositionComponent;
import dungeon.DungeonBuilder;
import dungeon.DungeonBuilder.DungeonSize;
import dungeon.DungeonBuilder.DungeonType;
import village.Village;

public class World {
	
	public static final int CHUNK_SIZE = 25;
	private static String name = "world";
	
	public static void main(String[] args) {
		createOverworld();
	}
	
	public void initialize(){
		long time = System.currentTimeMillis();
		
		createOverworld();
		
		createLocations();
		
		System.out.println("Tiempo de creación del World Map: " + (System.currentTimeMillis() - time));
	}
	
	private static void createOverworld() {
		float[][] elevationMap = Noise.generatePerlinNoise(1000, 1000, 3);
//		float[][] temperatureMap = Noise.generatePerlinNoise(1000, 1000, 3);
		
		int[][] initialMap = new int[1000][1000];
		
		for(int x = 0; x < 5; x++){
			for(int y = 0; y < 5; y++){
				float elevation = elevationMap[x][y];
				if(elevation <= 0.4f) {
					initialMap[x][y] = 1; // fields
				}
				else if(elevation <= 0.7f) {
					initialMap[x][y] = 2; // forests
				}
				else{
					initialMap[x][y] = 3; // mountains
				}
			}
		}
		
		StateSaver.createInitialSave(initialMap);
		
	}
	
	private void createLocations() {
		new Village(new PositionComponent(200, 200, 0));
//		DungeonBuilder.createDungeon(new PositionComponent(200, 200, 0), DungeonType.REGULAR, DungeonSize.TINY);
		new Cave(new PositionComponent(250, 250, 0), RNG.getRandom(CaveSize.values()));

	}

	public static String getName() {
		return name;
	}
	
}
