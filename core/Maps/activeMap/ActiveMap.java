package activeMap;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import components.Mappers;
import components.PositionComponent;
import components.Type;
import eventSystem.EventSystem;
import main.Tile;
import world.World;

public abstract class ActiveMap {
	
	public static final int MAP_SIZE_IN_TILES = World.CHUNK_SIZE * 4;
	private static Tile[][] map;
	
	public static void refresh(){
		map = new Tile[MAP_SIZE_IN_TILES][MAP_SIZE_IN_TILES];
		
		HashSet<Entity> npcs = new HashSet<>();
		
		PositionComponent pos00 = getPos00();
		int gx = pos00.getGx();
		int gy = pos00.getGy();
		int gz = pos00.getGz();
		int lx = pos00.getLx();
		int ly = pos00.getLy();
		
		for (int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				PositionComponent pos  = new PositionComponent(gx, gy, gz, lx + x, ly + y);
				Tile tile = pos.getTile();
				map[x][y] = tile;
				
				Entity actor = tile.get(Type.ACTOR);
				if(actor != null && !Mappers.playerMap.has(actor)) {
					npcs.addAll(tile.getEntities(Mappers.timedMap));
				}
			}
		}
		npcs.removeIf(e -> Mappers.playerMap.has(e));
		EventSystem.setEventList(npcs);
	}
	
	private static PositionComponent getPos00(){
		PositionComponent playerPos = Juego.PLAYER.getComponent(PositionComponent.class);
		
		int lx = playerPos.getLx() - (map.length/2);
		int ly = playerPos.getLy() - (map[0].length/2);
		int gx = playerPos.getGx();
		int gy = playerPos.getGy();
		int gz = playerPos.getGz();
		
		PositionComponent pos00 = new PositionComponent(gx, gy, gz, lx, ly);
		
		return pos00;
	}

	public static Tile[][] getMap() {
		return map;
	}

}
