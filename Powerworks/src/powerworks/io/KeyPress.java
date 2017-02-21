package powerworks.io;


public class KeyPress extends ControlPress{
    protected KeyPress(ControlPressType type, ControlOption option) {
	super(type, option);
    }
    
    @Override
    public KeyControlOption getOption() {
	return (KeyControlOption) option;
    }
}
