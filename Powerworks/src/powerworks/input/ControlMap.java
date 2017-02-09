package powerworks.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public enum ControlMap {
    DEFAULT("/settings/binds.txt");

    HashMap<KeyStroke, KeyControlOption> keyBinds = new HashMap<KeyStroke, KeyControlOption>();
    HashMap<MouseClick, MouseControlOption> mouseBinds = new HashMap<MouseClick, MouseControlOption>();
    
    private ControlMap(String path) {
	if (!path.endsWith(".txt"))
	    throw new IllegalArgumentException("Invalid file extension");
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(new File(ControlMap.class.getResource(path).getFile())));
	    String line, first, last;
	    int count = 0;
	    while ((line = reader.readLine()) != null && line.contains("=")) {
		if(line.contains("k")) continue;
		if(line.contains("m")) break;
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		keyBinds.put(KeyStroke.getKeyStroke(first), KeyControlOption.valueOf(last));
	    }
	    while ((line = reader.readLine()) != null && line.contains("=")) {
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		mouseBinds.put(MouseClick.getMouseClick(first), MouseControlOption.valueOf(last));
	    }
	    System.out.println("Loaded " + count + " keybinds");
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void save() {
	//TODO save the binds
    }
    
    public KeyControlOption getKeyControlOption(KeyStroke key) {
	KeyControlOption control = null;
	for(Entry<KeyStroke, KeyControlOption> e : keyBinds.entrySet()) {
	    if(e.getKey().equals(key))
		control = e.getValue();
	}
	return control;
    }
    
    public MouseControlOption getMouseControlOption(MouseClick click) {
	return mouseBinds.get(click);
    }
    
    public void setKeyEvent(KeyStroke key, KeyControlOption control) {
	if(keyBinds.replace(key, control) == null) {
	    System.err.println("Attempted to set key " + key + " to " + control.name() + " but the key was not defined previously, adding it as new definition");
	    keyBinds.put(key, control);
	}
    }
    
    public void setMouseButton(MouseClick click, MouseControlOption control) {
	if(mouseBinds.replace(click, control) == null) {
	    System.err.println("Attempted to set mouse button " + click + " to " + control.name() + " but the button was not defined previously, adding it as new definition");
	    mouseBinds.put(click, control);
	}
    }
}