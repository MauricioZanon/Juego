package main;

import static components.Mappers.descMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.juego.Juego;

import components.GraphicsComponent;
import components.Mappers;
import components.MovementComponent.MovementType;
import components.PositionComponent;
import components.TransitableComponent;
import components.TranslucentComponent;
import components.Type;

public class Tile {
	
	private PositionComponent pos;
	
	private Entity gas = null;
	private Entity actor = null;
	private Entity feature = null;
	private LinkedList<Entity> items = new LinkedList<>();
	private Entity terrain = null;
	
	private float lightLevel = 0.1f;
	
	private Visibility visibility = Visibility.VISIBLE;
	
	public Tile(PositionComponent p){
		pos = p;
	}
	
	public void put(Entity e) {
		Type type = e.getComponent(Type.class);
		switch(type) {
		case ACTOR:
			actor = e;
			e.add(pos);
			break;
		case FEATURE:
			feature = e;
			break;
		case ITEM:
			items.add(e);
			break;
		case TERRAIN:
			terrain = e;
			break;
		case GAS:
			gas = e;
			break;
		}
	}
	
	public Entity get(Type type) {
		switch(type) {
		case ACTOR:
			return actor;
		case FEATURE:
			return feature;
		case ITEM:
			if(items.isEmpty()) return null;
			else return items.getFirst();
		case TERRAIN:
			return terrain;
		default:
			return null;
		}
	}
	
	public void remove(Entity e) {
		Type type = e.getComponent(Type.class);
		switch(type) {
		case ACTOR:
			if(descMap.get(actor).name.equals(descMap.get(e).name)) actor = null;
			break;
		case FEATURE:
			if(descMap.get(feature).name.equals(descMap.get(e).name)) feature = null;
			break;
		case ITEM:
			items.remove(e);
			break;
		case TERRAIN:
			if(descMap.get(terrain).name.equals(descMap.get(e).name)) terrain = null;
			break;
		case GAS:
			if(descMap.get(gas).name.equals(descMap.get(e).name)) gas = null;
			break;
		}
	}
	public void remove(Type type) {
		switch(type) {
		case ACTOR:
			actor = null;
			break;
		case FEATURE:
			feature = null;
			break;
		case ITEM:
			items.clear();
			break;
		case TERRAIN:
			terrain = null;
			break;
		case GAS:
			gas = null;
			break;
		}
	}
	
	public Set<Entity> getEntities(){
		Set<Entity> entities = new HashSet<>();
		if(actor != null) entities.add(actor);
		if(feature != null) entities.add(feature);
		if(terrain != null) entities.add(terrain);
		if(!items.isEmpty()) entities.addAll(items);
		if(gas != null) entities.add(gas);
		
		return entities;
	}
	
	public <T extends Component> Set<Entity> getEntities(ComponentMapper<T> componentMapper){
		Set<Entity> entities = getEntities();
		entities.removeIf(e -> !componentMapper.has(e));
		return entities;
	}
	
	public Set<Entity> getEntities(Predicate<Entity> cond){
		Set<Entity> entities = getEntities();
		entities.removeIf(e -> !cond.test(e));
		return entities;
	}
	
	public <T extends Component> Set<T> getComponents (ComponentMapper<T> componentMapper){
		Set<T> components = new HashSet<T>();
		for(Entity e : getEntities()) {
			T c = componentMapper.get(e);
			if(c != null) components.add(c);
		}
		return components;
	}
	
	public GraphicsComponent getFrontGC() {
		TreeSet<GraphicsComponent> queue = new TreeSet<>(GraphicsComponent.frontComparator);
		for(GraphicsComponent gc : getComponents(Mappers.graphMap)) {
			queue.add(gc);
		}
		try{return queue.first();}
		catch(NoSuchElementException e) {return null;}
	}

	public GraphicsComponent getBackGC() {
		TreeSet<GraphicsComponent> queue = new TreeSet<>(GraphicsComponent.backComparator);
		for(GraphicsComponent gc : getComponents(Mappers.graphMap)) {
			queue.add(gc);
		}
		try{return queue.first();}
		catch(NoSuchElementException e) {return null;}
	}
	
	public float getLightLevel() {
		return 1f;
//		return pos.coord[2] == 0 ? Math.max(Time.getLightLevel(), lightLevel) : lightLevel;
	}

	public void setLightLevel(float lightLevel) {
		this.lightLevel = MathUtils.clamp(this.lightLevel + lightLevel, 0.1f, 1f);
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibiliy(Visibility visibiliy) {
		this.visibility = visibiliy;
	}

	public PositionComponent getPos() {
		return pos;
	}
	
	public boolean isTransitable() {
		if(terrain == null) return false;
		for(TransitableComponent c : getComponents(Mappers.transitableMap)) {
			if(!c.allowedMovementType.contains(MovementType.WALK)) return false;
		}
		return true;
	}
	
	public boolean isTranslucent() {
		if(terrain == null) return false;
		for(TranslucentComponent c : getComponents(Mappers.translucentMap)) {
			if(!c.translucent) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String message = "=====>TILE " + pos + "<=====";
		message += actor == null ? "" : "\nActor: \t\t" + descMap.get(actor).name;
		message += feature == null ? "" : "\nFeature: \t" + descMap.get(feature).name;
		message += items.isEmpty() ? "" : "\nItem: \t\t" + descMap.get(items.getFirst()).name;
		message += terrain == null ? "" : "\nTerrain: \t" + descMap.get(terrain).name;
		message += gas == null ? "" : "\nGas: \t" + descMap.get(gas).name;
		message += "\n= = = = = = = = = = = = = = = = = = = = = = = = \n";
		
		return message; 
	}
	
	public void serialize(StringBuilder sb) {
		try {sb.append("," + terrain.flags);
		}catch(NullPointerException e) {}
		try {sb.append("," + actor.flags);
		}catch(NullPointerException e) {}
		for(Entity item : items) {
			sb.append("," + item.flags);
		}
		try {sb.append("," + feature.flags);
		}catch(NullPointerException e) {}
		sb.append("/");
	}
	
	public boolean isEmpty() {
		return gas == null && actor == null && feature == null && items.isEmpty() && terrain == null;
	}
	
	public enum Visibility{
		NOT_VIEWED,
		VISIBLE,
		VIEWED;
	}

	public void dump() {
		PooledEngine engine = Juego.ENGINE;
		try {engine.removeEntity(actor);
		}catch(NullPointerException e) {}
		for(Entity item : items) {
			engine.removeEntity(item);
		}
		try {engine.removeEntity(feature);
		}catch(NullPointerException e) {}
		
	}
}