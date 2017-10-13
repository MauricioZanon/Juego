package components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public enum Faction implements Component{
	
	ANIMALS(),
	GOBLINS(),
	SLIMES(),
	HUMANS(),
	ORCS(GOBLINS);
	
	public HashSet<Faction> friends = new HashSet<>();
	
	Faction(Faction... friends){
		for(int i = 0; i < friends.length; i++) {
			this.friends.add(friends[i]);
		}
	}
	
	public boolean isEnemy(Entity actor) {
		Faction faction = Mappers.factionMap.get(actor);
		return faction == null || faction == this || friends.contains(faction) ? false : true;
	}

}
