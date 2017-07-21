package powerworks.world.level.tile;

import powerworks.inventory.item.ItemType;

public class OreTile extends Tile {

    protected int amount;
    protected OreTileType type;
    
    public OreTile(OreTileType type, int x, int y, int amount) {
	super(type, x, y);
	this.amount = amount;
	this.type = type;
    }

    public int getAmount() {
	return amount;
    }

    public void addAmount(int amount) {
	this.amount += amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
    }
    
    public ItemType getOreItem() {
	return type.getOreItem();
    }
}