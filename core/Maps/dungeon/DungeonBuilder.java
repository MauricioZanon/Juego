package dungeon;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import factories.FeatureFactory;

public class DungeonBuilder {
	
	public static void createDungeon(PositionComponent pos) {
		DungeonType type = RNG.getRandom(DungeonType.values());
		DungeonSize size = RNG.getRandom(DungeonSize.values());
		createDungeon(pos, type, size);
	}
	
	//FIXME: Hay que sacar las escaleras que bajan del último piso
	public static void createDungeon(PositionComponent entrancePos, DungeonType type, DungeonSize size) {
		Set<DungeonLevel> levels = new HashSet<>();
		int depth = RNG.nextGaussian(4, 2);
		
		Entity stair = FeatureFactory.createFeature("stair");
		Mappers.graphMap.get(stair).ASCII = ">";
		stair.add(entrancePos);
		entrancePos.getTile().put(stair);
		
		PositionComponent startingPos = entrancePos.clone();
		startingPos.coord[2]++;
		
		while(levels.size() < depth) {
			DungeonLevel level = new DungeonLevel(startingPos, type, size);
			if(level.isValidLevel()) {
				levels.add(level);
				startingPos = level.getDownStair().clone();
				startingPos.coord[2]++;
			}
		}
		
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

