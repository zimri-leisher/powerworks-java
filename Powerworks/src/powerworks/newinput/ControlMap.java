package powerworks.newinput;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public enum ControlMap {
    DEFAULT("/settings/binds.txt");
    
    HashMap<KeyControlMapping, KeyControlOption> keyBinds = new HashMap<KeyControlMapping, KeyControlOption>();
    HashMap<MouseControlMapping, MouseControlOption> mouseBinds = new HashMap<MouseControlMapping, MouseControlOption>();
    
    static class KeyControlMapping {
	int keyCode, modifier;
	
	private KeyControlMapping(int keyCode, int modifier) {
	    this.keyCode = keyCode;
	    this.modifier = modifier;
	}
	
	@Override
	public boolean equals(Object map) {
	    return map instanceof KeyControlMapping && ((KeyControlMapping) map).keyCode == keyCode && ((KeyControlMapping) map).modifier == modifier;
	}
	
	@Override
	public String toString() {
	    return modifier + ":" + keyCode;
	}
	
	static KeyControlMapping getMapping(String map) {
	    int modifier = 0;
	    String modifierS = map.substring(0, map.indexOf(":"));
	    if(modifierS.equals("~"))
		modifier = -1;
	    else
		modifier = Integer.parseInt(modifierS);
	    return new KeyControlMapping(Integer.parseInt(map.substring(map.indexOf(":") + 1)), modifier);
	}
    }
    
    static class MouseControlMapping {
	int buttonCode, modifier;
	
	private MouseControlMapping(int buttonCode, int modifier) {
	    this.buttonCode = buttonCode;
	    this.modifier = modifier;
	}
	
	@Override
	public boolean equals(Object map) {
	    return map instanceof MouseControlMapping && ((MouseControlMapping) map).buttonCode == buttonCode && ((MouseControlMapping) map).modifier == modifier;
	}
	
	@Override
	public String toString() {
	    return modifier + ":" + buttonCode;
	}
	
	static MouseControlMapping getMapping(String map) {
	    int modifier = 0;
	    String modifierS = map.substring(0, map.indexOf(":"));
	    if(modifierS.equals("~"))
		modifier = -1;
	    else
		modifier = Integer.parseInt(modifierS);
	    return new MouseControlMapping(Integer.parseInt(map.substring(map.indexOf(":") + 1)), modifier);
	}
    }
    
    private ControlMap(String path) {
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
		keyBinds.put(KeyControlMapping.getMapping(first), KeyControlOption.valueOf(last));
	    }
	    while ((line = reader.readLine()) != null && line.contains("=")) {
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		mouseBinds.put(MouseControlMapping.getMapping(first), MouseControlOption.valueOf(last));
	    }
	    System.out.println("Loaded " + count + " keybinds");
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public KeyControlOption getKeyControl(int keyCode) {
	return getKeyControl(keyCode, InputManager.getModifier());
    }
    
    public KeyControlOption getKeyControl(int keyCode, int modifier) {
	for(Entry<KeyControlMapping, KeyControlOption> e : keyBinds.entrySet()) {
	    if(e.getKey().keyCode == keyCode && (e.getKey().modifier == -1 || e.getKey().modifier == modifier))
		return e.getValue();
	}
	return null;
    }
    
    public MouseControlOption getMouseControl(int buttonCode) {
	return getMouseControl(buttonCode, InputManager.getModifier());
    }
    
    public MouseControlOption getMouseControl(int buttonCode, int modifier) {
	for(Entry<MouseControlMapping, MouseControlOption> e : mouseBinds.entrySet()) {
	    if(e.getKey().buttonCode == buttonCode && (e.getKey().modifier == -1 || e.getKey().modifier == modifier))
		return e.getValue();
	}
	return null;
    }
}
