package statusEffects;

import com.badlogic.ashley.core.Entity;

import effects.Trigger;

public abstract class StatusEffect{
	
	protected String name;
	protected Entity affectedActor;
	protected int duration;
	protected int timeLeft;
	protected Trigger trigger;
	
	public abstract void affect();

	public int getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}

	public Entity getAffectedActor() {
		return affectedActor;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public Trigger getTrigger() {
		return trigger;
	}
	
}
