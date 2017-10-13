package eventSystem;

import static components.Mappers.timedMap;

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.juego.Juego;

import AI.AISystem;

public class EventSystem extends EntitySystem {
	
	private static Set<Entity> timedEntities = null;
	
	public static void setEventList(Set<Entity> e) {
		timedEntities = e;
	}
	
	@Override
	public void update(float deltaTime) {
		while(timedMap.get(Juego.PLAYER).actionPoints < 100){
			for(Entity entity : timedEntities){
				if(!timedMap.get(entity).isActive)
					continue;
				if(timedMap.get(entity).actionPoints < 100)
					timedMap.get(entity).actionPoints++;
				else
					AISystem.execute(entity);
			}
			timedMap.get(Juego.PLAYER).actionPoints++;
		}
		AISystem.execute(Juego.PLAYER);
	}

}
