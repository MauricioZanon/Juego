package effects;
import static components.Mappers.attMap;
import static components.Mappers.equipMap;

import com.badlogic.ashley.core.Entity;

public abstract class DamageCalculator {
	
	public static float calculate(Entity attacker, Entity receiver) {
		float damage = attMap.get(attacker).get("damage");
		if(equipMap.has(attacker)) {
			damage += equipMap.get(attacker).getStatsfor("damage");
		}
		
		float defense = attMap.get(receiver).get("defense");
		if(equipMap.has(receiver)) {
			defense += equipMap.get(receiver).getStatsfor("defense");
		}
		
		float result = damage - defense;

		if(result > 0) return result;
		else return 0;
		
	}

}
