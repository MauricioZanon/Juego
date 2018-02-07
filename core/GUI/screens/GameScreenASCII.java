package screens;

import static components.Mappers.graphMap;
import static tools.FontLoader.fonts;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.mygdx.juego.Juego;

import components.GraphicsComponent;
import components.HealthComponent;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import console.Console;
import console.Message;
import eventSystem.Map;
import main.Tile;
import main.Tile.Visibility;
import menus.Menu;
import tools.CustomShapeRenderer;
import tools.FontLoader;
import world.Time;

public class GameScreenASCII implements Screen{
	
	private SpriteBatch batch;
	private CustomShapeRenderer shapeRenderer;
	
	private static GameScreenASCII instance = new GameScreenASCII();
	public final static int TILE_SIZE = 18;
	public int gameScreenSize = Gdx.graphics.getHeight(); 
	private int gameScreenTiles = Gdx.graphics.getHeight()/TILE_SIZE;
	
	public Menu menu = null;
	
	private GameScreenASCII(){
		FontLoader.load(TILE_SIZE);
	}
	
	public static GameScreenASCII getInstance(){
		return instance;
	}
	
	public void show() {
		batch = new SpriteBatch();
		shapeRenderer = new CustomShapeRenderer();
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		drawMap();
		drawConsole();
		drawMiniMap();
		drawStats();
		drawTime();
		drawSideBar();
		if(menu == null) {
			drawMarker();
			drawMarkedTileInfo();
		}else {
			menu.render(batch, shapeRenderer);
		}
		
		dispose();
	}
	
	private int mapCenter = (gameScreenSize / 2) - 8;
	
	private void drawMap(){
		PositionComponent pos = Mappers.posMap.get(Juego.player).clone();
		pos.coord[0] -= 50;
		pos.coord[1] -= 50;
		
		Tile[][] map = Map.getSquareAreaAsArray(pos, 100, 100);
		int center = map.length/2;
		
		//Dibujar fondo
		shapeRenderer.begin(ShapeType.Filled);
		for (int x = -gameScreenTiles/2; x <= gameScreenTiles/2; x++){
			for(int y = -gameScreenTiles/2; y <= gameScreenTiles/2; y++){
				Tile tile = null;
				GraphicsComponent gc = null;
				try {
					tile = map[center+x][center+y];
					gc = tile.getBackGC();
				}
				catch(ArrayIndexOutOfBoundsException | NullPointerException e) {continue;}
				if(tile.getVisibility() != Visibility.VISIBLE || gc == null || gc.backColor == null) continue;
				int xImg = (x * TILE_SIZE) + mapCenter;
				int yImg = (y * TILE_SIZE) + mapCenter;
				
				Color color = new Color(gc.backColor).lerp(Color.BLACK, 1f - tile.getLightLevel());
				shapeRenderer.setColor(color);
				shapeRenderer.rect(xImg, yImg, TILE_SIZE, TILE_SIZE);
			}
		}
		shapeRenderer.end();
		
		// Dibujar ASCII
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		for (int x = -gameScreenTiles/2; x <= gameScreenTiles/2; x++){
			for(int y = -gameScreenTiles/2; y <= gameScreenTiles/2; y++){
				Tile tile = null;
				try {
					tile = map[center+x][center+y];
				}
				catch(ArrayIndexOutOfBoundsException | NullPointerException e) {continue;}
				if(tile == null || tile.getVisibility() != Visibility.VISIBLE) continue;
				int xImg = (x * TILE_SIZE) + mapCenter;
				int yImg = (y * TILE_SIZE) + mapCenter;
				
				GraphicsComponent gc;
				Color color;
				gc = tile.getFrontGC();
				if(gc == null) continue;
				color = new Color(gc.frontColor).lerp(Color.BLACK, 1 - tile.getLightLevel());
				drawASCII(batch, fonts.get(gc.font), color, layout, gc.ASCII, xImg, yImg);
			}
		}
		batch.end();
	}
	
	private void drawASCII(SpriteBatch batch, BitmapFont font, Color color, GlyphLayout layout, String ASCII, int xPos, int yPos){
		font.setColor(color);
		layout.setText(font, ASCII);
		
		float correctedXPos = xPos + (TILE_SIZE - layout.width) / 2;
		float correctedYPos = yPos - (TILE_SIZE - layout.height) / 2 + TILE_SIZE;
		
		font.draw(batch, layout, correctedXPos, correctedYPos);
	}
	
	private final int MINI_MAP_SIZE = Gdx.graphics.getWidth() - gameScreenSize;
	private final int SIDE_BAR_X_POS = gameScreenSize + 8;
	private final int MINI_MAP_Y_POS = 0;
	private final float PIXEL_SIZE = (float)MINI_MAP_SIZE / 100;
	
	private void drawMiniMap(){
		PositionComponent pos = Mappers.posMap.get(Juego.player).clone();
		pos.coord[0] -= 50;
		pos.coord[1] -= 50;
		
		Tile[][] map = Map.getSquareAreaAsArray(pos, 100, 100);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		for (int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				Tile tile = map[x][y];
				if(tile == null || tile.getVisibility() == Visibility.NOT_VIEWED)
					shapeRenderer.setColor(Color.BLACK);
				else if(tile.getVisibility() == Visibility.VIEWED)
					shapeRenderer.setColor(Color.DARK_GRAY);
				else{
					Color color;
					if(tile.get(Type.ACTOR) != null) {
						color = graphMap.get(tile.get(Type.ACTOR)).frontColor;
					}
					else if(tile.get(Type.FEATURE) != null) {
						color = graphMap.get(tile.get(Type.FEATURE)).frontColor;
					}else {
						try {
							color = graphMap.get(tile.get(Type.TERRAIN)).backColor;
						}catch(NullPointerException e) {continue;}
					}
					color = new Color(color).lerp(Color.BLACK, 1f - tile.getLightLevel());
					shapeRenderer.setColor(color);
				}
				shapeRenderer.rect(SIDE_BAR_X_POS + x*PIXEL_SIZE, MINI_MAP_Y_POS + y*PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
			}
		}
		shapeRenderer.end();
	}
	
	private void drawSideBar(){
		shapeRenderer.begin(ShapeType.Line);

		int y = MINI_MAP_Y_POS + MINI_MAP_SIZE;
		
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.line(gameScreenSize + 4, 0, gameScreenSize + 4, Gdx.graphics.getHeight());
		shapeRenderer.line(gameScreenSize + 4, y, Gdx.graphics.getWidth(), y);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.line(gameScreenSize + 7, 0, gameScreenSize + 7, Gdx.graphics.getHeight());
		shapeRenderer.line(gameScreenSize + 4, y - 3, Gdx.graphics.getWidth(), y - 3);
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.line(gameScreenSize + 6, 0, gameScreenSize + 6, Gdx.graphics.getHeight());
		shapeRenderer.line(gameScreenSize + 5, 0, gameScreenSize + 5, Gdx.graphics.getHeight());
		shapeRenderer.line(gameScreenSize + 4, y - 1, Gdx.graphics.getWidth(), y - 1);
		shapeRenderer.line(gameScreenSize + 4, y - 2, Gdx.graphics.getWidth(), y - 2);
		
		shapeRenderer.end();
	}
	
	private final int HEALTH_BAR_X_POS = SIDE_BAR_X_POS + 20;
	private final int HEALTH_BAR_Y_POS = gameScreenSize - 40;
	private final int HEALTH_BAR_HEIGHT = 20;
	private final int HEALTH_BAR_WIDTH = 100;
	private final Color HEALTH_BAR_COLOR = new Color(.5607f, .7372f, .5607f, 1f);
	private final Color HEALTH_BAR_BACK_COLOR = new Color(HEALTH_BAR_COLOR).lerp(Color.BLACK, .2f);
	private final Color HEALTH_BAR_BORDER_COLOR = new Color(HEALTH_BAR_BACK_COLOR).lerp(Color.BLACK, .2f);
	
	private void drawStats(){
		HealthComponent playerHP = Mappers.healthMap.get(Juego.player);
		float healthPercent = HEALTH_BAR_WIDTH * playerHP.curHP / playerHP.maxHP;
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(HEALTH_BAR_BORDER_COLOR);
		shapeRenderer.rect(HEALTH_BAR_X_POS - 4, HEALTH_BAR_Y_POS - 4, HEALTH_BAR_WIDTH + 8, HEALTH_BAR_HEIGHT + 8);
		
		shapeRenderer.setColor(HEALTH_BAR_BACK_COLOR);
		shapeRenderer.rect(HEALTH_BAR_X_POS - 2, HEALTH_BAR_Y_POS - 2, HEALTH_BAR_WIDTH + 4, HEALTH_BAR_HEIGHT + 4);
		
		shapeRenderer.setColor(HEALTH_BAR_COLOR);
		shapeRenderer.rect(HEALTH_BAR_X_POS, HEALTH_BAR_Y_POS, healthPercent, HEALTH_BAR_HEIGHT);
		
		shapeRenderer.end();
	}
	
	private void drawTime() {
		BitmapFont font = FontLoader.fonts.get("general");
		font.setColor(Color.WHITE);
		
		batch.begin();
		font.draw(batch, Time.getHour(), HEALTH_BAR_X_POS, HEALTH_BAR_Y_POS - 50);
		font.draw(batch, Mappers.posMap.get(Juego.player).toString(), HEALTH_BAR_X_POS, HEALTH_BAR_Y_POS - 100);
		batch.end();
	}
	
	private void drawConsole(){
		int y = HEALTH_BAR_Y_POS - 130;
		float lineHeight = fonts.get("console").getLineHeight();
		int consoleWidth = Gdx.graphics.getWidth() - gameScreenSize;
		ArrayList<Message> messages = Console.getLattestMessages();
		
		batch.begin();
		for (int i = 0; i < messages.size(); i++){
			Message m = messages.get(i);
			fonts.get("console").draw(batch, m.getText(), SIDE_BAR_X_POS, y - (i * lineHeight), consoleWidth, Align.left, true);
		}
		batch.end();
	}
	
	private void drawMarkedTileInfo(){
		Tile tile = getClickedTile();
		
		if(tile != null && tile.getVisibility() != Visibility.NOT_VIEWED && !tile.isEmpty()){
			String text = "";
			if(tile.get(Type.ACTOR) != null){
				text = Mappers.descMap.get(tile.get(Type.ACTOR)).name;
			}else if(tile.get(Type.FEATURE) != null){
				text = Mappers.descMap.get(tile.get(Type.FEATURE)).name;
			}else if(tile.get(Type.ITEM) != null){
				text = Mappers.descMap.get(tile.get(Type.ITEM)).name;
			}
			
			batch.begin();
			fonts.get("console").draw(batch, text, markerX - TILE_SIZE, markerY + TILE_SIZE*2);
			batch.end();
		}
	}
	
	public boolean showMarker = false;
	private int markerX, markerY = gameScreenSize;
	
	public void refreshMarker(int xMouse, int yMouse){
		if(xMouse <= gameScreenSize){
			showMarker = true;
			markerX = (int)(xMouse / TILE_SIZE) * TILE_SIZE;
			markerY = (int)(yMouse / TILE_SIZE) * TILE_SIZE;
		}
	}
	
	private void drawMarker(){
		if(showMarker) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(markerX, markerY, TILE_SIZE, TILE_SIZE);
			shapeRenderer.end();
		}
	}
	
	public void moveMarker(int x, int y){
		markerX += x * TILE_SIZE;
		markerY += y * TILE_SIZE;
	}
	
	public void resetMarker(){
		markerX = gameScreenSize / 2;
		markerY = gameScreenSize / 2;
	}

	public Tile getClickedTile(){
		int X0 = Mappers.posMap.get(Juego.player).coord[0];
		int Y0 = Mappers.posMap.get(Juego.player).coord[1];
		int Z0 = Mappers.posMap.get(Juego.player).coord[2];
		int clickX = X0 + (markerX / TILE_SIZE) - gameScreenSize / (2 * TILE_SIZE);
		int clickY = Y0 + (markerY / TILE_SIZE) - gameScreenSize / (2 * TILE_SIZE);
		try {return Map.getTile(clickX, clickY, Z0);}
		catch(ArrayIndexOutOfBoundsException e) {return null;}
	}
	
	public void resize(int width, int height) {
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void hide() {
		
	}

	public void dispose() {
		shapeRenderer.dispose();
		batch.dispose();
	}
	
	
//	private InputProcessor createAimInputProcessor() {
//		return new InputProcessor(){
//			public boolean keyDown(int keycode) {
//				if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
//				switch(keycode){
//					case Keys.ESCAPE:
//						Juego.ENGINE.getSystem(RenderSystem.class).setScreen(GameScreenASCII.getInstance());
//						break;
//					case Keys.ENTER:
//						PositionComponent pos = getClickedTile().getPos();
////						Skill skill = SkillsMenu.getInstance().getSelectedSkill();
////						System.out.println(skill.getName());
////						skill.affect(pos);
////						setGameInput();
//						break;
//				}
//				return false;
//			}
//
//			public boolean keyUp(int keycode) {
//				return false;
//			}
//
//			public boolean keyTyped(char character) {
//				if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
//				
//				switch(character){
//					case '1':
//						moveMarker(-1,  -1);
//						break;
//					case '2':
//						moveMarker(0, -1);
//						break;
//					case '3':
//						moveMarker(1, -1);
//						break;
//					case '4':
//						moveMarker(-1, 0);
//						break;
//					case '6':
//						moveMarker(1, 0);
//						break;
//					case '7':
//						moveMarker(-1, 1);
//						break;
//					case '8':
//						moveMarker(0, 1);
//						break;
//					case '9':
//						moveMarker(1, 1);
//						break;
//				}
//				return false;
//			}
//
//			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//				if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
//				Tile tile = getClickedTile();
//				switch(button){
//					case 0:
////						Skill skill = SkillsScreen.getInstance().getSelectedSkill();
////						skill.cast(Juego.player, tile);
//						break;
//				}
//				return false;
//			}
//
//			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//				return false;
//			}
//
//			public boolean touchDragged(int screenX, int screenY, int pointer) {
//				return false;
//			}
//
//			public boolean mouseMoved(int screenX, int screenY) {
//				if(!Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput) return false;
//				refreshMarker(screenX, Gdx.graphics.getHeight() - screenY);
//				return false;
//			}
//
//			public boolean scrolled(int amount) {
//				return false;
//			}
//		};
//	}

}
