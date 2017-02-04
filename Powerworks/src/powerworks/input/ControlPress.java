package powerworks.input;


public abstract class ControlPress {
    ControlOption option;
    ControlPressType type;
    
    public ControlPress(ControlOption option, ControlPressType type) {
	this.option = option;
	this.type = type;
    }
    
    public ControlOption getControl() {
	return option;
    }
    
    public ControlPressType getPressType() {
	return type;
    }
}
