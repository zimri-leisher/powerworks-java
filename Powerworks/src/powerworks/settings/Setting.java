package powerworks.settings;

import java.util.ArrayList;
import java.util.List;
import powerworks.graphics.screen.gui.GUIElement;

public abstract class Setting<T> {
    
    @SuppressWarnings("serial")
    static ArrayList<Setting<?>> settings = new ArrayList<Setting<?>>() {{
	add(new BooleanSetting("Thread Waiting", "Sleeps the thread every update, taking most of the load off of the CPU and GPU \nat the cost of decreased FPS and UPS", false));
    }};
    
    String name, desc;

    protected Setting(String name, String desc) {
	this.name = name;
	this.desc = desc;
    }
    
    public abstract void setValue(T value);
    
    public String getName() {
	return name;
    }
    
    public String getDesc() {
	return desc;
    }
    
    public abstract T getValue();
    
    public abstract GUIElement generateGUIElement();
    
    public static List<Setting<?>> getSettings() {
	return settings;
    }
    
    public static Object getValue(String settingName) {
	settings.stream().filter(setting -> setting.name == settingName).forEach(null);
	return null;
    }
    
    public static List<GUIElement> genOptionsList() {
	List<GUIElement> el = new ArrayList<GUIElement>(settings.size());
	int yPixel = 0;
	for(Setting<?> setting : settings) {
	}
	return null;
    }
}
