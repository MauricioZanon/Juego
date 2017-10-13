package statusEffects;

import com.badlogic.ashley.core.Entity;

import effects.Effects;
import effects.Trigger;

public class Poisoned extends StatusEffect{
	
	public Poisoned(Entity actor){
		affectedActor = actor;
		duration = 10;
		timeLeft = duration;
		name = "poisoned";
		trigger = Trigger.END_TURN;
	}

	public Poisoned(Entity actor, int duration){
		affectedActor = actor;
		this.duration = duration;
		timeLeft = duration;
		name = "poisoned";
	}

	@Override
	public void affect() {
		Effects.damage(affectedActor, 10f);
		timeLeft--;
	}

}
