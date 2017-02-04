package powerworks.moving;

import java.awt.Rectangle;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.data.PhysicalObject;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.graphics.Screen;
import powerworks.level.Level;
import powerworks.moving.droppeditem.DroppedItem;
import powerworks.moving.entity.Player;

public abstract class Moving implements PhysicalObject, Collidable {

    public static final int AIR_DRAG = 4;
    public static final int DEFAULT_MAX_SPEED = 20;
    protected int x, y;
    protected int velX, velY;
    protected Hitbox hitbox;
    protected boolean hasMoved = false;
    
    public Moving(Hitbox hitbox) {
	this.hitbox = hitbox;
	Collidable.collidables.put(this);
    }
    
    protected boolean getCollision(int moveX, int moveY) {
	for (Collidable col : Collidable.collidables.retrievePossible(this)) {
	    if (this != col && col.getHitbox().solid && col.toString() != "Void" && !(this instanceof Player && col instanceof DroppedItem)) {
		Rectangle otherRect = new Rectangle(col.getXPixel() + col.getHitbox().xStart, col.getYPixel() + col.getHitbox().yStart, col.getHitbox().width, col.getHitbox().height);
		Rectangle thisRect = new Rectangle(x + hitbox.xStart + moveX, y + hitbox.yStart + moveY, hitbox.width, hitbox.height);
		if (otherRect.intersects(thisRect))
		    return true;
	    }
	}
	return false;
    }
    
    public void update() {
	move();
    }
    
    public abstract void render();

    protected void move() {
	if (velX + x + hitbox.xStart + hitbox.width > Level.level.getWidthPixels())
	    x = -hitbox.xStart;
	if (velY + y + hitbox.yStart + hitbox.height > Level.level.getHeightPixels())
	    y = -hitbox.yStart;
	if (velX + x + hitbox.xStart < 0)
	    x = Level.level.getWidthPixels() - (hitbox.xStart + hitbox.width);
	if (velY + y + hitbox.yStart < 0)
	    y = Level.level.getHeightPixels() - (hitbox.yStart + hitbox.height);
	int pVelX = velX, pVelY = velY;
	if (velX != 0 || velY != 0) {
	    if (!getCollision(velX, velY)) {
		x += velX;
		y += velY;
		if (this instanceof Player)
		    EventManager.sendEvent(new ViewMoveEvent(x, y));
	    } else {
		if (!getCollision(velX, 0)) {
		    x += velX;
		    if (this instanceof Player)
			EventManager.sendEvent(new ViewMoveEvent(x, y));
		}
		if (!getCollision(0, velY)) {
		    y += velY;
		    if (this instanceof Player)
			EventManager.sendEvent(new ViewMoveEvent(x, y));
		}
	    }
	}
	if (pVelX != velX || pVelY != velY)
	    hasMoved = true;
	else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
    }

    @Override
    public int getWidthPixels() {
	return hitbox.width;
    }

    @Override
    public int getHeightPixels() {
	return hitbox.height;
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
	return x;
    }

    @Override
    public int getYPixel() {
	return y;
    }
    
    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }
    
    @Override
    public Hitbox getHitbox() {
	return hitbox;
    }
    
    @Override
    public boolean hasMoved() {
	return hasMoved;
    }
}
