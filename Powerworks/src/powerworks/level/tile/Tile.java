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

    public Tile(TileType type, int xTile, int yTile, int rotation) {
	this.type = type;
	this.xPixel = xTile << 4;
	this.yPixel = yTile << 4;
	texture = type.getTexture();
	this.rotation = rotation;
	if (type.hitbox.solid)
	    Collidable.collidables.add(this);
    }

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
	    Collidable.collidables.add(this);
    }

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

    @Override
    public String toString() {
	return type.name;
    }

    public void render() {
	Screen.screen.renderTexturedObject(this);
	if (Game.game.showHitboxes() && type.hitbox.solid)
	    renderHitbox();
    }

    public boolean isBreakable() {
	return type.breakable;
    }

    public boolean isSolid() {
	return type.hitbox.solid;
    }

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
	    Collidable.collidables.add(this);
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
}
