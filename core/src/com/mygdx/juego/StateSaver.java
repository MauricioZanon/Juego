package com.mygdx.juego;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.Chunk;
import main.Tile;

/**
 * TODO: guardar el personaje y el heightMap del World
 *
 */
public class StateSaver {
	
	private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:../core/assets/Saves/world.db");
            conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
	
	public static void createInitialSave(int[][] initialMap) {
		try {
			Files.deleteIfExists(Paths.get("../core/assets/Saves/world.db"));
		} catch (IOException e1) {}
        Connection con = connect();
        try {
			PreparedStatement createWorldTable = con.prepareStatement("CREATE TABLE Chunks( " + 
																	"ChunkCoord TEXT PRIMARY KEY UNIQUE NOT NULL, " + 
																	"Entities  TEXT NOT NULL);");
			createWorldTable.execute();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void save(Chunk chunk) {
		long tiempo = System.currentTimeMillis();
		Connection con = connect();
		boolean failed = false;
		do {
			try{
				Tile[][] map = chunk.getChunkMap();
				String chunkCoord = Integer.toString(chunk.getGx()) + ":" + Integer.toString(chunk.getGy()) + ":" + Integer.toString(chunk.getGz());
				String entities = "";
				for(int x = 0; x < map.length; x++) {
					for(int y = 0; y < map[0].length; y++) {
						entities += map[x][y].getSaveString();
					}
				}
				failed = insert(chunkCoord, entities, con);
			}catch(NullPointerException e) {
				continue;
			}
		}while(failed);
		try {
			con.close();
		} catch (SQLException e) {}
		System.out.println("Time to save " + (System.currentTimeMillis() - tiempo) / 1000 + " segundos");
	}
	
	public static boolean insert(String chunkCoord, String entities, Connection con) {
        try {
        	PreparedStatement pstmt = con.prepareStatement("REPLACE INTO Chunks(ChunkCoord, Entities) VALUES(?, ?)");
        	pstmt.setString(1, chunkCoord);
        	pstmt.setString(2, entities);
        	pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return true;
        }
        return false;
    }
}
