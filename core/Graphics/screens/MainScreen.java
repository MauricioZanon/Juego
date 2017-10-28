package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.juego.Juego;

import tools.FontLoader;

public class MainScreen implements Screen{
	
	private static MainScreen instance = new MainScreen();
	
	private final InputProcessor MAIN_SCREEN_INPUT = createMainScreenInputProcessor();
	
	private SpriteBatch batch;
	
	private MainScreen() {}

	public static MainScreen getInstance() {
		return instance;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(MAIN_SCREEN_INPUT);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		BitmapFont font = FontLoader.fonts.get("menu");
		
		font.draw(batch, "[N]ew game", 100, 400);
		font.draw(batch, "[L]oad", 100, 370);
		font.draw(batch, "[O]ptions", 100, 340);
		font.draw(batch, "[E]xit", 100, 310);
		
		
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
				case Keys.N:
					Juego.startGame();
					break;
				case Keys.L:
//					loadGame();
					break;
				case Keys.O:
//					showOptions();
					break;
				case Keys.ESCAPE:
				case Keys.E:
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
