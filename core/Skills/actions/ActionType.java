package actions;

/**
 * Los tipos de acción que pueden terminar un turno
 * @author Mauro
 *
 * TODO una speed asociada a cada tipo de accion
 */
public enum ActionType{
	WALK("move speed"),
	ATTACK("attack speed"),
	WAIT("move speed"),
	CAST_SPELL("attack speed"),
	USE_ITEM("move speed"),
	EQUIP("move speed"),
	WIELD("move speed"),
	THROW("attack speed"),
	CRAFT("move speed"),
	PICK_UP("move speed");
	
	public String asociatedStat;
	
	ActionType(String as){
		asociatedStat = as;
	}
}
