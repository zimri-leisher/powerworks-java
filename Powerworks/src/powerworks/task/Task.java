package powerworks.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import powerworks.main.Game;
import powerworks.main.Setting;

public abstract class Task {

    static List<Task> scheduled = new ArrayList<Task>();
    static int nextID = -1;

    public static void update() {
	long time = 0;
	if (Game.showUpdateTimes())
	    time = System.nanoTime();
	int size = scheduled.size();
	if (size != 0) {
	    Iterator<Task> i = scheduled.iterator();
	    while (i.hasNext()) {
		Task r = i.next();
		if (!r.world || !(Setting.PAUSE_IN_ESCAPE_MENU.getValue() && Game.isPaused())) {
		    if (r.cancel)
			i.remove();
		    else {
			if (r.delay == 0) {
			    r.run();
			    if (r.repeat) {
				r.delay = r.original;
			    } else
				i.remove();
			} else {
			    r.delay--;
			}
		    }
		}
	    }
	}
	if (Game.showUpdateTimes())
	    System.out.println("Updating tasks took:         " + (System.nanoTime() - time) + " ns");
    }

    /**
     * 
     * @param worldTask
     *            true if this task is related to the functioning of the world.
     *            This means it will be paused if the world is paused, sped up
     *            if the world is sped up, etc.
     */
    public Task(boolean worldTask) {
	world = worldTask;
    }

    public Task() {
	world = false;
    }

    boolean repeat = false, cancel = false, world;
    int delay = 0, original = 0;

    public abstract void run();

    public void runLater(int timeToRun) {
	delay = timeToRun;
	repeat = false;
	scheduled.add(this);
    }

    /**
     * @param timeToRun
     *            delay before running
     * @param cycleTime
     *            time in between repetitions
     */
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
