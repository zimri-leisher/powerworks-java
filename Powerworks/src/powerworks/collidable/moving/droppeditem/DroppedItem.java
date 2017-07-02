package powerworks.collidable.moving.droppeditem;

import powerworks.collidable.Collidable;
import powerworks.collidable.moving.Moving;
import powerworks.collidable.moving.living.Player;
import powerworks.graphics.ImageCollection;
import powerworks.graphics.Texture;
import powerworks.inventory.item.ItemType;
import powerworks.main.Game;

public class DroppedItem extends Moving {

    ItemType type;

    public DroppedItem(ItemType type, int xPixel, int yPixel) {
	super(xPixel, yPixel, type.getDroppedHitbox(), ImageCollection.createCollection(type.getTexture()));
	this.type = type;
	Game.getLevel().getDroppedItems().add(this);
    }

    @Override
    public Texture getTexture() {
	return type.getTexture();
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderLevelObject(this);
	if(Game.showHitboxes())
	    renderHitbox();
    }
    
    @Override
    protected boolean getCollision(int moveX, int moveY) {
	for(Collidable col : Game.getLevel().getCollidables().getIntersecting(xPixel + hitbox.getXStart() + moveX, yPixel + hitbox.getYStart() + moveY, hitbox.getWidthPixels(), hitbox.getHeightPixels())) {
	    if(!(col instanceof Player) && col != this)
		return true;
	}
	return false;
    }

    @Override
    public void remove() {
	super.remove();
	type = null;
	Game.getLevel().getDroppedItems().remove(this);
    }
    
    @Override
    public String toString() {
	return type.toString() + " dropped item at " + xPixel + ", " + yPixel;
    }
    
}
