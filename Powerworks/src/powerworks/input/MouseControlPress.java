package powerworks.input;


public class MouseControlPress extends ControlPress {

    int xPixel, yPixel;
    
    public MouseControlPress(MouseControlOption option, ControlPressType type, int xPixel, int yPixel) {
	super(option, type);
    }
    
    public int getXPixel() {
	return xPixel;
    }
    
    public int getYPixel() {
	return yPixel;
    }
    
    @Override
    public MouseControlOption getControl() {
	return (MouseControlOption) option;
    }
}
