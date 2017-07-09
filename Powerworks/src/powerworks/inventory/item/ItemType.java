package powerworks.inventory.item;

import java.util.HashMap;
import powerworks.collidable.Hitbox;
import powerworks.collidable.block.BlockType;
import powerworks.collidable.block.MachineBlockType;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;

public class ItemType {

    public static HashMap<String, ItemType> types = new HashMap<String, ItemType>();
    public static final ItemType ERROR = new ItemType("Error", Image.ERROR, 0, 10);
    public static final ItemType IRON_INGOT = new ItemType("Iron Ingot", Image.IRON_INGOT, 1, 10);
    public static final ItemType IRON_ORE = new ItemType("Iron Ore", Image.IRON_ORE_ITEM, 2, 5);
    public static final ItemType CONVEYOR_BELT = new ItemType("Conveyor Belt", Image.CONVEYOR_BELT_ITEM, 3, 20, MachineBlockType.CONVEYOR_BELT_CONNECTED_UP);
    public static final ItemType ORE_MINER = new ItemType("Ore Miner", Image.ERROR, 4, 10, MachineBlockType.ORE_MINER);
    
    int id;
    Texture texture;
    String name;
    String desc = "";
    int maxStackSize;
    BlockType placedBlock;

    protected ItemType(String name, Texture texture, int id, int maxStackSize, BlockType block) {
	this.id = id;
	this.texture = texture;
	this.name = name;
	this.maxStackSize = maxStackSize;
	this.placedBlock = block;
	types.put(name, this);
    }

    protected ItemType(String name, Texture texture, int id, int maxStackSize) {
	this(name, texture, id, maxStackSize, BlockType.ERROR);
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
