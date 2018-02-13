package main;

public class EmptyChunk extends Chunk{
	
	public EmptyChunk(int posX, int posY, int posZ){
		gx = posX;
		gy = posY;
		gz = posZ;
		fillLevel(t -> {});
	}
	
}
