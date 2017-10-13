package screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;

public class RenderSystem extends EntitySystem{
	
	private static Screen actualScreen;
	
	public static void setScreen(Screen s){
		actualScreen = s;
	}
	
	public void drawScreen(){
		actualScreen.show();
		actualScreen.render(0);
	}
	
	@Override
	public void update(float deltaTime) {
		actualScreen.show();
		actualScreen.render(0);
	}

}
