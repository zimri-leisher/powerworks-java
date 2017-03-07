package powerworks.moving.droppeditem;

import powerworks.collidable.Hitbox;
import powerworks.data.SpatialOrganizer;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.inventory.item.ItemType;
import powerworks.level.Level;
import powerworks.main.Game;
import powerworks.moving.Moving;

public class DroppedItem extends Moving implements TexturedObject {

    public static final SpatialOrganizer<DroppedItem> droppedItems = new SpatialOrganizer<DroppedItem>();
    
    ItemType type;
    Hitbox hitbox;

    /**
     * Instantiates a DroppedItem object
     * 
     * @param type
     *            the ItemType to use
     * @param xPixel
     *            the x pixel
     * @param yPixel
     *            the y pixel
     */
    public DroppedItem(ItemType type, int xPixel, int yPixel) {
	super(type.getDroppedHitbox());
	this.type = type;
	this.xPixel = xPixel;
	this.yPixel = yPixel;
	droppedItems.add(this);
    }
    
    @Override
    public void render() {
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes())
	    renderHitbox();
    }

    @Override
    public Texture getTexture() {
	return type.getTexture();
    }

    public ItemType getType() {
	return type;
    }

    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }

    @Override
    public String toString() {
	return type.getName() + " (Item)";
    }

    @Override
    protected void move() {
	if (velX + xPixel + hitbox.xStart + hitbox.width > Level.level.getWidthPixels())
	    xPixel = -hitbox.xStart;
	if (velY + yPixel + hitbox.yStart + hitbox.height > Level.level.getHeightPixels())
	    yPixel = -hitbox.yStart;
	if (velX + xPixel + hitbox.xStart < 0)
	    xPixel = Level.level.getWidthPixels() - (hitbox.xStart + hitbox.width);
	if (velY + yPixel + hitbox.yStart < 0)
	    yPixel = Level.level.getHeightPixels() - (hitbox.yStart + hitbox.height);
	int pVelX = velX, pVelY = velY;
	if (velX != 0 || velY != 0) {
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
	}
	if (pVelX != velX || pVelY != velY)
	    hasMoved = true;
	else
	    hasMoved = false;
	velX /= AIR_DRAG;
	velY /= AIR_DRAG;
    }

    @Override
    public int getRotation() {
	return 0;
    }

    @Override
    public double getScale() {
	return 1;
    }

    @Override
    public Hitbox getHitbox() {
	return hitbox;
    }
}