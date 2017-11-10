package inputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.juego.Juego;

import actions.Actions;
import components.PositionComponent;
import eventSystem.EventSystem;
import world.Direction;
import world.Explorer;

public class FeatureInput implements InputProcessor{
	
	public boolean keyDown(int keycode) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		switch(keycode){
			case Keys.ESCAPE:
				Gdx.input.setInputProcessor(new GameInput());
				break;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		PositionComponent playerPos = Juego.player.getComponent(PositionComponent.class);
		PositionComponent featurePos = null;
		
		switch(character){
			case '1':
				featurePos = Explorer.getPosition(playerPos, Direction.SW);
				break;
			case '2':
				featurePos = Explorer.getPosition(playerPos, Direction.S);
				break;
			case '3':
				featurePos = Explorer.getPosition(playerPos, Direction.SE);
				break;
			case '4':
				featurePos = Explorer.getPosition(playerPos, Direction.W);
				break;
			case '5':
				featurePos = playerPos;
				break;
			case '6':
				featurePos = Explorer.getPosition(playerPos, Direction.E);
				break;
			case '7':
				featurePos = Explorer.getPosition(playerPos, Direction.NW);
				break;
			case '8':
				featurePos = Explorer.getPosition(playerPos, Direction.N);
				break;
			case '9':
				featurePos = Explorer.getPosition(playerPos, Direction.NE);
				break;
			default:
				break;
		}
		if(featurePos != null) {
			Actions.useFeature(Juego.player, featurePos);
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
