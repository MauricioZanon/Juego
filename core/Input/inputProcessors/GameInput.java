package inputProcessors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.juego.Juego;

import actions.ActionType;
import actions.Actions;
import components.ItemType;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import console.MessageFactory;
import eventSystem.EventSystem;
import factories.ItemFactory;
import factories.TerrainFactory;
import main.Tile;
import menu.EntitySelectMenu;
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
				Tile tile = Mappers.posMap.get(Juego.player).getTile();
				tile.put(ItemFactory.createPotion());
				break;
			case Keys.CONTROL_LEFT:
				Tile tile2 = Mappers.posMap.get(Juego.player).getTile();
				tile2.put(ItemFactory.createRandomItem());
				break;
			case Keys.COMMA:
				Actions.pickUp(Explorer.getTile(Juego.player.getComponent(PositionComponent.class)));
				break;
			case Keys.E:
				MessageFactory.createMessage("Use what?");
				Gdx.input.setInputProcessor(new FeatureInput());
				break;
			case Keys.I:
				List<Entity> items = Mappers.inventoryMap.get(Juego.player).getAll();
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(items, new LinkedList<String>(), (i) -> {},Color.RED, "Inventory");
				break;
				
			case Keys.W:
				List<Entity> equipment = Mappers.inventoryMap.get(Juego.player).getList(ItemType.EQUIPMENT);
				List<String> quipmentStats = new LinkedList<String>();
				quipmentStats.add("defense");
				quipmentStats.add("weigth");
				Consumer<Entity> equip = i -> Actions.equip(Juego.player, i);
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(equipment, quipmentStats, equip, Color.BLUE, "Wear");
				break;
				
			case Keys.Q:
				List<Entity> potions = Mappers.inventoryMap.get(Juego.player).getList(ItemType.POTION);
				List<String> potionStats = new LinkedList<String>();
				potionStats.add("effect");
				Consumer<Entity> quaff = i -> Actions.quaff(Juego.player, (Entity)i);
				GameScreenASCII.getInstance().menu = new EntitySelectMenu(potions, potionStats, quaff, Color.GREEN, "Quaff");
				break;
				
			case Keys.S:
//				RenderSystem.setScreen(SkillsScreen.getInstance());
				break;
			case Keys.X:
				State<Entity> exploreState = Mappers.AIMap.get(Juego.player).states.get("exploring");
				Mappers.AIMap.get(Juego.player).fsm.changeState(exploreState);
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
		PositionComponent pos = Juego.player.getComponent(PositionComponent.class);
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
				Actions.endTurn(Juego.player, ActionType.WAIT);
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
		
		PositionComponent playerPos = Juego.player.getComponent(PositionComponent.class);
		Tile clickedTile = GameScreenASCII.getInstance().getClickedTile();
		ArrayList<Tile> lista;
		
		switch(button){
			case 0: //click izquierdo
				Mappers.movMap.get(Juego.player).path = PathFinder.findPath(playerPos, clickedTile.getPos(), Juego.player);
				State<Entity> exploreState = Mappers.AIMap.get(Juego.player).states.get("wandering");
				Mappers.AIMap.get(Juego.player).fsm.changeState(exploreState);
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
				Entity item = ItemFactory.createItem("key");
				
				Mappers.lockMap.get(clickedTile.get(Type.FEATURE)).isLocked = true;
				Mappers.lockMap.get(clickedTile.get(Type.FEATURE)).key = item;
				
				playerPos.getTile().put(item);
				
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
