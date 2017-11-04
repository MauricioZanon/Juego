package states.player;

import static components.Mappers.movMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;

import actions.Actions;
import components.Mappers;
import pathFind.Path;
import states.npc.WanderState;

public class PlayerWanderState extends WanderState{
	
	@Override
	public void enter(Entity entity) {
		Mappers.graphMap.get(entity).frontColor = Color.WHITE;
	}
	
	@Override
	public void update(Entity entity) {
		Path path = movMap.get(entity).path;
		if(path != null && !path.isEnded()) {
			Actions.followPath(entity);
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
