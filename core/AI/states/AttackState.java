package states;

import static components.Mappers.factionMap;
import static components.Mappers.posMap;
import static components.Mappers.movMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import actions.Actions;
import components.Faction;
import components.Type;
import main.Tile;
import pathFind.Path;
import pathFind.PathFinder;
import world.Explorer;

public class AttackState implements State<Entity> {
	
	@Override
	public void enter(Entity entity) {
		Tile origin = posMap.get(entity).getTile();
		Faction faction = factionMap.get(entity);
		Tile enemyTile = Explorer.getNearbyTile(origin, t -> t.get(Type.ACTOR) != null && faction.isEnemy(t.get(Type.ACTOR)));
		Path path = PathFinder.findPath(origin.getPos(), enemyTile.getPos(), entity);
		movMap.get(entity).path = path;
	}

	@Override
	public void update(Entity entity) {
		Actions.followPath(entity);
	}

	@Override
	public void exit(Entity entity) {
		
	}

	@Override
	public boolean onMessage(Entity entity, Telegram telegram) {
		return false;
	}

}
