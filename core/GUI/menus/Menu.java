package menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tools.CustomShapeRenderer;
import tools.FontLoader;

public abstract class Menu {
	
	protected Color color;
	protected String title;
	
	protected int startingX;
	protected int startingY;
	
	protected int menuWidth;
	protected int menuHeight;
	
	protected BitmapFont itemsFont = FontLoader.fonts.get("menu");
	protected BitmapFont titleFont = FontLoader.fonts.get("title");
	
	protected int selectedItem = 0;
	
	public abstract void render(SpriteBatch batch, CustomShapeRenderer sr);
	
	protected abstract void changeSelectedItem(int v);
	

}
