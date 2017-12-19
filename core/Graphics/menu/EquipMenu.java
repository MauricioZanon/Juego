package menu;

import static components.Mappers.attMap;
import static components.Mappers.descMap;
import static components.Mappers.inventoryMap;
import static components.Mappers.itemTypeMap;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.juego.Juego;

import actions.Actions;
import components.ItemType;
import components.Mappers;
import console.MessageFactory;
import inputProcessors.GameInput;
import screens.GameScreenASCII;
import tools.CustomShapeRenderer;

public class EquipMenu extends Menu{
	
	//TODO: separar en dos menus, wear para la ropa, armadura, etc y wield para sostener algo en la mano
	
	private final InputProcessor EQUIP_INPUT = createEquipProcessor();
	
	public EquipMenu() {
		items = inventoryMap.get(Juego.player).getList(ItemType.EQUIPMENT);
		recalculateSize();
		Gdx.input.setInputProcessor(EQUIP_INPUT);
	}
	
	@Override
	public void render(SpriteBatch batch, CustomShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.drawMenuBox(startingX, startingY, menuWidth, menuHeight, Color.BLUE);
		sr.end();
		
		batch.begin();
		int x = startingX + 5;
		int y = startingY + menuHeight - 33;
		
		titleFont.setColor(Color.BLUE);
		titleFont.draw(batch, "EQUIPMENT", x, y + 30);

		for(Entity p : items){
			String equipmentName = descMap.get(p).name;
			int quantity = (int) attMap.get(p).get("quantity");
			String text = equipmentName;
			if(quantity > 1) {
				text += " (" + quantity + ")";
			}
			
			itemsFont.setColor(items.indexOf(p) == selectedItem ? Color.CHARTREUSE : Color.WHITE);
			itemsFont.draw(batch, text, x, y);
			y -= 15;
		}
		
		batch.end();
		
	}

	private InputProcessor createEquipProcessor() {
		return new InputProcessor() {
			
			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.ESCAPE:
					GameScreenASCII.getInstance().menu = null;
					Gdx.input.setInputProcessor(new GameInput());
					break;
				case Keys.DOWN:
					changeSelectedItem(1);
					break;
				case Keys.UP:
					changeSelectedItem(-1);
					break;
				case Keys.ENTER:
					GameScreenASCII.getInstance().menu = null;
					Gdx.input.setInputProcessor(new GameInput());
					if(!items.isEmpty()){
						Entity e = items.get(selectedItem);
						ItemType type = itemTypeMap.get(e);
						HashMap<ItemType, Entity> equipmentOnPlayer = Mappers.equipMap.get(Juego.player).equipment;
						
						if(equipmentOnPlayer.keySet().contains(type)) {
							String name = Mappers.descMap.get(equipmentOnPlayer.get(type)).name;
							MessageFactory.createMessage("You must remove your " + name + " first.");
						}else {
							Actions.equip(Juego.player, e);
							inventoryMap.get(Juego.player).remove(e);
							String name = Mappers.descMap.get(e).name;
							MessageFactory.createMessage("You put on your " + name + ".");
						}
					}
					break;
			}
			return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
		};
	}
}
