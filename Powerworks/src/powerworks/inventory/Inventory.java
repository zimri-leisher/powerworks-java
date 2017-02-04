package powerworks.inventory;

import powerworks.graphics.gui.GUIPane;
import powerworks.inventory.item.Item;
import powerworks.inventory.item.ItemType;

public class Inventory {

    Item[] items;
    String name;
    int width;
    int height;
    GUIPane gui;

    public Inventory(Item[] items, String name, int width, int height) {
	this.items = items;
	this.name = name;
	this.width = width;
	this.height = height;
    }

    public Inventory(String name, int width, int height) {
	this.items = new Item[width * height];
	this.name = name;
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
     *            the amount to check for
     * @return true if present, false otherwise
     */
    public boolean hasItem(ItemType item, int quantity) {
	for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == item.getID() && items[i].quantity >= quantity)
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
	int leftToAdd = item.quantity;
	loop: for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == item.getID() && items[i].quantity != items[i].getMaxStack()) {
		if (items[i].quantity + item.quantity > items[i].getMaxStack()) {
		    leftToAdd = items[i].getMaxStack() - items[i].quantity;
		    items[i].quantity = items[i].getMaxStack();
		    break loop;
		}
		items[i].quantity += leftToAdd;
		return;
	    }
	}
	for (int i = 0; i < items.length; i++) {
	    if (items[i] == null) {
		items[i] = item;
		return;
	    }
	}
    }
    
    /**
     * Gives an Item object to the inventory
     * @param type the type of the item
     * @param quantity the quantity to give
     */
    public void giveItem(ItemType type, int quantity) {
	int leftToAdd = quantity;
	loop: for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == type.getID() && items[i].quantity != items[i].getMaxStack()) {
		if (items[i].quantity + quantity > items[i].getMaxStack()) {
		    leftToAdd = items[i].getMaxStack() - items[i].quantity;
		    items[i].quantity = items[i].getMaxStack();
		    break loop;
		}
		items[i].quantity += leftToAdd;
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
	int leftToSubtract = item.quantity;
	for (int i = 0; i < items.length; i++) {
	    if (items[i] != null && items[i].getID() == item.getID()) {
		if (items[i].quantity - leftToSubtract <= 0) {
		    leftToSubtract -= items[i].quantity;
		    items[i] = null;
		} else {
		    items[i].quantity -= leftToSubtract;
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
	    if (items[i] != null && items[i].getID() == type.getID() && items[i].quantity - quantity <= 0) {
		leftToSubtract -= items[i].quantity;
		items[i] = null;
	    }
	}
	for (int i = items.length - 1; i >= 0; i--) {
	    if (items[i] != null && items[i].getID() == type.getID()) {
		if (items[i].quantity - leftToSubtract > 0) {
		    items[i].quantity -= leftToSubtract;
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

    /**
     * Gets an item from an inventory
     * 
     * @param index
     *            the index of the item
     * @return the item
     */
    public Item getItem(int index) {
	return items[index];
    }
}
