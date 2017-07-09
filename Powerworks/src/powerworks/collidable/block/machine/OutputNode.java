package powerworks.collidable.block.machine;

import java.util.Arrays;
import java.util.List;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;

public class OutputNode {
    /**
     * Relative to block
     */
    private int xTile, yTile;
    private List<ItemType> output;
    
    OutputNode(int xTile, int yTile, ItemType...output) {
	this.xTile = xTile;
	this.yTile = yTile;
	this.output = Arrays.asList(output);
    }
    
    /**
     * @return true if an item was output, false otherwise
     */
    boolean output(ItemType type) {
	if(!output.contains(type))
	    return false;
	return Game.getLevel().tryDropItem(type, xTile << 4, yTile << 4);
    }
}
