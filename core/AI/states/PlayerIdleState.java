package states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;

public class PlayerIdleState extends IdleState{
	
	@Override
	public void enter(Entity entity) {
	}
	
	@Override
	public void update(Entity entity) {
	}

	@Override
	public void exit(Entity entity) {
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
}
