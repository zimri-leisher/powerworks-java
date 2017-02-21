package powerworks.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import powerworks.graphics.ImageCollection;

public class ControlMap {
    public static final ControlMap DEFAULT = new ControlMap("/settings/binds.txt");
    
    HashMap<KeyMapping, KeyControlOption> keyBinds = new HashMap<KeyMapping, KeyControlOption>();
    HashMap<MouseMapping, MouseControlOption> mouseBinds = new HashMap<MouseMapping, MouseControlOption>();
    HashMap<MouseWheelMapping, MouseWheelControlOption> mouseWheelBinds = new HashMap<MouseWheelMapping, MouseWheelControlOption>();
    
    static class KeyMapping {
	int keyCode, modifier;
	
	private KeyMapping(int keyCode, int modifier) {
	    this.keyCode = keyCode;
	    this.modifier = modifier;
	}
	
	@Override
	public boolean equals(Object map) {
	    return map instanceof KeyMapping && ((KeyMapping) map).keyCode == keyCode && ((KeyMapping) map).modifier == modifier;
	}
	
	@Override
	public String toString() {
	    return modifier + ":" + keyCode;
	}
	
	static KeyMapping getMapping(String map) {
	    int modifier = 0;
	    String modifierS = map.substring(0, map.indexOf(":"));
	    if(modifierS.equals("~"))
		modifier = -1;
	    else
		modifier = Integer.parseInt(modifierS);
	    return new KeyMapping(Integer.parseInt(map.substring(map.indexOf(":") + 1)), modifier);
	}
    }
    
    static class MouseMapping {
	int buttonCode, modifier;
	
	private MouseMapping(int buttonCode, int modifier) {
	    this.buttonCode = buttonCode;
	    this.modifier = modifier;
	}
	
	@Override
	public boolean equals(Object map) {
	    return map instanceof MouseMapping && ((MouseMapping) map).buttonCode == buttonCode && ((MouseMapping) map).modifier == modifier;
	}
	
	@Override
	public String toString() {
	    return modifier + ":" + buttonCode;
	}
	
	static MouseMapping getMapping(String map) {
	    int modifier = 0;
	    String modifierS = map.substring(0, map.indexOf(":"));
	    if(modifierS.equals("~"))
		modifier = -1;
	    else
		modifier = Integer.parseInt(modifierS);
	    return new MouseMapping(Integer.parseInt(map.substring(map.indexOf(":") + 1)), modifier);
	}
    }
    
    static class MouseWheelMapping {
	int up, modifier;
	
	private MouseWheelMapping(int up, int modifier) {
	    this.up = up;
	    this.modifier = modifier;
	}
	
	@Override
	public boolean equals(Object map) {
	    return map instanceof MouseWheelMapping && ((MouseWheelMapping) map).up == up && ((MouseWheelMapping) map).modifier == modifier;
	}
	
	@Override
	public String toString() {
	    return modifier + ":" + up;
	}
	
	static MouseWheelMapping getMapping(String map) {
	    int modifier = 0;
	    String modifierS = map.substring(0, map.indexOf(":"));
	    if(modifierS.equals("~"))
		modifier = -1;
	    else
		modifier = Integer.parseInt(modifierS);
	    return new MouseWheelMapping(Integer.parseInt(map.substring(map.indexOf(":") + 1)), modifier);
	}
    }
    
    private ControlMap(String path) {
	try {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(ImageCollection.class.getResourceAsStream(path)));
	    String line, first, last;
	    int count = 0;
	    while ((line = reader.readLine()) != null && line.contains("=")) {
		if(line.contains("m")) break;
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		keyBinds.put(KeyMapping.getMapping(first), KeyControlOption.valueOf(last));
	    }
	    while ((line = reader.readLine()) != null && line.contains("=")) {
		if(line.contains("mw")) break;
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		mouseBinds.put(MouseMapping.getMapping(first), MouseControlOption.valueOf(last));
	    }
	    while((line = reader.readLine()) != null && line.contains("=")) {
		count++;
		first = line.split(Pattern.quote("="))[0].replaceAll(Pattern.quote("="), "");
		last = line.split(Pattern.quote("="))[1];
		mouseWheelBinds.put(MouseWheelMapping.getMapping(first), MouseWheelControlOption.valueOf(last));
	    }
	    System.out.println(mouseWheelBinds);
	    System.out.println("Loaded " + count + " keybinds");
	    reader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void setKeyBind(int keyCode, int modifier, KeyControlOption option) {
	keyBinds.remove(keyBinds.get(option));
	keyBinds.put(new KeyMapping(keyCode, modifier), option);
    }
    
    public KeyControlOption getKeyControl(int keyCode) {
	return getKeyControl(keyCode, InputManager.getModifier());
    }
    
    public KeyControlOption getKeyControl(int keyCode, int modifier) {
	for(Entry<KeyMapping, KeyControlOption> e : keyBinds.entrySet()) {
	    if(e.getKey().keyCode == keyCode && (e.getKey().modifier == -1 || e.getKey().modifier == modifier))
		return e.getValue();
	}
	return null;
    }
    
    public MouseControlOption getMouseControl(int buttonCode) {
	return getMouseControl(buttonCode, InputManager.getModifier());
    }
    
    public MouseControlOption getMouseControl(int buttonCode, int modifier) {
	for(Entry<MouseMapping, MouseControlOption> e : mouseBinds.entrySet()) {
	    if(e.getKey().buttonCode == buttonCode && (e.getKey().modifier == -1 || e.getKey().modifier == modifier))
		return e.getValue();
	}
	return null;
    }

    public MouseWheelControlOption getMouseWheelControl(int up, int modifier) {
	for(Entry<MouseWheelMapping, MouseWheelControlOption> e : mouseWheelBinds.entrySet()) {
	    if(e.getKey().up == up && (e.getKey().modifier == -1 || e.getKey().modifier == modifier))
		return e.getValue();
	}
	return null;
    }
    
    public MouseWheelControlOption getMouseWheelControl(int up) {
	return getMouseWheelControl(up, InputManager.getModifier());
    }
}
