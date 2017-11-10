package features;

import static components.Mappers.descMap;
import static components.Mappers.lockMap;
import static components.Mappers.transitableMap;
import static components.Mappers.translucentMap;
import static components.Mappers.graphMap;
import static components.Mappers.inventoryMap;

import com.badlogic.ashley.core.Entity;

import components.GraphicsComponent;
import components.LockComponent;
import components.Mappers;
import components.MovementComponent.MovementType;
import components.PositionComponent;
import effects.Effects;
import world.Explorer;

public abstract class FeaturesEffects {
	
	public static void use(Entity feature, Entity user) {
		String featureName = descMap.get(feature).name;
		switch(featureName) {
		case "stair":
			stair(feature, user);
			break;
		case "door":
			door(feature, user);
		}
	}
	
	private static void door(Entity door, Entity user) {
		LockComponent doorLock = lockMap.get(door);
		if(doorLock.isLocked) {
			if(inventoryMap.get(user).inv.values().contains(doorLock.key)) {
				doorLock.isLocked = false;
			}
		}
		else if(!doorLock.isClosed) {
			translucentMap.get(door).translucent = false;
			transitableMap.get(door).allowedMovementType.remove(MovementType.WALK);
			graphMap.get(door).ASCII = "+";
			doorLock.isClosed = true;
		}else {
			translucentMap.get(door).translucent = true;
			transitableMap.get(door).allowedMovementType.add(MovementType.WALK);
			graphMap.get(door).ASCII = "/";
			doorLock.isClosed = false;
		}
		
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
