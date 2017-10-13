package components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;

import components.MovementComponent.MovementType;

public class TransitableComponent implements Component{
	
	public HashSet<MovementType> allowedMovementType = new HashSet<>();
	
}
