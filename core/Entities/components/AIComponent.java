package components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;

public class AIComponent implements Component{
	
	public HashMap<String, State<Entity>> states = new HashMap<>();
	
	public DefaultStateMachine<Entity, State<Entity>> fsm = new DefaultStateMachine<>();

}
