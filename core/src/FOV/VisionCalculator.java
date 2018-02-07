package FOV;

import static components.Mappers.factionMap;
import static components.Mappers.visionMap;

import java.util.HashSet;

import com.badlogic.ashley.core.Entity;

import components.Faction;
import components.Mappers;
import components.Type;
import eventSystem.Map;
import main.Tile;
import main.Tile.Visibility;

public abstract class VisionCalculator {
	
	public static void calculateVision(Entity actor){
		if(Mappers.playerMap.has(actor))
			calculatePlayerVision(actor);
		else 
			calculateNPCVision(actor);
	}

	private static void calculatePlayerVision(Entity player) {
		long tiempo = System.currentTimeMillis();
		HashSet<Tile> previouslyVisibleTiles = visionMap.get(player).visionMap;
		HashSet<Tile> newVisibleTiles = new HashSet<>();
		HashSet<Tile> enemyTiles = visionMap.get(player).enemyTiles;
		enemyTiles.clear();
		Tile originTile = Mappers.posMap.get(player).getTile();
		Tile[][] area = Map.getCircundatingAreaAsArray(visionMap.get(player).sightRange, originTile, true);
		Faction faction = factionMap.get(player);
		
		for(int x = 0; x < area.length; x++) {
			for(int y = 0; y < area[0].length; y++) {
				Tile tile = area[x][y];
				if(tile != null && !newVisibleTiles.contains(tile)){
					calculateLOS(originTile, tile, newVisibleTiles, enemyTiles, faction, area);
				}
				
			}
		}
		
		newVisibleTiles.forEach(t -> t.setVisibiliy(Visibility.VISIBLE));
		
		previouslyVisibleTiles.removeAll(newVisibleTiles);
		previouslyVisibleTiles.forEach(t -> t.setVisibiliy(Visibility.VIEWED));
		
		visionMap.get(player).visionMap = newVisibleTiles;
		visionMap.get(player).enemyTiles = enemyTiles;
		
		System.out.println("vision calculator refresh time: " + (System.currentTimeMillis() - tiempo));
	}
	

	private static void calculateNPCVision(Entity npc){
		Tile originTile = Mappers.posMap.get(npc).getTile();
		HashSet<Tile> visibleTiles = visionMap.get(npc).visionMap;
		visibleTiles.clear();
		HashSet<Tile> enemyTiles = visionMap.get(npc).enemyTiles;
		enemyTiles.clear();
		Tile[][] area = Map.getCircundatingAreaAsArray(visionMap.get(npc).sightRange, originTile, true);
		Faction faction = factionMap.get(npc);
		
		for(int x = 0; x < area.length; x++) {
			for(int y = 0; y < area[0].length; y++) {
				Tile tile = area[x][y];
				if(tile != null && !tile.isEmpty()){
					calculateLOS(originTile, tile, visibleTiles, enemyTiles, faction, area);
				}
			}
		}
		visionMap.get(npc).visionMap = visibleTiles;
		visionMap.get(npc).enemyTiles = enemyTiles;
	}
	
	private static void calculateLOS(Tile start, Tile end, HashSet<Tile> visibleTiles, HashSet<Tile> enemyTiles, Faction faction, Tile[][] area){
		for(Tile tile : Map.getStraigthLine(start.getPos(), end.getPos(), area)){
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