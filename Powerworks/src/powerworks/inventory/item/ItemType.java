package powerworks.inventory.item;

import java.util.HashMap;
import powerworks.block.BlockType;
import powerworks.block.machine.MachineBlockType;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;

public class ItemType {

    public static HashMap<String, ItemType> types = new HashMap<String, ItemType>();
    public static final ItemType ERROR = new ItemType("Error", Image.ERROR, 0, 10);
    public static final ItemType IRON_INGOT = new ItemType("Iron Ingot", Image.IRON_INGOT, 1, 10);
    public static final ItemType IRON_ORE = new ItemType("Iron Ore", Image.IRON_ORE_ITEM, 2, 5,
	    Hitbox.IRON_ORE_ITEM);
    public static final ItemType CONVEYOR_BELT = new ItemType("Conveyor Belt", Image.CONVEYOR_BELT_ITEM, 3, 20, MachineBlockType.CONVEYOR_BELT_CONNECTED_UP,
	    Hitbox.CONVEYOR_BELT_ITEM);
    public static final ItemType ORE_MINER = new ItemType("Ore Miner", Image.ERROR, 4, 10, MachineBlockType.ORE_MINER, Hitbox.TILE);
    
    int id;
    Texture texture;
    String name;
    String desc = "";
    int maxStackSize;
    BlockType placedBlock;
    Hitbox droppedHitbox;

    protected ItemType(String name, Texture texture, int id, int maxStackSize, BlockType block, Hitbox dropppedHitbox) {
	this.id = id;
	this.texture = texture;
	this.name = name;
	this.maxStackSize = maxStackSize;
	this.placedBlock = block;
	this.droppedHitbox = dropppedHitbox;
	types.put(name, this);
    }

    protected ItemType(String name, Texture texture, int id, int maxStackSize) {
	this(name, texture, id, maxStackSize, BlockType.ERROR, Hitbox.NONE);
    }

    protected ItemType(String name, Texture texture, int id, int maxStackSize, Hitbox hitbox) {
	this(name, texture, id, maxStackSize, BlockType.ERROR, hitbox);
    }

    public int getID() {
	return id;
    }

    public Texture getTexture() {
	return texture;
    }

    public String getName() {
	return name;
    }

    public String getDesc() {
	return desc;
    }

    public int getMaxStackSize() {
	return maxStackSize;
    }

    public BlockType getPlacedBlock() {
	return placedBlock;
    }

    public Hitbox getDroppedHitbox() {
	return droppedHitbox;
    }

    @Override
    public String toString() {
	return name;
    }
    
    public static ItemType getItemType(String t) {
	return types.get(t);
    }

    public static HashMap<String, ItemType> getItemTypes() {
	return types;
    }
}
