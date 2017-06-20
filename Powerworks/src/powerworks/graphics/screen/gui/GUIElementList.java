package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIElementList extends GUIElement implements Scrollable {

    private GUIScrollBar scroll;
    private GUIGroup elements;

    /**
     * Adding children to this will <i> not </i> put them in the list, so to add
     * them to it, use the add() method to put them in the list
     */
    protected GUIElementList(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, GUIGroup elements) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.elements = elements;
	scroll = new GUIScrollBar(this, widthPixels - 6, 0, heightPixels, layer + 1);
    }

    public void add(GUIElement el) {
	elements.addChild(el);
	onScroll();
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
    public void render() {
	Game.getRenderEngine().setClip(xPixel, yPixel, widthPixels, heightPixels);
	elements.render();
	Game.getRenderEngine().resetClip();
	children.stream().filter(s -> s.isOpen() && !(s == elements)).sorted((ScreenObject obj, ScreenObject obj2) -> obj.getLayer() > obj2.getLayer() ? 1 : obj.getLayer() == obj2.getLayer() ? 0 : -1)
		.forEach(ScreenObject::render);
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
    public void onScroll() {
	elements.setRelYPixel((int) (-scroll.getCurrentPos() * ((double) elements.getHeightPixels() / (double) heightPixels)));
    }

    @Override
    public String toString() {
	return "GUI element list at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size();
    }
    
    @Override
    public void remove() {
	super.remove();
	scroll = null;
	elements = null;
    }
}
