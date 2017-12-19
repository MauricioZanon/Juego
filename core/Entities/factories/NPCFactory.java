package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;

public abstract class NPCFactory extends Factory{
	
	private final static String PATH = "../core/assets/Data/Entities/Enemies.xml";
	
	private static HashMap<String, String> NPCStrings = loadEntities(PATH);
	
	public static Entity createNPC(){
		return createNPC(RNG.getRandom(NPCStrings.keySet()));
	}
	
	public static Entity createNPC(String name){
		if(!NPCStrings.keySet().contains(name)) {
			return null;
		}
		else {
			Entity npc = create(NPCStrings.get(name));
			Mappers.AIMap.get(npc).fsm.setOwner(npc);
			return npc;
		}
	}
	
	public static void main(String[] args) {
		NPCStrings.keySet().forEach(s -> System.out.println(s));
	}
	
}
