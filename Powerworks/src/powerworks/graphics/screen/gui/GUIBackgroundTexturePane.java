package powerworks.graphics.screen.gui;

import powerworks.graphics.screen.ScreenObject;

public class GUIBackgroundTexturePane extends GUITexturePane{

    /**
     * Automatically creates a suitable image for the width and height
     */
    public GUIBackgroundTexturePane(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer, null);
    }
    
    @Override
    public void render() {
	
    }
}
