package com.mygdx.juego;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;

import FOV.VisionCalculator;
import components.AIComponent;
import components.AttributeComponent;
import components.EquipmentComponent;
import components.Faction;
import components.GraphicsComponent;
import components.HealthComponent;
import components.InventoryComponent;
import components.MovementComponent;
import components.DescriptionComponent;
import components.PlayerComponent;
import components.PositionComponent;
import components.StatusEffectsComponent;
import components.TimedComponent;
import components.Type;
import components.VisionComponent;
import eventSystem.ActiveMap;
import eventSystem.EventSystem;
import screens.GameScreenASCII;
import screens.MainScreen;
import states.AttackState;
import states.PlayerExploreState;
import states.PlayerWanderState;
import tools.RenderSystem;
import world.World;

public class Juego extends Game {
	
	public static final PooledEngine ENGINE = new PooledEngine();
	public static final Entity PLAYER = ENGINE.createEntity();
	public static World world = new World();
	
    public void create() {
    	ENGINE.addSystem(new RenderSystem());
    	ENGINE.getSystem(RenderSystem.class).setScreen(MainScreen.getInstance());
    }
    
    public static void startGame() {
    	world.initialize();
    	
    	ENGINE.addSystem(new EventSystem());
    	spawnPlayer();
    	
    	ENGINE.getSystem(RenderSystem.class).setScreen(GameScreenASCII.getInstance());
	}

    
    private static void spawnPlayer(){
    	PLAYER.add(ENGINE.createComponent(PlayerComponent.class));
    	
    	PLAYER.add(Type.ACTOR);
    	PLAYER.add(Faction.HUMANS);
    	
		PositionComponent playerPos = new PositionComponent(5, 5, 0);
		playerPos.getTile().put(PLAYER);
		PLAYER.add(playerPos);
		
		DescriptionComponent nc = ENGINE.createComponent(DescriptionComponent.class);
		nc.name = "player";
		PLAYER.add(nc);
		
		GraphicsComponent gc = ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "@";
		gc.font = "general";
		gc.frontColor = Color.WHITE;
		gc.renderPriority = 3;
		PLAYER.add(gc);
		
		AttributeComponent ac = ENGINE.createComponent(AttributeComponent.class);
		ac.set("damage", 15f);
		ac.set("move speed", 25f);
		ac.set("attack speed", 25f);
		PLAYER.add(ac);
		
		HealthComponent hc = ENGINE.createComponent(HealthComponent.class);
		hc.maxHP = 1000;
		hc.curHP = 1000;
		hc.HPreg = 100;
		PLAYER.add(hc);
		
		PLAYER.add(ENGINE.createComponent(VisionComponent.class));
		PLAYER.add(ENGINE.createComponent(TimedComponent.class));
		PLAYER.add(ENGINE.createComponent(StatusEffectsComponent.class));
		PLAYER.add(ENGINE.createComponent(InventoryComponent.class));
		PLAYER.add(ENGINE.createComponent(EquipmentComponent.class));
		
		AIComponent ai = ENGINE.createComponent(AIComponent.class);
		ai.fsm.setOwner(PLAYER);
		ai.states.put("wandering", new PlayerWanderState());
		ai.states.put("exploring", new PlayerExploreState());
		ai.states.put("attacking", new AttackState());
		ai.fsm.setGlobalState(ai.states.get("idling"));
		PLAYER.add(ai);
		
		PLAYER.add(ENGINE.createComponent(MovementComponent.class));
		
		VisionCalculator.calculateVision(PLAYER);
		ActiveMap.refresh();
		
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