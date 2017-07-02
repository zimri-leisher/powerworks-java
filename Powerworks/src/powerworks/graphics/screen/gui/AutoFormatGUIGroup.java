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
	el.setLayer(layer + 1, true);
	el.setRelYPixel(currentYPixel);
	el.setRelXPixel(xPixelPadding);
	currentYPixel += yPixelSeparation + el.getHeightPixels();
	updateDimensions();
	heightPixels += yPixelSeparation;
    }
    
    @Override
    public String toString() {
	return "Auto formatting GUI group " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size();
    }
}
