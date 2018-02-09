package components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component{
	
	public float maxHP = 20;
	public float curHP = 20;
	public float HPreg = 0.1f;
	
	public String serialize() {
		return maxHP + "-" + curHP;
	}
	
}
