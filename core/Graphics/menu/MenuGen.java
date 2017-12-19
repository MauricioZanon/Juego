package menu;

import static components.Mappers.descMap;

import java.util.List;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import inputProcessors.GameInput;
import screens.GameScreenASCII;
import tools.CustomShapeRenderer;
import tools.FontLoader;

public class MenuGen<T> {
	
	public final InputProcessor INPUT = createInputProcessor();
	
	private List<T> list;
	private List<String> relevantStats; //TODO implementar esto
	private Consumer<T> action;
	private Color color;
	private String title;
	
	private int startingX = 350;
	private int startingY = 350;
	
	private int menuWidth = 350;
	private int menuHeight = 350;
	
	//TODO font para titulos y atributos de la tabla (nombre, peso, etc)
	protected BitmapFont itemsFont = FontLoader.fonts.get("menu");
	protected BitmapFont titleFont = FontLoader.fonts.get("title");
	
	protected static int selectedItem = 0;
	
	public MenuGen(List<T> list, List<String> relevantStats, Consumer<T> action, Color color, String title) {
		this.list = list;
		this.relevantStats = relevantStats;
		this.action = action;
		this.color = color;
		this.title = title;
		Gdx.input.setInputProcessor(INPUT);
	}
	
	public void render(SpriteBatch batch, CustomShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.drawMenuBox(startingX, startingY, menuWidth, menuHeight, color);
		sr.end();
		
		batch.begin();
		int x = startingX + 5;
		int y = startingY + menuHeight - 33;
		
		titleFont.setColor(color);
		titleFont.draw(batch, title, x, y + 30);

		for(T item : list){
			String name = descMap.get((Entity)item).name;
			String text = name;
			
			itemsFont.setColor(list.indexOf(item) == selectedItem ? Color.CHARTREUSE : Color.WHITE);
			itemsFont.draw(batch, text, x, y);
			y -= 15;
		}
		
		batch.end();
	}
	
	protected void changeSelectedItem(int v){
		selectedItem += v;
		if(selectedItem < 0) selectedItem = list.size() - 1;
		else if(selectedItem >= list.size()) selectedItem = 0;
	}
	
	protected void recalculateSize() {
		menuHeight = (int) ((list.size() * itemsFont.getCapHeight()) + 50);
		MathUtils.clamp(menuHeight, 30, Gdx.graphics.getHeight());
		startingY = (Gdx.graphics.getHeight() - menuHeight) / 2;
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
