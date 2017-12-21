package tools;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;

public class RenderSystem extends EntitySystem{
	
	private Screen actualScreen;
	
	public synchronized void setScreen(Screen s){
		actualScreen = s;
	}
	
	private long tiempoTotal = 0;
	private int tiemposSumados = 0;
	
	@Override
	public void update(float deltaTime) {
		long tiempo = System.currentTimeMillis();
		
		actualScreen.show();
		actualScreen.render(deltaTime);
		
		tiempoTotal += System.currentTimeMillis() - tiempo;
		tiemposSumados++;
		if(tiemposSumados >= 100) {
			System.out.println("Tiempo promedio de renderizado de " + actualScreen.getClass().getName() + " " + tiempoTotal/tiemposSumados + " ms.");
			tiempoTotal = 0;
			tiemposSumados = 0;
		}
	}

}
