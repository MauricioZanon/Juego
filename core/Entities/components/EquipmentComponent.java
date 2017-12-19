package components;

import static components.Mappers.attMap;
import static components.Mappers.itemTypeMap;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

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
		equipment.put(type, e);
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
