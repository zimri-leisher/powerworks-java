package powerworks.settings;

import powerworks.graphics.screen.gui.GUIElement;

public class BooleanSetting extends Setting<Boolean>{

    Boolean current;
    
    protected BooleanSetting(String setting, String desc, boolean defValue) {
	super(setting, desc);
	current = defValue;
    }
    
    @Override
    public Boolean getValue() {
	return current;
    }
    
    @Override
    public void setValue(Boolean value) {
	current = value;
    }

    @Override
    public GUIElement generateGUIElement() {
	return null;
    }
}
