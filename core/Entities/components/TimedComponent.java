package components;

import com.badlogic.ashley.core.Component;

//Todas las entidades que actuan por turnos o se deben activar en algún momento tienen un TimedComponent

public class TimedComponent implements Component{
	
	public int nextTurn = 0;
	
	public boolean isActive = true;

}
