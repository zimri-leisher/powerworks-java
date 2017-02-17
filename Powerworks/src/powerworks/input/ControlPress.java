package powerworks.input;

import powerworks.input.ControlOption;

public abstract class ControlPress {
    ControlPressType type;
    ControlOption option;
    
    protected ControlPress(ControlPressType type, ControlOption option) {
	this.type = type;
	this.option = option;
    }
    
    public ControlPressType getType() {
	return type;
    }
    
    public ControlOption getOption() {
	return option;
    }
    
    @Override
    public String toString() {
	return option + " " + type;
    }
}
