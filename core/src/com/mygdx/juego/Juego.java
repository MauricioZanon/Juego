package com.mygdx.juego;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import FOV.VisionCalculator;
import components.Mappers;
import components.PositionComponent;
import eventSystem.EventSystem;
import eventSystem.Map;
import factories.FeatureFactory;
import factories.ItemFactory;
import factories.NPCFactory;
import factories.TerrainFactory;
import inputProcessors.GameInput;
import screens.GameScreenASCII;
import screens.MainScreen;
import tools.RenderSystem;
import world.WorldBuilder;

public class Juego extends Game {
	
	public static final PooledEngine ENGINE = new PooledEngine();
	public static Entity player = null;
	
    public void create() {
    	initializeFactories();
    	RenderSystem render = new RenderSystem();
    	render.setScreen(MainScreen.getInstance());
    	ENGINE.addSystem(render);
    }
    
    public static void newGame() {
    	StateSaver.createInitialSave();
    	
		new WorldBuilder().initialize();
		
    	player = PlayerBuilder.createBasePlayer();
		PositionComponent playerPos = ENGINE.createComponent(PositionComponent.class);
		playerPos.coord = new int[] {0, 0, 0};
		playerPos.getTile().put(player);
		
		Juego.startGame();
    }
    
    public static void loadGame() {
    	StateLoader.loadPlayer();
    	Mappers.posMap.get(player).getTile().put(player);
    	startGame();
    }
    
    public static void startGame() {
    	ENGINE.addSystem(new EventSystem());
    	
    	Map.refresh();
    	VisionCalculator.calculateVision(player);
    	
    	ENGINE.getSystem(RenderSystem.class).setScreen(GameScreenASCII.getInstance());
    	Gdx.input.setInputProcessor(new GameInput());
    	
    	StateSaver.savingThread.start();
	}
    
    public static void initializeFactories() {
    	TerrainFactory.initialize();
    	FeatureFactory.initialize();
    	ItemFactory.initialize();
    	NPCFactory.initialize();
    }

	@Override
    public void dispose() {
    }

    @Override
    public void render() {
    	long tiempo = System.currentTimeMillis();
    	ENGINE.update((1000/60) - (System.currentTimeMillis() - tiempo));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}