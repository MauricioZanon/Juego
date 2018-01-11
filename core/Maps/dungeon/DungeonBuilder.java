package dungeon;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import factories.FeatureFactory;

public class DungeonBuilder {
	
	public static void createDungeon(PositionComponent pos) {
		DungeonType type = RNG.getRandom(DungeonType.values());
		DungeonSize size = RNG.getRandom(DungeonSize.values());
		createDungeon(pos, type, size);
	}
	
	public static void createDungeon(PositionComponent entrancePos, DungeonType type, DungeonSize size) {
		int depth = RNG.nextGaussian(4, 2);
		DungeonLevel[] levels = new DungeonLevel[depth];
		
		Entity stair = FeatureFactory.createFeature("stair");
		Mappers.graphMap.get(stair).ASCII = ">";
		stair.add(entrancePos);
		entrancePos.getTile().put(stair);
		
		PositionComponent startingPos = entrancePos.clone();
		startingPos.coord[2]++;
		
		for (int i = 0; i < depth;) {
			DungeonLevel level = null;
			switch(type) {
			case REGULAR:
				level = new DungeonRegularLevel(startingPos, size);
				break;
			case WATER:
				level = new DungeonWaterLevel(startingPos, size);
				break;
			default:
				level = new DungeonRegularLevel(startingPos, size);
				break;
			}
			if(level.isValidLevel()) {
				levels[i] = level;
				startingPos = level.getDownStair().clone();
				startingPos.coord[2]++;
				i++;
			}
		}
		levels[depth-1].getDownStair().getTile().remove(Type.FEATURE);
		new Dungeon(levels);
	}

	public enum DungeonType{
		REGULAR,
		LAVA,
		WATER,
		ABANDONED;
	}
	
	public enum DungeonSize{
		TINY(3),
		SMALL(9),
		MEDIUM(15),
		BIG(25),
		HUGE(40);
		
		public int roomQuantity;
		
		DungeonSize(int rooms) {
			roomQuantity = RNG.nextGaussian(rooms, rooms/33);
		}
	}
}

