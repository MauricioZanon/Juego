import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.juego.Juego;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

import components.AIComponent;
import components.AttributeComponent;
import components.EquipmentComponent;
import components.Faction;
import components.GraphicsComponent;
import components.HealthComponent;
import components.InventoryComponent;
import components.ItemType;
import components.LightSourceComponent;
import components.LockComponent;
import components.MovementComponent;
import components.NameComponent;
import components.PickupableComponent;
import components.PlayerComponent;
import components.SkillsComponent;
import components.StatusEffectsComponent;
import components.TimedComponent;
import components.TransitableComponent;
import components.TranslucentComponent;
import components.Type;
import components.VisionComponent;
import states.AttackState;
import states.ExploreState;
import states.IdleState;


@XStreamAlias("Ejemplos") // Hace que XStream use este alias cuando cree el XML
public class EjemploXStream {
	
	@XStreamOmitField // Hace que esta variable no se guarde en el XML
	private int variable = 0;
	
	private static final String PATH = "assets/Data/Templates.xml";

	private static void saveObject(Object o){
		
		XStream xstream = new XStream(new DomDriver());
		
		String s = xstream.toXML(o); // Se crea un string que contiene el XML con el objeto a guardar
		
		FileWriter fw;
		try {
			fw = new FileWriter(PATH);
			fw.write(s);
			fw.close();
		} catch (IOException e) {
			System.out.println("save failed");
		} 
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T> Entity loadObject(){
		
		XStream xstream = new XStream(new DomDriver());
		
		T[] p = (T[]) xstream.fromXML(new File(PATH));
		
		Entity e = Juego.ENGINE.createEntity();
		for(int i = 0; i < p.length; i++) {
			e.add((Component) p[i]);
		}
		
		System.out.println(e.getComponent(NameComponent.class).name);
		
		return e;
	}
	
	private static void createActor() {
		Entity actor = Juego.ENGINE.createEntity();
		
		actor.add(Type.ACTOR);
		
		NameComponent nc = Juego.ENGINE.createComponent(NameComponent.class);
		nc.name = "Actor template";
		actor.add(nc);
		
		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "g";
		gc.frontColor = Color.PINK;
		gc.font = "general";
		gc.renderPriority = 3;
		actor.add(gc);
		
		AttributeComponent ac = Juego.ENGINE.createComponent(AttributeComponent.class);
		ac.set("STR", 10f);
		ac.set("CON", 10f);
		ac.set("DEX", 10f);
		ac.set("CUN", 10f);
		ac.set("INT", 10f);
		ac.set("WIS", 10f);
		ac.set("damage", 15f);
		actor.add(ac);
		
		/*
		 * TODO las cosas equipadas se guardan mal, 
		 * hay que hacer como con las demas entities y guardar sus components como un array en vez de una entidad
		 */
		EquipmentComponent ec = Juego.ENGINE.createComponent(EquipmentComponent.class);
		actor.add(ec);
		
		AIComponent ai = Juego.ENGINE.createComponent(AIComponent.class);
		ai.states.put("attacking", new AttackState());
		ai.states.put("exploring", new ExploreState());
		ai.states.put("idling", new IdleState());
		ai.fsm.setInitialState(ai.states.get("exploring"));
		
		actor.add(ai);
		
		actor.add(Juego.ENGINE.createComponent(HealthComponent.class));
		actor.add(Juego.ENGINE.createComponent(MovementComponent.class));
		actor.add(Juego.ENGINE.createComponent(TimedComponent.class));
		actor.add(Faction.HUMANS);
		actor.add(Juego.ENGINE.createComponent(InventoryComponent.class));
		actor.add(Juego.ENGINE.createComponent(StatusEffectsComponent.class));
		actor.add(Juego.ENGINE.createComponent(SkillsComponent.class));
		actor.add(Juego.ENGINE.createComponent(VisionComponent.class));
		
		saveObject(actor.getComponents().toArray());
		loadObject();
	}
	
	private static void createItem() {
		Entity item = Juego.ENGINE.createEntity();
		
		item.add(Type.ITEM);
		item.add(ItemType.WEAPON);
		
		NameComponent nc = Juego.ENGINE.createComponent(NameComponent.class);
		nc.name = "Item template";
		item.add(nc);
		
		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "/";
		gc.frontColor = Color.GRAY;
		gc.renderPriority = 1;
		item.add(gc);
		

		item.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		
		AttributeComponent ac = Juego.ENGINE.createComponent(AttributeComponent.class);
		ac.set("damage", 10f);
		item.add(ac);
		
		saveObject(item.getComponents().toArray());
		loadObject();
	}
	
	private static void createFeature() {
		Entity feature = Juego.ENGINE.createEntity();
		
		feature.add(Type.FEATURE);
		
		NameComponent nc = Juego.ENGINE.createComponent(NameComponent.class);
		nc.name = "Feature template";
		feature.add(nc);
		
		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "?";
		gc.font = "general";
		gc.frontColor = Color.RED;
		gc.renderPriority = 2;
		feature.add(gc);

		saveObject(feature.getComponents().toArray());
		loadObject();
	}
	
	private static void createPotion() {
		Entity feature = Juego.ENGINE.createEntity();
		
		feature.add(Type.ITEM);
		feature.add(ItemType.POTION);
		feature.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		
		NameComponent nc = Juego.ENGINE.createComponent(NameComponent.class);
		nc.name = "Potion template";
		feature.add(nc);
		
		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
		gc.ASCII = "i";
		gc.font = "general";
		gc.frontColor = Color.RED;
		gc.renderPriority = 1;
		feature.add(gc);

		saveObject(feature.getComponents().toArray());
		loadObject();
	}
	
	private static void createEntityWithAllComponents() {
		Entity e = Juego.ENGINE.createEntity();
		
		e.add(Juego.ENGINE.createComponent(GraphicsComponent.class));
		e.add(Type.ACTOR);
		e.add(Juego.ENGINE.createComponent(NameComponent.class));
		e.add(Juego.ENGINE.createComponent(TransitableComponent.class));
		e.add(Juego.ENGINE.createComponent(TranslucentComponent.class));
		e.add(Juego.ENGINE.createComponent(TimedComponent.class));
		e.add(Juego.ENGINE.createComponent(LightSourceComponent.class));
		e.add(Juego.ENGINE.createComponent(AttributeComponent.class));
		e.add(Juego.ENGINE.createComponent(LockComponent.class));

		//Actor components
		e.add(Juego.ENGINE.createComponent(PlayerComponent.class));
		e.add(Juego.ENGINE.createComponent(HealthComponent.class));
		e.add(Juego.ENGINE.createComponent(EquipmentComponent.class));
		e.add(Juego.ENGINE.createComponent(InventoryComponent.class));
		e.add(Juego.ENGINE.createComponent(StatusEffectsComponent.class));
		e.add(Juego.ENGINE.createComponent(SkillsComponent.class));
		e.add(Juego.ENGINE.createComponent(AIComponent.class));
		e.add(Juego.ENGINE.createComponent(VisionComponent.class));
		e.add(Faction.HUMANS);
		
		//Item components
		e.add(Juego.ENGINE.createComponent(PickupableComponent.class));
		e.add(ItemType.WEAPON);

		saveObject(e.getComponents().toArray());
		loadObject();
	}
	
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		
		createEntityWithAllComponents();
		createItem();
		createPotion();
		createFeature();
		createActor();
		
		loadObject();
		
	}

}
