package powerworks.inventory;

import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;

public class Inventory {

    Item[] items;
    int width;
    int height;

    public Inventory(Item[] items, int width, int height) {
	this.items = items;
	this.width = width;
	this.height = height;
    }

    public Inventory(int width, int height) {
	this.items = new Item[width * height];
	this.width = width;
	this.height = height;
    }

    /**
     * Sets an item in the inventory
     * 
     * @param index
     *            the index
     * @param type
     *            the type to set the index to
     */
    public void setItem(ItemType type, int index) {
	items[index] = new Item(type);
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

    /**
     * Sets an item in the inventory to the given type and quantity
     * 
     * @param type
     *            the type to set the index to
     * @param quantity
     *            the quantity of the item
     * @param index
     *            the index
     */
    public void setItem(ItemType type, int quantity, int index) {
	items[index] = new Item(type, quantity);
    }

    /**
     * Checks if the inventory has the item (looks at id)
     * 
     * @param item
     *            the item to check for both id and quantity
     * @return true if item id is present in inventory, false otherwise
     */
    public boolean hasItem(ItemType item) {
	return hasItem(item, 1);
    }

    /**
     * Checks if an inventory has the item in the specified quantity
     * 
     * @param item
     *            the item to check for
     * @param quantity
     *            the amount to check for (more will return true also)
     * @return true if present in the quantity specified or more, false otherwise
     */
    public boolean hasItem(ItemType item, int quantity) {
	for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == item.getID() && items[i].getQuantity() >= quantity)
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
     * @param type the type of the item
     * @param quantity the quantity to give
     */
    public void giveItem(ItemType type, int quantity) {
	int leftToAdd = quantity;
	loop: for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == type.getID() && items[i].getQuantity() != items[i].getMaxStack()) {
		if (items[i].getQuantity() + quantity > items[i].getMaxStack()) {
		    leftToAdd = items[i].getMaxStack() - items[i].getQuantity();
		    items[i].setQuantity(items[i].getMaxStack());;
		    break loop;
		}
		items[i].setQuantity(items[i].getQuantity() + leftToAdd);
		return;
	    }
	}
	for (int i = 0; i < items.length; i++) {
	    if (items[i] == null) {
		items[i] = new Item(type, quantity);
		return;
	    }
	}
    }

    /**
     * Takes an item from the inventory
     * 
     * @param item
     *            the item to remove
     */
    public void takeItem(Item item) {
	int leftToSubtract = item.getQuantity();
	for (int i = items.length - 1; i >= 0; i--) {
	    if (items[i] != null && items[i].getID() == item.getID()) {
		if (items[i].getQuantity() - leftToSubtract <= 0) {
		    leftToSubtract -= items[i].getQuantity();
		    items[i] = null;
		} else {
		    items[i].setQuantity(items[i].getQuantity() - leftToSubtract);;
		}
	    }
	}
    }

    /**
     * Takes an item from the inventory
     * 
     * @param type
     *            the type to remove
     * @param quantity
     *            the quantity to remove
     */
    public void takeItem(ItemType type, int quantity) {
	int leftToSubtract = quantity;
	for (int i = items.length - 1; i >= 0; i--) {
	    if (items[i] != null && items[i].getID() == type.getID() && items[i].getQuantity() - quantity <= 0) {
		leftToSubtract -= items[i].getQuantity();
		items[i] = null;
	    }
	}
	for (int i = items.length - 1; i >= 0; i--) {
	    if (items[i] != null && items[i].getID() == type.getID()) {
		if (items[i].getQuantity() - leftToSubtract > 0) {
		    items[i].setQuantity(items[i].getQuantity() - leftToSubtract);
		    return;
		}
	    }
	}
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
}
