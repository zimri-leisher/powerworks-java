package powerworks.input;


public class MouseControlPress extends ControlPress {

    int xPixel, yPixel;
    
    public MouseControlPress(ControlOption option, ControlPressType type, int xPixel, int yPixel) {
	super(option, type);
    }
    
    public int getXPixel() {
	return xPixel;
    }
    
    public int getYPixel() {
	return yPixel;
    }
}
