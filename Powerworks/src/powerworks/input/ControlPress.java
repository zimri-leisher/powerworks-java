package powerworks.input;

public class ControlPress {
    ControlOption control;
    ControlPressType type;
    
    public ControlPress(ControlOption control, ControlPressType type) {
	this.control = control;
	this.type = type;
    }
}