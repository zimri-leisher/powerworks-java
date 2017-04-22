package powerworks.collidable;

import powerworks.main.Game;
import powerworks.world.level.LevelObject;

public abstract class Collidable extends LevelObject {

    protected Hitbox hitbox;
    
    protected Collidable(int xPixel, int yPixel, Hitbox hitbox) {
	this(xPixel, yPixel, 0, 0, hitbox);
    }
    
    protected Collidable(int xPixel, int yPixel, int texXPixelOffset, int texYPixelOffset, Hitbox hitbox) {
	super(xPixel, yPixel, texXPixelOffset, texYPixelOffset);
	this.hitbox = hitbox;
	if(hitbox.isSolid())
	    Game.getLevel().getCollidables().add(this);
    }

    /**
     * @return the hitbox for collision
     */
    public Hitbox getHitbox() {
	return hitbox;
    }

    /**
     * Renders the hitbox
     */
    public void renderHitbox() {
	Game.getRenderEngine().renderSquare(0xFF0C00, xPixel + hitbox.getXStart(), yPixel + hitbox.getYStart(), hitbox.getWidthPixels(), hitbox.getHeightPixels());
    }
}
