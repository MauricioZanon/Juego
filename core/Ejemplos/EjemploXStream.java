//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPathExpressionException;
//
//import org.xml.sax.SAXException;
//
//import com.badlogic.ashley.core.Component;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.gdx.graphics.Color;
//import com.mygdx.juego.Juego;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamOmitField;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//
//import components.AIComponent;
//import components.AttributeComponent;
//import components.DescriptionComponent;
//import components.EquipmentComponent;
//import components.Faction;
//import components.GraphicsComponent;
//import components.HealthComponent;
//import components.InventoryComponent;
//import components.ItemType;
//import components.LockComponent;
//import components.MovementComponent;
//import components.PickupableComponent;
//import components.SkillsComponent;
//import components.StatusEffectsComponent;
//import components.TimedComponent;
//import components.TransitableComponent;
//import components.TranslucentComponent;
//import components.Type;
//import components.VisionComponent;
//import states.npc.AttackState;
//import states.npc.WanderState;
//
//
//@XStreamAlias("Ejemplos") // Hace que XStream use este alias cuando cree el XML
//public class EjemploXStream {
//	
//	@XStreamOmitField // Hace que esta variable no se guarde en el XML
//	private int variable = 0;
//	
//	private static final String PATH = "assets/Data/Templates.xml";
//
//	private static void saveObject(Object o){
//		
//		XStream xstream = new XStream(new DomDriver());
//		
//		String s = xstream.toXML(o); // Se crea un string que contiene el XML con el objeto a guardar
//		
//		FileWriter fw;
//		try {
//			fw = new FileWriter(PATH);
//			fw.write(s);
//			fw.close();
//		} catch (IOException e) {
//			System.out.println("save failed");
//		} 
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	private static <T> Entity loadObject(){
//		
//		XStream xstream = new XStream(new DomDriver());
//		
//		T[] p = (T[]) xstream.fromXML(new File(PATH));
//		
//		Entity e = Juego.ENGINE.createEntity();
//		for(int i = 0; i < p.length; i++) {
//			e.add((Component) p[i]);
//		}
//		
//		System.out.println(e.getComponent(DescriptionComponent.class).name);
//		
//		return e;
//	}
//	
//	private static void createActor() {
//		Entity actor = Juego.ENGINE.createEntity();
//		
//		actor.add(Type.ACTOR);
//		
//		DescriptionComponent nc = Juego.ENGINE.createComponent(DescriptionComponent.class);
//		nc.name = "Actor template";
//		actor.add(nc);
//		
//		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
//		gc.ASCII = "g";
//		gc.frontColor = Color.PINK;
//		gc.font = "general";
//		gc.renderPriority = 3;
//		actor.add(gc);
//		
//		AttributeComponent ac = Juego.ENGINE.createComponent(AttributeComponent.class);
//		ac.set("STR", 10f);
//		ac.set("CON", 10f);
//		ac.set("DEX", 10f);
//		ac.set("CUN", 10f);
//		ac.set("INT", 10f);
//		ac.set("WIS", 10f);
//		ac.set("damage", 15f);
//		actor.add(ac);
//		
//		EquipmentComponent ec = Juego.ENGINE.createComponent(EquipmentComponent.class);
//		actor.add(ec);
//		
//		AIComponent ai = Juego.ENGINE.createComponent(AIComponent.class);
//		ai.states.put("attacking", new AttackState());
//		ai.states.put("wandering", new WanderState());
//		ai.fsm.setInitialState(ai.states.get("wandering"));
//		
//		actor.add(ai);
//		
//		actor.add(Juego.ENGINE.createComponent(HealthComponent.class));
//		actor.add(Juego.ENGINE.createComponent(MovementComponent.class));
//		actor.add(Juego.ENGINE.createComponent(TimedComponent.class));
//		actor.add(Faction.HUMANS);
//		actor.add(Juego.ENGINE.createComponent(InventoryComponent.class));
//		actor.add(Juego.ENGINE.createComponent(StatusEffectsComponent.class));
//		actor.add(Juego.ENGINE.createComponent(SkillsComponent.class));
//		actor.add(Juego.ENGINE.createComponent(VisionComponent.class));
//		
//		saveObject(actor.getComponents().toArray());
//		loadObject();
//	}
//	
//	private static void createItem() {
//		Entity item = Juego.ENGINE.createEntity();
//		
//		item.add(Type.ITEM);
//		item.add(ItemType.KEY);
//		
//		DescriptionComponent nc = Juego.ENGINE.createComponent(DescriptionComponent.class);
//		nc.name = "key";
//		item.add(nc);
//		
//		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
//		gc.ASCII = ",";
//		gc.font = "general";
//		gc.frontColor = Color.GRAY;
//		gc.renderPriority = 1;
//		item.add(gc);
//
//		item.add(Juego.ENGINE.createComponent(PickupableComponent.class));
//		
//		saveObject(item.getComponents().toArray());
//		loadObject();
//	}
//	
//	private static void createFeature() {
//		Entity feature = Juego.ENGINE.createEntity();
//		
//		feature.add(Type.FEATURE);
//		
//		DescriptionComponent nc = Juego.ENGINE.createComponent(DescriptionComponent.class);
//		nc.name = "door";
//		feature.add(nc);
//		
//		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
//		gc.ASCII = "+";
//		gc.font = "general";
//		gc.frontColor = Color.BROWN;
//		gc.renderPriority = 2;
//		feature.add(gc);
//		
//		LockComponent lc = Juego.ENGINE.createComponent(LockComponent.class);
//		lc.isClosed = true;
//		lc.isLocked = false;
//		feature.add(lc);
//		
//		TransitableComponent tc = Juego.ENGINE.createComponent(TransitableComponent.class);
//		feature.add(tc);
//		
//		TranslucentComponent translucentC = Juego.ENGINE.createComponent(TranslucentComponent.class);
//		translucentC.translucent = false;
//		feature.add(translucentC);
//		
//		saveObject(feature.getComponents().toArray());
//		loadObject();
//	}
//	
//	private static void createPotion() {
//		Entity potion = Juego.ENGINE.createEntity();
//		
//		potion.add(Type.ITEM);
//		potion.add(ItemType.POTION);
//		potion.add(Juego.ENGINE.createComponent(PickupableComponent.class));
//		
//		DescriptionComponent nc = Juego.ENGINE.createComponent(DescriptionComponent.class);
//		nc.name = "Potion template";
//		potion.add(nc);
//		
//		GraphicsComponent gc = Juego.ENGINE.createComponent(GraphicsComponent.class);
//		gc.ASCII = "i";
//		gc.font = "general";
//		gc.frontColor = Color.RED;
//		gc.renderPriority = 1;
//		potion.add(gc);
//
//		saveObject(potion.getComponents().toArray());
//		loadObject();
//	}
//	
//	
//	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
//		
//		createPotion();
//		createFeature();
//		createItem();
//		createActor();
//		
//	}
//
//}
