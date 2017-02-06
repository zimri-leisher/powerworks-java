package powerworks.input;

public class KeyControlPress extends ControlPress{

    public KeyControlPress(KeyControlOption option, ControlPressType type) {
	super(option, type);
    }
    
    @Override
    public KeyControlOption getControl() {
	return (KeyControlOption) option;
    }
}