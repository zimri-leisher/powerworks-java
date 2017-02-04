package powerworks.moving.droppeditem;

import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.data.Quadtree;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.inventory.item.ItemType;
import powerworks.level.Level;
import powerworks.main.Game;
import powerworks.moving.Moving;

public class DroppedItem extends Moving implements TexturedObject {

    public static final Quadtree<DroppedItem> droppedItems = new Quadtree<DroppedItem>(0, 0, Level.level.getWidthPixels(), Level.level.getHeightPixels());
    
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
	this.x = xPixel;
	this.y = yPixel;
	Collidable.collidables.put(this);
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
	    } else {
		if (!getCollision(velX, 0)) {
		    x += velX;
		}
		if (!getCollision(0, velY)) {
		    y += velY;
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
    public int getWidthPixels() {
	return hitbox.width;
    }

    @Override
    public int getHeightPixels() {
	return hitbox.height;
    }

    @Override
    public Hitbox getHitbox() {
	return hitbox;
    }
}