package states;

import static components.Mappers.AIMap;
import static components.Mappers.movMap;
import static components.Mappers.posMap;

import com.badlogic.ashley.core.Entity;

import actions.Actions;
import components.AIComponent;
import main.Tile;
import main.Tile.Visibility;
import pathFind.Path;
import pathFind.PathFinder;
import world.Explorer;

public class PlayerExploreState extends ExploreState{

	@Override
	public void update(Entity entity) {
		Path path = movMap.get(entity).path;
		if(path != null && !path.isEnded()) {
			Actions.followPath(entity);
		}else {
			findNewPath(entity);
			if(path == null) {
				AIComponent ai = AIMap.get(entity);
				ai.fsm.changeState(ai.states.get("idling"));
			}
			else {
				Actions.followPath(entity);
			}
		}
		
	}
	
	@Override
	protected void findNewPath(Entity entity) {
		Tile origin = posMap.get(entity).getTile();
		Tile destination = Explorer.getNearbyTile(origin, t -> t.getVisibility() == Visibility.NOT_VISIBLE);
		if(destination != null) {
			movMap.get(entity).path = PathFinder.findPath(origin.getPos(), destination.getPos(), entity);
		}
	}

}
