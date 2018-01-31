package com.mygdx.juego;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.Chunk;
import main.Tile;
import world.World;

public class StateSaver {
	
	public static void main(String[] args) {
		Juego.initializeFactories();
		Juego.world = new World();
		Juego.world.initialize();
		save();
	}
	
	private static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:../core/assets/Saves/Test2.db";
            conn = DriverManager.getConnection(url);
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
	
	public static void save() {
		long tiempo = System.currentTimeMillis();
		Connection con = connect();
		Chunk[][][] worldMap = Juego.world.getMap();
		for(int gx = 0; gx < worldMap.length; gx++) {
			System.out.println("saving...");
			for(int gy = 0; gy < worldMap[0].length; gy++) {
				for(int gz = 0; gz < worldMap[0][0].length; gz++) {
					boolean failed = false;
					do {
						try{
							Tile[][] map = worldMap[gx][gy][gz].getChunkMap();
							String chunkCoord = Integer.toString(gx) + ":" + Integer.toString(gy) + ":" + Integer.toString(gz);
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
				}
			}
		}
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
