package powerworks.block.machine;

public class ConveyorBeltBlock extends MachineBlock {

    public static final int CONVEYOR_BELT_ACCELERATION = 1;
    
    public ConveyorBeltBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
    }

    @Override
    public void update() {
	//for (Moving moving : Moving.movingEntities.getIntersecting(xPixel, yPixel, 16, 16)) {
	    //int xVel = (rotation == 1) ? CONVEYOR_BELT_ACCELERATION : (rotation == 3) ? -CONVEYOR_BELT_ACCELERATION : 0;
	    //int yVel = (rotation == 0) ? -CONVEYOR_BELT_ACCELERATION : (rotation == 2) ? CONVEYOR_BELT_ACCELERATION : 0;
	    //moving.addVel(xVel, yVel);
	//}
    }
}
