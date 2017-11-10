package components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class LockComponent implements Component{
	
	/**
	 * Este component va en puertas, cofres y cualquier otra cosa que pueda tener una cerradura
	 * TODO: crear un enum con todos los estados que pueda tener una puerta (OPEN, CLOSED, LOCKED, STUCK)
	 */
	
	public boolean isClosed = false;
	public boolean isLocked = false;
	public Entity key = null;
	
}
