package powerworks.input;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public enum ControlMap {
    DEFAULT("/settings/binds.txt");

    HashMap<Integer, ControlOption> binds = new HashMap<Integer, ControlOption>();
    
    private ControlMap(String path) {
	if (!path.endsWith(".txt"))
	    throw new IllegalArgumentException("Invalid file extension");
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
	    String line, first, last;
	    int count = 0;
	    while ((line = reader.readLine()) != null && line.contains(":")) {
		count++;
		first = line.split(":")[0].replaceAll(":", "");
		last = line.split(":")[1];
		binds.put(KeyEvent.getExtendedKeyCodeForChar(Integer.parseInt(first)), ControlOption.valueOf(last));
	    }
	    System.out.println("Loaded " + count + " keybinds");
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public ControlOption getControlOption(KeyEvent key) {
	return binds.get(key.getKeyCode());
    }
    
    public void setKeyEvent(KeyEvent key, ControlOption control) {
	if(binds.replace(key.getKeyCode(), control) == null) {
	    System.err.println("Attempted to set key " + KeyEvent.getKeyText(e.getKeyCode()) + " to " + control.name() + " but the key was not defined previously, adding it as new definition");
	    binds.put(key.getKeyCode(), control);
	}
	
    }
}