package states.player;


import static components.Mappers.AIMap;
import static components.Mappers.movMap;
import static components.Mappers.posMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import actions.Actions;
import components.PositionComponent;
import console.MessageFactory;
import main.Tile;
import main.Tile.Visibility;
import pathFind.PathFinder;
import world.Explorer;

public class PlayerExploreState implements State<Entity>{
	
	@Override
	public void enter(Entity entity) {
		getNewExploringPath(entity);
	}

	@Override
	public void update(Entity entity) {
		if(movMap.get(entity).path == null) {
			getNewExploringPath(entity);
			if(movMap.get(entity).path != null) {
				Actions.followPath(entity);
			}
		}
	}

	@Override
	public void exit(Entity entity) {
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
	
	private void getNewExploringPath(Entity entity) {
		PositionComponent pos = posMap.get(entity);
		Tile destination = Explorer.getClosestTile(pos.getTile(), t -> t.getVisibility() == Visibility.NOT_VIEWED);
		if(destination == null) {
			MessageFactory.createMessage("There is nowhere else to explore");
			AIMap.get(entity).setState("wandering");
		}else {
			movMap.get(entity).path = PathFinder.findPath(pos, destination.getPos(), entity);
		}
	}
	
}
