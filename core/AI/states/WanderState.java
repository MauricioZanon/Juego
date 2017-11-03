package states;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import actions.ActionType;
import actions.Actions;

public class WanderState implements State<Entity> {

	@Override
	public void enter(Entity entity) {
		Actions.endTurn(entity, ActionType.WAIT);
	}

	@Override
	public void update(Entity entity) {
		Actions.endTurn(entity, ActionType.WAIT);
	}

	@Override
	public void exit(Entity entity) {
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}

}
