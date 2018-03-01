package world;

import com.mygdx.juego.Juego;

import RNG.Noise;
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
	private static int[][] initialMap = new int[1000][1000];
	
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
	}
	
	private void createLocations() {
		PositionComponent pos00 = Juego.ENGINE.createComponent(PositionComponent.class);
		pos00.coord = new int[] {0,0,0};
//		new Village(pos00);
//		DungeonBuilder.createDungeon(pos00, DungeonType.REGULAR, DungeonSize.TINY);
		new Cave(pos00, CaveSize.TINY);
	}

	public static String getName() {
		return name;
	}
	
}
