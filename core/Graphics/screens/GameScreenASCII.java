package screens;

import static screens.FontLoader.fonts;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.mygdx.juego.Juego;

import actions.ActionType;
import actions.Actions;
import activeMap.ActiveMap;
import components.GraphicsComponent;
import components.HealthComponent;
import components.Mappers;
import components.PositionComponent;
import components.Type;
import console.Console;
import console.Message;
import console.MessageFactory;
import cosas.Tile;
import cosas.Tile.Visibility;
import fatories.ItemFactory;
import pathFind.PathFinder;
import world.Direction;
import world.Explorer;
import world.World;

public class GameScreenASCII implements Screen{
	
	private final InputProcessor GAME_INPUT = createGameInputProcessor();
	private final InputProcessor AIM_INPUT = createAimInputProcessor();
	private final InputProcessor USE_FEATURE_INPUT = createUseFeatureInputProcessor();
	
	private SpriteBatch batch;
	private CustomShapeRenderer shapeRenderer;
	
	private static GameScreenASCII instance = new GameScreenASCII();
	protected final static int TILE_SIZE = 18; // tamaño anterior 18
	private int gameScreenSize = Gdx.graphics.getHeight();
	private int gameScreenTiles = Gdx.graphics.getHeight()/TILE_SIZE;
	
	public Menu menu = null;
	
	/**
	 * Para poner caracteres unicode en el XML hay que poner
	 * 
	 * &#x0d74; en el XML si se quiere agregar el caracter \u0d74 (൴)
	 */
	
	private GameScreenASCII(){
		FontLoader.load(TILE_SIZE);
		setGameInput();
	}
	
	public static GameScreenASCII getInstance(){
		return instance;
	}
	
	public void show() {
		batch = new SpriteBatch();
		shapeRenderer = new CustomShapeRenderer();
	}

	public void render(float delta) {
//		long tiempo = System.currentTimeMillis();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		drawMap();
		drawConsole();
		drawMiniMap();
		drawStats();
		drawFrame();
		if(menu == null) {
			drawMarker();
			drawMarkedTileInfo();
		}else {
			menu.render(batch, shapeRenderer);
		}
		
		dispose();
		
//		System.out.println("Tiempo de render:   " + (System.currentTimeMillis() - tiempo) + "ms");
	}
	
	private void drawMap(){
		Tile[][] map = ActiveMap.getMap();
		int center = map.length/2;
		
		//Dibujar fondo
		shapeRenderer.begin(ShapeType.Filled);
		for (int x = -gameScreenTiles/2; x <= gameScreenTiles/2; x++){
			for(int y = -gameScreenTiles/2; y <= gameScreenTiles/2; y++){
				if(map[center+x][center+y] == null || map[center+x][center+y].getVisibility() == Visibility.NOT_VISIBLE) {
					continue;
				}
				Tile tile = map[center+x][center+y];
				int xImg = (x * TILE_SIZE) + (gameScreenSize / 2) - 8;
				int yImg = (y * TILE_SIZE) + (gameScreenSize / 2) - 8;
				
				GraphicsComponent gc = tile.getBackGC();
				if(gc == null) continue;
				Color color = new Color(gc.backColor);
				if(tile.getVisibility() == Visibility.VIEWED){
					shapeRenderer.setColor(color.lerp(Color.BLACK, 0.5f));
				}else {
					shapeRenderer.setColor(color);
				}
				shapeRenderer.rect(xImg, yImg, TILE_SIZE, TILE_SIZE);
			}
		}
		shapeRenderer.end();
		
		// Dibujar ASCII
		batch.begin();
		GlyphLayout layout = new GlyphLayout();
		for (int x = -gameScreenTiles/2; x <= gameScreenTiles/2; x++){
			for(int y = -gameScreenTiles/2; y <= gameScreenTiles/2; y++){
				if(map[center+x][center+y] == null || map[center+x][center+y].getVisibility() == Visibility.NOT_VISIBLE) {
					continue;
				}
				Tile tile = map[center+x][center+y];
				int xImg = (x * TILE_SIZE) + (gameScreenSize / 2) - 8;
				int yImg = (y * TILE_SIZE) + (gameScreenSize / 2) - 8;
				
				GraphicsComponent gc;
				Color color;
				if(tile.getVisibility() == Visibility.VIEWED){
					gc = Mappers.graphMap.get(tile.get(Type.TERRAIN));
					color = new Color(gc.frontColor);
					color.lerp(Color.BLACK, 0.5f);
				}else {
					gc = tile.getFrontGC();
					color = new Color(gc.frontColor);
				}
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
	private final float PIXEL_SIZE = (float)MINI_MAP_SIZE / (float) World.CHUNK_SIZE;
	
	private void drawMiniMap(){
		Tile[][] map = ActiveMap.getMap();
		
		shapeRenderer.begin(ShapeType.Filled);
		
		for (int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				if(map[x][y] == null || map[x][y].getVisibility() == Visibility.NOT_VISIBLE)
					shapeRenderer.setColor(new Color(.2f,.2f,.2f,0));
				else{
					Tile tile = map[x][y];
					Color color = tile.getBackGC().backColor;
					if(color != null) {
						shapeRenderer.setColor(color);
					}
				}
				shapeRenderer.rect(SIDE_BAR_X_POS + x*PIXEL_SIZE, MINI_MAP_Y_POS + y*PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
			}
		}
		shapeRenderer.end();
	}
	
	
	
	private void drawFrame(){
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
		HealthComponent playerHP = Mappers.healthMap.get(Juego.PLAYER);
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
		
		if(tile.getVisibility() != Visibility.NOT_VISIBLE && !tile.isEmpty()){
			String text = "";
			if(tile.get(Type.ACTOR) != null){
				text = Mappers.nameMap.get(tile.get(Type.ACTOR)).name;
			}else if(tile.get(Type.FEATURE) != null){
				text = Mappers.nameMap.get(tile.get(Type.FEATURE)).name;
			}else if(tile.get(Type.ITEM) != null){
				text = Mappers.nameMap.get(tile.get(Type.ITEM)).name;
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
		int X0 = ActiveMap.getMap().length / 2;
		int Y0 = ActiveMap.getMap().length / 2;
		int clickX = X0 + (markerX / TILE_SIZE) - gameScreenSize / (2 * TILE_SIZE);
		int clickY = Y0 + (markerY / TILE_SIZE) - gameScreenSize / (2 * TILE_SIZE);
		return ActiveMap.getMap()[clickX][clickY];
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
	
	
//===============INPUT HANDLER===============
	
	public void setGameInput(){
		Gdx.input.setInputProcessor(GAME_INPUT);
	}
	
	public void setAimInput(){
		Gdx.input.setInputProcessor(AIM_INPUT);
	}
	
	public void setUseFeatureInput() {
		Gdx.input.setInputProcessor(USE_FEATURE_INPUT);
		
	}
	
	private InputProcessor createGameInputProcessor() {
		return new InputProcessor(){
			public boolean keyDown(int keycode) {
				switch(keycode){
					case Keys.ESCAPE:
						System.exit(0);
						break;
					case Keys.SPACE:
						Tile tile = Mappers.posMap.get(Juego.PLAYER).getTile();
						tile.put(ItemFactory.createPotion());
						break;
					case Keys.CONTROL_LEFT:
						Tile tile2 = Mappers.posMap.get(Juego.PLAYER).getTile();
						tile2.put(ItemFactory.createRandomItem());
						break;
					case Keys.COMMA:
						Actions.pickUp(Explorer.getTile(Juego.PLAYER.getComponent(PositionComponent.class)));
						break;
					case Keys.E:
						MessageFactory.createMessage("Use what?");
						setUseFeatureInput();
						break;
					case Keys.I:
						menu = new InventoryMenu();
						break;
					case Keys.W:
						menu = new EquipMenu();
						break;
					case Keys.Q:
						menu = new QuaffMenu();
						break;
					case Keys.S:
//						RenderSystem.setScreen(SkillsScreen.getInstance());
						break;
					case Keys.X:
						Actions.explore(Juego.PLAYER);
						break;
				}
				return false;
			}

			public boolean keyUp(int keycode) {
				return false;
			}

			public boolean keyTyped(char character) {
				PositionComponent pos = Juego.PLAYER.getComponent(PositionComponent.class);
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
						Actions.endTurn(Juego.PLAYER, ActionType.WAIT);
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
				PositionComponent playerPos = Juego.PLAYER.getComponent(PositionComponent.class);
				Tile tile = getClickedTile();
				
				switch(button){
					case 0: //click izquierdo
						Mappers.movMap.get(Juego.PLAYER).path = PathFinder.findPath(playerPos, tile.getPos(), Juego.PLAYER);
						Actions.followPath(Juego.PLAYER);
						break;
					case 1: //click derecho
						System.out.println(tile);
	//					lista = World.getStraigthLine(playerPos, clickedPos);
	//					for(Tile tile : lista){
	//						Terrain wall = TerrainFactory.createTerrain("concrete wall");
	//						tile.setTerrain(wall);
	//					}
						break;
					case 2: //ruedita
	//					lista = World.getStraigthLine(clickedPos, playerPos);
	//					for(Tile tile : lista){
	//						Entity wall = TerrainPool.get("concrete wall");
	//						tile.setTerrain(wall);
	//					}
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
				GameScreenASCII.getInstance().refreshMarker(screenX, Gdx.graphics.getHeight() - screenY);
				return false;
			}

			public boolean scrolled(int amount) {
				return false;
			}
			
		};
	}
	
	//TODO: Cambiar para que interactue con todo (contenedores, features, items en el piso, etc) de la misma forma que Actions.bump()
	private InputProcessor createUseFeatureInputProcessor() {
		return new InputProcessor(){
			public boolean keyDown(int keycode) {
				switch(keycode){
					case Keys.ESCAPE:
						setGameInput();
						break;
				}
				return false;
			}

			public boolean keyUp(int keycode) {
				return false;
			}

			public boolean keyTyped(char character) {
				
				PositionComponent playerPos = Juego.PLAYER.getComponent(PositionComponent.class);
				PositionComponent featurePos = null;
				
				switch(character){
					case '1':
						featurePos = Explorer.getPosition(playerPos, Direction.SW);
						break;
					case '2':
						featurePos = Explorer.getPosition(playerPos, Direction.S);
						break;
					case '3':
						featurePos = Explorer.getPosition(playerPos, Direction.SE);
						break;
					case '4':
						featurePos = Explorer.getPosition(playerPos, Direction.W);
						break;
					case '5':
						featurePos = playerPos;
						break;
					case '6':
						featurePos = Explorer.getPosition(playerPos, Direction.E);
						break;
					case '7':
						featurePos = Explorer.getPosition(playerPos, Direction.NW);
						break;
					case '8':
						featurePos = Explorer.getPosition(playerPos, Direction.N);
						break;
					case '9':
						featurePos = Explorer.getPosition(playerPos, Direction.NE);
						break;
					default:
						break;
				}
				if(featurePos != null) {
					Actions.useFeature(Juego.PLAYER, featurePos);
				}
				setGameInput();
				return false;
			}

			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			public boolean scrolled(int amount) {
				return false;
			}
		};
	}
	
	private InputProcessor createAimInputProcessor() {
		return new InputProcessor(){
			public boolean keyDown(int keycode) {
				switch(keycode){
					case Keys.ESCAPE:
						RenderSystem.setScreen(GameScreenASCII.getInstance());
						break;
					case Keys.ENTER:
//						PositionComponent pos = getClickedPos();
//						ActiveSkill skill = SkillsScreen.getInstance().getSelectedSkill();
//						System.out.println(skill.getName());
//						skill.affect(pos);
//						setGameInput();
						break;
				}
				return false;
			}

			public boolean keyUp(int keycode) {
				return false;
			}

			public boolean keyTyped(char character) {
				
				switch(character){
					case '1':
//						GameScreen.getInstance().moveMarker(-1,  -1);
						break;
					case '2':
//						GameScreen.getInstance().moveMarker(0, -1);
						break;
					case '3':
//						GameScreen.getInstance().moveMarker(1, -1);
						break;
					case '4':
//						GameScreen.getInstance().moveMarker(-1, 0);
						break;
					case '6':
//						GameScreen.getInstance().moveMarker(1, 0);
						break;
					case '7':
//						GameScreen.getInstance().moveMarker(-1, 1);
						break;
					case '8':
//						GameScreen.getInstance().moveMarker(0, 1);
						break;
					case '9':
//						GameScreen.getInstance().moveMarker(1, 1);
						break;
				}
				return false;
			}

			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//				Position pos = GameScreen.getInstance().getClickedPos();
				switch(button){
					case 0:
//						ActiveSkill skill = SkillsScreen.getInstance().getSelectedSkill();
//						skill.affect(pos);	
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
				refreshMarker(screenX, Gdx.graphics.getHeight() - screenY);
				return false;
			}

			public boolean scrolled(int amount) {
				return false;
			}
		};
	}

}
