package inputProcessors;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.fsm.State;
import com.mygdx.juego.Juego;

import actions.ActionType;
import actions.Actions;
import components.Mappers;
import components.PositionComponent;
import console.MessageFactory;
import eventSystem.EventSystem;
import factories.ItemFactory;
import factories.TerrainFactory;
import main.Tile;
import menu.EquipMenu;
import menu.InventoryMenu;
import menu.QuaffMenu;
import pathFind.PathFinder;
import screens.GameScreenASCII;
import world.Direction;
import world.Explorer;

public class GameInput implements InputProcessor{
	
	public boolean keyDown(int keycode) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		switch(keycode){
			case Keys.ESCAPE:
				System.exit(0);
				break;
			case Keys.SPACE:
				Tile tile = Mappers.posMap.get(Juego.PLAYER).getTile();
				tile.put(ItemFactory.createPotion());
				break;
			case Keys.CONTROL_LEFT:
				Tile tile2 = Mappers.posMap.get(Juego.PLAYER).getTile();
				tile2.put(ItemFactory.createRandomItem());
				break;
			case Keys.COMMA:
				Actions.pickUp(Explorer.getTile(Juego.PLAYER.getComponent(PositionComponent.class)));
				break;
			case Keys.E:
				MessageFactory.createMessage("Use what?");
				Gdx.input.setInputProcessor(new FeatureInput());
				break;
			case Keys.I:
				GameScreenASCII.getInstance().menu = new InventoryMenu();
				break;
			case Keys.W:
				GameScreenASCII.getInstance().menu = new EquipMenu();
				break;
			case Keys.Q:
				GameScreenASCII.getInstance().menu = new QuaffMenu();
				break;
			case Keys.S:
//				RenderSystem.setScreen(SkillsScreen.getInstance());
				break;
			case Keys.X:
				State<Entity> exploreState = Mappers.AIMap.get(Juego.PLAYER).states.get("exploring");
				Mappers.AIMap.get(Juego.PLAYER).fsm.changeState(exploreState);
				Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput = false;
				break;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		PositionComponent pos = Juego.PLAYER.getComponent(PositionComponent.class);
		switch(character){
			case '1':
				Actions.bump(pos, Direction.SW);
				break;
			case '2':
				Actions.bump(pos, Direction.S);
				break;
			case '3':
				Actions.bump(pos, Direction.SE);
				break;
			case '4':
				Actions.bump(pos, Direction.W);
				break;
			case '5':
				Actions.endTurn(Juego.PLAYER, ActionType.WAIT);
				break;
			case '6':
				Actions.bump(pos, Direction.E);
				break;
			case '7':
				Actions.bump(pos, Direction.NW);
				break;
			case '8':
				Actions.bump(pos, Direction.N);
				break;
			case '9':
				Actions.bump(pos, Direction.NE);
				break;
		}
		GameScreenASCII.getInstance().showMarker = false;
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		PositionComponent playerPos = Juego.PLAYER.getComponent(PositionComponent.class);
		Tile clickedTile = GameScreenASCII.getInstance().getClickedTile();
		ArrayList<Tile> lista;
		
		switch(button){
			case 0: //click izquierdo
				Mappers.movMap.get(Juego.PLAYER).path = PathFinder.findPath(playerPos, clickedTile.getPos(), Juego.PLAYER);
				State<Entity> exploreState = Mappers.AIMap.get(Juego.PLAYER).states.get("wandering");
				Mappers.AIMap.get(Juego.PLAYER).fsm.changeState(exploreState);
				Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput = false;
				break;
			case 1: //click derecho
				lista = Explorer.getStraigthLine(playerPos, clickedTile.getPos());
				for(Tile tile : lista){
					Entity wall = TerrainFactory.get("concrete floor");
					tile.put(wall);
				}
				break;
			case 2: //ruedita
//					lista = World.getStraigthLine(clickedPos, playerPos);
//					for(Tile tile : lista){
//						Entity wall = TerrainPool.get("concrete wall");
//						tile.setTerrain(wall);
//					}
				break;
		}
		
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		GameScreenASCII.getInstance().refreshMarker(screenX, Gdx.graphics.getHeight() - screenY);
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
	

}
