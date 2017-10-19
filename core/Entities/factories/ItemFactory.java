package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;

public abstract class ItemFactory extends Factory{
	
	private final static String PATH_WEAPONS = "../core/assets/Data/Weapons.xml";
	private final static String PATH_ARMORS = "../core/assets/Data/Armors.xml";
	private final static String PATH_POTIONS = "../core/assets/Data/Potions.xml";
	
	private static HashMap<String, String> weaponStrings = loadEntities(PATH_WEAPONS);
	private static HashMap<String, String> armorStrings = loadEntities(PATH_ARMORS);
	private static HashMap<String, String> potionStrings = loadEntities(PATH_POTIONS);
	
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
		if(weaponStrings.keySet().contains(itemName)) {
			return create(weaponStrings.get(itemName)); 
		}
		else if(armorStrings.keySet().contains(itemName)) {
			return create(armorStrings.get(itemName)); 
		}
		else if(potionStrings.keySet().contains(itemName)){
			return create(potionStrings.get(itemName)); 
		}
		else {
			return null;
		}
		
	}
	
	/**===============================================================
	 * ============================EQUIPMENT==========================
	 * ===============================================================*/
	public static Entity createWeapon(){
		Entity weapon = create(RNG.getRandom(weaponStrings.values()));
		return weapon;
	}
	
	public static Entity createArmor() {
		Entity armor = create(RNG.getRandom(armorStrings.values()));
		return armor;
	}

	/**===============================================================
	 * ============================POTIONS============================
	 * ===============================================================*/
	public static Entity createPotion(){
		Entity potion = create(RNG.getRandom(potionStrings.values()));
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
		weaponStrings.keySet().forEach(s -> System.out.println(s));
		potionStrings.keySet().forEach(s -> System.out.println(s));
		
	}
	
}
