package dungeon;

import main.MultiLevelLocation;

public class Dungeon extends MultiLevelLocation{
	
	private DungeonLevel[] levels;
	
	public Dungeon(DungeonLevel[] levels){
		this.levels = levels;
	}

	public DungeonLevel[] getLevels() {
		return levels;
	}
	
}
