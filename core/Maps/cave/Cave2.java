package cave;

import static components.Mappers.nameMap;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import factories.FeatureFactory;
import factories.TerrainFactory;
import main.Location;
import main.Tile;
import world.Explorer;

public class Cave2 extends Location{
	
	private Set<Tile> floorTiles = new HashSet<>();
	private Set<Tile> wallTiles = new HashSet<>();
	
	private final Entity DIRT_WALL = TerrainFactory.get("dirt wall");
	private final Entity DIRT_FLOOR = TerrainFactory.get("dirt floor");
	
	/**Hecho con cellular automaton, no sirve como cueva pero puede servir para otras cosas, como para agregar spots de pasto o arboles*/
	public Cave2(PositionComponent startingPos) {
		Entity stair = FeatureFactory.createFeature("stair");
		Mappers.graphMap.get(stair).ASCII = ">";
		stair.add(startingPos);
		startingPos.getTile().put(stair);
		dig(startingPos);
		
		putWalls();
	}
	
	private void dig(PositionComponent startingPos) {
		PositionComponent firstPos = startingPos.clone();
		firstPos.setGz(firstPos.getGz() + 1);
		
		Set<Tile> area = Explorer.getCircundatingArea(40, firstPos.getTile(), true);
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
			if(nameMap.get(tile.get(Type.TERRAIN)).name.equals("dirt wall") &&
					Explorer.countAdjacency(tile, t -> nameMap.get(t.get(Type.TERRAIN)).name.equals("dirt wall")) < 4) {
				newFloorTiles.add(tile);
			}
			else if(nameMap.get(tile.get(Type.TERRAIN)).name.equals("dirt floor") && 
					Explorer.countAdjacency(tile, t -> nameMap.get(t.get(Type.TERRAIN)).name.equals("dirt wall")) >= 6) {
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
			for(Tile emptyTile : Explorer.getAdjacentTiles(tile, t -> t.get(Type.TERRAIN) == null)) {
				emptyTile.put(wall);
			}
		}
	}
	
}
