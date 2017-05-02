package powerworks.block.machine;

import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.PlaceBlockEvent;

public class ConveyorBeltBlock extends MachineBlock implements EventListener{

    public static final int CONVEYOR_BELT_ACCELERATION = 1;
    
    /**
     * 0 = no curve, 1 = curve right, 2 = curve left. Relative to direction
     */
    public int curve = 0;
    
    public ConveyorBeltBlock(MachineBlockType type, int xTile, int yTile) {
	super(type, xTile, yTile);
	EventManager.registerEventListener(this);
    }

    @EventHandler
    public void onPlaceBlockEvent(PlaceBlockEvent e) {
	if(Math.abs(e.xTile - (xPixel >> 4)) <= 1 && Math.abs(e.yTile - (yPixel >> 4)) <= 1) {
	    updateDirection();
	}
    }
    
    private void updateDirection() {
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
