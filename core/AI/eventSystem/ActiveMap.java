package eventSystem;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import components.Mappers;
import components.PositionComponent;
import main.Tile;
import world.Explorer;

public abstract class ActiveMap {
	
	public static final int MAP_SIZE_IN_TILES = 100;
	private static Tile[][] map;
	
	public static void refresh(){
		map = new Tile[MAP_SIZE_IN_TILES][MAP_SIZE_IN_TILES];
		
		HashSet<Entity> npcs = new HashSet<>();
		
		PositionComponent pos00 = getPos00();
		int x0 = pos00.coord[0];
		int y0 = pos00.coord[1];
		int z0 = pos00.coord[2];
		
		for (int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				try {
					Tile tile = Explorer.getTile(x0 + x, y0 + y, z0);
					map[x][y] = tile;
					npcs.addAll(tile.getEntities(Mappers.timedMap));
				}catch(ArrayIndexOutOfBoundsException e) {}
			}
		}
		npcs.removeIf(e -> Mappers.playerMap.has(e));
		EventSystem.setTimedEntities(npcs);
	}
	
	private static PositionComponent getPos00(){
		PositionComponent playerPos = Juego.PLAYER.getComponent(PositionComponent.class);
		
		int x = playerPos.coord[0] - (MAP_SIZE_IN_TILES/2);
		int y = playerPos.coord[1] - (MAP_SIZE_IN_TILES/2);
		int gz = playerPos.getGz();
		
		PositionComponent pos00 = new PositionComponent(x, y, gz);
		
		return pos00;
	}

	public static Tile[][] getMap() {
		return map;
	}

}
