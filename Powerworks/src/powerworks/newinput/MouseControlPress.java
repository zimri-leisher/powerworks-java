package powerworks.newinput;


public class MouseControlPress extends ControlPress{
    protected MouseControlPress(ControlPressType type, ControlOption option) {
	super(type, option);
    }
    
    @Override
    public MouseControlOption getOption() {
	return (MouseControlOption) option;
    }
}
