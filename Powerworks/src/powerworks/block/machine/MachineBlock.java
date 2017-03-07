package powerworks.block.machine;

import powerworks.audio.Sound;
import powerworks.block.Block;
import powerworks.block.BlockType;

public abstract class MachineBlock extends Block{

    boolean on;
    Sound onSound;
    
    public MachineBlock(BlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }
    
}
