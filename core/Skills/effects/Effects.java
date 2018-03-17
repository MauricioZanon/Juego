package effects;
import static components.Mappers.playerMap;
import static components.Mappers.posMap;
import static components.Mappers.timedMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;

import FOV.VisionCalculator;
import RNG.RNG;
import components.HealthComponent;
import components.Mappers;
import components.PositionComponent;
import components.StatusEffectsComponent;
import components.Type;
import console.MessageFactory;
import eventSystem.Map;
import main.Tile;

public abstract class Effects {
	
	public static void heal(Entity actor, int amount){
		HealthComponent hc = Mappers.healthMap.get(actor);
		hc.curHP = MathUtils.clamp(hc.curHP + amount, 0, hc.maxHP);
		
		String[] extraText = {Mappers.descMap.get(actor).name};
		String messageType = playerMap.has(actor) ? "PlayerHeals" : "NpcHeals";
		MessageFactory.loadMessage(messageType, extraText);
	}
	
	public static void randomTeleport(Entity actor){
		Tile tile = posMap.get(actor).getTile();
		PositionComponent newPos = RNG.getRandom(Map.getCircundatingAreaAsSet(10, tile, true), t -> t.isTransitable()).getPos();
		teleport(actor, newPos);
	}
	
	public static void teleport(Entity actor, PositionComponent newPos){
		if(playerMap.has(actor)){
			MessageFactory.createMessage("Suddenly you find yourself in another place");
		}
		move(actor, newPos);
	}
	
	/**
	 * @param entity la entidad que se va a mover
	 * @param newPos la posición a la que se va a mover
	 */
	public static void move(Entity entity, PositionComponent newPos) {
		Tile oldTile = posMap.get(entity).getTile();
		Tile newTile = newPos.getTile();
		oldTile.remove(entity);
		
		if(playerMap.has(entity)) {
			entity.add(newPos);
			Map.refresh();
			if(newTile.get(Type.ITEM) != null) {
				String[] extraText = {Mappers.descMap.get(newTile.get(Type.ITEM)).name};
				MessageFactory.loadMessage("StandingOnItem", extraText);
			}
		}
		VisionCalculator.calculateVision(entity);
		newTile.put(entity);
		Mappers.statusEffectsMap.get(entity).affect(Trigger.MOVES);
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
			die(actor);
		}
	}
	
	/**
	 * Hace que el actor ya no figure como activo
	 * @param actor el actor a eliminar
	 */
	private static void die(Entity actor) {
		if(playerMap.has(actor)) {
			System.exit(0);
		}
		PositionComponent pos = posMap.get(actor);
		pos.getTile().remove(actor);
		timedMap.get(actor).isActive = false;
	}
	
}
