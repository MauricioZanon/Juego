package factories;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;

public abstract class FeatureFactory extends Factory{
	
	private final static String PATH_FEATURES = "../core/assets/Data/Features.xml";
	
	private static HashMap<String, String> featureStrings = loadEntities(PATH_FEATURES);
	
	public static Entity get(String name){
		return create(featureStrings.get(name));
	}
	
	public static void main(String[] args) {
		featureStrings.keySet().forEach(s -> System.out.println(s));
	}
	
}
