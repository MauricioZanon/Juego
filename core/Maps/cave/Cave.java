package cave;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import factories.FeatureFactory;
import factories.TerrainFactory;
import main.MultiLevelLocation;
import main.Tile;
import world.Explorer;


public class Cave extends MultiLevelLocation{
	
	private Set<Tile> floorTiles = new HashSet<>();
	
	/**Hecho con random walks*/
	public Cave(PositionComponent startingPos, CaveSize size) {
		Entity stair = FeatureFactory.createFeature("stair");
		Mappers.graphMap.get(stair).ASCII = ">";
		stair.add(startingPos);
		startingPos.getTile().put(stair);
		
		PositionComponent firstPos = startingPos.clone();
		firstPos.coord[2] += 1;
		Entity stair2 = FeatureFactory.createFeature("stair");
		Mappers.graphMap.get(stair2).ASCII = "<";
		stair2.add(firstPos);
		firstPos.getTile().put(stair2);
		
		dig(firstPos, size.floorTiles);
		
		putWalls();
	}
	
	private void dig(PositionComponent startingPos, int floorTilesAmount) {
		Set<Miner> miners = new HashSet<>();
		miners.add(new Miner(startingPos, floorTiles));
		
		while(floorTiles.size() < floorTilesAmount) {
			Set<Miner> newMiners = new HashSet<>();
			Set<Miner> inactiveMiners = new HashSet<>();
			
			for(Miner miner : miners) {
				miner.dig();
				if(RNG.nextInt(500) == 1) {
					PositionComponent newMinerPos = null;
					for(Tile tile : floorTiles) {
						if(Explorer.isOrthogonallyAdjacent(tile, t -> t.get(Type.TERRAIN) == null)) {
							newMinerPos = tile.getPos();
							if(newMinerPos == null) { 
								continue;
							}
							break;
						}
					}
					if(newMinerPos != null) {
						newMiners.add(new Miner(newMinerPos, floorTiles));
					}
				}
			}
			miners.removeAll(inactiveMiners);
			miners.addAll(newMiners);
		}
	}
	
	private void putWalls() {
		Entity dirtWall = TerrainFactory.get("dirt wall");
		Entity dirtFloor = TerrainFactory.get("dirt floor");
		for(Tile tile : floorTiles) {
			for(Tile emptyTile : Explorer.getAdjacentTiles(tile, t -> t.get(Type.TERRAIN) == null)) {
				if(Explorer.countOrthogonalAdjacency(emptyTile, t -> !t.isTransitable()) != 0) {
					emptyTile.put(dirtWall);
				}else {
					emptyTile.put(dirtFloor);
				}
			}
		}
	}
	
	public enum CaveSize{
		TINY(100),
		SMALL(1000),
		MEDIUM(2000),
		BIG(4000),
		HUGE(8000);
		
		private int floorTiles;
		
		CaveSize(int tiles) {
			floorTiles = RNG.nextGaussian(tiles, tiles/33);
		}

		public int getFloorTiles() {
			return floorTiles;
		}
	}
	
}
