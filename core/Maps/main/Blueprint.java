package main;

import components.PositionComponent;
import world.Direction;

public class Blueprint {
	
	private String[][] array;
	private boolean possible = true;
	
	public Blueprint(String[][] b){
		array = b;
	}
	
	public void rotate() {
		int ii = 0;
		int jj = 0;
		String[][] rotatedArray = new String[array[0].length][array.length]; 
		for(int i=0; i<array[0].length; i++){
			for(int j=array.length-1; j>=0; j--){
				rotatedArray[ii][jj] = array[j][i];
				jj++;
			}
			jj = 0;
			ii++;
		}
		array = rotatedArray;
	}

	public boolean isPossible() {
		return possible;
	}

	public void setPossible(boolean possible) {
		this.possible = possible;
	}
	
	public String[][] getArray() {
		return array;
	}
	
	public PositionComponent correctPosition(PositionComponent startingPos) {
		int[] location = new int[2];
		for(int i = 0; i < array.length; i++){
			for(int j = 0; j < array[0].length; j++){
				if(array[i][j].equals("<")) {
					location[0] = i;
					location[1] = j;
					break;
				}
			}
		}
		
		PositionComponent correctedPos = startingPos.clone();
		correctedPos.coord[0] -= location[0];
		correctedPos.coord[1] += location[1];
		
		return correctedPos;
	}

	/**
	 * Busca un anchor en el blueprint que corresponda con la dirección dada, si no lo encuentra devuelve null
	 * @param dir: La dirección de la salida de la habitación a la que va a ester unida este blueprint
	 * @return
	 */
	
	public PositionComponent correctPosition(PositionComponent startingPos, Direction dir) {
		int[] coord = new int[2];
		
		switch(dir){
		case N:
			for(int i = 0; i < array.length; i++){
				for(int j = 0; j < array[0].length; j++){
					if((j == array[0].length - 1 || array[i][j + 1].equals(" ")) &&
							(array[i][j].equals("u") || array[i][j].equals("U"))){
						coord[0] = i;
						coord[1] = j;
						break;
					}
				}
			}
			break;
		case S:
			for(int i = 0; i < array.length; i++){
				for(int j = 0; j < array[0].length; j++){
					if((j == 0 || array[i][j - 1].equals(" ")) &&
							(array[i][j].equals("u") || array[i][j].equals("U"))){
						coord[0] = i;
						coord[1] = j;
						break;
					}
				}
			}
			break;
		case W:
			for(int i = 0; i < array.length; i++){
				for(int j = 0; j < array[0].length; j++){
					if((i == array.length - 1 || array[i + 1][j].equals(" ")) &&
							(array[i][j].equals("u") || array[i][j].equals("U"))){
						coord[0] = i;
						coord[1] = j;
						break;
					}
				}
			}
			break;
		case E:
			for(int i = 0; i < array.length; i++){
				for(int j = 0; j < array[0].length; j++){
					if((i == 0 || array[i - 1][j].equals(" ")) 
							&& (array[i][j].equals("u") || array[i][j].equals("U"))){
						coord[0] = i;
						coord[1] = j;
						break;
					}
				}
			}
			break;
		default:
			break;
		}
		PositionComponent correctedPos = startingPos.clone();
		correctedPos.coord[0] -= coord[0];
		correctedPos.coord[1] += coord[1];
		
		return correctedPos;
	}
	
	@Override
	public String toString(){
		String string = "";
		for(int i = 0; i < array[0].length;i++){
			for(int j = 0; j < array.length; j++){
				string += array[j][i];
			}
			string += "\n";
		}
		return string;
	}
	
}
