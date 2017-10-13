package FOV;

import static components.Mappers.translucentMap;
import static components.Mappers.visionMap;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import components.Mappers;
import components.Type;
import cosas.Tile;
import cosas.Tile.Visibility;
import world.Explorer;

public abstract class VisionCalculator {
	
	public static void calculateVision(Entity actor){
		if(Mappers.playerMap.has(actor))
			calculatePlayerVision(actor);
		else 
			calculateNPCVision(actor);
	}

	private static void calculatePlayerVision(Entity player) {
		visionMap.get(player).visionMap.forEach(t -> t.setVisibiliy(Visibility.VIEWED));
		
		Tile originTile = Mappers.posMap.get(player).getTile();
		
		HashSet<Tile> visibleTiles = new HashSet<Tile>();
		Set<Tile> area = Explorer.getCircundatingArea(visionMap.get(player).sightRange, originTile, true);
		
		for(Tile t : area) {
			if(!visibleTiles.contains(t)){
				calculateLOS(originTile, t, visibleTiles);
			}
		}
		visibleTiles.forEach(t -> t.setVisibiliy(Visibility.VISIBLE));
		
		visionMap.get(player).visionMap = visibleTiles;
	}
	

	private static void calculateNPCVision(Entity npc){
		Tile originTile = Mappers.posMap.get(npc).getTile();

		HashSet<Tile> visibleTiles = new HashSet<Tile>();
		for(Tile tile : Explorer.getCircundatingArea(visionMap.get(npc).sightRange, originTile, true)){
			if(!tile.isEmpty()){
				calculateLOS(originTile, tile, visibleTiles);
			}
		}
		visionMap.get(npc).visionMap = visibleTiles;
	}
	
	private static void calculateLOS(Tile start, Tile end, HashSet<Tile> visibleTiles){
		for(Tile tile : Explorer.getStraigthLine(start.getPos(), end.getPos())){
			visibleTiles.add(tile);
			if(tile.get(Type.TERRAIN) == null || !translucentMap.get(tile.get(Type.TERRAIN)).translucent){
				return;
			}
		}
	}
	
}