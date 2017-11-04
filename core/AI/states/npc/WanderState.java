package states.npc;

import static components.Mappers.AIMap;
import static components.Mappers.posMap;
import static components.Mappers.visionMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import RNG.RNG;
import actions.Actions;
import components.Mappers;
import world.Direction;

public class WanderState implements State<Entity> {

	@Override
	public void enter(Entity entity) {
		Mappers.graphMap.get(entity).ASCII = "w";
	}

	@Override
	public void update(Entity entity) {
		if(!visionMap.get(entity).enemyTiles.isEmpty()) {
			System.out.println("enemy found");
			AIMap.get(entity).setState("attacking");
		}else {
			System.out.println("wander");
			Actions.bump(posMap.get(entity), RNG.getRandom(Direction.values()));
		}
	}

	@Override
	public void exit(Entity entity) {
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}

}
