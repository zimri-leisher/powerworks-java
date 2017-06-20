package powerworks.settings;

import java.util.ArrayList;
import java.util.List;
import powerworks.graphics.screen.gui.GUIElement;

public class MultiOptionSetting extends Setting<String>{

    String current;
    List<String> options;
    
    @SafeVarargs
    protected MultiOptionSetting(String setting, String desc, String...options) {
	super(setting, desc);
	this.options = new ArrayList<String>();
	if(options.length < 2) throw new IllegalArgumentException("There must be at least 2 options");
	for(int i = 0; i < options.length; i++)
	    this.options.add(options[i]);
	this.current = options[0];
    }
    
    @Override
    public String getValue() {
	return current;
    }
    
    @Override
    public void setValue(String value) {
	current = value;
    }
    
    public List<String> getOptions() {
	return options;
    }

    @Override
    public GUIElement generateGUIElement() {
	return null;
    }
}