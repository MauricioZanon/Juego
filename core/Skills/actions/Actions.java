package actions;

import static components.Mappers.AIMap;
import static components.Mappers.attMap;
import static components.Mappers.descMap;
import static components.Mappers.equipMap;
import static components.Mappers.movMap;
import static components.Mappers.playerMap;
import static components.Mappers.posMap;
import static components.Mappers.timedMap;
import static components.Mappers.visionMap;

import java.util.NoSuchElementException;

import com.badlogic.ashley.core.Entity;
import com.mygdx.juego.Juego;

import components.InventoryComponent;
import components.Mappers;
import components.MovementComponent.MovementType;
import components.PositionComponent;
import components.Type;
import console.MessageFactory;
import effects.DamageCalculator;
import effects.Effects;
import effects.Trigger;
import eventSystem.ActiveMap;
import eventSystem.EventSystem;
import features.FeaturesEffects;
import main.Tile;
import main.Tile.Visibility;
import pathFind.Path;
import pathFind.PathFinder;
import potions.QuaffEffects;
import world.Direction;
import world.Explorer;

/*TODO hacer que los actores tengan una lista de enums con las acciones que pueden realizar y crear un método en esta clase
 * que les permita usarlas según el parámetro que manden
 */

public abstract class Actions {
	
	/**
	 * Intentar moverse en una dirección, la acción realizada depende de lo que se encuentre
	 * @param oldPos
	 * @param dir
	 */
	public static void bump(PositionComponent oldPos, Direction dir){
		Tile oldTile = oldPos.getTile();
		PositionComponent newPos = new PositionComponent(oldPos.coord[0] + dir.movX, oldPos.coord[1] + dir.movY, oldPos.getGz());
		Tile newTile = newPos.getTile();
		Entity actor = oldTile.get(Type.ACTOR);
		
		if(newTile.get(Type.ACTOR) != null){
			attack(oldTile, newTile);
		}
		else if(newTile.get(Type.FEATURE) != null && !newTile.isTransitable()){
			useFeature(actor, newPos);
		}
		else if(Mappers.transitableMap.get(newTile.get(Type.TERRAIN)).allowedMovementType.contains(MovementType.WALK)){
			walk(oldPos, newPos);
		}
		if(!newTile.isTransitable()) {
			return;
		}
	}
	
	/**
	 * Caminar a un tile adyacente
	 * @param oldPos tile actual
	 * @param newPos tile al que se está caminando
	 */
	public static void walk(PositionComponent oldPos, PositionComponent newPos){
		Tile oldTile = oldPos.getTile();
		Entity actor = oldTile.get(Type.ACTOR);
		Effects.move(actor, newPos);
		Mappers.statusEffectsMap.get(actor).affect(Trigger.MOVES);
		endTurn(actor, ActionType.WALK);
	}
	
	/**
	 * Atacar a un tile adyacente
	 * @param attackerTile el tile desde el cual surge el ataque
	 * @param receiverTile el tile al que es dirigido
	 */
	public static void attack(Tile attackerTile, Tile receiverTile){
		Entity attacker = attackerTile.get(Type.ACTOR);
		Entity receiver = receiverTile.get(Type.ACTOR);
		
		String[] extraText = {descMap.get(attacker).name, descMap.get(receiver).name};
		if(attackerTile.getVisibility() == Visibility.VISIBLE && receiverTile.getVisibility() == Visibility.VISIBLE){
			if(playerMap.has(attacker))
				MessageFactory.loadMessage("PlayerAttacksNPC", extraText);
			else if (playerMap.has(receiver))
				MessageFactory.loadMessage("NPCAttacksPlayer", extraText);
			else
				MessageFactory.loadMessage("NPCAttacksNPC", extraText);
		}
		float damage = DamageCalculator.calculate(attacker, receiver);
		Effects.damage(receiver, damage);
		endTurn(attacker, ActionType.ATTACK);
	}
	
	/**
	 * Levantar item
	 * @param tile el tile en el que se intenta levantar el item
	 */
	public static void pickUp(Tile tile){
		Entity actor = tile.get(Type.ACTOR);
		InventoryComponent actorInv = Mappers.inventoryMap.get(actor);
		if(tile.get(Type.ITEM) != null){
			Entity item = tile.get(Type.ITEM);
			actorInv.add(item);
			if(playerMap.has(actor)){
				String[] extraText = {descMap.get(item).name};
				MessageFactory.loadMessage("PlayerPicksUp", extraText);
			}
			tile.remove(item);
			endTurn(actor, ActionType.PICK_UP);
		}
	}
	
	/**
	 * tomar poción TODO: hacer que se manejen los efectos de las pociones igual a como se manejan los efectos de las features
	 * @param actor el actor que toma la poción
	 * @param potion la poción que se está tomando
	 */
	public static void quaff(Entity actor, Entity potion){
		QuaffEffects.getFor(descMap.get(potion).name).accept(actor);
		endTurn(actor, ActionType.USE_ITEM);
	}
	
	public static void equip(Entity actor, Entity equipment) {
		equipMap.get(actor).equip(equipment);
	}
	
	/**
	 * Busca un tile sin explorar, pide un camino y hace que el actor siga el camino
	 * @param actor el actor que explora
	 */
	public static void explore(Entity actor){
		Tile origin = posMap.get(actor).getTile();
		Tile destination = Explorer.getClosestTile(origin, t -> t.getVisibility() == Visibility.NOT_VISIBLE);
		if(destination != null) {
			movMap.get(actor).path = PathFinder.findPath(origin.getPos(), destination.getPos(), actor);
			followPath(actor);
		}else {
			MessageFactory.createMessage("There is nothing left to explore nearby.");
		}
	}
	
	/**
	 * Sigue un path, cuando el path termina lo elimina
	 * @param actor 
	 */
	public static void followPath(Entity actor) {
		if(!visionMap.get(actor).enemyTiles.isEmpty() && !AIMap.get(actor).isInState("attacking")) {
			if(playerMap.has(actor)) {
				MessageFactory.createMessage("There's an enemy nearby.");
			}
			movMap.get(actor).path = null;
			AIMap.get(actor).setState("attacking");
			return;
		}
		PositionComponent next = null;
		Path path = movMap.get(actor).path;
		try{
			next = path.getNext();
			path.advance();
		}catch(NullPointerException | IndexOutOfBoundsException | NoSuchElementException e){
			movMap.get(actor).path = null;
			return;
		}
		if(path != null && path.isEnded()){
			movMap.get(actor).path = null;
		}
		Actions.bump(posMap.get(actor), Direction.get(posMap.get(actor),next));
	}
	
	/**
	 * Intenta usar un feature en una posición adyacente
	 * @param actor el actor que lo usa
	 * @param featurePos la posición del feature usado
	 */
	public static void useFeature(Entity actor, PositionComponent featurePos) {
		Entity feature = featurePos.getTile().get(Type.FEATURE);
		if(feature != null) {
			FeaturesEffects.use(feature, actor);
		}
		endTurn(actor, ActionType.USE_ITEM);
	}
	
	/**
	 * Termina el turno de la entidad y disminuye sus ap según la acción realizada y la velocidad de la entidad
	 * @param entity
	 * @param action la última acción realizada
	 */
	public static void endTurn(Entity entity, ActionType action) {
		timedMap.get(entity).nextTurn += (int) attMap.get(entity).get(action.asociatedStat);
		Mappers.statusEffectsMap.get(entity).affect(Trigger.END_TURN);
		if(playerMap.has(entity)) {
			ActiveMap.refresh();
			Juego.ENGINE.getSystem(EventSystem.class).waitingForPlayerInput = false;
		}
	}

	/**
	 * Hace que el actor ya no figure como activo
	 * @param actor el actor a eliminar
	 */
	public static void die(Entity actor) {
		if(playerMap.has(actor)) {
			System.exit(0);
		}
		PositionComponent pos = posMap.get(actor);
		pos.getTile().remove(actor);
		timedMap.get(actor).isActive = false;
	}
	
}
