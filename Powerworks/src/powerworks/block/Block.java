package powerworks.block;

import powerworks.audio.Sound;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.data.Timer;
import powerworks.graphics.Image;
import powerworks.graphics.SyncAnimation;
import powerworks.graphics.Texture;
import powerworks.io.ControlPressType;
import powerworks.io.MouseEvent;
import powerworks.main.Game;
import powerworks.task.Task;

public class Block<T extends BlockType> extends Collidable {

    protected T type;
    private boolean requiresUpdate = true, removing = false;
    private Timer removingTimer;
    private int rotation = 0;

    public Block(T type, int xTile, int yTile) {
	super(xTile << 4, yTile << 4, type.getTextureXPixelOffset(), type.getTextureYPixelOffset(), type.hitbox);
	this.type = type;
	requiresUpdate = type.defaultRequiresUpdate;
	if (type.hitbox.isSolid())
	    Game.getLevel().getCollidables().add(this);
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    public void render() {
	Game.getRenderEngine().renderLevelObject(this);
	if (Game.showHitboxes())
	    renderHitbox();
    }

    public void remove() {
	if (type.hitbox.isSolid())
	    Game.getLevel().getCollidables().remove(this);
	Block<?>[] blocks = Game.getLevel().getBlocks();
	for (int y = 0; y < type.heightTiles; y++) {
	    int ya = y + yPixel >> 4;
	    for (int x = 0; x < type.widthTiles; x++) {
		int xa = x + xPixel >> 4;
		blocks[xa + ya * Game.getLevel().getWidthTiles()] = null;
	    }
	}
    }

    public void setRotation(int rotation) {
	this.rotation = rotation;
    }

    public Sound getFootstepSound() {
	return type.footstep;
    }

    public int getWidthTiles() {
	return type.getWidthTiles();
    }

    public int getHeightTiles() {
	return type.getHeightTiles();
    }

    public int getXTile() {
	return xPixel >> 4;
    }

    public int getYTile() {
	return yPixel >> 4;
    }

    public T getType() {
	return type;
    }

    @Override
    public void onMouseActionOn(MouseEvent e) {
	ControlPressType p = e.getType();
	if (e.getButton() == 3) {
	    switch (p) {
		case PRESSED:
		    removing = true;
		    removingTimer = new Timer(108, 1, true);
		    removingTimer.runTaskOnFinish(new Task() {

			@Override
			public void run() {
			    remove();
			    SyncAnimation.CURSOR_RIGHT_CLICK.stop();
			    SyncAnimation.CURSOR_RIGHT_CLICK.reset();
			    Game.getMouse().setTexture(Image.CURSOR_DEFAULT);
			}
		    });
		    Game.getMouse().setTexture(SyncAnimation.CURSOR_RIGHT_CLICK);
		    SyncAnimation.CURSOR_RIGHT_CLICK.play();
		    break;
		case RELEASED:
		    removingTimer.resetTimes();
		    removingTimer.stop();
		    removing = false;
		    SyncAnimation.CURSOR_RIGHT_CLICK.stop();
		    SyncAnimation.CURSOR_RIGHT_CLICK.reset();
		    Game.getMouse().setTexture(Image.CURSOR_DEFAULT);
		    break;
	    }
	}
    }

    @Override
    public void onMouseLeave() {
	removing = false;
    }

    public void update() {
    }

    public boolean isPlaceable() {
	return type.placeable;
    }

    public boolean isSolid() {
	return type.hitbox.isSolid();
    }

    @Override
    public Texture getTexture() {
	return type.getTextures()[rotation];
    }

    @Override
    public String toString() {
	return type.name + " block at x tile " + (xPixel >> 4) + ", y tile " + (yPixel >> 4) + ", with rotation " + rotation;
    }

    @Override
    public Hitbox getHitbox() {
	return type.hitbox;
    }
    
    @Override
    public int getXPixel() {
	return xPixel;
    }

    @Override
    public int getYPixel() {
	return yPixel;
    }

    public boolean requiresUpdate() {
	return requiresUpdate;
    }
}
