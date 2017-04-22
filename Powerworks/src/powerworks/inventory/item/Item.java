package powerworks.inventory.item;

import powerworks.block.BlockType;
import powerworks.graphics.Texture;

public class Item{
    private int quantity;
    private ItemType type;
    
    public Item(ItemType type, int quantity) {
	this.type = type;
	this.quantity = quantity;
    }
    
    public Item(ItemType itemtype) {
	this(itemtype, 1);
    }
    
    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }
    
    public void setType(ItemType type) {
	this.type = type;
    }
    
    public int getQuantity() {
	return quantity;
    }
    
    public ItemType getType() {
	return type;
    }
    
    public int getID() {
	return type.id;
    }
    
    public Texture getTexture() {
	return type.texture;
    }
    
    public String getName() {
	return type.name;
    }
    
    public String getDesc() {
	return type.desc;
    }
    
    public int getMaxStack() {
	return type.maxStackSize;
    }
    
    public BlockType getPlacedBlock() {
	return type.placedBlock;
    }
    
    public boolean isPlaceable() {
	if(type.placedBlock != BlockType.ERROR) return true;
	return false;
    }
}