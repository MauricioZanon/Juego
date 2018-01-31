package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import RNG.RNG;
import components.PickupableComponent;
import components.Type;

public abstract class ItemFactory extends Factory{
	
	//TODO: buscar una mejor forma de separar los tipos de items
	
	private static HashMap<Integer, String> weaponsId;
	private static HashMap<String, String> weaponsName;
	private static HashMap<Integer, String> armorsId;
	private static HashMap<String, String> armorsName;
	private static HashMap<Integer, String> potionsId;
	private static HashMap<String, String> potionsName;
	private static HashMap<Integer, String> specialItemsId;
	private static HashMap<String, String> specialItemsName;
	
	public static void initialize() {
		weaponsId = loadEntities("../core/assets/Data/Entities/Weapons.xml");
		weaponsName = makeMapWithNames(weaponsId);
		armorsId = loadEntities("../core/assets/Data/Entities/Armors.xml");
		armorsName = makeMapWithNames(armorsId);
		potionsId = loadEntities("../core/assets/Data/Entities/Potions.xml");
		potionsName = makeMapWithNames(potionsId);
		specialItemsId = loadEntities("../core/assets/Data/Entities/Special items.xml");
		specialItemsName = makeMapWithNames(specialItemsId);
	}
	
	public static Entity createRandomItem(){
		int rarity = RNG.nextInt(100) + 1;
		if(rarity < 80){
			return createPotion();
		}
		else if(rarity < 95){
			return createArmor();
		}
		else {
			return createWeapon();
		}
	}
	
	public static Entity createItem(String itemName) {
		String itemString;
		if(weaponsName.keySet().contains(itemName)) {
			itemString = weaponsName.get(itemName); 
		}
		else if(armorsName.keySet().contains(itemName)) {
			itemString = armorsName.get(itemName); 
		}
		else if(potionsName.keySet().contains(itemName)){
			itemString = potionsName.get(itemName); 
		}
		else if(specialItemsName.keySet().contains(itemName)){
			itemString = specialItemsName.get(itemName); 
		}
		else {
			return null;
		}
		Entity item = create(itemString);
		item.add(Type.ITEM);
		item.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		return item;
	}
	
	public static Entity createItem(int id) {
		String itemString;
		if(id >= 6000 && specialItemsId.keySet().contains(id)) {
			itemString = specialItemsId.get(id);
		}
		else if(id >= 5000 && potionsId.keySet().contains(id)) {
			itemString = potionsId.get(id);
		}
		else if(id >= 4000 && weaponsId.keySet().contains(id)) {
			itemString = weaponsId.get(id);
		}
		else if(id >= 3000 && armorsId.keySet().contains(id)) {
			itemString = armorsId.get(id);
		}
		else {
			return null;
		}
		Entity item = create(itemString);
		item.add(Type.ITEM);
		item.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		return item;
	}
	
	/**===============================================================
	 * ============================EQUIPMENT==========================
	 * ===============================================================*/
	public static Entity createWeapon(){
		Entity weapon = create(RNG.getRandom(weaponsName.values()));
		weapon.add(Type.ITEM);
		weapon.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		return weapon;
	}
	
	public static Entity createArmor() {
		Entity armor = create(RNG.getRandom(armorsName.values()));
		armor.add(Type.ITEM);
		armor.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		return armor;
	}

	/**===============================================================
	 * ============================POTIONS============================
	 * ===============================================================*/
	public static Entity createPotion(){
		Entity potion = create(RNG.getRandom(potionsName.values()));
		potion.add(Type.ITEM);
		potion.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		return potion;
	}
	
//	private static String generateFakeName(String[] colorString){
//		return findText(potionsDoc, "//descriptions/desc", 1).get(0) + " " + colorString[1].trim() + " potion";
//	}
//	
//	private static Color generateColor(String[] colorString){
//		float r = Float.parseFloat(colorString[2]);
//		float g = Float.parseFloat(colorString[3]);
//		float b = Float.parseFloat(colorString[4]);
//		return new Color(r, g, b, 1f);
//	}
	
	public static void main(String[] args) {
		initialize();
		weaponsName.keySet().forEach(s -> create(weaponsName.get(s)));
		weaponsId.keySet().forEach(s -> create(weaponsId.get(s)));
		armorsName.keySet().forEach(s -> create(armorsName.get(s)));
		armorsId.keySet().forEach(s -> create(armorsId.get(s)));
		potionsName.keySet().forEach(s -> create(potionsName.get(s)));
		potionsId.keySet().forEach(s -> create(potionsId.get(s)));
		specialItemsName.keySet().forEach(s -> create(specialItemsName.get(s)));
		specialItemsId.keySet().forEach(s -> create(specialItemsId.get(s)));
		
	}
	
}
