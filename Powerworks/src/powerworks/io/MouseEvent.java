package powerworks.io;


public class MouseEvent {
    private int button;
    private ControlPressType type;
    /**
     * Relative to screen
     */
    private int xPixel, yPixel;
    
    public MouseEvent(int xPixel, int yPixel, int button, ControlPressType type) {
	this.button = button;
	this.type = type;
	this.xPixel = xPixel;
	this.yPixel = yPixel;
    }
    
    public int getButton() {
	return button;
    }
    
    public ControlPressType getType() {
	return type;
    }
    
    public int getXPixel() {
	return xPixel;
    }
    
    public int getYPixel() {
	return yPixel;
    }
}
