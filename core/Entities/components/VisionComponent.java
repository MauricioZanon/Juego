package components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;

import main.Tile;

public class VisionComponent implements Component{
	
	//TODO aca va el tipo de vision de una entidad mas su mapa de visión
	
	public int sightRange = 50;
	
	public HashSet<Tile> visionMap = new HashSet<>();

}
