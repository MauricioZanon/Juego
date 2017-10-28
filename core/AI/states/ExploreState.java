package states;

import static components.Mappers.movMap;
import static components.Mappers.posMap;

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import RNG.RNG;
import actions.ActionType;
import actions.Actions;
import main.Tile;
import pathFind.Path;
import pathFind.PathFinder;
import world.Explorer;

public class ExploreState implements State<Entity>{

	@Override
	public void enter(Entity entity) {
		findNewPath(entity);
	}

	@Override
	public void update(Entity entity) {
		Path path = movMap.get(entity).path;
		if(path != null && !path.isEnded()) {
			Actions.followPath(entity);
		}else {
			findNewPath(entity);
			Actions.endTurn(entity, ActionType.WAIT);
		}
	}

	@Override
	public void exit(Entity entity) {
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}
	
	protected void findNewPath(Entity entity) {
		Tile origin = posMap.get(entity).getTile();
		Set<Tile> area = Explorer.getCircundatingArea(10, origin, false);
		Tile destination = RNG.getRandom(area, t -> t.isTransitable());
		if(destination != null) {
			movMap.get(entity).path = PathFinder.findPath(origin.getPos(), destination.getPos(), entity);
		}
	}

}
