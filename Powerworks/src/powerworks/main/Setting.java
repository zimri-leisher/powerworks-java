package powerworks.main;

import java.util.ArrayList;
import java.util.List;
import powerworks.graphics.screen.gui.GUIElement;
import powerworks.task.Task;

public abstract class Setting<T> {

    private static List<Setting<?>> settings = new ArrayList<Setting<?>>();
    public static final Setting<Boolean> THREAD_WAITING = new BooleanSetting("Thread Waiting",
	    "Sleeps the thread in the main loop resulting in vastly reduced CPU and GPU processing power at the cost of reduced FPS and possibly UPS", true, new Task() {

		@Override
		public void run() {
		    Game.setFPSMode(false);
		}
	    }, new Task() {

		@Override
		public void run() {
		    Game.setFPSMode(true);
		}
	    });
    public static final Setting<Boolean> PAUSE_IN_ESCAPE_MENU = new BooleanSetting("Pause in Escape Menu", "Pauses the game while the escape menu is open", true, new Task() {

	@Override
	public void run() {
	    Game.setPauseInEscapeMenu(true);
	}
    }, new Task() {

	@Override
	public void run() {
	    Game.setPauseInEscapeMenu(false);
	}
    });
    public static final Setting<Boolean> SHOW_HITBOXES = new BooleanSetting("Show Hitboxes", "Highlights the hitboxes of entities with a red square", false, new Task() {

	@Override
	public void run() {
	    Game.setShowHitboxes(true);
	}
    }, new Task() {

	@Override
	public void run() {
	    Game.setShowHitboxes(false);
	}
    });
    String name, desc;

    protected Setting(String name, String desc) {
	this.name = name;
	this.desc = desc;
	settings.add(this);
    }

    public String getName() {
	return name;
    }

    public String getDesc() {
	return desc;
    }

    public abstract void setValue(T val);

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
	for (Setting<?> setting : settings) {
	    el.add(setting.generateGUIElement());
	}
	return el;
    }
}
