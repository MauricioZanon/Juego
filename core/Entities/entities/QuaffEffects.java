package entities;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import effects.Effects;

public abstract class QuaffEffects {
	
	private static HashMap<String, Consumer<Entity>> quaffEffectsList = new HashMap<>();
	
	static{
		fillQuaffEffectsList();
	}
	
	private static void fillQuaffEffectsList() {
		quaffEffectsList.put("healing potion", a -> {
			Effects.heal(a, 10);
		});
		
//		quaffEffectsList.put("teleportation potion", a -> {
//			Effects.randomTeleport(a);
//		});
		
		quaffEffectsList.put("cure potion", a -> {
			Effects.cure(a);
		});
	}
	
	public static Consumer<Entity> getFor(String potionName){
		return quaffEffectsList.get(potionName);
	}


}
