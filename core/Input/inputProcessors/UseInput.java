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
		
		PositionComponent playerPos = Juego.player.getComponent(PositionComponent.class);
		PositionComponent usedEntityPos = null;
		
		switch(keycode){
			case Keys.ESCAPE:
				Gdx.input.setInputProcessor(new GameInput());
				return true;
			case Keys.NUMPAD_1:
				usedEntityPos = Map.getPosition(playerPos, Direction.SW);
				break;
			case Keys.NUMPAD_2:
				usedEntityPos = Map.getPosition(playerPos, Direction.S);
				break;
			case Keys.NUMPAD_3:
				usedEntityPos = Map.getPosition(playerPos, Direction.SE);
				break;
			case Keys.NUMPAD_4:
				usedEntityPos = Map.getPosition(playerPos, Direction.W);
				break;
			case Keys.NUMPAD_5:
				usedEntityPos = playerPos;
				break;
			case Keys.NUMPAD_6:
				usedEntityPos = Map.getPosition(playerPos, Direction.E);
				break;
			case Keys.NUMPAD_7:
				usedEntityPos = Map.getPosition(playerPos, Direction.NW);
				break;
			case Keys.NUMPAD_8:
				usedEntityPos = Map.getPosition(playerPos, Direction.N);
				break;
			case Keys.NUMPAD_9:
				usedEntityPos = Map.getPosition(playerPos, Direction.NE);
				break;
		}
		
		if(usedEntityPos != null) {
			Actions.useFeature(Juego.player, usedEntityPos);
		}
		Gdx.input.setInputProcessor(new GameInput());
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
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
