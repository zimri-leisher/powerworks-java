package powerworks.graphics;

public abstract class ClickableScreenObject extends ScreenObject {

    protected int widthPixels, heightPixels;
    protected boolean open = false;
    /**
     * Don't forget about this!
     */
    protected boolean mouseOn = false;

    /**
     * @param xPixel
     *            relative to screen
     * @param yPixel
     *            relative to screen
     * @param widthPixels
     *            for determining if it was clicked on
     * @param heightPixels
     *            for determining if it was clicked on
     */
    protected ClickableScreenObject(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	super(xPixel, yPixel);
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
    }

    public int getWidthPixels() {
	return widthPixels;
    }

    public int getHeightPixels() {
	return heightPixels;
    }

    public boolean isOpen() {
	return open;
    }

    public void open() {
	open = true;
	onOpen();
    }

    public void close() {
	open = false;
	onClose();
	mouseOn = false;
    }

    public void toggle() {
	if(open)
	    close();
	else
	    open();
    }

    /**
     * X pixel and Y pixel are not relative
     */
    public abstract void onClick(int xPixel, int yPixel);

    /**
     * Triggered any time a click is performed that isn't on this object
     */
    public abstract void onClickOff();

    public abstract void onMouseEnter();

    public abstract void onMouseLeave();

    public abstract void onOpen();

    public abstract void onClose();

    /**
     * X pixel and Y pixel are not relative
     */
    public abstract void onRelease(int xPixel, int yPixel);
}
