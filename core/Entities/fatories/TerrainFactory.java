package fatories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

public abstract class TerrainFactory extends Factory {
	
	private final static String PATH_TERRAINS = "../core/assets/Data/Terrains.xml";
	
	private static HashMap<String, String> terrainStrings = loadEntities(PATH_TERRAINS);
	private static HashMap<String, Entity> terrainPool = fillPool();
	
	public static Entity get(String name){
		return terrainPool.get(name);
	}
	
	public static Entity getNewInstance(String name) {
		return create(terrainStrings.get(name));
	}
	
	public static void main(String[] args) {
		terrainPool.keySet().forEach(s -> System.out.println(s));
	}
	
	private static HashMap<String, Entity> fillPool(){
		HashMap<String, Entity> map = new HashMap<>();
		for(String terrainName : terrainStrings.keySet()) {
			map.put(terrainName, create(terrainStrings.get(terrainName)));
		}
		return map;
	}
	
}
