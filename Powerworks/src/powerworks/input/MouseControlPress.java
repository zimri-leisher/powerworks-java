package powerworks.input;


public class MouseControlPress extends ControlPress {
    
    public MouseControlPress(MouseControlOption option, ControlPressType type) {
	super(option, type);
    }
    
    @Override
    public MouseControlOption getControl() {
	return (MouseControlOption) option;
    }
}
