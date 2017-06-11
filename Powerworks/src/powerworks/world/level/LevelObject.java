package powerworks.world.level;

import powerworks.graphics.Texture;

public abstract class LevelObject {

    /**
     * Relative to level
     */
    protected int xPixel, yPixel;
    
    protected int texXPixelOffset, texYPixelOffset;

    protected LevelObject(int xPixel, int yPixel, int texXPixelOffset, int texYPixelOffset) {
	this.xPixel = xPixel;
	this.yPixel = yPixel;
	this.texXPixelOffset = texXPixelOffset;
	this.texYPixelOffset = texYPixelOffset;
    }

    
    protected LevelObject(int xPixel, int yPixel) {
	this(xPixel, yPixel, 0, 0);
    }
    
    public int getTextureXPixelOffset() {
	return texXPixelOffset;
    }
    
    public int getTextureYPixelOffset() {
	return texYPixelOffset;
    }
    
    /**
     * Relative to level
     */
    public int getXPixel() {
	return xPixel;
    }

    /**
     * Relative to level
     */
    public int getYPixel() {
	return yPixel;
    }

    /**
     * Should be overriden if you want to modify
     * @return the rotation of the texture in degrees
     */
    public int getRotation() {
	return 0;
    }

    /**
     * Should be overriden if you want to modify
     * @return scale of both x and y for the texture
     */
    public float getScale() {
	return 1;
    }

    /**
     * Should be overriden if you want to modify
     * @return scale of x for the texture
     */
    public float getWidthScale() {
	return 1;
    }

    /**
     * Should be overriden if you want to modify
     * @return scale of y for the texture
     */
    public float getHeightScale() {
	return 1;
    }

    /**
     * @return the texture to render
     */
    public abstract Texture getTexture();

    /**
     * Should immediately render, using the Renderer's methods (from <tt>Game.getRenderEngine.renderLevelObject</tt>). Also, should take into account texture offsets
     */
    public abstract void render();

    public abstract void update();

    /**
     * Removes from level, frees resources, basically totally deletes from existance
     */
    public abstract void remove();
}
