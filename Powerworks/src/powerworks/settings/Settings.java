package powerworks.settings;

import java.util.ArrayList;

public class Settings {
    @SuppressWarnings("serial")
    static ArrayList<Setting<?>> settings = new ArrayList<Setting<?>>() {{
	add(new MultiOptionSetting<String>("Dynamic Resource Allocation", "Allocates system resources based on current performance\n to maintain fps and ups specified", "[ON]", "[OFF]"));
    }};
}