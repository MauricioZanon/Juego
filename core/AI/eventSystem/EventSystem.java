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

public class EventSystem extends EntitySystem {
	
	private long turn = 0;
	private PriorityQueue<Entity> timedEntities = new PriorityQueue<>(createComparator()); // Las entidades del ActiveMap que actuen por turnos
	
	public boolean waitingForPlayerInput = false;
	
	public void setTimedEntities(HashSet<Entity> e) {
		timedEntities.clear();
		timedEntities.addAll(e);
	}
	
	
	/**
	 * FIXME:
	 * Con el if tarda mucho porque despues de cada ciclo tiene que renderizar antes de empezar el siguiente
	 * Hay que cambiar el if por un while para que simuler todos los movimientos antes de darle control al personaje
	 * 
	 * FIXME:
	 * Cuando se cambia el if por el while si se hace explorar al personaje la pantalla no se renderiza hasta que termina,
	 * esto es porque mientras explora no se le da el control al personaje, entonces nunca sale del while, la manera correcta de arreglarlo
	 * seria moviendo el renderizado a otro thread
	 */
	@Override
	public void update(float deltaTime) {
		Entity entity = null;
		do {
			entity = timedEntities.remove();
			if(!timedMap.get(entity).isActive) {
				Juego.ENGINE.removeEntity(entity);
				continue;
			}
			if(playerMap.has(entity)) waitingForPlayerInput = true;
			long entityTurn = timedMap.get(entity).nextTurn;
			if(entityTurn < turn) {
				timedMap.get(entity).nextTurn = turn + 10;
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