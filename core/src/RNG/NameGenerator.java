package RNG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class NameGenerator {
	
	private static HashMap<String, List<String>> names = new HashMap<>();
	
	static {
		try {
			loadNames();
		} catch (IOException e) {}
	}
	
	public static String createName(String actor) {
		String name = RNG.getRandom(names.get(actor));
		String raceName = actor.split(" ")[0];
		if(!names.keySet().contains(raceName + " surname")) {
			return name;
		}
		String surname = RNG.getRandom(names.get(raceName + " surname"));
		names.get(raceName + " surname").remove(surname);
		return name + " " + surname;
	}
	
	private static void loadNames() throws IOException {
		File directory = new File("../core/assets/names");
		File[] nameFiles = directory.listFiles();

		BufferedReader in;
		for(File file : nameFiles) {
			String fileName = file.getName().replaceAll(".txt", "");
			names.put(fileName, new ArrayList<String>());
			in = new BufferedReader(new FileReader(file));
			String name = null;
			while((name = in.readLine()) != null) {
				names.get(fileName).add(name);
			}
			in.close();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("=====FEMALE ELVES=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("elven female"));
		}
		
		System.out.println("\n=====MALE ELVES=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("elven male"));
		}
		
		System.out.println("\n=====FEMALE HUMANS=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("human female"));
		}
		
		System.out.println("\n=====MALE HUMANS=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("human male"));
		}
		
		System.out.println("\n=====ORCS=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("orc"));
		}
		
		System.out.println("\n=====DRAGONS=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("dragon"));
		}
		
		System.out.println("\n=====CITIES=====");
		for(int i = 0; i < 25; i++) {
			System.out.println(createName("cities"));
		}
	}

}
