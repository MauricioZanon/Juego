package components;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Component;

import main.Tile;

public class VisionComponent implements Component{
	
	public Set<Tile> enemyTiles = new HashSet<Tile>();
	
	public int sightRange = 50;
	
	public HashSet<Tile> visionMap = new HashSet<>();

}
