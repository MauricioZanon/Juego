package entities;

import static components.Mappers.descMap;
import static components.Mappers.inventoryMap;
import static components.Mappers.lockMap;

import com.badlogic.ashley.core.Entity;

import components.LockComponent;
import components.PositionComponent;
import components.Type;
import effects.Effects;
import factories.FeatureFactory;

public abstract class FeaturesEffects {
	
	public static void use(PositionComponent featurePos, Entity user) {
		Entity feature = featurePos.getTile().get(Type.FEATURE);
		String featureName = descMap.get(feature).name;
		switch(featureName) {
		case "up stair":
			upStair(featurePos, user);
			break;
		case "down stair":
			downStair(featurePos, user);
			break;
		case "open door":
			closeDoor(featurePos);
			break;
		case "closed door":
			openDoor(feature, featurePos, user);
			break;
		}
	}
	
	private static void closeDoor(PositionComponent doorPos) {
		if(doorPos.getTile().get(Type.ACTOR) == null) {
			doorPos.getTile().put(FeatureFactory.createFeature("closed door"));
		}
	}
	
	private static void openDoor(Entity door, PositionComponent doorPos, Entity user) {
		LockComponent doorLock = lockMap.get(door);
		if(doorLock.isLocked) {
			if(inventoryMap.get(user).inv.values().contains(doorLock.key)) {
				doorLock.isLocked = false;
			}
		}else {
			doorPos.getTile().put(FeatureFactory.createFeature("open door"));
		}
		
	}

	private static void upStair(PositionComponent pos, Entity user) {
		PositionComponent stairPos = pos.clone();
		stairPos.coord[2]--;
		Effects.move(user, stairPos);
	}
	
	private static void downStair(PositionComponent pos, Entity user) {
		PositionComponent stairPos = pos.clone();
		stairPos.coord[2]++;
		Effects.move(user, stairPos);
	}
}
