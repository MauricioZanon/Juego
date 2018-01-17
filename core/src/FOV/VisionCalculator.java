package FOV;

import static components.Mappers.factionMap;
import static components.Mappers.visionMap;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import components.Faction;
import components.Mappers;
import components.Type;
import main.Tile;
import main.Tile.Visibility;
import world.Explorer;

public abstract class VisionCalculator {
	
	public static void calculateVision(Entity actor){
		if(Mappers.playerMap.has(actor))
			calculatePlayerVision(actor);
		else 
			calculateNPCVision(actor);
	}

	private static void calculatePlayerVision(Entity player) {
		HashSet<Tile> previoslyVisibleTiles = visionMap.get(player).visionMap;
		HashSet<Tile> newVisibleTiles = new HashSet<>();
		HashSet<Tile> enemyTiles = visionMap.get(player).enemyTiles;
		enemyTiles.clear();
		Tile originTile = Mappers.posMap.get(player).getTile();
		Set<Tile> area = Explorer.getCircundatingArea(visionMap.get(player).sightRange, originTile, true);
		Faction faction = factionMap.get(player);
		
		for(Tile t : area) {
			if(!newVisibleTiles.contains(t)){
				calculateLOS(originTile, t, newVisibleTiles, enemyTiles, faction);
			}
		}
		newVisibleTiles.forEach(t -> t.setVisibiliy(Visibility.VISIBLE));
		
		previoslyVisibleTiles.removeAll(newVisibleTiles);
		previoslyVisibleTiles.forEach(t -> t.setVisibiliy(Visibility.VIEWED));
		
		visionMap.get(player).visionMap = newVisibleTiles;
		visionMap.get(player).enemyTiles = enemyTiles;
	}
	

	private static void calculateNPCVision(Entity npc){
		Tile originTile = Mappers.posMap.get(npc).getTile();
		HashSet<Tile> visibleTiles = visionMap.get(npc).visionMap;
		visibleTiles.clear();
		HashSet<Tile> enemyTiles = visionMap.get(npc).enemyTiles;
		enemyTiles.clear();
		Faction faction = factionMap.get(npc);
		
		for(Tile tile : Explorer.getCircundatingArea(visionMap.get(npc).sightRange, originTile, true)){
			if(!tile.isEmpty()){
				calculateLOS(originTile, tile, visibleTiles, enemyTiles, faction);
			}
		}
		visionMap.get(npc).visionMap = visibleTiles;
		visionMap.get(npc).enemyTiles = enemyTiles;
	}
	
	private static void calculateLOS(Tile start, Tile end, HashSet<Tile> visibleTiles, HashSet<Tile> enemyTiles, Faction faction){
		for(Tile tile : Explorer.getStraigthLine(start.getPos(), end.getPos())){
			visibleTiles.add(tile);
			if(faction.isEnemy(tile.get(Type.ACTOR))) {
				enemyTiles.add(tile);
			}
			if(tile.get(Type.TERRAIN) == null || !tile.isTranslucent()){
				return;
			}
		}
	}
	
}