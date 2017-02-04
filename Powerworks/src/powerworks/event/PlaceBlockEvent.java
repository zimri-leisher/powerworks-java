package powerworks.event;

import powerworks.block.Block;

public class PlaceBlockEvent extends Event{
    public int xTile, yTile;
    public Block block;
    
    public PlaceBlockEvent(Block block, int xTile, int yTile) {
	this.block = block;
	this.xTile = xTile;
	this.yTile = yTile;
    }
}