package powerworks.inventory.item;

import powerworks.block.BlockType;
import powerworks.collidable.Hitbox;
import powerworks.graphics.StaticTexture;
import powerworks.graphics.Texture;

public enum ItemType {
    ERROR("Test Item", StaticTexture.ERROR, 0, 10), IRON_INGOT("Iron Ingot", StaticTexture.IRON_INGOT, 1, 10), IRON_ORE("Iron Ore", StaticTexture.IRON_ORE_ITEM, 2, 5,
	    Hitbox.IRON_ORE_ITEM), CONVEYOR_BELT("Conveyor Belt", StaticTexture.CONVEYOR_BELT_ITEM, 3, 20, BlockType.CONVEYOR_BELT_CONNECTED_UP,
		    Hitbox.CONVEYOR_BELT_ITEM), ORE_MINER("Ore Miner", StaticTexture.ERROR, 4, 10, BlockType.ORE_MINER, Hitbox.TILE);

    int id;
    Texture texture;
    String name;
    String desc = "";
    int maxStackSize;
    BlockType placedBlock;
    Hitbox droppedHitbox;

    private ItemType(String name, Texture texture, int id, int maxStackSize, BlockType block, Hitbox dropppedHitbox) {
	this.id = id;
	this.texture = texture;
	this.name = name;
	this.maxStackSize = maxStackSize;
	this.placedBlock = block;
	this.droppedHitbox = dropppedHitbox;
    }

    private ItemType(String name, Texture texture, int id, int maxStackSize) {
	this(name, texture, id, maxStackSize, BlockType.ERROR, Hitbox.NONE);
    }

    private ItemType(String name, Texture texture, int id, int maxStackSize, Hitbox hitbox) {
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
}
