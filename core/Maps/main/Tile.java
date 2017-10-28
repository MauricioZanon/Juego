package main;

import static components.Mappers.nameMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import components.GraphicsComponent;
import components.Mappers;
import components.PositionComponent;
import components.TransitableComponent;
import components.Type;
import components.MovementComponent.MovementType;

public class Tile {
	
	private PositionComponent pos;
	
	private Entity actor = null;
	private Entity feature = null;
	private LinkedList<Entity> items = new LinkedList<>();
	private Entity terrain = null;
	
	private Visibility visibility = Visibility.NOT_VISIBLE;
	
	public Tile(PositionComponent p){
		pos = p;
	}
	
	public void put(Entity e) {
		Type type = e.getComponent(Type.class);
		switch(type) {
			case ACTOR:
				actor = e;
				break;
			case FEATURE:
				feature = e;
				break;
			case ITEM:
				items.add(e);
				break;
			case TERRAIN:
				terrain = e;
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
				if(nameMap.get(actor).name.equals(nameMap.get(e).name)) actor = null;
				break;
			case FEATURE:
				if(nameMap.get(feature).name.equals(nameMap.get(e).name)) feature = null;
				break;
			case ITEM:
				items.remove(e);
				break;
			case TERRAIN:
				if(nameMap.get(terrain).name.equals(nameMap.get(e).name)) terrain = null;
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
		}
	}
	
	public Set<Entity> getEntities(){
		Set<Entity> entities = new HashSet<>();
		if(actor != null) entities.add(actor);
		if(feature != null) entities.add(feature);
		if(terrain != null) entities.add(terrain);
		if(!items.isEmpty()) entities.addAll(items);
		
		return entities;
	}
	
	public <T extends Component> Set<Entity> getEntities(ComponentMapper<T> componentMapper){
		Set<Entity> entities = getEntities();
		entities.removeIf(e -> !componentMapper.has(e));
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
		for(TransitableComponent t : getComponents(Mappers.transitableMap)) {
			if(!t.allowedMovementType.contains(MovementType.WALK)) return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		String message = "=====>TILE " + pos + "<=====";
		message += actor == null ? "" : "\nActor: \t\t" + nameMap.get(actor).name;
		message += feature == null ? "" : "\nFeature: \t" + nameMap.get(feature).name;
		message += items.isEmpty() ? "" : "\nItem: \t\t" + nameMap.get(items.getFirst()).name;
		message += terrain == null ? "" : "\nTerrain: \t" + nameMap.get(terrain).name;
		message += "\n= = = = = = = = = = = = = = = = = = = = = = = = \n";
		
		return message; 
	}
	
	public boolean isEmpty() {
		return actor == null && items.isEmpty() && feature == null;
	}
	
	public enum Visibility{
		NOT_VISIBLE,
		VISIBLE,
		VIEWED;
	}


}
