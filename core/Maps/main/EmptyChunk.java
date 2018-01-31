package main;

public class EmptyChunk extends Chunk{
	
	public EmptyChunk(int posX, int posY, int posZ){
		globalPosX = posX;
		globalPosY = posY;
		globalPosZ = posZ;
	}

	@Override
	protected void buildLevel() {
		
	}
}
