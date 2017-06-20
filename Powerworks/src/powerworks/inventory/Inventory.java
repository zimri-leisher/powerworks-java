package powerworks.inventory;

import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;

public class Inventory {

    Item[] items;
    int width;
    int height;

    public Inventory(int width, int height) {
	this.items = new Item[width * height];
	this.width = width;
	this.height = height;
    }

    public void setItem(Item item, int index) {
	items[index] = item;
    }

    public void setItem(ItemType type, int quantity, int index) {
	setItem(new Item(type, quantity), index);
    }

    public int getSize() {
	return items.length;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public boolean hasItem(ItemType item) {
	return hasItem(item, 1);
    }

    /**
     * Checks if an inventory has the item in the specified quantity or less
     */
    public boolean hasItem(ItemType type, int quantity) {
	for (Item item : items) {
	    if (item.getType() == type && item.getQuantity() >= quantity)
		return true;
	}
	return false;
    }

    /**
     * Gives an item to the player
     * 
     * @param item
     *            the item to give
     */
    public void giveItem(Item item) {
	giveItem(item.getType(), item.getQuantity());
    }

    /**
     * Gives an Item object to the inventory
     * 
     * @return false if the inventory was unable to accept the item (it is full)
     */
    public boolean giveItem(ItemType type, int quantity) {
	int left = quantity;
	int max = type.getMaxStackSize();
	for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getType() == type && items[i].getQuantity() < items[i].getMaxStack()) {
		if (items[i].getQuantity() + left <= max) {
		    items[i].setQuantity(items[i].getQuantity() + left);
		    return true;
		} else {
		    left -= (max - items[i].getQuantity());
		    items[i].setQuantity(max);
		    if (items[i + 1] != null && items[i + 1].getType() != type) {
			if (shiftRight(i + 1)) {
			    items[i] = new Item(type, left);
			} else {
			    return false;
			}
		    }
		}
	    }
	}
	for (int i = 0; i < items.length; i++) {
	    if (items[i] == null) {
		items[i] = new Item(type, left);
		return true;
	    }
	}
	return false;
    }

    /**
     * Shifts the inventory after the given index one to the right, inclusive
     * 
     * @return false if unable to do this
     */
    private boolean shiftRight(int index) {
	if (items[items.length - 1] != null)
	    return false;
	Item[] temp = items.clone();
	for (int i = index + 1; i < items.length; i++) {
	    temp[i] = items[i - 1];
	}
	temp[index] = null;
	items = temp;
	return true;
    }
    
    private boolean shiftLeft(int index) {
	if(items[index - 1] != null)
	    return false;
	Item[] temp = items.clone();
	//TODO
	return true;
    }

    /**
     * Takes an item from the inventory
     * 
     * @param item
     *            the item to remove
     */
    public void takeItem(Item item) {
	takeItem(item.getType(), item.getQuantity());
    }

    /**
     * Takes an item from the inventory
     * 
     * @return true if was able to remove the specified type and quantity, false
     *         otherwise
     */
    public boolean takeItem(ItemType type, int quantity) {
	int left = quantity;
	for (int i = items.length - 1; i >= 0; i--) {
	    if (items[i] != null && items[i].getType() == type) {
		if (items[i].getQuantity() > left) {
		    items[i].setQuantity(items[i].getQuantity() - left);
		    return true;
		} else {
		    left -= items[i].getQuantity();
		    items[i] = null;
		}
	    }
	}
	return false;
    }

    /**
     * Removes an item from an inventory in its entirety
     * 
     * @param index
     *            the index of the item
     */
    public void removeItem(int index) {
	items[index] = null;
    }

    public Item getItem(int index) {
	return items[index];
    }

    @Override
    public String toString() {
	return "Inventory with width " + width + " and height " + height;
    }
}
