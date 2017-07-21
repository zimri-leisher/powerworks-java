package powerworks.collidable.block.machine;

import java.util.Arrays;
import java.util.List;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.ItemType;

public class InputNode {

    /**
     * Relative to block
     */
    private int xTile, yTile;
    private List<ItemType> input;
    private Inventory dest;

    /**
     * @param input
     *            the acceptable inputs
     */
    InputNode(int xTile, int yTile, Inventory destination, ItemType... input) {
	this.xTile = xTile;
	this.yTile = yTile;
	this.input = Arrays.asList(input);
	this.dest = destination;
    }
    
    public int getRelativeXTile() {
	return xTile;
    }
    
    public int getRelativeYTile() {
	return yTile;
    }
    
    public List<ItemType> getAcceptableInputs() {
	return input;
    }
    
    public Inventory getDestination() {
	return dest;
    }

    /**
     * @param quantity
     *            the amount of items to output
     * @return true if an item was outputted, false otherwise
     */
    boolean input(ItemType type, int quantity) {
	if (!input.contains(type))
	    return false;
	return dest.giveItem(type, quantity);
    }
}
