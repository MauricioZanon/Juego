package pathFind;

import java.util.LinkedList;

import components.PositionComponent;

public class Path {
	
	private LinkedList<PositionComponent> path = new LinkedList<>();
	
	/**
	 * Agrega una posición al principio del path, se usa solo cuando el path se está creando
	 * @param pos
	 */
	public void addFirst(PositionComponent pos){
		path.addFirst(pos);
	}
	
	/**
	 * @return La próxima posición del path
	 */
	public PositionComponent getNext(){
		return path.getFirst();
	}
	
	/**
	 * Saca la primera posicion del path
	 */
	public void advance(){
		path.removeFirst();
	}
	
	/**
	 * @return si ya se recorrió
	 */
	public boolean isEnded(){
		return path.isEmpty();
	}
	
	public int getDistance(){
		return path.size();
	}
	
	/**
	 * @return la última posición del path
	 */
	public PositionComponent getDestination() {
		return path.getLast();
	}

}
