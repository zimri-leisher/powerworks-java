package powerworks.collidable.moving;

import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.collidable.block.Block;
import powerworks.collidable.block.machine.ConveyorBeltBlock;
import powerworks.collidable.moving.living.Player;
import powerworks.data.SpatialOrganizer;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;
import powerworks.main.Game;
import powerworks.world.level.Chunk;

public abstract class Moving extends Collidable {

    public static final int AIR_DRAG = 4;
    public static final int DEFAULT_MAX_SPEED = 20;
    protected int lastX, lastY;
    protected int velX, velY;
    protected boolean hasMoved = false;
    protected ImageCollection textures;
    protected int dir = 0;

    public Moving(int xPixel, int yPixel, Hitbox hitbox, ImageCollection textures) {
	super(xPixel, yPixel, hitbox);
	this.textures = textures;
    }
    
    @Override
    public void addToLevel() {
	super.addToLevel();
	System.out.println(currentChunk);
	currentChunk.getMovingEntities().add(this);
    }

    protected boolean getCollision(int moveX, int moveY) {
	return Game.getLevel().anyCollidableIntersecting(hitbox, moveX + xPixel, moveY + yPixel, c -> c != this);
    }

    @Override
    public void update() {
	Block b = Game.getLevel().getBlockFromPixel(xPixel + hitbox.getXStart() + hitbox.getWidthPixels() / 2, yPixel + hitbox.getYStart() + hitbox.getHeightPixels() / 2);
	if (b instanceof ConveyorBeltBlock) {
	    int xVel = (b.getRotation() == 1) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 3) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    int yVel = (b.getRotation() == 0) ? -ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : (b.getRotation() == 2) ? ConveyorBeltBlock.CONVEYOR_BELT_ACCELERATION : 0;
	    addVel(xVel, yVel);
	}
	if (velX != 0 || velY != 0)
	    move();
    }

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
	if (pXPixel != xPixel || pYPixel != yPixel) {
	    hasMoved = true;
	    onMove(pXPixel, pYPixel);
	} else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
    }
    
    protected void onMove(int pXPixel, int pYPixel) {
	currentChunk = Game.getLevel().updateChunk(this);
    }
    
    @Override
    public Texture getTexture() {
	return textures.getTexture(dir);
    }

    public int getVelX() {
	return velX;
    }

    public int getVelY() {
	return velY;
    }

    public void setVelX(int velX) {
	this.velX = velX;
    }

    public void setVelY(int velY) {
	this.velY = velY;
    }

    public void addVel(int velX, int velY) {
	if (velX != 0) {
	    int newVelX = velX + this.velX;
	    if (newVelX < 0) {
		if (-newVelX >= DEFAULT_MAX_SPEED)
		    this.velX = -DEFAULT_MAX_SPEED;
		else
		    this.velX = newVelX;
	    } else {
		if (newVelX >= DEFAULT_MAX_SPEED)
		    this.velX = DEFAULT_MAX_SPEED;
		else
		    this.velX = newVelX;
	    }
	}
	if (velY != 0) {
	    int newVelY = velY + this.velY;
	    if (newVelY < 0) {
		if (-newVelY >= DEFAULT_MAX_SPEED)
		    this.velY = -DEFAULT_MAX_SPEED;
		else
		    this.velY = newVelY;
	    } else {
		if (newVelY >= DEFAULT_MAX_SPEED)
		    this.velY = DEFAULT_MAX_SPEED;
		else
		    this.velY = newVelY;
	    }
	}
    }

    @Override
    public void remove() {
	super.remove();
	textures = null;
	currentChunk.getMovingEntities().remove(this);
    }

    @Override
    public String toString() {
	return "Moving object at " + xPixel + ", " + yPixel + ", with x velocity " + velX + " and y velocity " + velY + ", direction of " + dir;
    }
}
