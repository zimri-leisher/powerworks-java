package powerworks.collidable.moving.droppeditem;

import powerworks.collidable.Hitbox;
import powerworks.collidable.moving.Moving;
import powerworks.collidable.moving.living.Player;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;

public class DroppedItem extends Moving {

    ItemType type;

    public DroppedItem(ItemType type, int xPixel, int yPixel) {
	super(xPixel, yPixel, Hitbox.DROPPED_ITEM, ImageCollection.createCollection(type.getTexture()));
	this.type = type;
    }
    
    @Override
    public void addToLevel() {
	super.addToLevel();
	currentChunk.getDroppedItems().add(this);
    }
    
    public ItemType getType() {
	return type;
    }

    @Override
    public Texture getTexture() {
	return type.getTexture();
    }
    
    public float getScale() {
	return 0.5f;
    }
    
    @Override
    public void renderHitbox() {
	super.renderHitbox(0x00474F);
    }

    @Override
    protected boolean getCollision(int moveX, int moveY) {
	return Game.getLevel().anyCollidableIntersecting(hitbox, moveX + xPixel, moveY + yPixel, c -> c != this && !(c instanceof Player));
    }

    @Override
    public void remove() {
	super.remove();
	type = null;
	currentChunk.getDroppedItems().remove(this);
    }
    
    @Override
    public String toString() {
	return type.toString() + " dropped item at " + xPixel + ", " + yPixel;
    }
    
}
