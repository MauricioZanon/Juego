//import org.junit.Ignore;
//import org.junit.Test;
//
//import com.mygdx.juego.Juego;
//
//import RNG.RNG;
//import dungeon.DungeonBuilder;
//import main.Tile;
//import world.Explorer;
//
//public class WorldTests {
//
//	@Test
//	public void worldCreation(){
//		for(int i = 0; i < 10; i++){
//			Juego.world.initialize();
//		}
//	}
//	
//	@Ignore
//	@Test
//	public void dungeonCreation() {
//		for(int i = 0; i < 50; i++) {
//			Juego.world.initialize();
//			int x = RNG.nextInt(Juego.world.WIDTH * Juego.world.CHUNK_SIZE);
//			int y = RNG.nextInt(Juego.world.HEIGHT * Juego.world.CHUNK_SIZE);
//			Tile tile = Explorer.getTile(x, y, 0);
//			DungeonBuilder.createDungeon(tile.getPos());
//		}
//	}
//	
//}
//	