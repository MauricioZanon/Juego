package com.mygdx.juego;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import forest.ForestLevel;
import main.Chunk;

public class StateLoader {
	
	private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:../core/assets/Saves/world.db");
        } catch (SQLException e) {
            System.out.println("load connection failed " + e.getMessage());
        } 
        return conn;
    }
	
	public static Chunk load(String chunkPos) {
		Connection con = connect();
		Chunk chunk = null;
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT Entities FROM Chunks WHERE ChunkCoord='" + chunkPos + "'");
			ResultSet rs = pstmt.executeQuery();
			chunk = new Chunk(chunkPos, rs.getString("Entities"));
		} catch (SQLException e) {
			int[] coords = Arrays.stream(chunkPos.split(":")).mapToInt(Integer::parseInt).toArray();
	    	int gx = coords[0];
	    	int gy = coords[1];
	    	chunk = new ForestLevel(gx, gy);
		}
		return chunk;
	}
	
}
