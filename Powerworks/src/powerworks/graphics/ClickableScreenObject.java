package powerworks.graphics;


public abstract class ClickableScreenObject extends ScreenObject {

    protected int widthPixels, heightPixels;
    
    /**
     * @param xPixel relative to screen
     * @param yPixel relative to screen
     * @param widthPixels for determining if it was clicked on
     * @param heightPixels for determining if it was clicked on
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
    
    /**
     * X and Y pixel are relative to this render component
     */
    public abstract void onClick(int xPixel, int yPixel);
    
    /**
     * Triggered any time a click is performed that isn't on this object
     */
    public abstract void onClickOff();
    
    public abstract void whileMouseOver();
    
    /**
     * X and Y pixel are relative to this render component
     */
    public abstract void onRelease(int xPixel, int yPixel);
}
