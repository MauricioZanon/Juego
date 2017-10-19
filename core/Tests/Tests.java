import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import world.World;

@RunWith(Suite.class)
@SuiteClasses({RNGTests.class, FactoriesTests.class, WorldTests.class})

public class Tests {
	
	@BeforeClass
	public static void setUp() {
		World.initialize();
	}
	
}
