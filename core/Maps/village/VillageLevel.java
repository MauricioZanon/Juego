//package village;
//
//import static com.mygdx.juego.Juego.world;
//import static components.Mappers.descMap;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//import com.badlogic.ashley.core.Entity;
//
//import RNG.RNG;
//import components.PositionComponent;
//import components.Type;
//import factories.FeatureFactory;
//import factories.TerrainFactory;
//import main.Blueprint;
//import main.Chunk;
//import main.RoomFactory;
//import main.Tile;
//import world.Direction;
//import world.Explorer;
//
//public class VillageLevel extends Chunk{
//	
//	private Set<Tile> road = new HashSet<>();
//	
//	public VillageLevel(int posX, int posY){
//		globalPosX = posX;
//		globalPosY = posY;
//		globalPosZ = 0;
//		
//		Entity grassFloor = TerrainFactory.get("grass floor");
//		Consumer<Tile> createGrassFloor = t -> t.put(grassFloor);
//		fillLevel(createGrassFloor);
//		
//		buildLevel();
//	}
//	
//	@Override
//	protected void buildLevel() {
//		buildRoad();
//		
//		Predicate<Tile> isGrassFloor = t -> t.get(Type.TERRAIN) != null && descMap.get(t.get(Type.TERRAIN)).name.equals("grass floor");
//		Predicate<Tile> adjacentToGrassFloor = t -> Explorer.isOrthogonallyAdjacent(t, isGrassFloor);
//		for(int i = 0; i < 10; i++){
//			Tile roadAnchor = RNG.getRandom(road, adjacentToGrassFloor);
//			createHouse(roadAnchor);
//		}
//	}
//	
//	/**
//	 * TODO: volver a hacer, el camino tiene que poder generarse vertical , horizontal y poder doblar
//	 */
//	private void buildRoad(){
//		int wide = RNG.nextInt(2, 4);
//		int yPos = world.CHUNK_SIZE / 2; 
//		
//		for(int i = 0; i < world.CHUNK_SIZE; i += wide){
//			road.addAll(Explorer.getCircundatingArea(wide, getChunkMap()[i][yPos], false));
//		}
//		
//		Entity floor = TerrainFactory.get("concrete floor");
//		for(Tile tile : road){
//			tile.put(floor); 
//		}
//	}
//	
//	private void createHouse(Tile roadAnchor){
//		Set<Tile> tiles = Explorer.getOrthogonalTiles(roadAnchor, t -> descMap.get(t.get(Type.TERRAIN)).name.equals("grass floor"));
//
//		Tile initialHouseTile = RNG.getRandom(tiles);
//		
//		Direction dir = Direction.get(roadAnchor, initialHouseTile);
//		Blueprint bp = RoomFactory.createHouseBlueprint(dir);
//		List<Integer[]> posibleAnchors = bp.getAnchors(dir);
//		if(posibleAnchors.isEmpty()) return;
//		Integer[] bpAnchor = RNG.getRandom(posibleAnchors);
//		PositionComponent startingPos = initialHouseTile.getPos().clone();
//		
//		startingPos.coord[0] -= bpAnchor[0];
//		startingPos.coord[1] -= bpAnchor[1];
//		
//		Set<Tile> floors = new HashSet<>();
//		Set<Tile> walls = new HashSet<>();
//		Set<Tile> doors = new HashSet<>();
//		
//		char[][] bpArray = bp.getArray();
//		for(int i = 0; i < bpArray.length; i++) {
//			for(int j = 0; j < bpArray[0].length; j++) {
//				Tile tile = Explorer.getTile(startingPos.coord[0] + i, startingPos.coord[1] + j, startingPos.coord[2]);
//				if(tile == null || !validHouseTile(tile)) return;
//				
//				char symbol = bpArray[i][j];
//				switch(symbol) {
//				case '.':
//					floors.add(tile);
//					break;
//				case 'u':
//				case '+':
//					doors.add(tile);
//					floors.add(tile);
//					break;
//				case '#':
//					walls.add(tile);
//					break;
//				}
//			}
//		}
//		buildHouse(walls, floors, doors);
//	}
//	
//	private boolean validHouseTile(Tile tile) {
//		return tile.get(Type.TERRAIN) != null && descMap.get(tile.get(Type.TERRAIN)).name.equals("grass floor");
//	}
//
//	private void buildHouse(Set<Tile> walls, Set<Tile> floors, Set<Tile> doors){
//		Entity houseWall = TerrainFactory.get("wooden wall");
//		Entity houseFloor = TerrainFactory.get("wooden floor");
//		
//		walls.forEach(t -> t.put(houseWall));
//		floors.forEach(t -> t.put(houseFloor));
//		doors.forEach(t -> {
//			t.put(FeatureFactory.createFeature("door"));
//			t.put(houseFloor);
//		});
//	}
//	
//}
