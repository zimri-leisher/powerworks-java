package powerworks.graphics.screen.gui;

import powerworks.data.GeometryHelper;
import powerworks.graphics.Image;
import powerworks.graphics.Renderer;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIScrollBar extends GUIElement {

    public static interface Scrollable {

	public void onScrollbarMove();
	public int getViewHeightPixels();
	public int getMaxHeightPixels();
    }

    private int currentPos = 0;
    private int scrollBarHeight;
    private boolean dragging = false;
    private int mYPixelPrev;
    private Texture top, mid, bottom;
    private Texture[] scrollBar, current;

    /**
     * Width pixels is always 6, height defaults to 12 if lower than 12
     */
    public GUIScrollBar(Scrollable parent, int xPixel, int yPixel, int heightPixels, int layer) {
	super((ScreenObject) parent, xPixel, yPixel, 6, Math.max(12, heightPixels), layer);
	scrollBar = new Texture[9];
	current = new Texture[3];
	scrollBar[0] = current[0] = Image.GUI_SCROLL_BAR_UNHIGHLIGHT_TOP;
	scrollBar[1] = current[1] = Image.GUI_SCROLL_BAR_UNHIGHLIGHT_MIDDLE;
	scrollBar[2] = current[2] = Image.GUI_SCROLL_BAR_UNHIGHLIGHT_BOTTOM;
	scrollBar[3] = Image.GUI_SCROLL_BAR_HIGHLIGHT_TOP;
	scrollBar[4] = Image.GUI_SCROLL_BAR_HIGHLIGHT_MIDDLE;
	scrollBar[5] = Image.GUI_SCROLL_BAR_HIGHLIGHT_BOTTOM;
	scrollBar[6] = Image.GUI_SCROLL_BAR_CLICK_TOP;
	scrollBar[7] = Image.GUI_SCROLL_BAR_CLICK_MIDDLE;
	scrollBar[8] = Image.GUI_SCROLL_BAR_CLICK_BOTTOM;
	top = Image.GUI_SCROLL_BAR_TOP;
	mid = Image.GUI_SCROLL_BAR_MIDDLE;
	bottom = Image.GUI_SCROLL_BAR_BOTTOM;
	onScrollableHeightChange();
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
	int mXPixel = mouse.getXPixel();
	int mYPixel = mouse.getYPixel();
	switch (mouse.getType()) {
	    case PRESSED:
		if (GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, scrollBarHeight)) {
		    dragging = true;
		    mYPixelPrev = mYPixel;
		    setBarTexture(2);
		}
		break;
	    case RELEASED:
		if (mouseOn && GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, scrollBarHeight))
		    setBarTexture(1);
		else
		    setBarTexture(0);
		dragging = false;
		break;
	}
    }

    private void setBarTexture(int t) {
	current[0] = scrollBar[t * 3];
	current[1] = scrollBar[t * 3 + 1];
	current[2] = scrollBar[t * 3 + 2];
    }

    public int getCurrentPos() {
	return currentPos;
    }
    
    @Override
    public void setParent(ScreenObject parent) {
	super.setParent(parent);
	onScrollableHeightChange();
    }

    /**
     * Does not do anything if dragging already
     */
    public void setCurrentPos(int pos) {
	if (dragging)
	    return;
	currentPos = Math.min(heightPixels - 2 - scrollBarHeight, Math.max(pos, 0));
	((Scrollable) parent).onScrollbarMove();
    }

    @Override
    public void render() {
	Renderer r = Game.getRenderEngine();
	r.renderTexture(top, xPixel, yPixel);
	r.renderTexture(true, mid, xPixel, yPixel + 2, 6, heightPixels - 4);
	r.renderTexture(bottom, xPixel, yPixel + heightPixels - 2);
	r.renderTexture(current[0], xPixel + 1, currentPos + 1 + yPixel);
	for(int i = current[0].getHeightPixels(); i < scrollBarHeight - current[2].getHeightPixels(); i++)
	    r.renderTexture(current[1], xPixel + 1, i + currentPos + 1 + yPixel);
	r.renderTexture(current[2], xPixel + 1, scrollBarHeight - current[2].getHeightPixels() + yPixel + currentPos + 1);
	super.render();
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
	switch (mouse.getType()) {
	    case RELEASED:
		setBarTexture(0);
		dragging = false;
		break;
	}
    }

    @Override
    public void onMouseLeave() {
	super.onMouseLeave();
	if (!dragging)
	    setBarTexture(0);
    }

    @Override
    public void update() {
	int mYPixel = InputManager.getMouseYPixel();
	int mXPixel = InputManager.getMouseXPixel();
	if (mouseOn && !dragging) {
	    if (GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, scrollBarHeight))
		setBarTexture(1);
	    else
		setBarTexture(0);
	}
	if (mYPixel != mYPixelPrev && dragging) {
	    if (yPixel + currentPos + (mYPixel - mYPixelPrev) + 1 + scrollBarHeight <= yPixel + heightPixels - 1 && yPixel + (currentPos + (mYPixel - mYPixelPrev)) >= yPixel) {
		currentPos += mYPixel - mYPixelPrev;
		mYPixelPrev = mYPixel;
		((Scrollable) parent).onScrollbarMove();
	    }
	}
    }

    public void onScrollableHeightChange() {
	Scrollable p = (Scrollable) parent;
	scrollBarHeight = Math.min((int) (((double) p.getViewHeightPixels() / (double) ((p.getMaxHeightPixels() == 0) ? 1 : p.getMaxHeightPixels())) * (double) (heightPixels)), heightPixels - 2);
    }

    public int getMaxPos() {
	return heightPixels - 2 - scrollBarHeight;
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
	return "GUI scroll bar " + id + " at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", current position: "
		+ currentPos;
    }

    @Override
    public void remove() {
	super.remove();
	current = scrollBar = null;
	top = mid = bottom = null;
    }

    /**
     * Should be handled in the parent class
     */
    @Override
    public void onScrollOn(int scroll) {
    }
}
