package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;

public abstract class NPCFactory extends Factory{
	
	private final static String PATH = "../core/assets/Data/Enemies.xml";
	
	private static HashMap<String, String> NPCStrings = loadEntities(PATH);
	
	public static Entity createNPC(){
		return create(NPCStrings.get(RNG.getRandom(NPCStrings.keySet())));
	}
	
	public static Entity createNPC(String name){
		if(!NPCStrings.keySet().contains(name)) return null;
		else return create(NPCStrings.get(name));
	}
	
	public static void main(String[] args) {
		NPCStrings.keySet().forEach(s -> System.out.println(s));
	}
	
}
