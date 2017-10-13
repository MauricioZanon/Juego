package AI;

import com.badlogic.ashley.core.Entity;

import actions.Actions;
import components.Mappers;

public class PlayerAI extends AI{

	public static void execute(Entity player) {
		if(Mappers.movMap.get(player).path != null) {
			Actions.followPath(player);
		}
		
	}

}
