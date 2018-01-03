package dungeon;

import java.util.HashSet;
import java.util.Set;

import components.Mappers;
import components.Type;
import main.Tile;
import world.Explorer;

public class Room {
	
	private Set<Tile> floorTiles = new HashSet<>();
	
	public Room(Set<Tile> floorTiles){
		this.floorTiles = floorTiles;
	}

	public Set<Tile> getFloorTiles() {
		return floorTiles;
	}
	
	public Set<Tile> getDoorTiles(){
		Set<Tile> doorTiles = new HashSet<>();
		for(Tile tile : floorTiles) {
			for(Tile adjacentTile : Explorer.getAdjacentTiles(tile, t -> t.get(Type.FEATURE) != null)) {
				if(Mappers.descMap.get(adjacentTile.get(Type.FEATURE)).name.equals("door")) {
					doorTiles.add(adjacentTile);
				}
			}
		}
		return doorTiles;
	}
}
