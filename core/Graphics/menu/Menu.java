package menu;

import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import tools.CustomShapeRenderer;
import tools.FontLoader;

public abstract class Menu{
	
	protected List<Entity> items;
	
	protected int startingX = 350;
	protected int startingY = 350;
	
	protected int menuWidth = 350;
	protected int menuHeight = 350;
	
	//TODO font para titulos y atributos de la tabla (nombre, peso, etc)
	protected BitmapFont itemsFont = FontLoader.fonts.get("menu");
	protected BitmapFont titleFont = FontLoader.fonts.get("title");
	
	protected static int selectedItem = 0;
	
	public abstract void render(SpriteBatch batch, CustomShapeRenderer sr);
	
	protected void changeSelectedItem(int v){
		selectedItem = Math.abs((selectedItem+v) % (items.size()));
	}
	
	protected void recalculateSize() {
		menuHeight = (int) ((items.size() * itemsFont.getCapHeight()) + 50);
		MathUtils.clamp(menuHeight, 30, Gdx.graphics.getHeight());
		startingY = (Gdx.graphics.getHeight() - menuHeight) / 2;
	}

}
