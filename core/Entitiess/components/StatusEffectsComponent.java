package components;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.ashley.core.Component;

import effects.Trigger;
import statusEffects.StatusEffect;

public class StatusEffectsComponent implements Component{
	
	public HashMap<Trigger, HashSet<StatusEffect>> effects = new HashMap<Trigger, HashSet<StatusEffect>>();
	
	public void affect(Trigger t) {
		try {
			effects.get(t).forEach(e -> e.affect());
		}catch(NullPointerException e) {}
	}
	
	public void add(StatusEffect s) {
		Trigger t = s.getTrigger();
		if(!effects.keySet().contains(t)) {
			effects.put(t, new HashSet<StatusEffect>());
		}
		effects.get(t).add(s);
	}
	
	public void remove(StatusEffect s) {
		Trigger t = s.getTrigger();
		if(effects.keySet().contains(t)) {
			effects.get(t).remove(s);
		}
	}

}
