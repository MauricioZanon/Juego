//package worldLoader;
//
//import static components.Mappers.descMap;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//import com.badlogic.ashley.core.Entity;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//
//import RNG.RNG;
//import components.Mappers;
//import main.Tile;
//
//public abstract class WorldSaver {
//	
//	/**
//	 * TODO: Se tiene que guardar solo el seed del mundo juntos con los tiles que hayan tenido algun cambio
//	 * 		 Cada vez que se carga una partida se ve que los tiles guardados sean distintos a los tiles generados con el seed,
//	 * 		 de no ser asi se elimina el tile del save file
//	 */
//	
//	private static final String PATH = "../core/assets/Data/";
//	private static XStream xstream = new XStream(new DomDriver());
//	private static FileWriter fw;
//	
//	public static void createSaveFile() {
//		try {
//			fw = new FileWriter(PATH + "save.xml");
//			fw.write(String.valueOf(RNG.getSeed()));
//		} catch (IOException e) {}
//	}
//	
//	public static void saveTile(Tile tile) {
//		String s = "\n<Tile>";
//		int[] tileCoords = tile.getPos().coord;
//		s += "\n\t" + xstream.toXML(tileCoords[0] + " " + tileCoords[1] + " " + tileCoords[2]);
//		for(Entity e : tile.getEntities()) {
//			s += "\n\t";
//			switch(Mappers.typeMap.get(e)) {
//			case ACTOR:
//				s += xstream.toXML("Actor: " + descMap.get(e).name);
//				break;
//			case FEATURE:
//				s += xstream.toXML("Feature: " + descMap.get(e).name);
//				break;
//			case ITEM:
//				s += xstream.toXML("Item: " + descMap.get(e).name);
//				break;
//			case TERRAIN:
//				s += xstream.toXML("Terreno: " + descMap.get(e).name);
//				break;
//			default:
//				break;
//			}
//		}
//		s += "\n</Tile>";
//		try {
//			fw.write(s);
//		} catch (IOException e) {}
//	}
//}
