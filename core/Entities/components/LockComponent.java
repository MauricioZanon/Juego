package components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class LockComponent implements Component{
	
	public boolean isClosed = false;
	public boolean isLocked = false;
	public Entity key = null;
	
}
