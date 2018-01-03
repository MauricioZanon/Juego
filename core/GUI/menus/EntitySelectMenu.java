package menus;

import static components.Mappers.attMap;
import static components.Mappers.descMap;

import java.util.List;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import inputProcessors.GameInput;
import screens.GameScreenASCII;
import tools.CustomShapeRenderer;

public class EntitySelectMenu extends Menu{
	
	public final InputProcessor INPUT = createInputProcessor();
	
	private List<Entity> list;
	private List<String> relevantStats; //TODO implementar esto
	private Consumer<Entity> action;
	
	public EntitySelectMenu(List<Entity> list, List<String> relevantStats, Consumer<Entity> action, Color color, String title) {
		this.list = list;
		this.relevantStats = relevantStats;
		this.action = action;
		this.color = color;
		this.title = title;
		Gdx.input.setInputProcessor(INPUT);
		
		menuWidth = (relevantStats.size() + 1) * 100;
		menuWidth = MathUtils.clamp(menuWidth, 150, GameScreenASCII.getInstance().gameScreenSize);
		menuHeight = (int) (list.size() * itemsFont.getCapHeight() + 63);
		
		startingX = GameScreenASCII.getInstance().gameScreenSize / 2 - menuWidth / 2;
		startingY = GameScreenASCII.getInstance().gameScreenSize / 2 - menuHeight / 2;
	}
	
	public void render(SpriteBatch batch, CustomShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.drawMenuBox(startingX, startingY, menuWidth, menuHeight, color);
		sr.end();
		
		batch.begin();
		int x = startingX + 5;
		float y = startingY + menuHeight - 33;
		
		titleFont.setColor(color);
		titleFont.draw(batch, title, x, y + 30);
		
		itemsFont.setColor(Color.WHITE);
		itemsFont.draw(batch, "NAME", x, y);
		for(int i = 0; i < relevantStats.size(); i++) {
			itemsFont.draw(batch, relevantStats.get(i).toUpperCase(), x + ((i+1)*100), y);
		}
		y-= itemsFont.getCapHeight()*1.2;
		
		for(Entity item : list){
			String name = descMap.get((Entity)item).name;
			String text = name;
			
			itemsFont.setColor(list.indexOf(item) == selectedItem ? Color.CHARTREUSE : Color.WHITE);
			itemsFont.draw(batch, text, x, y);
			for(int i = 0; i < relevantStats.size(); i++) {
				itemsFont.draw(batch, Float.toString(attMap.get(item).get(relevantStats.get(i))), x + ((i+1)*100), y);
			}
			y -= itemsFont.getCapHeight()*1.2;
		}
		
		batch.end();
	}
	
	protected void changeSelectedItem(int v){
		selectedItem += v;
		if(selectedItem < 0) selectedItem = list.size() - 1;
		else if(selectedItem >= list.size()) selectedItem = 0;
	}
	
	private InputProcessor createInputProcessor() {
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
					if(!list.isEmpty()){
						action.accept(list.get(selectedItem));
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
				switch(character) {
				case '2':
					changeSelectedItem(1);
					break;
				case '8':
					changeSelectedItem(-1);
					break;
				}
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
