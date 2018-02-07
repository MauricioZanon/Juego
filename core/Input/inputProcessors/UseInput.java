package inputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.juego.Juego;

import actions.Actions;
import components.PositionComponent;
import eventSystem.Map;
import eventSystem.EventSystem;
import world.Direction;

public class UseInput implements InputProcessor{
	
	public boolean keyDown(int keycode) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		switch(keycode){
			case Keys.ESCAPE:
				Gdx.input.setInputProcessor(new GameInput());
				return true;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		PositionComponent playerPos = Juego.player.getComponent(PositionComponent.class);
		PositionComponent usedEntityPos = null;
		
		switch(character){
			case '1':
				usedEntityPos = Map.getPosition(playerPos, Direction.SW);
				return true;
			case '2':
				usedEntityPos = Map.getPosition(playerPos, Direction.S);
				return true;
			case '3':
				usedEntityPos = Map.getPosition(playerPos, Direction.SE);
				return true;
			case '4':
				usedEntityPos = Map.getPosition(playerPos, Direction.W);
				return true;
			case '5':
				usedEntityPos = playerPos;
				return true;
			case '6':
				usedEntityPos = Map.getPosition(playerPos, Direction.E);
				return true;
			case '7':
				usedEntityPos = Map.getPosition(playerPos, Direction.NW);
				return true;
			case '8':
				usedEntityPos = Map.getPosition(playerPos, Direction.N);
				break;
			case '9':
				usedEntityPos = Map.getPosition(playerPos, Direction.NE);
				return true;
		}
		if(usedEntityPos != null) {
			Actions.useFeature(Juego.player, usedEntityPos);
		}
		Gdx.input.setInputProcessor(new GameInput());
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
		
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

}
