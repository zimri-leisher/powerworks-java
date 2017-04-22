package powerworks.graphics.screen.gui;

import powerworks.graphics.ClickableScreenObject;

public abstract class GUIElement extends ClickableScreenObject{
    
    /**
     * Higher layer number means on top
     */
    int layer;
    GUI parent;
    
    GUIElement(GUI parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(xPixel, yPixel, widthPixels, heightPixels);
	parent.elements.add(this);
	this.parent = parent;
	this.layer = layer;
    }
    
    /**
     * Should render relative to parent gui and not render if the parent gui is not open, remember x and y pixel are both relative to it.
     */
    public abstract void render();
    
    public void update() {}
    
}
