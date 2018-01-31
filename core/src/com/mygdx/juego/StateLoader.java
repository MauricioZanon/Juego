package com.mygdx.juego;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import components.PositionComponent;
import factories.FeatureFactory;
import factories.ItemFactory;
import factories.NPCFactory;
import factories.TerrainFactory;
import main.Chunk;
import main.EmptyChunk;
import main.Tile;
import world.World;

public class StateLoader {

	private static Connection connect(String path) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(path);
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            conn.createStatement().execute("PRAGMA schema.journal_mode = TRUNCATE");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
	
	public static void load() {
		long tiempo = System.currentTimeMillis();
		World world = new World();
		Chunk[][][] map = world.getMap();
		Connection con = connect("jdbc:sqlite:../core/assets/Saves/Test2.db");
		try (
            Statement stmt  = con.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT * FROM Chunks")){
			
            while (rs.next()) {
            	String chunkCoord = rs.getString("ChunkCoord");
            	int[] coords = Arrays.stream(chunkCoord.split(":")).mapToInt(Integer::parseInt).toArray();
            	Chunk chunk = new EmptyChunk(coords[0], coords[1], coords[2]);
            	map[coords[0]][coords[1]][coords[2]] = chunk;
            	String chunkString = rs.getString("Entities");
            	String[] tileStrings = chunkString.split("-");
            	for(int i = 0; i < tileStrings.length; i++) {
            		String[] entitiesStrings = tileStrings[i].split("\\.");
            		int[] pos = Arrays.stream(entitiesStrings[0].split(":")).mapToInt(Integer::parseInt).toArray();
            		Tile t = new Tile(new PositionComponent(pos));
            		for(int j = 1; j < entitiesStrings.length; j++) {
            			int id = Integer.parseInt(entitiesStrings[j]);
            			if(id >= 3000) {
            				t.put(ItemFactory.createItem(id));
            			}
            			else if(id >= 2000) {
            				t.put(FeatureFactory.createFeature(id));
            			}
            			else if(id >= 1000) {
            				t.put(TerrainFactory.get(id));
            			}
            			else {
            				t.put(NPCFactory.createNPC(id));
            			}
            		}
            		chunk.getChunkMap()[pos[0] % World.CHUNK_SIZE][pos[1] % World.CHUNK_SIZE] = t;
            	}
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		Juego.world = world;
		System.out.println("Tiempo para cargar mundo: " + (System.currentTimeMillis() - tiempo));
	}
	

}
