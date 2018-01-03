package XPReader;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class VisualizadorDeXP {
	
	public static void main(String[] args) {
		XPChar[][] room = null;
		try {
			room = REXReader.loadXP("assets/rooms and Buildings/Dungeon starting rooms/2.xp").layers.get(0).data;
		} catch (IOException | DataFormatException e) {}
		
		mostrar(room);
		XPChar[][] roomV = espejoVertical(room);
		mostrar(roomV);
		XPChar[][] roomH = espejoHorizontal(room);
		mostrar(roomH);
	}
	
	private static XPChar[][] espejoVertical(XPChar[][] room){
		XPChar[][] invertido = new XPChar[room.length][room[0].length];        
        for(int col = 0; col < room[0].length;col++){
            for(int row = 0; row < room.length;row++){
            	invertido[row][invertido[0].length-1-col] =  room[row][col];
            }
        }
        return invertido;
	}
	
	private static XPChar[][] espejoHorizontal(XPChar[][] room){
		XPChar[][] invertido = new XPChar[room.length][room[0].length];
		for(int col = 0; col < room[0].length;col++){
			for(int row = 0; row < room.length;row++){
				invertido[invertido.length-1-row][col] =  room[row][col];
			}
		}
        return invertido;
	}
	
	private static void mostrar(XPChar[][] room){
		for (XPChar[] lista : room){
			for(XPChar c : lista){
				System.out.print(c.code);
			}
			System.out.println();
		}
	}

}
