package com.mygdx.juego;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.ashley.core.Entity;

import components.Mappers;
import components.Type;
import eventSystem.Map;
import main.Chunk;

/**
 * TODO: guardar el heightMap del World y los lugares de las locations
 */
public class StateSaver {
	
	private volatile static HashMap<String, String> chunksToSave = new HashMap<>();
	public static Object saveThreadLock = new Object();
	
	public static Thread savingThread = new Thread(() -> {
		synchronized (saveThreadLock) {
			while(true) {
				Connection con = connect();
				Set<Entry<String, String>> entries = new HashSet<>();
				entries.addAll(chunksToSave.entrySet());
				chunksToSave.clear();
				
				for(Entry<String, String> e : entries) {
					save(e.getKey(), e.getValue(), con);
				}
				close(con);
				try {
					saveThreadLock.wait();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	});
	
	static {
		savingThread.setName("saving thread");
		savingThread.start();
	}
		
	private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:../core/assets/Saves/world.db");
//            conn.createStatement().execute("PRAGMA locking_mode = EXCLUSIVE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
	
	private static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {}
	}
	
	public static void createInitialSave() {
		try {
			Files.deleteIfExists(Paths.get("../core/assets/Saves/world.db"));
		} catch (IOException e1) {}
        Connection con = connect();
        try {
			PreparedStatement createWorldTable = con.prepareStatement("CREATE TABLE Chunks( " + 
																	"ChunkCoord TEXT PRIMARY KEY UNIQUE NOT NULL, " + 
																	"Entities  TEXT NOT NULL);");
			createWorldTable.execute();
			PreparedStatement createPlayerTable = con.prepareStatement("CREATE TABLE Player( " +
																		"Position TEXT NOT NULL, " +
																		"HP TEXT, " +
																		"Stats TEXT NOT NULL, " +
																		"Equipment TEXT, " +
																		"Effects TEXT, " +
																		"Items TEXT);");
			createPlayerTable.execute();
			PreparedStatement createWorldInfoTable = con.prepareStatement("CREATE TABLE WorldInfo( " +
																		"Seed INTEGER NOT NULL, " +
																		"Items TEXT);");
			createWorldInfoTable.execute();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void save(String chunkCoord, String entities, Connection con) {
		boolean failed = false;
		do {
			failed = insert(chunkCoord, entities, con);
		}while(failed);
	}
	
	public static void save(Chunk chunk, Connection con) {
		boolean failed = false;
		do {
			String chunkCoord = Integer.toString(chunk.getGx()) + ":" + Integer.toString(chunk.getGy()) + ":" + Integer.toString(chunk.getGz());
			String entities = chunk.serialize();
			failed = insert(chunkCoord, entities, con);
		}while(failed);
	}
	
	public static void saveGameState() {
		long tiempo = System.currentTimeMillis();
		savePlayerState();
		saveWorldState();
		System.out.println("save time " + (System.currentTimeMillis() - tiempo));
	}
	
	public static void savePlayerState() {
		Entity player = Juego.player;
		
		Connection con = connect();
		System.out.println("chunks en memoria " + Map.getChunksInMemory().values().size());
		
		String playerPos = Mappers.posMap.get(player).toString();
		String playerHP = Mappers.healthMap.get(player).serialize();
		String playerStats = Mappers.attMap.get(player).serialize();
		String playerEquipment = Mappers.equipMap.get(player).serialize();
		String playerEffects = Mappers.statusEffectsMap.get(player).serialize();
		String playerItems = Mappers.inventoryMap.get(player).serialize();
		
		try {
			PreparedStatement reset = con.prepareStatement("DELETE FROM Player");
			reset.executeUpdate();
			
        	PreparedStatement pstmt = con.prepareStatement("REPLACE INTO Player(Position, HP, Stats, Equipment, Effects, Items) VALUES(?, ?, ?, ?, ?, ?)");
        	pstmt.setString(1, playerPos);
        	pstmt.setString(2, playerHP);
        	pstmt.setString(3, playerStats);
        	pstmt.setString(4, playerEquipment);
        	pstmt.setString(5, playerEffects);
        	pstmt.setString(6, playerItems);
        	pstmt.executeUpdate();
		} catch (SQLException e) {
			close(con);
			System.out.println(e.getMessage());
   	    }
		close(con);
	}
	
	public static void saveWorldState() {
		Mappers.posMap.get(Juego.player).getTile().remove(Type.ACTOR);
		Connection con = connect();
		for(Chunk chunk : Map.getChunksInMemory().values()) {
			save(chunk, con);
		}
		close(con);
		Mappers.posMap.get(Juego.player).getTile().put(Juego.player);
	}
	
	private static boolean insert(String chunkCoord, String entities, Connection con) {
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
	
	public static void addChunkToSaveList(Chunk chunk) {
		String chunkCoord = Integer.toString(chunk.getGx()) + ":" + Integer.toString(chunk.getGy()) + ":" + Integer.toString(chunk.getGz());
		String entities = chunk.serialize();
		
		chunksToSave.put(chunkCoord, entities);
	}
	
}
