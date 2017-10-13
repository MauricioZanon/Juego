package dungeon;

import java.util.HashSet;
import java.util.Set;

import cosas.Location;

public class Dungeon extends Location{
	
	private Set<DungeonLevel> levels = new HashSet<>();
	
	public Dungeon(Set<DungeonLevel> levels){
		this.levels = levels;
	}

	public Set<DungeonLevel> getLevels() {
		return levels;
	}
	
}
