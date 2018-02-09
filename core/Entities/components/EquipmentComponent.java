package components;

import static components.Mappers.attMap;
import static components.Mappers.itemTypeMap;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class EquipmentComponent implements Component{
	
	public HashMap<ItemType, Entity> wearedEquipment = new HashMap<>();
	
	public Entity get(ItemType type) {
		return wearedEquipment.get(type);
	}
	
	public Entity removeEquipmentOn(ItemType type) {
		return wearedEquipment.remove(type);
	}
	
	public void equip(Entity e) {
		ItemType type = itemTypeMap.get(e);
		wearedEquipment.put(type, e);
	}
	
	public float getStatsfor(String stat) {
		float value = 0;
		try {
			for(Entity eq : wearedEquipment.values()) {
				value += attMap.get(eq).get(stat);
			}
		}catch (NullPointerException e) {
			return 0;
		}
		return value;
	}
	
	public String serialize() {
		String result = "";
		for(Entity equipment : wearedEquipment.values()) {
			result += equipment.flags + "/";
		}
		return result;
	}

}
