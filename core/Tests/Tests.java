import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.mygdx.juego.Juego;

@RunWith(Suite.class)
@SuiteClasses({RNGTests.class, FactoriesTests.class, WorldTests.class})

public class Tests {
	
	@BeforeClass
	public static void setUp() {
		Juego.world.initialize();
	}
	
}
