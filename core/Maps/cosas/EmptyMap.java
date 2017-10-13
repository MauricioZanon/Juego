package cosas;

public class EmptyMap extends Chunk{
	
	public EmptyMap(int posX, int posY, int posZ){
		globalPosX = posX;
		globalPosY = posY;
		globalPosZ = posZ;
	}

	@Override
	protected void buildLevel() {
		
	}
}
