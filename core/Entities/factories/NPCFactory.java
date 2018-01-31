package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import RNG.RNG;
import components.Mappers;
import components.TimedComponent;
import components.Type;

public abstract class NPCFactory extends Factory{
	
	private static HashMap<Integer, String> NPCsId;
	private static HashMap<String, String> NPCsName;
	
	public static void initialize() {
		NPCsId = loadEntities("../core/assets/Data/Entities/Enemies.xml");
		NPCsName = makeMapWithNames(NPCsId);
	}
	
	public static Entity createNPC(){
		return createNPC(RNG.getRandom(NPCsId.keySet()));
	}
	
	public static Entity createNPC(String name){
		if(NPCsName.keySet().contains(name)) {
			Entity npc = create(NPCsName.get(name));
			npc.add(Type.ACTOR);
			npc.add(Juego.ENGINE.createComponent(TimedComponent.class));
			Mappers.AIMap.get(npc).fsm.setOwner(npc);
			return npc;
		}
		else {
			return null;
		}
	}
	
	public static Entity createNPC(int id) {
		if(NPCsId.keySet().contains(id)) {
			Entity npc = create(NPCsId.get(id));
			npc.add(Type.ACTOR);
			npc.add(Juego.ENGINE.createComponent(TimedComponent.class));
			Mappers.AIMap.get(npc).fsm.setOwner(npc);
			return npc;
		}
		else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		initialize();
		NPCsId.keySet().forEach(s -> create(NPCsId.get(s)));
		NPCsName.keySet().forEach(s -> create(NPCsName.get(s)));
	}
	
}
