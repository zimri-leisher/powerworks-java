package powerworks.graphics.screen.gui;

import powerworks.data.GeometryHelper;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIScrollBar extends GUIElement {

    private int currentPos = 0;
    private boolean dragging = false;
    private int mYPixelPrev;
    private Texture current, highlight, click, unhighlight, top, mid, bottom;

    /**
     * Width pixels is always 6, height defaults to 12 if lower than 12
     */
    public GUIScrollBar(Scrollable parent, int xPixel, int yPixel, int heightPixels, int layer) {
	super((ScreenObject) parent, xPixel, yPixel, 6, Math.max(12, heightPixels), layer);
	highlight = Image.GUI_SCROLL_BAR_HIGHLIGHT;
	unhighlight = current = Image.GUI_SCROLL_BAR;
	click = Image.GUI_SCROLL_BAR_CLICK;
	top = Image.GUI_SCROLL_BAR_TOP;
	mid = Image.GUI_SCROLL_BAR_MIDDLE;
	bottom = Image.GUI_SCROLL_BAR_BOTTOM;
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
	int mXPixel = mouse.getXPixel();
	int mYPixel = mouse.getYPixel();
	switch (mouse.getType()) {
	    case PRESSED:
		if (GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, 8)) {
		    dragging = true;
		    mYPixelPrev = mYPixel;
		    current = click;
		}
		break;
	    case RELEASED:
		current = (mouseOn && GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, 8)) ? highlight : unhighlight;
		dragging = false;
		break;
	}
    }

    public int getCurrentPos() {
	return currentPos;
    }

    @Override
    public void render() {
	Game.getRenderEngine().renderTexture(top, xPixel, yPixel);
	Game.getRenderEngine().renderTexture(true, mid, xPixel, yPixel + 2, 6, heightPixels - 4, 0, 1f, true);
	Game.getRenderEngine().renderTexture(bottom, xPixel, yPixel + heightPixels - 2);
	Game.getRenderEngine().renderTexture(current, xPixel + 1, currentPos + 1 + yPixel);
	super.render();
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
	switch (mouse.getType()) {
	    case RELEASED:
		current = unhighlight;
		dragging = false;
		break;
	}
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onMouseLeave() {
	super.onMouseLeave();
	if (!dragging)
	    current = unhighlight;
    }

    @Override
    public void update() {
	int mYPixel = InputManager.getMouseYPixel();
	int mXPixel = InputManager.getMouseXPixel();
	if (mouseOn && !dragging) {
	    if (GeometryHelper.intersects(mXPixel, mYPixel, 1, 1, xPixel + 1, currentPos + yPixel + 1, 4, 8))
		current = highlight;
	    else
		current = unhighlight;
	}
	if (mYPixel != mYPixelPrev && dragging) {
	    if (yPixel + currentPos + (mYPixel - mYPixelPrev) + 9 <= yPixel + heightPixels - 1 && yPixel + (currentPos + (mYPixel - mYPixelPrev)) >= yPixel) {
		currentPos += mYPixel - mYPixelPrev;
		mYPixelPrev = mYPixel;
		((Scrollable) parent).onScroll();
	    }
	}
    }

    public int getMaxPos() {
	return heightPixels - 10;
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
	return "GUI scroll bar at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", current position: " + currentPos;
    }

    @Override
    public void remove() {
	super.remove();
	current = highlight = unhighlight = top = mid = bottom = click = null;
    }
}
