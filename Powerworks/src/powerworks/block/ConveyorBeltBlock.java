package powerworks.block;

import powerworks.level.Level;
import powerworks.moving.entity.Entity;

public class ConveyorBeltBlock extends Block {

    public ConveyorBeltBlock(BlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }

    public static final int CONVEYOR_BELT_ACCELERATION = 1;


    public void updateDir() {
	if (Level.level.getBlockFromPixel(xPixel + 16, yPixel) != null && Level.level.getBlockFromPixel(xPixel + 16, yPixel).type.toString().contains("CONVEYOR_BELT")) {
	}
    }

    // TODO use CommandZones for this
    @Override
    public void update() {
	for (Entity entity : Entity.entities.getIntersecting(xPixel, yPixel, 16, 16)) {
	    System.out.println(entity);
	    //entity.addVel((moveDir == 3) ? -CONVEYOR_BELT_ACCELERATION : (moveDir == 1) ? CONVEYOR_BELT_ACCELERATION : 0,
		    //(moveDir == 2) ? CONVEYOR_BELT_ACCELERATION : (moveDir == 0) ? -CONVEYOR_BELT_ACCELERATION : 0);
	}
    }
}
