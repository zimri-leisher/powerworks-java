package powerworks.graphics.screen;

import powerworks.io.MouseEvent;
import powerworks.main.Game;

public abstract class ClickableScreenObject extends ScreenObject {

    /**
     * Used to determine whether or not to adjusst width and height when the
     * screen size changes
     */
    protected boolean adjustDimensions;
    protected int widthPixels, heightPixels;
    protected boolean mouseOn = false;

    /**
     * @param xPixel
     *            relative to parent
     * @param yPixel
     *            relative to parent
     * @param widthPixels
     *            width of the click box
     * @param heightPixels
     *            height of the click box
     */
    protected ClickableScreenObject(int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	this(null, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    /**
     * @param parent
     *            the parent to render this relative to
     * @param xPixel
     *            relative to parent
     * @param yPixel
     *            relative to parent
     * @param widthPixels
     *            width of the click box
     * @param heightPixels
     *            height of the click box
     */
    protected ClickableScreenObject(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(parent, xPixel, yPixel, layer);
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
	Game.getScreenManager().getClickableScreenObjects().add(this);
    }

    public int getWidthPixels() {
	return widthPixels;
    }

    public int getHeightPixels() {
	return heightPixels;
    }

    public boolean isMouseOn() {
	return mouseOn;
    }

    public void setMouseOn(boolean mouseOn) {
	this.mouseOn = mouseOn;
	if(mouseOn)
	    onMouseEnter();
	else
	    onMouseLeave();
    }

    @Override
    public void close() {
	super.close();
	setMouseOn(false);
    }

    /**
     * Triggered any time a click is performed on this object X and Y pixel are
     * relative to screen
     */
    public abstract void onMouseActionOn(MouseEvent mouse);

    /**
     * Triggered any time a click is performed that isn't on this object X and Y
     * pixel are relative to screen
     */
    public abstract void onMouseActionOff(MouseEvent mouse);

    public abstract void onMouseEnter();

    public abstract void onMouseLeave();

    /**
     * This is an example of the Adapter pattern wherein a parent abstract class
     * provides concrete code for a little used method so as not to force child
     * classes to always override it
     */
    public void onScrollOn(int scroll) {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
	super.onScreenSizeChange(oldWidthPixels, oldHeightPixels);
	if (adjustDimensions) {
	    int width = Game.getScreenWidth();
	    int height = Game.getScreenHeight();
	    this.widthPixels = (int) (width * ((float) widthPixels / oldWidthPixels));
	    this.heightPixels = (int) (height * ((float) heightPixels / oldHeightPixels));
	}
    }

    @Override
    public void remove() {
	super.remove();
	Game.getScreenManager().getClickableScreenObjects().remove(this);
    }
}
