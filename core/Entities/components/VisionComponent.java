package components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;

import main.Tile;

public class VisionComponent implements Component{
	
	public int sightRange = 50;
	
	public HashSet<Tile> visionMap = new HashSet<>();
	public HashSet<Tile> enemyTiles = new HashSet<>();

}
