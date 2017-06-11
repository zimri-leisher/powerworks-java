package powerworks.world.level;

import powerworks.block.BlockType;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.main.Game;

public class GhostBlock extends LevelObject {

    private BlockType type;
    private boolean placeable;
    private boolean render;
    private int rotation;

    public GhostBlock(BlockType type, int xTile, int yTile, boolean placeable, int rotation) {
	super(xTile << 4, yTile << 4);
	this.rotation = rotation;
	this.type = type;
	this.placeable = placeable;
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    public void setLoc(int xPixel, int yPixel) {
	this.xPixel = xPixel;
	this.yPixel = yPixel;
    }

    public void setRender(boolean render) {
	this.render = render;
    }

    public void setType(BlockType type) {
	this.type = type;
    }

    public void setPlaceable(boolean placeable) {
	this.placeable = placeable;
    }

    public void setRotation(int rotation) {
	this.rotation = rotation;
    }

    public boolean isPlaceable() {
	return placeable;
    }

    public BlockType getType() {
	return type;
    }

    @Override
    public void update() {
    }

    @Override
    public int getRotation() {
	return rotation;
    }

    @Override
    public void render() {
	if (render) {
	    Game.getRenderEngine().renderTexture(type.getTextures()[rotation], xPixel + type.getTextureXPixelOffset(), yPixel + type.getTextureYPixelOffset(), 1, 1, 1, rotation, 0.5f, false);
	    Game.getRenderEngine().renderTexture(Image.ARROW, xPixel + type.getTextureXPixelOffset(), yPixel + type.getTextureYPixelOffset(), 1, 1, 1, rotation, 0.5f, false);
	    if (placeable) {
		float scaleWidth = (float) (type.getWidthTiles() << 4) / (float) Image.BLOCK_PLACEABLE.getWidthPixels();
		float scaleHeight = (float) (type.getHeightTiles() << 4) / (float) Image.BLOCK_PLACEABLE.getHeightPixels();
		Game.getRenderEngine().renderTexture(Image.BLOCK_PLACEABLE, xPixel, yPixel, 1, scaleWidth, scaleHeight, 0, 1f, false);
	    } else {
		float scaleWidth = (float) (type.getWidthTiles() << 4) / (float) Image.BLOCK_NOT_PLACEABLE.getWidthPixels();
		float scaleHeight = (float) (type.getHeightTiles() << 4) / (float) Image.BLOCK_NOT_PLACEABLE.getHeightPixels();
		Game.getRenderEngine().renderTexture(Image.BLOCK_NOT_PLACEABLE, xPixel, yPixel, 1, scaleWidth, scaleHeight, 0, 1f, false);
	    }
	}
    }

    @Override
    public void remove() {
    }
}
