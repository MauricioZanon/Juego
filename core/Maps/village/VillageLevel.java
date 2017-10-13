package village;

import static components.Mappers.nameMap;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Entity;

import RNG.RNG;
import components.Type;
import cosas.Chunk;
import cosas.Tile;
import fatories.TerrainFactory;
import world.Direction;
import world.Explorer;
import world.World;

public class VillageLevel extends Chunk{
	
	private Set<Tile> road = new HashSet<>();
	
	public VillageLevel(int posX, int posY, int posZ){
		globalPosX = posX;
		globalPosY = posY;
		globalPosZ = posZ;
		
		Entity grassFloor = TerrainFactory.get("grass floor");
		Consumer<Tile> createGrassFloor = t -> t.put(grassFloor);
		fillLevel(createGrassFloor);
		
		buildLevel();
//		spawnNPCs("villager");
		
	}
	
	@Override
	protected void buildLevel() {
		System.out.println("Building village...");
		buildRoad();
		
		for(int i = 0; i < 10; i++){
			Predicate<Tile> isGrassFloor = t -> t.get(Type.TERRAIN) != null && nameMap.get(t.get(Type.TERRAIN)).name.equals("grass floor");
			Predicate<Tile> adjacentToGrassFloor = t -> Explorer.isOrthogonallyAdjacent(t, isGrassFloor);
			Tile startingTile = RNG.getRandom(road, adjacentToGrassFloor);
			simulateHouse(startingTile);
		}
	}
	
	private void buildRoad(){
		int wide = RNG.nextInt(2, 4);
		int yPos = RNG.nextInt(20, World.CHUNK_SIZE - 20);
		
		for(int i = 0; i < World.CHUNK_SIZE; i += wide){
			road.addAll(Explorer.getCircundatingArea(wide, getChunkMap()[i][yPos], false));
		}
		
		Entity floor = TerrainFactory.get("concrete floor");
		for(Tile tile : road){
			tile.put(floor); 
		}
	}
	
	private void simulateHouse(Tile roadTile){
		
		Set<Tile> tiles = Explorer.getOrthogonalTiles(roadTile, t -> nameMap.get(t.get(Type.TERRAIN)).name.equals("grass floor"));

		Tile initialHouseTile = RNG.getRandom(tiles);
		
		Direction dir = Direction.get(initialHouseTile, roadTile);
		
		int width = RNG.nextInt(6, 9);
		int height = RNG.nextInt(6, 9);
		
		int x1 = initialHouseTile.getPos().getLx();
		int y1 = initialHouseTile.getPos().getLy();
		int x2 = x1 + width;
		int y2 = y1 + height;
		if(dir.movY == 1){ //Si es Direction.S
			y2 = y1 - height;
			int aux = y1;
			y1 = y2; 
			y2 = aux + 1;
		}
		
		Set<Tile> walls = new HashSet<>();
		Set<Tile> floors = new HashSet<>();
		
		for(int i = y1; i < y2; i++){
			for(int j = x1; j < x2; j++){
				Tile tile;
				try{
					tile = getChunkMap()[j][i];
				}catch(ArrayIndexOutOfBoundsException e){
					return;
				}
				if(!validHouseTile(tile)) {
					return;
				}
				if(i == y1 || i == y2-1 || j == x1 || j == x2-1){
					walls.add(tile);
				}else{
					floors.add(tile);
				}
			}
		}
		
		buildHouse(walls, floors);
		
	}
	
	private boolean validHouseTile(Tile tile) {
		return nameMap.get(tile.get(Type.TERRAIN)).name.equals("grass floor");
	}

	//TODO agregar las puertas y hacer que siempre esten mirando al camino
	private void buildHouse(Set<Tile> walls, Set<Tile> floors){
		Entity houseWall = TerrainFactory.get("wooden wall");
		Entity houseFloor = TerrainFactory.get("wooden floor");
		
		walls.forEach(t -> t.put(houseWall));
		floors.forEach(t -> t.put(houseFloor));
		
	}
	
}
