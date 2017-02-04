package powerworks.settings;

import java.util.ArrayList;

public class MultiOptionSetting<T> extends Setting<T>{

    ArrayList<T> options;
    
    @SafeVarargs
    protected MultiOptionSetting(String name, String desc, T...options) {
	super(name, desc);
	if(options.length < 2) throw new IllegalArgumentException("There must be at least 2 options");
	for(int i = 0; i < options.length; i++)
	    this.options.add(options[i]);
	this.currentValue = options[0];
    }
}