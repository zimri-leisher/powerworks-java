package powerworks.graphics.screen.gui;

import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;

public class GUIDescription extends GUIElement {

    private String text;
    
    protected GUIDescription(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, String text) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
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
    
    @Override
    public String toString() {
	return "GUI description " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + " with text " + text + ", layer: " + layer + ", # of children: " + children.size();
    }
    
    @Override
    public void remove() {
	text = null;
    }

}
