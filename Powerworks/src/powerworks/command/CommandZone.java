package powerworks.command;

import java.util.ArrayList;
import java.util.List;
import powerworks.data.ListAcquirer;
import powerworks.data.Timer;

public class CommandZone {
    
    static List<CommandZone> zones = new ArrayList<CommandZone>();
    
    public static void update() {
	zones.forEach(CommandZone::refresh);
    }
    
    ListAcquirer<?> getter;
    Command c;
    Timer t;
    int tick = 0;
    
    public CommandZone(ListAcquirer<?> getter, Command c) {
	this(getter, c, null, 0);
    }
    
    public CommandZone(ListAcquirer<?> getter, Command c, Timer t, int tickActivationNumber) {
	zones.add(this);
	this.c = c;
	this.getter = getter;
	this.t = t;
	this.tick = tickActivationNumber;
    }
    
    void refresh() {
	if(t != null) {
	    if(t.getCurrentTick() == tick)
		
	}
    }
}
