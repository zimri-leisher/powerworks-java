package powerworks.block.machine;

import powerworks.block.Block;
import powerworks.block.BlockType;

public abstract class MachineBlock extends Block{

    // Cast to get on sound
    boolean on;
    
    public MachineBlock(BlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }
}
