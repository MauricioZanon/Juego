import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.ashley.core.Entity;

import factories.FeatureFactory;
import factories.ItemFactory;
import factories.NPCFactory;
import factories.TerrainFactory;

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
	public void crearItemSinDarNombre() {
		Entity item = ItemFactory.createItem("");
		assertTrue("Crear item sin dar un nombre", item == null);
	}
	
	@Test
	public void NPCCreation(){
		Entity npc;
		for(int i = 0; i < 1000; i++){
			npc = NPCFactory.createNPC();
			assertTrue("1000 NPC Creation", npc != null);
		}
	}
	
	@Test
	public void crearNPCSinDarNombre() {
		Entity npc = NPCFactory.createNPC("");
		assertTrue("Crear NPC sin dar un nombre", npc == null);
	}
	
	@Test
	public void featureCreation(){
		Entity feature = FeatureFactory.createFeature("");
		assertTrue("Crear feature sin dar un nombre", feature == null);
	}
	
	@Test
	public void pedirTerrenoSinDarNombre() {
		Entity terrain = TerrainFactory.get("");
		assertTrue("Pedir terreno sin dar un nombre", terrain == null);
	}
	

}
