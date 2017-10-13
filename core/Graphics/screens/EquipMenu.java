package screens;

import static components.Mappers.attMap;
import static components.Mappers.inventoryMap;
import static components.Mappers.nameMap;

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

public class EquipMenu extends Menu{
	
	private final InputProcessor EQUIP_INPUT = createEquipProcessor();
	
	public EquipMenu() {
		items = inventoryMap.get(Juego.PLAYER).getList(ItemType.EQUIPMENT);
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
			String equipmentName = nameMap.get(p).name;
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
					GameScreenASCII.getInstance().setGameInput();
					break;
				case Keys.DOWN:
					changeSelectedItem(1);
					break;
				case Keys.UP:
					changeSelectedItem(-1);
					break;
				case Keys.ENTER:
					GameScreenASCII.getInstance().menu = null;
					GameScreenASCII.getInstance().setGameInput();
					if(!items.isEmpty()){
						Entity equipment = items.get(selectedItem);
						Actions.equip(Juego.PLAYER, equipment);
						inventoryMap.get(Juego.PLAYER).remove(equipment);
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
