package menu;

import static components.Mappers.attMap;
import static components.Mappers.inventoryMap;
import static components.Mappers.descMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.juego.Juego;

import inputProcessors.GameInput;
import screens.GameScreenASCII;
import tools.CustomShapeRenderer;

public  class InventoryMenu extends Menu{
	
	private final InputProcessor INVENTORY_INPUT = createInventoryInputProcessor();
	
	public InventoryMenu() {
		items = inventoryMap.get(Juego.player).getAll();
		recalculateSize();
		Gdx.input.setInputProcessor(INVENTORY_INPUT);
	}
	
	@Override
	public void render(SpriteBatch batch, CustomShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.drawMenuBox(startingX, startingY, menuWidth, menuHeight, Color.FIREBRICK);
		sr.end();
		
		batch.begin();
		
		int x = startingX + 5;
		int y = startingY + menuHeight - 33;
		
		titleFont.setColor(Color.FIREBRICK);
		titleFont.draw(batch, "INVENTORY", x, y + 30);

		for(Entity i : items){
			String itemName = descMap.get(i).name;
			int quantity = 0;
			try{
				quantity = (int) attMap.get(i).get("quantity");
			}catch(NullPointerException e) {}
			String text = itemName;
			if(quantity > 1) {
				text += " (" + quantity + ")";
			}
			
			itemsFont.setColor(items.indexOf(i) == selectedItem ? Color.CHARTREUSE : Color.WHITE);
			itemsFont.draw(batch, text, x, y);
			y -= 15;
		}
		
		batch.end();
		
	}
	
	private InputProcessor createInventoryInputProcessor() {
		return new InputProcessor(){
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

