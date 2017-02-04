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
}