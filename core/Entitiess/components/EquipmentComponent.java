package components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import console.MessageFactory;

import static components.Mappers.itemTypeMap;
import static components.Mappers.attMap;

public class EquipmentComponent implements Component{
	
	public HashMap<ItemType, Entity> equipment = new HashMap<>(); //TODO cambiar nombre
	
	public Entity get(ItemType type) {
		return equipment.get(type);
	}
	
	public Entity removeEquipmentOn(ItemType type) {
		return equipment.remove(type);
	}
	
	public void equip(Entity e) {
		ItemType type = itemTypeMap.get(e);
		if(equipment.keySet().contains(type)) {
			String name = Mappers.nameMap.get(equipment.get(type)).name;
			MessageFactory.createMessage("You must remove your " + name + " first.");
		}else {
			equipment.put(type, e);
			String name = Mappers.nameMap.get(e).name;
			MessageFactory.createMessage("You put on your " + name + ".");
		}
	}
	
	public float getStatsfor(String stat) {
		float value = 0;
		try {
			for(Entity eq : equipment.values()) {
				value += attMap.get(eq).get(stat);
			}
		}catch (NullPointerException e) {
			return 0;
		}
		return value;
	}

}
