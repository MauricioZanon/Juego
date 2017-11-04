package components;

import static components.Mappers.attMap;
import static components.Mappers.itemTypeMap;
import static components.Mappers.descMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class InventoryComponent implements Component{
	
	public HashMap<String, Entity> inv = new HashMap<>();
	
	public void add(Entity i) {
		String itemName = descMap.get(i).name;
		if(!inv.keySet().contains(itemName)) {
			inv.put(itemName, i);
		}else {
			Entity item = inv.get(itemName);
			attMap.get(item).change("quantity", attMap.get(i).get("quantity"));
		}
	}
	
	public void add(List<Entity> items) {
		items.forEach(i -> add(i));
	}
	
	public Entity get(String itemName) {
		if(inv.keySet().contains(itemName)) {
			return inv.get(itemName);
		}
		return null;
	}
	
	public List<Entity> getList(ItemType type){
		ArrayList<Entity> itemList = new ArrayList<>();
		for(Entity item : inv.values()) {
			if(itemTypeMap.get(item).is(type)) {
				itemList.add(item);
			}
		}
		return itemList;
	}
	
	public List<Entity> getAll(){
		return new ArrayList<Entity>(inv.values());
	}

	public Entity remove(String itemName) {
		if(inv.keySet().contains(itemName)) {
			attMap.get(inv.get(itemName)).change("quantity", -1);
			Entity item = inv.get(itemName);
			if(attMap.get(inv.get(itemName)).get("quantity") <= 0) {
				inv.remove(itemName);
			}
			return item;
		}
		return null;
	}
	
	public Entity removeAll(String itemName){
		if(inv.keySet().contains(itemName)) {
			return inv.remove(itemName);
		}
		return null;
	}
	
	public Entity remove(Entity i) {
		return remove(descMap.get(i).name);
	}
	
	public Entity removeAll(Entity item) {
		return removeAll(descMap.get(item).name);
	}

}
