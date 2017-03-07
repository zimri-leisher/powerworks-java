package powerworks.block.machine;

import powerworks.audio.AudioManager;
import powerworks.audio.Sound;
import powerworks.block.Block;
import powerworks.block.BlockType;
import powerworks.level.Level;
import powerworks.moving.entity.Entity;

public class ConveyorBeltBlock extends MachineBlock {

    public static final int CONVEYOR_BELT_ACCELERATION = 1;
    
    public ConveyorBeltBlock(BlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }

    @Override
    public void update() {
	AudioManager.playSound(Sound.GRASS_FOOTSTEP, xPixel, yPixel, 1);
	for (Entity entity : Entity.entities.getIntersecting(xPixel, yPixel, 16, 16)) {
	    entity.addVel(0, -CONVEYOR_BELT_ACCELERATION);
	}
    }
}
