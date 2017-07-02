package powerworks.io;


public class MouseWheelPress extends ControlPress{

    protected MouseWheelPress(ControlPressType type, ControlOption option) {
	super(type, option);
    }
    
    @Override
    public MouseWheelControlOption getOption() {
	return (MouseWheelControlOption) option;
    }
}