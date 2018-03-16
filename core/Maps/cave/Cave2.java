package cave;

import static components.Mappers.descMap;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.PositionComponent;
import components.Type;
import eventSystem.Map;
import factories.FeatureFactory;
import factories.TerrainFactory;
import main.MultiLevelLocation;
import main.Tile;

public class Cave2 extends MultiLevelLocation{
	
	private Set<Tile> floorTiles = new HashSet<>();
	private Set<Tile> wallTiles = new HashSet<>();
	
	private final Entity DIRT_WALL = TerrainFactory.get("dirt wall");
	private final Entity DIRT_FLOOR = TerrainFactory.get("dirt floor");
	
	/**Hecho con cellular automaton, no sirve como cueva pero puede servir para otras cosas, como para agregar spots de pasto o arboles*/
	public Cave2(PositionComponent startingPos) {
		Entity stair = FeatureFactory.createFeature("down stair");
		stair.add(startingPos);
		startingPos.getTile().put(stair);
		dig(startingPos);
		
		putWalls();
	}
	
	private void dig(PositionComponent startingPos) {
		PositionComponent firstPos = startingPos.clone();
		firstPos.coord[2]++;
		
		Set<Tile> area = Map.getCircundatingAreaAsSet(40, firstPos.getTile(), true);
		for(Tile tile : area) {
			if(RNG.nextInt(100) < 55) {
				tile.put(DIRT_WALL);
				wallTiles.add(tile);
			}else {
				tile.put(DIRT_FLOOR);
				floorTiles.add(tile);
			}
		}
		for(int i = 0; i < 4; i++) {
			soften();
		}
		
	}
	
	private void soften() {
		Set<Tile> tiles = new HashSet<>();
		Set<Tile> newFloorTiles = new HashSet<>();
		Set<Tile> newWallTiles = new HashSet<>();
		tiles.addAll(floorTiles);
		tiles.addAll(wallTiles);
		
		for(Tile tile : tiles) {
			if(descMap.get(tile.get(Type.TERRAIN)).name.equals("dirt wall") &&
					Map.countAdjacency(tile, t -> descMap.get(t.get(Type.TERRAIN)).name.equals("dirt wall")) < 4) {
				newFloorTiles.add(tile);
			}
			else if(descMap.get(tile.get(Type.TERRAIN)).name.equals("dirt floor") && 
					Map.countAdjacency(tile, t -> descMap.get(t.get(Type.TERRAIN)).name.equals("dirt wall")) >= 6) {
				newWallTiles.add(tile);
			}
		}
		for(Tile tile : newFloorTiles) {
			tile.put(DIRT_FLOOR);
			floorTiles.add(tile);
			wallTiles.remove(tile);
		}
		for(Tile tile : newWallTiles) {
			tile.put(DIRT_WALL);
			wallTiles.add(tile);
			floorTiles.remove(tile);
		}
	}

	private void putWalls() {
		Entity wall = TerrainFactory.get("dirt wall");
		for(Tile tile : floorTiles) {
			for(Tile emptyTile : Map.getAdjacentTiles(tile, t -> t.get(Type.TERRAIN) == null)) {
				emptyTile.put(wall);
			}
		}
	}
	
}
