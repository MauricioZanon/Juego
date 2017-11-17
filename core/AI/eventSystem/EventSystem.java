package eventSystem;

import static components.Mappers.AIMap;
import static components.Mappers.playerMap;
import static components.Mappers.timedMap;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.mygdx.juego.Juego;

import FOV.VisionCalculator;
import world.Time;

public class EventSystem extends EntitySystem {
	
	private long turn = 0;
	private PriorityQueue<Entity> timedEntities = new PriorityQueue<>(createComparator()); // Las entidades del ActiveMap que actuen por turnos
	
	public boolean waitingForPlayerInput = false;
	
	public void setTimedEntities(HashSet<Entity> e) {
		timedEntities.clear();
		timedEntities.addAll(e);
	}
	
	
	@Override
	public void update(float deltaTime) {
		Entity entity = null;
		do {
			entity = timedEntities.remove();
			if(!timedMap.get(entity).isActive) { //Si la entidad no debería actuar se la quita del engine
				Juego.ENGINE.removeEntity(entity);
				continue;
			}
			long entityTurn = timedMap.get(entity).nextTurn;
			if(playerMap.has(entity)) {
				waitingForPlayerInput = true;
				Time.advanceTime((int) (entityTurn - turn));
			}
			// Si el turno de la entidad es menor al turno actual es porque se la acaba de agregar al engine
			if(entityTurn < turn) { 
				timedMap.get(entity).nextTurn = turn + 6;
			}else {
				turn = entityTurn;
				VisionCalculator.calculateVision(entity);
				AIMap.get(entity).fsm.update();
			}
			timedEntities.add(entity);
		}while(!playerMap.has(entity));
	}
	
	private Comparator<Entity> createComparator(){
		return new Comparator<Entity>() {
			@Override
			public int compare(Entity a, Entity b) {
				long turnA = timedMap.get(a).nextTurn;
				long turnB = timedMap.get(b).nextTurn;
				
				if(turnA > turnB) return 1;
				if(turnA < turnB) return -1;
				return 0;
			}
		};
	}
}