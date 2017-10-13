package components;

import java.util.Comparator;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * El objecto que tenga el render priority mas alto en el tile es el que se va a mostrar en pantalla
 * 
 * 0 - terrenos
 * 1 - items
 * 2 - features
 * 3 - actores
 */
public class GraphicsComponent implements Component{
	
	//TODO: agregar boolean visible para saber si esto se debe dibujar o no
	//TODO separar este components en dos distintos, uno para el front color (con el ASCII y fuente) y otro
	//		para el back color (con el color original, el color mostrado y la chance que tiene de modificar el color)
	
	public String ASCII = null;
	public String font = null;
	public Color frontColor = null;
	public Color backColor = null;
	public int renderPriority = 0;
	
	public static Comparator<GraphicsComponent> frontComparator = new Comparator<GraphicsComponent>() {
		@Override
		public int compare(GraphicsComponent g1, GraphicsComponent g2) {
			if(g1 == null || g1.renderPriority < g2.renderPriority) return 1;
			if(g2 == null || g1.renderPriority > g2.renderPriority) return -1;
			return 0;
		}
	};

	public static Comparator<GraphicsComponent> backComparator = new Comparator<GraphicsComponent>() {
		@Override
		public int compare(GraphicsComponent g1, GraphicsComponent g2) {
			if(g1 == null && g2 == null) return 0;
			if(g1 == null) return 1;
			if(g2 == null) return -1;
			if(g1.backColor == null && g2.backColor == null) return 0;
			if(g1.backColor == null) return 1;
			if(g2.backColor == null) return -1;
			if(g1.renderPriority < g2.renderPriority) return 1;
			if(g1.renderPriority > g2.renderPriority) return -1;
			return 0;
		}
	};
	
}
