package powerworks.block;

import powerworks.level.Level;
import powerworks.moving.entity.Entity;

public class ConveyorBeltBlock extends Block {

    public static final int CONVEYOR_BELT_ACCELERATION = 1;
    private int moveDir;

    public ConveyorBeltBlock(BlockType type, int x, int y) {
	super(type, x, y);
	updateDir();
    }

    public void updateDir() {
	if (Level.level.getBlockFromTile(x + 1, y) != null && Level.level.getBlockFromTile(x + 1, y).type.toString().contains("CONVEYOR_BELT")) {
	}
    }

    // TODO use CommandZones for this
    @Override
    public void update() {
	int xPixel = x << 4;
	int yPixel = y << 4;
	for (Entity entity : Entity.entities.retrieveIn(xPixel, yPixel, 16, 16)) {
	    entity.addVel((moveDir == 3) ? -CONVEYOR_BELT_ACCELERATION : (moveDir == 1) ? CONVEYOR_BELT_ACCELERATION : 0,
		    (moveDir == 2) ? CONVEYOR_BELT_ACCELERATION : (moveDir == 0) ? -CONVEYOR_BELT_ACCELERATION : 0);
	}
    }
}
