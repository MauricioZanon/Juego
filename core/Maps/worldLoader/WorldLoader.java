//package worldLoader;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import RNG.RNG;
//
//public abstract class WorldLoader {
//
//	private static final String PATH = "../core/assets/Data/WorldSave.xml";
//	private static BufferedReader reader;
//	
//	public static void loadWorld() {
//		try {
//			reader = new BufferedReader(new FileReader(PATH));
//		} catch (FileNotFoundException e) {System.out.println("fails");}
//		long seed = 0;
//		try {
//			seed = Long.parseLong(reader.readLine());
//			System.out.println("seed " + seed);
//		} catch (IOException e) {}
//		
//		RNG.setSeed(seed);
//	}
//	
//}
