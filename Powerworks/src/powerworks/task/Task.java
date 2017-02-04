package powerworks.task;

import java.util.ArrayList;
import java.util.List;

public abstract class Task {

    static List<Task> scheduled = new ArrayList<Task>();
    static int nextID = -1;
    
    public static void update() {
	for(int i = 0; i < scheduled.size(); i++) {
	    Task r = scheduled.get(i);
	    if(r.cancel)
		scheduled.remove(r);
	    else {
		if(r.delay == 0) {
		    r.run();
		    if(r.repeat) {
			r.delay = r.original;
		    } else scheduled.remove(r);
		} else {
		    r.delay--;
		}
	    }
	}
    }
    
    boolean repeat = false, cancel = false;
    int delay = 0, original = 0;
    
    public abstract void run();
    
    public void runLater(int timeToRun) {
	delay = timeToRun;
	repeat = false;
	scheduled.add(this);
    }
    
    public void repeat(int timeToRun, int cycleTime) {
	repeat = true;
	original = cycleTime;
	delay = timeToRun;
	scheduled.add(this);
    }
    
    public void setDelay(int delay) {
	this.delay = delay;
    }
    
    public void cancel() {
	cancel = true;
    }
    
    public static List<Task> getTasks() {
	return scheduled;
    }
    
}
