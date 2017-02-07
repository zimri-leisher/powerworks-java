package powerworks.input;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public enum ControlMap {
    DEFAULT("/settings/binds.txt");

    HashMap<Integer, KeyControlOption> keyBinds = new HashMap<Integer, KeyControlOption>();
    HashMap<Integer, MouseControlOption> mouseBinds = new HashMap<Integer, MouseControlOption>();
    
    private ControlMap(String path) {
	if (!path.endsWith(".txt"))
	    throw new IllegalArgumentException("Invalid file extension");
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(new File(ControlMap.class.getResource(path).getFile())));
	    String line, first, last;
	    int count = 0;
	    while ((line = reader.readLine()) != null && line.contains(":")) {
		count++;
		first = line.split(":")[0].replaceAll(":", "");
		last = line.split(":")[1];
		keyBinds.put(Integer.parseInt(first), KeyControlOption.valueOf(last));
	    }
	    System.out.println("Loaded " + count + " keybinds");
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public KeyControlOption getKeyControlOption(KeyEvent key) {
	return keyBinds.get(key.getKeyCode());
    }
    
    public KeyControlOption getKeyControlOption(Integer keyCode) {
	return keyBinds.get(keyCode);
    }
    
    public MouseControlOption getMouseControlOption(Integer buttonCode) {
	return mouseBinds.get(buttonCode);
    }
    
    public void setKeyEvent(KeyEvent key, KeyControlOption control) {
	if(keyBinds.replace(key.getKeyCode(), control) == null) {
	    System.err.println("Attempted to set key " + KeyEvent.getKeyText(key.getKeyCode()) + " to " + control.name() + " but the key was not defined previously, adding it as new definition");
	    keyBinds.put(key.getKeyCode(), control);
	}
    }
    
    public void setMouseButton(Integer buttonCode, MouseControlOption control) {
	if(mouseBinds.replace(buttonCode, control) == null) {
	    System.err.println("Attempted to set mouse button " + buttonCode + " to " + control.name() + " but the button was not defined previously, adding it as new definition");
	    mouseBinds.put(buttonCode, control);
	}
    }
}