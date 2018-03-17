package pathFind;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.ashley.core.Entity;

import components.PositionComponent;
import eventSystem.Map;
import main.Chunk;
import main.Tile;

public abstract class PathFinder {

	private static final int MAX_DISTANCE = Chunk.SIZE;
	private static Comparator<Node> comparador = new Comparator<Node>(){
		@Override
		public int compare(Node n1, Node n2) {
			if(n1.f < n2.f){
				return -1;
			}
			else if(n1.f > n2.f){
				return 1;
			}
			else{
				return 0;
			}
		}
	};
	
	/**
	 * Busca un camino desde la primer posición hasta la segunda, si no es posible devuelve null
	 * TODO: Hacer que los tiles sean transitables o no dependiendo de el tipo de movimiento del actor
	 */
	public static Path findPath(PositionComponent start, PositionComponent end, Entity actor){
		if(!end.getTile().isTransitable()){
			return null;
		}
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> closed = new ArrayList<Node>();
		Node currentNode = new Node(start, null, 0, Map.getDistance(start, end));
		open.add(currentNode);
		
		while(!open.isEmpty()){
			open.sort(comparador);
			currentNode = open.remove(0);
			closed.add(currentNode);
			
			if(currentNode.pos.equals(end)){
				return createPath(currentNode);
			}
			
			// Evaluando tiles adyacentes
			for(Tile tile : Map.getOrthogonalTiles(currentNode.pos.getTile(), t -> t.isTransitable())){
				PositionComponent neighborPos = tile.getPos();
				double g = Map.getDistance(start, neighborPos);
				if(g > MAX_DISTANCE){
					continue;
				}
				double h = currentNode.h + Map.getDistance(currentNode.pos, end);
				Node neighborNode = new Node(neighborPos, currentNode, g, h);
				
				if(!nodePresent(closed, neighborNode) && (!nodePresent(open, neighborNode) || h < getNode(open, neighborNode).h)){
					open.add(neighborNode);
				}
			}
			for(Tile tile : Map.getDiagonalTiles(currentNode.pos.getTile(), t -> t.isTransitable())){
				PositionComponent neighborPos = tile.getPos();
				double g = Map.getDistance(start, neighborPos);
				if(g > MAX_DISTANCE){
					continue;
				}
				double h = currentNode.h + Map.getDistance(currentNode.pos, end);
				Node neighborNode = new Node(neighborPos, currentNode, g, h);
				
				if(!nodePresent(closed, neighborNode) && (!nodePresent(open, neighborNode) || h < getNode(open, neighborNode).h)){
					open.add(neighborNode);
				}
			}
		}
		return null; // si el camino no es posible devuelve null
	}
	
	private static Path createPath(Node currentNode){
		Path path = new Path();
		while(currentNode.parent != null){
			path.addFirst(currentNode.pos);
			currentNode = currentNode.parent;
		}
		return path;
	}
	
	private static boolean nodePresent(ArrayList<Node> lista, Node n){
		for(Node node : lista){
			if(node.pos.equals(n.pos)){
				return true;
			}
		}
		return false;
	}
	
	private static Node getNode(ArrayList<Node> list, Node n){
		for(Node node : list){
			if(node.pos.equals(n.pos)){
				return node;
			}
		}
		return null;
	}

	public static int getWalkableDistance(Tile start, Tile end) {
		Path path = findPath(start.getPos(), end.getPos(), null);

		return path == null ? -1 : path.getDistance();
	}
	

	
//	public static ArrayList<Tile> getStraigthLine(Tile start, Tile end, Position[][] map){
//		ArrayList<Tile> linea = new ArrayList<Tile>();
//		if(start.equals(end)){
//			linea.add(start);
//			return linea;
//		}
//		double x1 = start.getPosX(), x2 = end.getPosX(), y1 = start.getPosY(), y2 = end.getPosY();
//		double angle = calculateAngle(x1, y1, x2, y2);
//		double xMov = Math.cos(angle * 0.01745);
//		double yMov = Math.sin(angle * 0.01745);
//		double x = x1 + 0.5;
//		double y = y1 + 0.5;
//		while ((int)x != x2 || (int)y != y2) {
//			try {
//				linea.add(map[(int) x][(int) y]);
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			x += xMov;
//			y += yMov;
//		}
//		linea.add(map[(int) x][(int) y]);
//		return linea;
//	}
	
	
}
