package XPReader;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class REXLoader {
	
	public static char[][] laod(String path){
		XPChar[][] XProom = null;
		try {
			XProom = REXReader.loadXP(path).layers.get(0).data;
		} catch (IOException | DataFormatException e) {}
		char[][] charRoom = new char[XProom.length][XProom[0].length];
		
		for(int i = 0; i < charRoom.length; i++) {
			for(int j = 0; j < charRoom[0].length; j++) {
				charRoom[i][j] = XProom[i][j].code;
			}
		}
		return charRoom;
	}
}
