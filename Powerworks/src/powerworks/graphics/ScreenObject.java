package powerworks.graphics;

import java.util.ArrayList;
import java.util.List;
import powerworks.main.Game;

public abstract class ScreenObject {
    
    /**
     * Relative to screen
     */
    protected int xPixel, yPixel;
    
    protected ScreenObject(int xPixel, int yPixel) {
	this.xPixel = xPixel;
	this.yPixel = yPixel;
	Game.getRenderEngine().getScreenObjects().add(this);
    }
    
    public int getXPixel() {
	return xPixel;
    }
    
    public int getYPixel() {
	return yPixel;
    }
    
    /**
     * Should be overridden if you want to modify
     */
    public float getScale() {
	return 1;
    }
    
    /**
     * Should be overridden if you want to modify
     */
    public float getWidthScale() {
	return 1;
    }
    
    /**
     * Should be overridden if you want to modify
     */
    public float getHeightScale() {
	return 1;
    }
    
    /**
     * Should be overridden if you want to modify
     */
    public int getRotation() {
	return 0;
    }
    
    /**
     * Should return null if it does not render through renderScreenObject
     */
    public abstract Texture getTexture();
    
    public abstract void render();
    
    public abstract void update();
    
    /**
     * Used for keeping things like the hotbar at the bottom of the screen if the size changes
     */
    public abstract void onScreenSizeChange();
}
