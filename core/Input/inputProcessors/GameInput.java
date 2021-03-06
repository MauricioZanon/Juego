package inputProcessors;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.juego.Juego;
import com.mygdx.juego.StateSaver;

import actions.ActionType;
import actions.Actions;
import components.ItemType;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import console.MessageFactory;
import eventSystem.EventSystem;
import eventSystem.Map;
import factories.ItemFactory;
import factories.TerrainFactory;
import main.Tile;
import menus.EntitySelectMenu;
import screens.GameScreenASCII;
import world.Direction;

public class GameInput implements InputProcessor{
	
	public static Direction playerDir = null;
	
	/**
	 * El tiempo que paso desde que se empezo a mantener una tecla
	 */
	public static int pressTime = 0;
	
	public boolean keyDown(int keycode) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		List<Entity> entities; //Lista para usar con el EntitySelectMenu
		List<String> relevantStats; //Lista para usar con el EntitySelectMenu
		Consumer<Entity> action;
		
		switch(keycode){
			case Keys.ESCAPE:
				System.exit(0);
				return true;
			case Keys.SPACE:
				Tile tile = Mappers.posMap.get(Juego.player).getTile();
				System.out.println(Juego.ENGINE.getEntities().size());
				tile.put(ItemFactory.createPotion());
				return true;
			case Keys.CONTROL_LEFT:
				Tile tile2 = Mappers.posMap.get(Juego.player).getTile();
				tile2.put(ItemFactory.createRandomItem());
				return true;
			case Keys.COMMA:
				int[] playerCoord = Mappers.posMap.get(Juego.player).coord;
				Actions.pickUp(Map.getTile(playerCoord[0], playerCoord[1], playerCoord[2]));
				return true;
			case Keys.L:
				StateSaver.saveGameState();
				return true;
			case Keys.E:
				MessageFactory.createMessage("Use what?");
				Gdx.input.setInputProcessor(new UseInput());
				return true;
			case Keys.I:
				entities = Mappers.inventoryMap.get(Juego.player).getAll();
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(entities, new LinkedList<String>(), (i) -> {},Color.RED, "Inventory");
				return true;
				
			case Keys.W:
				entities = Mappers.inventoryMap.get(Juego.player).getList(ItemType.EQUIPMENT);
				relevantStats = new LinkedList<String>();
				relevantStats.add("defense");
				relevantStats.add("weigth");
				action = i -> Actions.equip(Juego.player, i);
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(entities, relevantStats, action, Color.BLUE, "Wear");
				return true;
				
			case Keys.T:
				entities = new LinkedList<>(Mappers.equipMap.get(Juego.player).wearedEquipment.values());
				relevantStats = new LinkedList<String>();
				relevantStats.add("defense");
				relevantStats.add("weigth");
				action = i -> Actions.takeOff(Juego.player, i);
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(entities, relevantStats, action, Color.BLUE, "Take off");
				return true;
				
			case Keys.Q:
				entities = Mappers.inventoryMap.get(Juego.player).getList(ItemType.POTION);
				relevantStats = new LinkedList<String>();
				relevantStats.add("effect");
				action = i -> Actions.quaff(Juego.player, (Entity)i);
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(entities, relevantStats, action, Color.GREEN, "Quaff");
				return true;
				
			case Keys.X:
				State<Entity> exploreState = Mappers.AIMap.get(Juego.player).states.get("exploring");
				Mappers.AIMap.get(Juego.player).fsm.changeState(exploreState);
				Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput = false;
				return true;
				
			case Keys.NUMPAD_1:
				playerDir = Direction.SW;
				return true;
			case Keys.NUMPAD_2:
				playerDir = Direction.S;
				return true;
			case Keys.NUMPAD_3:
				playerDir = Direction.SE;
				return true;
			case Keys.NUMPAD_4:
				playerDir = Direction.W;
				return true;
			case Keys.NUMPAD_5:
				Actions.endTurn(Juego.player, ActionType.WAIT);
				return true;
			case Keys.NUMPAD_6:
				playerDir = Direction.E;
				return true;
			case Keys.NUMPAD_7:
				playerDir = Direction.NW;
				return true;
			case Keys.NUMPAD_8:
				playerDir = Direction.N;
				return true;
			case Keys.NUMPAD_9:
				playerDir = Direction.NE;
				return true;
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		playerDir = null;
		pressTime = 0;
		return false;
	}

	public boolean keyTyped(char character) {
//		if(playerDir != null) return false;
//		PositionComponent playerPos = Mappers.posMap.get(Juego.player);
//		switch(character) {
//		case '1':
//			playerDir = Direction.SW;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '2':
//			playerDir = Direction.S;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '3':
//			playerDir = Direction.SE;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '4':
//			playerDir = Direction.W;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '5':
//			Actions.endTurn(Juego.player, ActionType.WAIT);
//			return true;
//		case '6':
//			playerDir = Direction.E;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '7':
//			playerDir = Direction.NW;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '8':
//			playerDir = Direction.N;
//			Actions.bump(playerPos, playerDir);
//			break;
//		case '9':
//			playerDir = Direction.NE;
//			Actions.bump(playerPos, playerDir);
//			break;
//		}
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
		
		PositionComponent playerPos = Juego.player.getComponent(PositionComponent.class);
		Tile clickedTile = GameScreenASCII.getInstance().getClickedTile();
		Set<Tile> lista;
		
		switch(button){
			case 0: //click izquierdo
//				Mappers.movMap.get(Juego.player).path = PathFinder.findPath(playerPos, clickedTile.getPos(), Juego.player);
//				State<Entity> exploreState = Mappers.AIMap.get(Juego.player).states.get("wandering");
//				Mappers.AIMap.get(Juego.player).fsm.changeState(exploreState);
//				Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput = false;
				System.out.println(clickedTile);
				return true;
			case 1: //click derecho
				lista = Map.getOrthogonalTiles(clickedTile, t -> true);
				for(Tile tile : lista){
					Entity wall = TerrainFactory.get("concrete floor");
					tile.put(wall);
				}
				return true;
			case 2: //ruedita
				Entity item = ItemFactory.createItem("key");
				
				Mappers.lockMap.get(clickedTile.get(Type.FEATURE)).isLocked = true;
				Mappers.lockMap.get(clickedTile.get(Type.FEATURE)).key = item;
				
				playerPos.getTile().put(item);
				return true;
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
		if(Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) {
			GameScreenASCII.getInstance().refreshMarker(screenX, Gdx.graphics.getHeight() - screenY);
			return true;
		}
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
	

}
