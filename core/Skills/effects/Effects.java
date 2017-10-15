package effects;
import static components.Mappers.playerMap;
import static components.Mappers.posMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import FOV.VisionCalculator;
import RNG.RNG;
import actions.Actions;
import activeMap.ActiveMap;
import components.HealthComponent;
import components.Mappers;
import components.NameComponent;
import components.PositionComponent;
import components.StatusEffectsComponent;
import components.Type;
import console.MessageFactory;
import main.Tile;

public abstract class Effects {
	
	public static void heal(Entity actor, int amount){
		HealthComponent HC = Mappers.healthMap.get(actor);
		HC.curHP = MathUtils.clamp(HC.curHP + amount, 0, HC.maxHP);
		
		String[] extraText = {Mappers.nameMap.get(actor).name};
		String messageType = playerMap.has(actor) ? "PlayerHeals" : "NpcHeals";
		MessageFactory.loadMessage(messageType, extraText);
	}
	
	//TODO Debería buscar un tile en las cercanías del actor, no del active map
	public static void randomTeleport(Entity actor){
		PositionComponent newPos = RNG.getRandom(ActiveMap.getMap(), t -> t.isTransitable() && t.get(Type.ACTOR) == null).getPos();
		teleport(actor, newPos);
	}
	
	public static void teleport(Entity actor, PositionComponent newPos){
		move(actor, newPos);
		if(playerMap.has(actor)){
			MessageFactory.createMessage("You suddenly find yourself in another place");
		}
	}
	
	/**
	 * @param entity la entidad que se va a mover
	 * @param newPos la posición a la que se va a mover
	 */
	public static void move(Entity entity, PositionComponent newPos) {
		Tile oldTile = posMap.get(entity).getTile();
		Tile newTile = newPos.getTile();
		oldTile.remove(entity);
		newTile.put(entity);
		entity.add(newPos);
		
		if(Mappers.visionMap.has(entity)) {
			VisionCalculator.calculateVision(entity);
		}
		
		if(playerMap.has(entity) && newTile.get(Type.ITEM) != null){
			String[] extraText = {newTile.get(Type.ITEM).getComponent(NameComponent.class).name}; 
			MessageFactory.loadMessage("StandingOnItem", extraText);
		}
	}
	
	/**
	 * Cura todos los stats no permanentes
	 * @param actor
	 */
	public static void cure(Entity actor){
		if(playerMap.has(actor)){
			MessageFactory.createMessage("All effects are removed from you");
		}
		StatusEffectsComponent sc = Mappers.statusEffectsMap.get(actor);
		sc.effects.clear();
	}
	
	public static void damage(Entity actor, Float amount){
		Mappers.healthMap.get(actor).curHP -= amount;
		if(Mappers.healthMap.get(actor).curHP <= 0) {
			Actions.die(actor);
		}
	}
	
}
