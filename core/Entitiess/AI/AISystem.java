package AI;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;

import components.Mappers;

public abstract class AISystem {
	
	private static HashMap<String, Consumer<Entity>> AIList = new HashMap<>();
	
	static {
		AIList.put("basic AI", e -> BasicAI.execute(e));
		AIList.put("player AI", e -> PlayerAI.execute(e));
	}
	
	
	public static void execute(Entity e) {
		AIList.get(Mappers.AIMap.get(e).AIType).accept(e);
	}

}
