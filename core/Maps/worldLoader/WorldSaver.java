package worldLoader;

import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import components.AIComponent;
import components.Mappers;
import factories.ItemFactory;
import factories.NPCFactory;
import main.Chunk;
import main.Tile;

public abstract class WorldSaver {
	
	private static final String PATH = "assets/Data/WorldSave.xml";
	private static XStream xstream = new XStream(new DomDriver());
	
	public static void saveWorld() {
		FileWriter fw = null;
		try {
			fw = new FileWriter(PATH, true);
		} catch (IOException e) {}
		Chunk[][][] map = Juego.world.getMap();
		for(int gz = 0; gz < map[0][0].length; gz++) {
			for(int gy = 0; gy < map[0].length; gy++) {
				for(int gx = 0; gx < map.length; gx++) {
					saveChunk(map[gx][gy][gz].getChunkMap(), fw);
				}
			}
		}
		try {
			fw.close();
		} catch (IOException e) {}
	}
	
	public static void saveChunk(Tile[][] map, FileWriter fw) {
		for(int y = 0; y < map[0].length; y++) {
			for(int x = 0; x < map.length; x++) {
				saveTile(map[x][y], fw);
			}
		}
		
	}
	
	public static void saveTile(Tile tile, FileWriter fw) {
		String s = "<Tile>";
		s += "\n" + xstream.toXML(tile.getPos());
		for(Entity e : tile.getEntities()) {
			switch(Mappers.typeMap.get(e)) {
				case ACTOR:
					e.remove(AIComponent.class);
					s += xstream.toXML("Actor: " + Mappers.descMap.get(e).name);
					break;
				case FEATURE:
					s += xstream.toXML("Feature: " + Mappers.descMap.get(e).name);
					break;
				case ITEM:
					s += xstream.toXML("Item: " + Mappers.descMap.get(e).name);
					break;
				case TERRAIN:
					s += xstream.toXML("Terreno: " + Mappers.descMap.get(e).name);
					break;
				default:
					break;
			}
			s += "\n";
		}
		s += "</Tile>\n";
			
		try {
			fw.write(s);
		} catch (IOException e1) {}
	}
	
	public static void main(String[] args) {
		Juego.world.initialize();
		Tile tile = Juego.world.getMap()[5][5][0].getChunkMap()[5][5];
		tile.put(NPCFactory.createNPC());
		tile.put(ItemFactory.createPotion());
		saveWorld();
		
	}

}
