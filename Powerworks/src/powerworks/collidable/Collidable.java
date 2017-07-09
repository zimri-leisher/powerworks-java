package powerworks.collidable;

import powerworks.io.MouseEvent;
import powerworks.main.Game;
import powerworks.world.level.Chunk;
import powerworks.world.level.LevelObject;

public abstract class Collidable extends LevelObject {

    protected Hitbox hitbox;
    protected boolean mouseOn = false;
    protected Chunk currentChunk;

    protected Collidable(int xPixel, int yPixel, Hitbox hitbox) {
	this(xPixel, yPixel, 0, 0, hitbox);
    }

    protected Collidable(int xPixel, int yPixel, int texXPixelOffset, int texYPixelOffset, Hitbox hitbox) {
	super(xPixel, yPixel, texXPixelOffset, texYPixelOffset);
	this.hitbox = hitbox;
	currentChunk = Game.getLevel().getAndLoadChunkAtPixel(xPixel, yPixel);
    }

    public void addToLevel() {
	if (hitbox.isSolid())
	    if(!currentChunk.getCollidables().add(this))
		Game.getLevel().addToCollidablesOnChunkBoundary(this);
    }

    public Chunk getCurrentChunk() {
	return currentChunk;
    }

    public boolean isMouseOn() {
	return mouseOn;
    }

    /**
     * For use by the level manager
     */
    public void setMouseOn(boolean mouseOn) {
	this.mouseOn = mouseOn;
	if (mouseOn)
	    onMouseEnter();
	else
	    onMouseLeave();
    }

    public Hitbox getHitbox() {
	return hitbox;
    }

    public void onMouseEnter() {
    }

    public void onMouseLeave() {
    }

    public void onMouseActionOn(MouseEvent e) {
    }

    public void onScrollOn(int scroll) {
    }

    @Override
    public void render() {
	super.render();
	if (Game.showHitboxes())
	    renderHitbox();
    }

    public void renderHitbox() {
	renderHitbox(0xFF0C00);
    }

    public void renderHitbox(int color) {
	Game.getRenderEngine().renderSquare(color, xPixel + hitbox.getXStart(), yPixel + hitbox.getYStart(), hitbox.getWidthPixels(), hitbox.getHeightPixels());
    }

    public void removeFromLevel() {
	if (hitbox.isSolid())
	    currentChunk.getCollidables().remove(this);
    }

    @Override
    public void remove() {
	removeFromLevel();
	hitbox = null;
    }

    @Override
    public String toString() {
	return "Collidable at " + xPixel + ", " + yPixel;
    }
}
