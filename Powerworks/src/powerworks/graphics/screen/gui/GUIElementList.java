package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIElementList extends GUIElement implements GUIScrollBar.Scrollable {

    private GUIScrollBar scroll;
    private GUIGroup elements;

    /**
     * Adding children to this will <i> not </i> put them in the list, so to add
     * them to it, use the add() method to put them in the list
     */
    public GUIElementList(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, GUIGroup elements) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.elements = elements;
	scroll = new GUIScrollBar(this, widthPixels - 6, 0, heightPixels, layer + 1);
	elements.setParent(this);
    }

    /**
     * Automatically adds this to the list and puts it in the correct position
     * or not depending on the type of GUIGroup inputted
     */
    public void add(GUIElement el) {
	elements.addChild(el);
	scroll.onScrollableHeightChange();
	onScrollbarMove();
    }

    public void test() {
	add(new GUITexturePane(null, 0, 0, layer + 1, Image.GUI_BUTTON));
    }
    
    public GUIGroup getElements() {
	return elements;
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
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
    public void onScrollbarMove() {
	elements.setRelYPixel((int) (Math.min(0, heightPixels - elements.getHeightPixels()) * (double) (scroll.getCurrentPos() / (double) scroll.getMaxPos())));
    }

    @Override
    public String toString() {
	return "GUI element list " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size();
    }

    @Override
    public void remove() {
	super.remove();
	scroll = null;
	elements = null;
    }

    @Override
    public void onScrollOn(int scroll) {
	this.scroll.setCurrentPos(this.scroll.getCurrentPos() + scroll);
    }

    @Override
    public int getViewHeightPixels() {
	return heightPixels;
    }

    @Override
    public int getMaxHeightPixels() {
	return elements.getHeightPixels();
    }
}
