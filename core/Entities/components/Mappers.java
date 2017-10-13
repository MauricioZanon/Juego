package components;

import com.badlogic.ashley.core.ComponentMapper;

public class Mappers {
	
	//Misc components
	public static ComponentMapper<Type> typeMap = ComponentMapper.getFor(Type.class);
	public static ComponentMapper<PositionComponent> posMap = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<GraphicsComponent> graphMap = ComponentMapper.getFor(GraphicsComponent.class);
	public static ComponentMapper<NameComponent> nameMap = ComponentMapper.getFor(NameComponent.class);
	public static ComponentMapper<TransitableComponent> transitableMap = ComponentMapper.getFor(TransitableComponent.class);
	public static ComponentMapper<TranslucentComponent> translucentMap = ComponentMapper.getFor(TranslucentComponent.class);
	public static ComponentMapper<TimedComponent> timedMap = ComponentMapper.getFor(TimedComponent.class);
	public static ComponentMapper<LightSourceComponent> lightMap = ComponentMapper.getFor(LightSourceComponent.class);
	public static ComponentMapper<AttributeComponent> attMap = ComponentMapper.getFor(AttributeComponent.class);
	public static ComponentMapper<LockComponent> lockMap = ComponentMapper.getFor(LockComponent.class);

	//Actor components
	public static ComponentMapper<PlayerComponent> playerMap = ComponentMapper.getFor(PlayerComponent.class);
	public static ComponentMapper<HealthComponent> healthMap = ComponentMapper.getFor(HealthComponent.class);
	public static ComponentMapper<MovementComponent> movMap = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<Faction> factionMap = ComponentMapper.getFor(Faction.class);
	public static ComponentMapper<EquipmentComponent> equipMap = ComponentMapper.getFor(EquipmentComponent.class);
	public static ComponentMapper<InventoryComponent> inventoryMap = ComponentMapper.getFor(InventoryComponent.class);
	public static ComponentMapper<StatusEffectsComponent> statusEffectsMap = ComponentMapper.getFor(StatusEffectsComponent.class);
	public static ComponentMapper<SkillsComponent> skillMap = ComponentMapper.getFor(SkillsComponent.class);
	public static ComponentMapper<AIComponent> AIMap = ComponentMapper.getFor(AIComponent.class);
	public static ComponentMapper<VisionComponent> visionMap = ComponentMapper.getFor(VisionComponent.class);
	
	//Item components
	public static ComponentMapper<ItemType> itemTypeMap = ComponentMapper.getFor(ItemType.class);
	public static ComponentMapper<PickupableComponent> pickupableMap = ComponentMapper.getFor(PickupableComponent.class);
	
	
}
