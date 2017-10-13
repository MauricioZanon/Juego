package components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;

public class AttributeComponent implements Component{
	
	private HashMap<String, Float> att = new HashMap<>();
	
	public void set(String a, float value) {
		att.put(a, value);
	}
	
	public void change(String a, float value) {
		if(att.keySet().contains(a)) {
			att.put(a, att.get(a) + value);
		}
	}
	
	public float get(String a) {
		return att.keySet().contains(a) ? att.get(a) : 0;
	}
	
}
