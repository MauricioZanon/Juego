package AI;

import static components.Mappers.factionMap;
import static components.Mappers.movMap;
import static components.Mappers.posMap;
import static components.Mappers.visionMap;

import com.badlogic.ashley.core.Entity;

import actions.ActionType;
import actions.Actions;
import components.Type;
import cosas.Tile;
import pathFind.PathFinder;


public abstract class BasicAI extends AI{
	
	/**
	 * Busca un enemigo cercano, si lo encuentra lo persigue y comienza a atacarlo
	 * @param entity la entidad que usa este AI
	 */
	public static void execute(Entity entity) {
		for(Tile t : visionMap.get(entity).visionMap){
			Entity actor = t.get(Type.ACTOR);
			if(actor != null && factionMap.get(entity).isEnemy(actor)){
				movMap.get(entity).path = PathFinder.findPath(posMap.get(entity), posMap.get(actor), entity);
				break;
			}
		}
		if(movMap.get(entity).path != null){
			Actions.followPath(entity);
		}else {
			Actions.endTurn(entity, ActionType.WAIT);
		}
	}

}
