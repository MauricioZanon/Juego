package com.mygdx.juego;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import RNG.RNG;
import components.AIComponent;
import components.AttributeComponent;
import components.DescriptionComponent;
import components.EquipmentComponent;
import components.Faction;
import components.GraphicsComponent;
import components.HealthComponent;
import components.InventoryComponent;
import components.MovementComponent;
import components.PlayerComponent;
import components.PositionComponent;
import components.StatusEffectsComponent;
import components.TimedComponent;
import components.Type;
import components.VisionComponent;
import eventSystem.ActiveMap;
import eventSystem.EventSystem;
import factories.ItemFactory;
import inputProcessors.GameInput;
import screens.GameScreenASCII;
import screens.MainScreen;
import states.player.PlayerAttackState;
import states.player.PlayerExploreState;
import states.player.PlayerWanderState;
import tools.RenderSystem;
import world.World;

public class Juego extends Game {
	
	public static final PooledEngine ENGINE = new PooledEngine();
	public volatile static Entity player = ENGINE.createEntity();
	public static World world = new World();
	
	public static Thread gameThread;
	public static Engine GRAPHICS_ENGINE = new Engine();
	
    public void create() {
    	RenderSystem render = new RenderSystem();
    	render.setScreen(MainScreen.getInstance());
    	GRAPHICS_ENGINE.addSystem(render);
    	
    	gameThread = new Thread(() -> {
    		while(true) {
    			long tiempo = System.currentTimeMillis();
    			ENGINE.update(0);
    			try {
					Thread.sleep((1000/30) - (System.currentTimeMillis() - tiempo));
				} catch (InterruptedException | IllegalArgumentException e) {}
    		}
    	});
    	gameThread.setName("game thread");
    }
    
    public static void startGame() {
    	System.out.println(RNG.getSeed());
    	world.initialize();
    	
    	ENGINE.addSystem(new EventSystem());
    	spawnPlayer();
    	
    	GRAPHICS_ENGINE.getSystem(RenderSystem.class).setScreen(GameScreenASCII.getInstance());
    	Gdx.input.setInputProcessor(new GameInput());
    	gameThread.start();
	}

    
    private static void spawnPlayer(){
    	player.add(ENGINE.createComponent(PlayerComponent.class));
    	
    	player.add(Type.ACTOR);
    	player.add(Faction.HUMANS);
    	
		PositionComponent playerPos = new PositionComponent(200, 200, 0);
		playerPos.getTile().put(player);
		player.add(playerPos);
		
		DescriptionComponent nc = ENGINE.createComponent(DescriptionComponent.class);
		nc.name = "player";
		player.add(nc);
		
		GraphicsComponent gc = ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "@";
		gc.font = "general";
		gc.frontColor = Color.WHITE;
		gc.renderPriority = 3;
		player.add(gc);
		
		AttributeComponent ac = ENGINE.createComponent(AttributeComponent.class);
		ac.set("damage", 15f);
		ac.set("move speed", 25f);
		ac.set("attack speed", 25f);
		player.add(ac);
		
		HealthComponent hc = ENGINE.createComponent(HealthComponent.class);
		hc.maxHP = 1000;
		hc.curHP = 1000;
		hc.HPreg = 100;
		player.add(hc);
		
		player.add(ENGINE.createComponent(VisionComponent.class));
		player.add(ENGINE.createComponent(TimedComponent.class));
		player.add(ENGINE.createComponent(StatusEffectsComponent.class));
		
		InventoryComponent ic = ENGINE.createComponent(InventoryComponent.class);
		ic.add(ItemFactory.createArmor());
		ic.add(ItemFactory.createArmor());
		ic.add(ItemFactory.createArmor());
		ic.add(ItemFactory.createArmor());
		ic.add(ItemFactory.createArmor());
		player.add(ic);
		
		player.add(ENGINE.createComponent(EquipmentComponent.class));
		
		AIComponent ai = ENGINE.createComponent(AIComponent.class);
		ai.fsm.setOwner(player);
		ai.states.put("wandering", new PlayerWanderState());
		ai.states.put("exploring", new PlayerExploreState());
		ai.states.put("attacking", new PlayerAttackState());
		ai.fsm.setGlobalState(ai.states.get("wandering"));
		ai.fsm.setInitialState(ai.states.get("wandering"));
		player.add(ai);
		
		player.add(ENGINE.createComponent(MovementComponent.class));
		
		ActiveMap.refresh();
		
    }

	@Override
    public void dispose() {
		
    }

    @Override
    public void render() {
    	long tiempo = System.currentTimeMillis();
    	GRAPHICS_ENGINE.update((1000/60) - (System.currentTimeMillis() - tiempo));
    	try {
			Thread.sleep((1000/60) - (System.currentTimeMillis() - tiempo));
		} catch (InterruptedException | IllegalArgumentException e) {}
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