package pathFind;

import components.PositionComponent;

public class Node {
	
	public PositionComponent pos;
	public Node parent;
	public double f = 0, g = 0, h = 0;
	
	/**
	 * @param p = posision que representa
	 * @param par = tile anterior en el camino
	 * @param g = distancia desde el origen hasta el tile actual
	 * @param h = distancia desde el tile actual hasta el destino
	 */
	public Node(PositionComponent p, Node par, double g, double h){
		pos = p;
		parent = par;
		this.g = g;
		this.h = h;
		f = g + h;
	}
	
}
