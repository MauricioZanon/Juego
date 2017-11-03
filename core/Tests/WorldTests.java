import org.junit.Ignore;
import org.junit.Test;

import com.mygdx.juego.Juego;

public class WorldTests {

	@Ignore
	@Test
	public void worldCreation(){
		for(int i = 0; i < 10; i++){
			Juego.world.initialize();
		}
	}
	
}
	