package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainScreen implements Screen{
	
	private static MainScreen instance = new MainScreen();
	
	private final InputProcessor MAIN_SCREEN_INPUT = createMainScreenInputProcessor();
	
	private SpriteBatch batch;
	private CustomShapeRenderer shapeRenderer;
	
	private String title =  "\n" + 
			" /$$                       /$$       /$$             /$$                         /$$                       /$$                 /$$   /$$           /$$          \n" + 
			"| $$                      | $$      |__/            | $$                        |__/                      | $$                | $$  /$$/          | $$          \n" + 
			"| $$        /$$$$$$       | $$$$$$$  /$$  /$$$$$$$ /$$$$$$    /$$$$$$   /$$$$$$  /$$  /$$$$$$         /$$$$$$$  /$$$$$$       | $$ /$$/   /$$$$$$ | $$ /$$   /$$\n" + 
			"| $$       |____  $$      | $$__  $$| $$ /$$_____/|_  $$_/   /$$__  $$ /$$__  $$| $$ |____  $$       /$$__  $$ /$$__  $$      | $$$$$/   /$$__  $$| $$| $$  | $$\n" + 
			"| $$        /$$$$$$$      | $$  \\ $$| $$|  $$$$$$   | $$    | $$  \\ $$| $$  \\__/| $$  /$$$$$$$      | $$  | $$| $$$$$$$$      | $$  $$  | $$$$$$$$| $$| $$  | $$\n" + 
			"| $$       /$$__  $$      | $$  | $$| $$ \\____  $$  | $$ /$$| $$  | $$| $$      | $$ /$$__  $$      | $$  | $$| $$_____/      | $$\\  $$ | $$_____/| $$| $$  | $$\n" + 
			"| $$$$$$$$|  $$$$$$$      | $$  | $$| $$ /$$$$$$$/  |  $$$$/|  $$$$$$/| $$      | $$|  $$$$$$$      |  $$$$$$$|  $$$$$$$      | $$ \\  $$|  $$$$$$$| $$|  $$$$$$$\n" + 
			"|________/ \\_______/      |__/  |__/|__/|_______/    \\___/   \\______/ |__/      |__/ \\_______/       \\_______/ \\_______/      |__/  \\__/ \\_______/|__/ \\____  $$\n" + 
			"                                                                                                                                                       /$$  | $$\n" + 
			"                                                                                                                                                      |  $$$$$$/\n" + 
			"                                                                                                                                                       \\______/ \n" + 
			"";
	
	private int selectedOption = 0;
	
	private MainScreen() {}

	public static MainScreen getInstance() {
		return instance;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		shapeRenderer = new CustomShapeRenderer();
		
		Gdx.input.setInputProcessor(MAIN_SCREEN_INPUT);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("../core/assets/fonts/CONSOLA.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		BitmapFont font = generator.generateFont(parameter);
		
		font.draw(batch, "New game", 100, 400);
		font.draw(batch, "Load", 100, 370);
		font.draw(batch, "Options", 100, 340);
		font.draw(batch, "Exit", 100, 310);
		font.setColor(Color.RED);
		font.draw(batch, title, 0, 800);
		
		
		batch.end();
		dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private InputProcessor createMainScreenInputProcessor() {
		return new InputProcessor() {

			@Override
			public boolean keyDown(int keycode) {
				switch(keycode){
				case Keys.ESCAPE:
					System.exit(0);
					break;
				default:
					System.out.println("default");
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
	}


}
