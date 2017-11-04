package dungeon;

import java.util.HashSet;
import java.util.Set;

import components.Mappers;
import components.Type;
import main.Tile;

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
			if(tile.get(Type.FEATURE) != null && Mappers.descMap.get(tile.get(Type.FEATURE)).name.equals("door")) {
				doorTiles.add(tile);
			}
		}
		return doorTiles;
	}
}
