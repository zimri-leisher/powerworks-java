package powerworks.newinput;


public class KeyControlPress extends ControlPress{
    protected KeyControlPress(ControlPressType type, ControlOption option) {
	super(type, option);
    }
    
    @Override
    public KeyControlOption getOption() {
	return (KeyControlOption) option;
    }
}
