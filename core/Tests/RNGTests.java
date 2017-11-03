import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Ignore;
import org.junit.Test;

import com.mygdx.juego.Juego;

import RNG.RNG;
import main.Chunk;
import main.Tile;

public class RNGTests {

	@Test
	public void random(){
		int limit = 50;
		for(int i = 0; i < 1000; i ++){
			int num = RNG.nextInt(limit);
			assertTrue("Random", 0 <= num && num < limit);
		}
	}
	
	@Test
	public void emptySet() {
		Set<?> set = Collections.EMPTY_SET;
		assertTrue("emptySet", RNG.getRandom(set) == null);
	}
	
	@Test
	public void randomBetween(){
		int min = 50;
		int max = 100;
		for(int i = 0; i < 1000; i ++){
			int num = RNG.nextInt(min, max);
			assertTrue("Random", min <= num && num < max);
		}
	}
	
	@Ignore //FIXME se freezea en las pruebas
	@Test
	public void randomTile(){
		Chunk level = Juego.world.getMap()[1][1][0];
		Predicate<Tile> cond = t -> !t.isTransitable();
		for(int i = 0; i < 1000; i++){
			Tile tile = RNG.getRandom(level.getChunkMap(), cond);
			assertTrue("Random", !tile.isTransitable());
		}
	}

}
