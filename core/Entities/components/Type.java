package components;

import com.badlogic.ashley.core.Component;

/**
 * Tipo de entidad
 */
public enum Type implements Component{
	ACTOR,
	FEATURE,
	ITEM,
	TERRAIN,
}
