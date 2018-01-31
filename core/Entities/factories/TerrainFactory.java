package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import components.Type;

public abstract class TerrainFactory extends Factory {
	
	private static HashMap<Integer, String> terrainsId;
	private static HashMap<String, String> terrainsName;
	private static HashMap<String, Entity> terrainPoolName;
	private static HashMap<Integer, Entity> terrainPoolId;
	
	public static void initialize() {
		terrainsId = loadEntities("../core/assets/Data/Entities/Terrains.xml");
		terrainsName = makeMapWithNames(terrainsId);
		fillPools();
	}
	
	public static Entity get(String name){
		if(!terrainPoolName.containsKey(name)) return null;
		else return terrainPoolName.get(name);
	}
	
	public static Entity get(int id){
		if(!terrainPoolId.containsKey(id)) return null;
		else return terrainPoolId.get(id);
	}
	
	public static Entity getNewInstance(String name) {
		Entity terrain = create(terrainsName.get(name));
		terrain.add(Type.TERRAIN);
		return terrain;
	}
	
	private static void fillPools(){
		HashMap<String, Entity> nameMap = new HashMap<>();
		for(String terrainName : terrainsName.keySet()) {
			Entity terrain = create(terrainsName.get(terrainName));
			terrain.add(Type.TERRAIN);
			nameMap.put(terrainName, terrain);
		}
		terrainPoolName = nameMap;

		HashMap<Integer, Entity> idMap = new HashMap<>();
		for(Integer terrainId : terrainsId.keySet()) {
			Entity terrain = create(terrainsId.get(terrainId));
			terrain.add(Type.TERRAIN);
			idMap.put(terrainId, terrain);
		}
		terrainPoolId = idMap;
	}
	
	public static void main(String[] args) {
		initialize();
		terrainsName.keySet().forEach(s -> create(terrainsName.get(s)));
		terrainsId.keySet().forEach(s -> create(terrainsId.get(s)));
	}
	
}
