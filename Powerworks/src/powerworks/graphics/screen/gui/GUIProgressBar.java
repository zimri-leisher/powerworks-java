package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;

public class GUIProgressBar extends GUIElement {

    private int max, current;
    private boolean showPercent;
    
    protected GUIProgressBar(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer, int start, int max, boolean showPercent) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
	this.max = max;
	this.current = start;
	this.showPercent = showPercent;
    }
    
    public void setProgress(int p) {
	current = p;
    }
    
    public int getProgress() {
	return current;
    }
    
    public int getMax() {
	return max;
    }
    
    public void setMax(int max) {
	this.max = max;
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
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
}
