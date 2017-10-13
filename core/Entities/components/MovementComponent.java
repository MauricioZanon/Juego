package components;

import com.badlogic.ashley.core.Component;

import pathFind.Path;

public class MovementComponent implements Component{
	
	public MovementType movementType = MovementType.WALK;
	public Path path = null;
	
	public enum MovementType{
		FLY,
		WALK,
		SWIM,
		FLY_WALK,
		WALK_SWIM,
		ALL,
	}
}
