package screens;
//TODO actualizar para que funcione con ashley


//package Screens;
//
//import java.util.LinkedList;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//import Skills.Active.ActiveSkill;
//
//import static Components.Mappers.skillMap;
//
//public class SkillsScreen implements Screen{
//	
//	private final InputProcessor INPUT = createInputProcessor();
//	
//	private SpriteBatch batch;
//	private static SkillsScreen instance = new SkillsScreen();
//	private OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//	
//	private int selectedSkill = 0;
//
//	@Override
//	public void show() {
//		batch = new SpriteBatch();
//		Gdx.input.setInputProcessor(INPUT);
//	}
//
//
//	@Override
//	public void render(float delta) {
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		
//		BitmapFont font = new BitmapFont();
//		int x = - Gdx.graphics.getWidth() / 2 + 10;
//		int y = Gdx.graphics.getHeight() / 2 - 10;
////		LinkedList<ActiveSkill> skills = Player.getInstance().getSkills();
////		for(Skill s : skills){
////			String text = s.getName();
////			font.setColor(skills.indexOf(s) == selectedSkill ? Color.CHARTREUSE : Color.WHITE);
////			font.draw(batch, text, x, y);
////			y -= 15;
////		}
//		
//		batch.end();
//	}
//	
//	public void changeSelectedSkill(int v){
////		if(selectedSkill + v < 0 || selectedSkill + v > Player.getInstance().getSkills().size() - 1){
////			return;
////		}
////		else{
////			selectedSkill += v;
////		}
//	}
//	
////	public ActiveSkill getSelectedSkill(){
////		return Player.getInstance().getSkills().get(selectedSkill);
////	}
//	
//	@Override
//	public void resize(int width, int height) {
//
//	}
//
//	@Override
//	public void pause() {
//
//	}
//
//	@Override
//	public void resume() {
//
//	}
//
//	@Override
//	public void hide() {
//		
//	}
//
//	@Override
//	public void dispose() {
//		batch.dispose();
//	}
//	
//	public static SkillsScreen getInstance() {
//		return instance;
//	}
//	
//	private InputProcessor createInputProcessor() {
//		return new InputProcessor(){
//			@Override
//			public boolean keyDown(int keycode) {
//				switch(keycode){
//					case Keys.ESCAPE:
//						RenderSystem.setScreen(GameScreenASCII.getInstance());
//						GameScreenASCII.getInstance().setGameInput();
//						break;
//					case Keys.DOWN:
//						SkillsScreen.getInstance().changeSelectedSkill(1);
//						break;
//					case Keys.UP:
//						SkillsScreen.getInstance().changeSelectedSkill(-1);
//						break;
//					case Keys.ENTER:
//						RenderSystem.setScreen(GameScreenASCII.getInstance());
////						if(getSelectedSkill().isAimed()){
////							GameScreenASCII.getInstance().setAimInput();
////							GameScreenASCII.getInstance().showMarker = true;
////						}
////						else{
////							getSelectedSkill().cast(Player.getInstance().getPosition());
////							ScreenManager.setScreen(GameScreenASCII.getInstance());
////							GameScreenASCII.getInstance().setGameInput();
////						}
//						break;
//				}
//				return false;
//			}
//
//			@Override
//			public boolean keyUp(int keycode) {
//				return false;
//			}
//
//			@Override
//			public boolean keyTyped(char character) {
//				return false;
//			}
//
//			@Override
//			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//				return false;
//			}
//
//			@Override
//			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//				return false;
//			}
//
//			@Override
//			public boolean touchDragged(int screenX, int screenY, int pointer) {
//				return false;
//			}
//
//			@Override
//			public boolean mouseMoved(int screenX, int screenY) {
//				return false;
//			}
//
//			@Override
//			public boolean scrolled(int amount) {
//				return false;
//			}
//			
//		};
//	}
//}
