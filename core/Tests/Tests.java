import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import fatories.ItemFactory;
import fatories.NPCFactory;
import main.Tile;
import village.VillageLevel;
import world.World;

public class Tests {
	
	@Test
	public void random(){
		int limit = 50;
		for(int i = 0; i < 1000; i ++){
			int num = RNG.nextInt(limit);
			assertTrue("Random", 0 <= num && num < limit);
		}
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
	
	@Test
	public void randomTile(){
		VillageLevel level = new VillageLevel(0, 0);
		Predicate<Tile> cond = t -> !t.isTransitable();
		for(int i = 0; i < 1000; i++){
			Tile tile = RNG.getRandom(level.getChunkMap(), cond);
			assertTrue("Random", !tile.isTransitable());
		}
	}
	
//	@Test
//	public void vaultCreation(){
//		for(int i = 0; i < 1000; i++){
//			XPChar[][] vault = RNG.getRandomVault();
//			assertTrue("Vault Creation", vault != null);
//		}
//	}
	
	@Test
	public void itemCreation(){
		for(int i = 0; i < 1000; i++){
			Entity item = ItemFactory.createRandomItem();
			assertTrue("Item Creation", item != null);
		}
	}
	
	@Test
	public void NPCCreation(){
		for(int i = 0; i < 1000; i++){
			Entity npc = NPCFactory.createNPC();
			assertTrue("NPC Creation", npc != null);
		}
	}
	
	@Test
	public void worldCreation(){
		for(int i = 0; i < 100; i++){
			World.initialize();
		}
	}
	
}
