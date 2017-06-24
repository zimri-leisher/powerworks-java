package powerworks.graphics.screen.gui;

import java.awt.Rectangle;
import java.util.List;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIGroup extends GUIElement {

    /**
     * For grouping a bunch of elements together generally to move them, without
     * having anything else present
     * 
     * @param elements
     *            adds these to children automatically
     */
    public GUIGroup(ScreenObject parent, int xPixel, int yPixel, int layer, List<GUIElement> elements) {
	super(parent, xPixel, yPixel, getWidthPixels(elements), getHeightPixels(elements), layer);
	children.addAll(elements);
    }

    /**
     * For adding elements later
     */
    public GUIGroup(ScreenObject parent, int xPixel, int yPixel, int layer) {
	super(parent, xPixel, yPixel, 0, 0, layer);
    }

    public void addChild(GUIElement el) {
	el.setParent(this);
	updateDimensions();
    }
    
    @Override
    public void render() {
	children.forEach(ScreenObject::render);
    }

    protected void updateDimensions() {
	Rectangle r = new Rectangle();
	for (ScreenObject s : children) {
	    if (s instanceof GUIElement) {
		GUIElement el = (GUIElement) s;
		r.add(new Rectangle(el.getRelXPixel(), el.getRelYPixel(), el.getWidthPixels(), el.getHeightPixels()));
	    }
	}
	widthPixels = (int) r.getWidth();
	heightPixels = (int) r.getHeight();
    }

    protected static int getWidthPixels(List<GUIElement> elements) {
	Rectangle r = new Rectangle();
	for (GUIElement el : elements) {
	    r.add(new Rectangle(el.getRelXPixel(), el.getRelYPixel(), el.getWidthPixels(), el.getHeightPixels()));
	}
	return (int) r.getWidth();
    }

    protected static int getHeightPixels(List<GUIElement> elements) {
	Rectangle r = new Rectangle();
	for (GUIElement el : elements) {
	    r.add(new Rectangle(el.getRelXPixel(), el.getRelYPixel(), el.getWidthPixels(), el.getHeightPixels()));
	}
	return (int) r.getHeight();
    }
    
    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public Texture getTexture() {
	return null;
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
	return "GUI group at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size();
    }
}
