package powerworks.graphics.screen;

import powerworks.io.MouseEvent;
import powerworks.main.Game;

public abstract class ClickableScreenObject extends ScreenObject {

    protected int widthPixels, heightPixels;
    /**
     * Don't forget about this!
     */
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
    }

    @Override
    public void close() {
	super.close();
	mouseOn = false;
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
    
    @Override
    public void remove() {
	super.remove();
	Game.getScreenManager().getClickableScreenObjects().remove(this);
    }
}
