package features;

import static components.Mappers.nameMap;

import java.util.HashMap;
import java.util.function.BiConsumer;

import com.badlogic.ashley.core.Entity;

import components.GraphicsComponent;
import components.Mappers;
import components.PositionComponent;
import effects.Effects;
import world.Explorer;

public abstract class FeaturesEffects {
	
	private static HashMap<String, BiConsumer<Entity, Entity>> effects = new HashMap<>();
	
	static {
		effects.put("stair", (f, u) -> stair(f, u));
	}
	
	public static void use(Entity feature, Entity user) {
		effects.get(nameMap.get(feature).name).accept(feature, user);
	}

	private static void stair(Entity stair, Entity user) {
		GraphicsComponent stairGC = Mappers.graphMap.get(stair);
		PositionComponent stairPC = Mappers.posMap.get(stair);
		
		int lx = stairPC.getLx();
		int ly = stairPC.getLy();
		int gx = stairPC.getGx();
		int gy = stairPC.getGy();
		int gz = stairGC.ASCII.equals(">") ? stairPC.getGz() + 1 : stairPC.getGz() - 1;
		PositionComponent newPC = Explorer.getPosition(gx, gy, gz, lx, ly);
		
		Effects.move(user, newPC);
	}
}
