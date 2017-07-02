package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;

public abstract class GUI extends ScreenObject{

    protected GUI(int xPixel, int yPixel, int layer) {
	super(xPixel, yPixel, layer);
    }

    @Override
    public void update() {
	
    }

    @Override
    protected void onOpen() {
	
    }

    @Override
    protected void onClose() {
	
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
	
    }
    
}
