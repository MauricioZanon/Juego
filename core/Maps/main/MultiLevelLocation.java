package main;

import java.util.ArrayList;

import components.PositionComponent;

/**
 * TODO: Cambiar el nombre a algo mas intuitivo
 */
public class MultiLevelLocation {
	
	protected ArrayList<Chunk> levels;
	protected PositionComponent entrancePos;
	
	public PositionComponent getEntrancePos() {
		return entrancePos;
	}
}
