package powerworks.collidable.moving.living;

import powerworks.block.Block;
import powerworks.block.machine.ConveyorBeltBlock;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.io.InputManager;
import powerworks.main.Game;
import powerworks.world.level.GhostBlock;

public class Player2 extends Living{

    private int lastMouseXPixel, lastMouseYPixel;
    private GhostBlock block = new GhostBlock(null, 0, 0, false, 0);
    
    protected Player2() {
	super(Game.getLevel().getWidthPixels() / 2, Game.getLevel().getHeightPixels() / 2, Hitbox.PLAYER, new Inventory(8, 4), "Player", Image.PLAYER_INVENTORY);
    }
    
    @Override
    public void update() {
	long time = 0;
	Block b = Game.getLevel().getBlockFromPixel(xPixel + hitbox.getXStart() + hitbox.getWidthPixels() / 2, yPixel + hitbox.getYStart() + hitbox.getHeightPixels());
	if (b instanceof ConveyorBeltBlock) {
	    int xVel = (b.getRotation() == 1) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 3) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    int yVel = (b.getRotation() == 0) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 2) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    addVel(xVel, yVel);
	}
	if (velX != 0 || velY != 0)
	    move();
	int mouseXPixel = InputManager.getMouseLevelXPixel();
	int mouseYPixel = InputManager.getMouseLevelYPixel();
	if (Game.showRenderTimes())
	    time = System.nanoTime();
	Item item = getHeldItem();
	if (item != null && getHeldItem().isPlaceable()) {
	    if (mouseXPixel != lastMouseXPixel || mouseYPixel != lastMouseYPixel) {
		int xTile = mouseXPixel >> 4;
		int yTile = mouseYPixel >> 4;
		if (Game.getLevel().getBlockFromTile(xTile, yTile) == null) {
		    if (item.getType().getPlacedBlock().getHitbox().isSolid()) {
			if (Game.getLevel().spaceForBlock(item.getType().getPlacedBlock(), xTile, yTile))
			    block.setPlaceable(true);
			else
			    block.setPlaceable(false);
		    } else
			block.setPlaceable(true);
		} else
		    block.setPlaceable(false);
		block.setLoc(xTile << 4, yTile << 4);
		lastMouseXPixel = mouseXPixel;
		lastMouseYPixel = mouseYPixel;
	    }
	    block.setType(item.getPlacedBlock());
	    block.setRender(true);
	} else {
	    block.setRender(false);
	}
    }
    
    public Item getHeldItem() {
	return Game.getHUD().getHotbar().getSelectedItem();
    }

    @Override
    protected void move() {
	if (velX > 0 && dir != 1)
	    dir = 1;
	if (velX < 0 && dir != 3)
	    dir = 3;
	if (velY > 0 && dir != 2)
	    dir = 2;
	if (velY < 0 && dir != 0)
	    dir = 0;
	if (velX + xPixel + hitbox.getXStart() + hitbox.getWidthPixels() > Game.getLevel().getWidthPixels())
	    xPixel = -hitbox.getXStart();
	if (velY + yPixel + hitbox.getYStart() + hitbox.getHeightPixels() > Game.getLevel().getHeightPixels())
	    yPixel = -hitbox.getYStart();
	if (velX + xPixel + hitbox.getXStart() < 0)
	    xPixel = Game.getLevel().getWidthPixels() - (hitbox.getXStart() + hitbox.getWidthPixels());
	if (velY + yPixel + hitbox.getYStart() < 0)
	    yPixel = Game.getLevel().getHeightPixels() - (hitbox.getYStart() + hitbox.getHeightPixels());
	int pXPixel = xPixel, pYPixel = yPixel;
	if (!getCollision(velX, velY)) {
	    xPixel += velX;
	    yPixel += velY;
	} else {
	    if (!getCollision(velX, 0)) {
		xPixel += velX;
	    }
	    if (!getCollision(0, velY)) {
		yPixel += velY;
	    }
	}
	if (pXPixel != xPixel || pYPixel != yPixel)
	    hasMoved = true;
	else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
    }
    
    @Override
    public void render() {
	
    }

    @Override
    public void remove() {
	
    }
}
