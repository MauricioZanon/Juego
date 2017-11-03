package eventSystem;

import static components.Mappers.AIMap;
import static components.Mappers.timedMap;

import java.util.Set;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.juego.Juego;

public class EventSystem extends EntitySystem {
	
	private static Set<Entity> timedEntities = null; // Las entidades del ActiveMap que actuen por turnos
	
	public static void setTimedEntities(Set<Entity> e) {
		timedEntities = e;
	}
	
	@Override
	public void update(float deltaTime) {
		while(timedMap.get(Juego.PLAYER).actionPoints < 100){
			for(Entity entity : timedEntities){
				if(!timedMap.get(entity).isActive) {
					Juego.ENGINE.removeEntity(entity);
				}
				else if(timedMap.get(entity).actionPoints < 100)
					timedMap.get(entity).actionPoints++;
				else
					AIMap.get(entity).fsm.update();
			}
			timedMap.get(Juego.PLAYER).actionPoints++;
		}
		AIMap.get(Juego.PLAYER).fsm.update();
	}

}
