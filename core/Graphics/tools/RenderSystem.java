package tools;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class RenderSystem extends EntitySystem{
	
	private Screen actualScreen;
	
	public synchronized void setScreen(Screen s){
		actualScreen = s;
	}
	
	@Override
	public void update(float deltaTime) {
		Gdx.app.postRunnable(() ->{ //un Runnable de LibGdx que usa OpenGL, si se usa un thread normal tira error
			actualScreen.show();
			actualScreen.render(deltaTime);
		});
	}

}
