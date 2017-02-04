package powerworks.block;

import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.level.Level;
import powerworks.main.Game;

public class Block implements TexturedObject, Collidable {

    int rotation;
    int x, y;
    BlockType type;
    boolean requiresUpdate = true;
    //test
    /**
     * Instantiates a Block object
     * 
     * @param type
     *            the BlockType to use
     * @param xTile
     *            the x tile
     * @param yTile
     *            the y tile
     */
    public Block(BlockType type, int xTile, int yTile) {
	this.x = xTile;
	this.y = yTile;
	this.type = type;
	requiresUpdate = type.defaultRequiresUpdate;
	if (type.hitbox.solid)
	    Collidable.collidables.put(this);
    }

    public void render() {
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes())
	    Screen.screen.renderHitbox(this);
    }

    public int getXTile() {
	return x;
    }

    public int getYTile() {
	return y;
    }

    public void update() {
    }

    public boolean isPlaceable() {
	return type.placeable;
    }

    @Override
    public Texture getTexture() {
	return type.texture;
    }

    public Texture getPlaceableTexture() {
	return type.placeableTexture;
    }

    public Texture getNotPlaceableTexture() {
	return type.notPlaceableTexture;
    }

    public static boolean spaceFor(BlockType type, int xTile, int yTile) {
	for (int y = 0; y < type.height; y++) {
	    for (int x = 0; x < type.width; x++) {
		if (Level.level.getBlockFromTile(x + xTile, y + yTile) != null || Level.level.getTileFromTile(x + xTile, y + yTile).isSolid()) {
		    return false;
		}
	    }
	}
	int xPixel = xTile << 4;
	int yPixel = yTile << 4;
	return !(Collidable.collidables.anyIn(xPixel, yPixel, type.getHitbox().width, type.getHitbox().height));
    }

    @Override
    public int getWidthPixels() {
	return type.getHitbox().width;
    }

    @Override
    public int getHeightPixels() {
	return type.getHitbox().height;
    }

    @Override
    public String toString() {
	return type.name;
    }

    @Override
    public Hitbox getHitbox() {
	return type.hitbox;
    }

    @Override
    public int getXPixel() {
	return x << 4;
    }

    @Override
    public int getYPixel() {
	return y << 4;
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }

    public boolean hasTransparency() {
	return type.texture.hasTransparency();
    }

    public boolean requiresUpdate() {
	return requiresUpdate;
    }

    @Override
    public double getScale() {
	return 1;
    }

    @Override
    public boolean hasMoved() {
	return false;
    }
}
