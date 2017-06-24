package powerworks.main;

import powerworks.graphics.screen.gui.GUIButton;
import powerworks.graphics.screen.gui.GUIElement;
import powerworks.task.Task;

public class BooleanSetting extends Setting<Boolean> {

    private Boolean current;
    private GUIButton b;
    private Task onEnable, onDisable;

    protected BooleanSetting(String setting, String desc, boolean defValue, Task onEnable, Task onDisable) {
	super(setting, desc);
	current = defValue;
	this.onEnable = onEnable;
	this.onDisable = onDisable;
    }
    
    @Override
    public void setValue(Boolean val) {
	if(val == current) return;
	current = val;
	if(current == true)
	    onEnable.run();
	else
	    onDisable.run();
	b.getText().setText(name + ": " + current);
    }
    
    @Override
    public Boolean getValue() {
	return current;
    }

    @Override
    public GUIElement generateGUIElement() {
	b = new GUIButton(null, 0, 0, 4, name + ": " + current, true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		current = !current;
		if(current == true)
		    onEnable.run();
		else
		    onDisable.run();
		b.getText().setText(name + ": " + current);
	    }
	});
	return b;
    }
}