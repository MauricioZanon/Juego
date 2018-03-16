package com.mygdx.juego;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;

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
import components.StatusEffectsComponent;
import components.TimedComponent;
import components.Type;
import components.VisionComponent;
import states.player.PlayerAttackState;
import states.player.PlayerExploreState;
import states.player.PlayerWanderState;

public abstract class PlayerBuilder {
	
	public static Entity createBasePlayer() {
		PooledEngine engine = Juego.ENGINE;
		Entity player = engine.createEntity();
		player.flags = -1;
		
		DescriptionComponent nc = engine.createComponent(DescriptionComponent.class);
		nc.name = "player";
		player.add(nc);
		
		GraphicsComponent gc = engine.createComponent(GraphicsComponent.class);
		gc.ASCII = "@";
		gc.font = "general";
		gc.frontColor = Color.WHITE;
		gc.renderPriority = 3;
		player.add(gc);
		
		AttributeComponent ac = engine.createComponent(AttributeComponent.class);
		ac.set("damage", 15f);
		ac.set("move speed", 25f);
		ac.set("attack speed", 25f);
		player.add(ac);
		
		HealthComponent hc = engine.createComponent(HealthComponent.class);
		hc.maxHP = 1000;
		hc.curHP = 1000;
		hc.HPreg = 100;
		player.add(hc);
		
		AIComponent ai = engine.createComponent(AIComponent.class);
		ai.fsm.setOwner(player);
		ai.states.put("wandering", new PlayerWanderState());
		ai.states.put("exploring", new PlayerExploreState());
		ai.states.put("attacking", new PlayerAttackState());
		ai.fsm.setGlobalState(ai.states.get("wandering"));
		ai.fsm.setInitialState(ai.states.get("wandering"));
		player.add(ai);
		
		player.add(engine.createComponent(InventoryComponent.class));
		player.add(engine.createComponent(EquipmentComponent.class));
		player.add(engine.createComponent(VisionComponent.class));
		player.add(engine.createComponent(TimedComponent.class));
		player.add(engine.createComponent(StatusEffectsComponent.class));
		player.add(engine.createComponent(MovementComponent.class));
		player.add(engine.createComponent(PlayerComponent.class));
		
		player.add(Type.ACTOR);
    	player.add(Faction.HUMANS);
		
		return player;
	}

}
