package powerworks.input;


public class MousePress extends ControlPress{
    protected MousePress(ControlPressType type, ControlOption option) {
	super(type, option);
    }
    
    @Override
    public MouseControlOption getOption() {
	return (MouseControlOption) option;
    }
}
