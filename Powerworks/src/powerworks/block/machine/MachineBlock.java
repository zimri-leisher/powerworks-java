package powerworks.block.machine;

import powerworks.block.Block;

public abstract class MachineBlock extends Block<MachineBlockType>{

    boolean on;
    
    public MachineBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }
}
