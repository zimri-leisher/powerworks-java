package powerworks.graphics.screen.gui;

import java.util.List;
import powerworks.graphics.screen.ScreenObject;

public class AutoFormatGUIGroup extends GUIGroup{

    private int currentYPixel, yPixelSeparation, xPixelPadding;
    
    /**
     * For grouping a bunch of elements together generally to move them, without
     * having anything else present
     * 
     * @param elements
     *            adds these to children automatically
     */
    public AutoFormatGUIGroup(ScreenObject parent, int xPixel, int yPixel, int layer, int yPixelSeparation, int xPixelPadding, List<GUIElement> elements) {
	super(parent, xPixel, yPixel, layer, elements);
	this.yPixelSeparation = currentYPixel = yPixelSeparation;
	this.xPixelPadding = xPixelPadding;
    }
    
    /**
     * For adding elements later
     */
    public AutoFormatGUIGroup(ScreenObject parent, int xPixel, int yPixel, int layer, int yPixelSeparation, int xPixelPadding) {
	super(parent, xPixel, yPixel, layer);
	this.yPixelSeparation = currentYPixel = yPixelSeparation;
	this.xPixelPadding = xPixelPadding;
    }
    
    @Override
    public void addChild(GUIElement el) {
	el.setParent(this);
	el.setRelYPixel(currentYPixel);
	el.setRelXPixel(xPixelPadding);
	currentYPixel += yPixelSeparation + el.getHeightPixels();
	updateDimensions();
	heightPixels += yPixelSeparation;
    }
}
