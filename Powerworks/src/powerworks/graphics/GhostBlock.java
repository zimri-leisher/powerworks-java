package powerworks.graphics;

import powerworks.block.BlockType;

public class GhostBlock implements TexturedObject {

    public BlockType type;
    public int xTile, yTile;
    public boolean placeable;
    public boolean render;
    public int rotation;

    public GhostBlock(BlockType type, int xTile, int yTile, boolean placeable, int rotation) {
	this.type = type;
	this.xTile = xTile;
	this.yTile = yTile;
	this.placeable = placeable;
	this.rotation = rotation;
    }

    public void render() {
	if (render) {
	    Screen.screen.renderTexturedObject(this);
	}
    }

    @Override
    public Texture getTexture() {
	return type.getTexture();
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
	return yTile << 4;
    }

    @Override
    public int getXPixel() {
	return xTile << 4;
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
