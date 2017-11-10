package components;

import com.badlogic.ashley.core.Component;

/**
 * Cada tipo de item guarda un atributo (group) que es el tipo de item del cual desciende
 */

public enum ItemType implements Component{
	
	CONSUMABLE(null),
		POTION(CONSUMABLE),
		SCROLL(CONSUMABLE),
		FOOD(CONSUMABLE),
		DRINK(CONSUMABLE),
		
	WAND(null),
	BOOK(null),
	
	EQUIPMENT(null),
		WEAPON(EQUIPMENT),
			SWORD(WEAPON),
			DAGGER(WEAPON),
			MACE(WEAPON),
			BOW(WEAPON),
		MUNITION(EQUIPMENT),
			ARROW(MUNITION),
		ARMOR(EQUIPMENT),
			HELMET(ARMOR),
			GLOVES(ARMOR),
			GREAVES(ARMOR),
			BOOTS(ARMOR),
			BREASTPLATE(ARMOR),
		CLOTHES(EQUIPMENT),
			HAT(CLOTHES),
			SHIRT(CLOTHES),
			PANTS(CLOTHES),
			SOCKS(CLOTHES),
		JEWELRY(EQUIPMENT),
			RING(JEWELRY),
			NECKLACE(JEWELRY),
	
	TOOL(null),
		KEY(TOOL),
		
	MATERIAL(null);
	
	private ItemType group = null;
	
	private ItemType(ItemType group) {
	    this.group = group;
	}
	
	public boolean is(ItemType other) {
	   if(this == other) return true;
	   else if(group == null) return false;
	   else return group.is(other);
	}
	
}
