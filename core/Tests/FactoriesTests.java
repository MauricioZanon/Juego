import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.badlogic.ashley.core.Entity;

import factories.ItemFactory;
import factories.NPCFactory;

public class FactoriesTests {

	@Test
	public void itemCreation(){
		Entity item;
		for(int i = 0; i < 1000; i++){
			item = ItemFactory.createRandomItem();
			assertTrue("1000 Item Creation", item != null);
			item = null;
		}
	}
	
	@Test
	public void NPCCreation(){
		Entity npc;
		for(int i = 0; i < 1000; i++){
			npc = NPCFactory.createNPC();
			assertTrue("1000 NPC Creation", npc != null);
		}
	}

}
