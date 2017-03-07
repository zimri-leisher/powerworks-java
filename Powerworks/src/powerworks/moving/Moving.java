package powerworks.moving;

import powerworks.audio.AudioManager;
import powerworks.block.Block;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.data.PhysicalObject;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.graphics.Screen;
import powerworks.level.Level;
import powerworks.moving.entity.Player;

public abstract class Moving implements PhysicalObject, Collidable {

    public static final int AIR_DRAG = 4;
    public static final int DEFAULT_MAX_SPEED = 20;
    protected int xPixel, yPixel, lastX, lastY;
    protected int velX, velY;
    protected Hitbox hitbox;
    private int footstepDistance = 0;
    protected boolean hasMoved = false;

    public Moving(Hitbox hitbox) {
	this.hitbox = hitbox;
	if (hitbox.solid)
	    Collidable.collidables.add(this);
    }

    protected boolean getCollision(int moveX, int moveY) {
	for (Collidable col : Collidable.collidables.getIntersecting(xPixel + moveX + hitbox.xStart, yPixel + moveY + hitbox.yStart, hitbox.width, hitbox.height)) {
	    if (col != this)
		return true;
	}
	return false;
    }

    public void update() {
	move();
    }

    public abstract void render();

    protected void move() {
	if (velX + xPixel + hitbox.xStart + hitbox.width > Level.level.getWidthPixels())
	    xPixel = -hitbox.xStart;
	if (velY + yPixel + hitbox.yStart + hitbox.height > Level.level.getHeightPixels())
	    yPixel = -hitbox.yStart;
	if (velX + xPixel + hitbox.xStart < 0)
	    xPixel = Level.level.getWidthPixels() - (hitbox.xStart + hitbox.width);
	if (velY + yPixel + hitbox.yStart < 0)
	    yPixel = Level.level.getHeightPixels() - (hitbox.yStart + hitbox.height);
	int pXPixel = xPixel, pYPixel = yPixel;
	if (velX != 0 || velY != 0) {
	    if (!getCollision(velX, velY)) {
		xPixel += velX;
		yPixel += velY;
		if (this instanceof Player)
		    EventManager.sendEvent(new ViewMoveEvent(xPixel, yPixel));
	    } else {
		if (!getCollision(velX, 0)) {
		    xPixel += velX;
		    if (this instanceof Player)
			EventManager.sendEvent(new ViewMoveEvent(xPixel, yPixel));
		}
		if (!getCollision(0, velY)) {
		    yPixel += velY;
		    if (this instanceof Player)
			EventManager.sendEvent(new ViewMoveEvent(xPixel, yPixel));
		}
	    }
	}
	if (pXPixel != xPixel || pYPixel != yPixel) {
	    hasMoved = true;
	    footstepDistance += Math.sqrt(Math.pow(xPixel - pXPixel, 2) + Math.pow(yPixel - pYPixel, 2));
	    if (footstepDistance > 20) {
		Block b = Level.level.getBlockFromPixel(xPixel, yPixel);
		if (b != null) {
		    AudioManager.playSound(b.getFootstepSound(), xPixel, yPixel, 1.0);
		} else
		    AudioManager.playSound(Level.level.getTileFromPixel(xPixel, yPixel).getFootstepSound(), xPixel, yPixel, 1.0);
		footstepDistance = 0;
	    }
	} else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
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
    public int getXPixel() {
	return xPixel;
    }

    @Override
    public int getYPixel() {
	return yPixel;
    }

    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }

    @Override
    public Hitbox getHitbox() {
	return hitbox;
    }
}
