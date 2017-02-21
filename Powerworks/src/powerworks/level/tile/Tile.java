package powerworks.level.tile;

import java.util.Random;
import powerworks.collidable.Collidable;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.main.Game;

public class Tile implements Collidable, TexturedObject{
    
    public int xPixel, yPixel;
    private Texture texture;
    public TileType type;
    private int rotation;

    /**
     * Instantiates a new Tile object
     * @param type the TileType
     * @param xTile the x tile
     * @param yTile the y tile
     * @param rotation the rotation of the tile
     */
    public Tile(TileType type, int xTile, int yTile, int rotation) {
	this.type = type;
	this.xPixel = xTile << 4;
	this.yPixel = yTile << 4;
	texture = type.getTexture();
	this.rotation = rotation;
	if (type.hitbox.solid)
	    Collidable.collidables.put(this);
    }

    /**
     * Instantiates a new Tile object
     * @param type the TileType
     * @param xTile the x tile
     * @param yTile the y tile
     */
    public Tile(TileType type, int xTile, int yTile) {
	this.type = type;
	this.xPixel = xTile << 4;
	this.yPixel = yTile << 4;
	texture = type.getTexture();
	if (type.rotateRandomly) {
	    Random rand = new Random();
	    rotation = rand.nextInt(4);
	} else {
	    rotation = 0;
	}
	if (type.hitbox.solid)
	    Collidable.collidables.put(this);
    }

    /**
     * Gets the texture
     * 
     * @return the texture
     */
    @Override
    public Texture getTexture() {
	return texture;
    }

    /**
     * Gets the rotation
     * 
     * @return the rotation (0 = 0 degrees, 1 = 90 degrees, etc)
     */
    @Override
    public int getRotation() {
	return rotation;
    }

    /**
     * Converts this to a string
     * 
     * @return the name of the material
     */
    @Override
    public String toString() {
	return type.name;
    }

    /**
     * Renders this
     * 
     * @param screen
     *            the screen to render it on
     */
    public void render() {
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes() && type.hitbox.solid)
	    renderHitbox();
    }

    /**
     * Checks if this can be broken
     * 
     * @return true if breakable, false otherwise
     */
    public boolean isBreakable() {
	return type.breakable;
    }

    /**
     * Checks if this is solid
     * 
     * @return true if solid, false otherwise
     */
    public boolean isSolid() {
	return type.hitbox.solid;
    }

    /**
     * Sets the material of this
     * 
     * @param type
     *            the material to set it to
     * @return
     */
    public void setType(TileType type) {
	this.type = type;
	if (type.rotateRandomly) {
	    Random rand = new Random();
	    rotation = rand.nextInt(4);
	} else {
	    rotation = 0;
	}
	texture = type.getTexture();
	if (type.hitbox.solid)
	    Collidable.collidables.put(this);
	else 
	    Collidable.collidables.remove(this);
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

    @Override
    public void renderHitbox() {
	Screen.screen.renderHitbox(this);
    }

    @Override
    public double getScale() {
	return 1;
    }

    @Override
    public int getWidthPixels() {
	return 16;
    }

    @Override
    public int getHeightPixels() {
	return 16;
    }
}
