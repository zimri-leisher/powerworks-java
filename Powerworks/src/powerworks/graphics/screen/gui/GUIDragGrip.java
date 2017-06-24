package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUIDragGrip extends GUIElement {

    private Texture texture;
    private boolean dragging;
    private int mXPixel, mYPixel;

    public GUIDragGrip(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, Texture texture) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.texture = texture;
    }

    public GUIDragGrip(ScreenObject parent, int xPixel, int yPixel, int layer) {
	this(parent, xPixel, yPixel, 8, 8, layer, Image.GUI_DRAG_GRIP);
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
	ControlPressType type = mouse.getType();
	switch (type) {
	    case PRESSED:
		dragging = true;
		mXPixel = InputManager.getMouseXPixel();
		mYPixel = InputManager.getMouseYPixel();
		break;
	    case RELEASED:
		dragging = false;
		break;
	}
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public Texture getTexture() {
	return null;
    }
    
    @Override
    public void render() {
	super.render();
	Game.getRenderEngine().renderTexture(texture, xPixel, yPixel);
    }

    @Override
    public void update() {
	if(dragging) {
	    int mXPixel1 = InputManager.getMouseXPixel();
	    int mYPixel1 = InputManager.getMouseYPixel();
	    if(mXPixel1 != mXPixel) {
		parent.setXPixel(parent.getXPixel() + (mXPixel1 - mXPixel));
		mXPixel = mXPixel1;
	    }
	    if(mYPixel1 != mYPixel) {
		parent.setYPixel(parent.getYPixel() + (mYPixel1 - mYPixel));
		mYPixel = mYPixel1;
	    }
	}
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
	return "GUI drag grip at " + xPixel + ", " + yPixel + ", width pixels: " + widthPixels + ", height pixels: " + heightPixels + ", layer: " + layer + ", # of children: " + children.size();
    }
    
    @Override
    public void remove() {
	super.remove();
	texture = null;
    }
}
