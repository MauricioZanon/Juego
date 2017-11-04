package states.npc;

import static components.Mappers.AIMap;
import static components.Mappers.movMap;
import static components.Mappers.posMap;
import static components.Mappers.visionMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import RNG.RNG;
import actions.Actions;
import components.Mappers;
import components.Type;
import main.Tile;
import pathFind.PathFinder;

public class AttackState implements State<Entity> {
	
	private Entity targetEnemy = null;
	
	@Override
	public void enter(Entity entity) {
		Mappers.graphMap.get(entity).ASCII = "a";
		Tile enemyTile = RNG.getRandom(visionMap.get(entity).enemyTiles);
		if(enemyTile == null) {
			AIMap.get(entity).setState("wandering");
		}else {
			targetEnemy = enemyTile.get(Type.ACTOR);
		}
	}

	@Override
	public void update(Entity entity) {
		if(targetEnemy == null) {  // si no hay enemigo, wander
			AIMap.get(entity).setState("wandering");
		}else {
			if(visionMap.get(entity).enemyTiles.contains(posMap.get(targetEnemy).getTile())) { // si el enemigo esta a la vista se actualiza el path
				movMap.get(entity).path = PathFinder.findPath(posMap.get(entity), posMap.get(targetEnemy), entity);
			}
			if(movMap.get(entity).path == null) { // si no hay camino para llegar al enemigo, wander
				AIMap.get(entity).setState("wandering");
			}else {
				System.out.println("attacking");
				Actions.followPath(entity);
			}
		}
	}

	@Override
	public void exit(Entity entity) {
		targetEnemy = null;
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}

}
