package powerworks.data;

import java.util.ArrayList;
import java.util.List;
import powerworks.task.Task;

public class Timer {

    static List<Timer> timers = new ArrayList<Timer>();
    int upt, maxTicks, updateCount = 0, currentTick = 0;
    Task perNTicks, onFinish, perNUpdates;
    int nUpdates, nTicks;
    boolean loop = false, playing = false;

    public Timer(int updatesPerTick, int maxTicks) {
	upt = updatesPerTick;
	this.maxTicks = maxTicks;
	timers.add(this);
    }
    
    public int getCurrentUpdate() {
	return updateCount;
    }
    
    public void play() {
	playing = true;
    }
    
    public void stop() {
	playing = false;
    }
    
    public void resetTimes() {
	currentTick = 0;
	updateCount = 0;
    }

    public void setLoop(boolean loop) {
	this.loop = loop;
    }
    
    public int getCurrentTick() {
	return currentTick;
    }
    
    public void runTaskEveryNUpdates(int updates, Task t) {
	perNUpdates = t;
	nUpdates = updates;
    }

    public void runTaskEveryNTicks(int ticks, Task t) {
	perNTicks = t;
	nTicks = ticks;
    }

    public void runTaskOnFinish(Task t) {
	onFinish = t;
    }

    private void nextUpdate() {
	updateCount++;
	if(nUpdates > 0 && updateCount % nUpdates == 0)
	    perNUpdates.run();
	if (updateCount % upt == 0) {
	    currentTick++;
	    if (currentTick % maxTicks == 0) {
		if (onFinish != null)
		    onFinish.run();
		if (!loop)
		    playing = false;
	    } else {
		if(nTicks > 0 && currentTick % nTicks == 0) {
		    perNTicks.run();
		}
	    }
	}
    }

    public boolean isPlaying() {
	return playing;
    }

    public static void update() {
	timers.stream().filter(Timer::isPlaying).forEach(Timer::nextUpdate);
    }
}
