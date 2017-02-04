package powerworks.moving.entity;

import powerworks.block.BlockType;
import powerworks.graphics.Screen;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;

public class GhostBlock implements TexturedObject{
    
    BlockType type;
    int xTile, yTile;
    boolean placeable;
    int rotation;
    
    public GhostBlock(BlockType type, int xTile, int yTile, boolean placeable, int rotation) {
	this.type = type;
	this.xTile = xTile;
	this.yTile = yTile;
	this.placeable = placeable;
	this.rotation = rotation;
    }
    
    public void render() {
	Screen.screen.renderTexturedObject(this);
    }
    
    @Override
    public Texture getTexture() {
	return (placeable) ? type.getPlaceableTexture() : type.getNotPlaceableTexture();
    }

    @Override
    public int getWidthPixels() {
	return getTexture().getWidthPixels();
    }

    @Override
    public int getHeightPixels() {
	return getTexture().getHeightPixels();
    }

    @Override
    public int getYPixel() {
	return 0;
    }

    @Override
    public int getXPixel() {
	return 0;
    }

    @Override
    public boolean hasMoved() {
	return false;
    }

    @Override
    public int getRotation() {
	return 0;
    }

    @Override
    public double getScale() {
	return 1;
    }
}
