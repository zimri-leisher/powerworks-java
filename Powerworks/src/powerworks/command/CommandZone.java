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
    
    ListAcquirer<? extends CommandHandler> getter;
    Command c;
    Timer t;
    int tick;
    int x, y, width, height;
    
    /**
     * Creates a new CommandZone that will execute the command inside for each object the getter gets
     * Useful for things like conveyor belts which have large zones in which they do a single action
     * @param getter the "getter", or the code that will return the list of objects based on dimensions
     * @param c the "command", or the code that will be sent to each object returned by the getter through the CommandHandler::handleCommand method
     * @param x the x pixel of the rectangle of the getter
     * @param y the y pixel of the rectangle of the getter
     * @param width the width pixel of the rectangle of the getter
     * @param height the height pixel of the rectangle of the getter
     * @param t the Timer that determines when to activate (run the command for each object returned by the getter) based on the tickActivationNumber
     * @param tickActivationNumber the number of ticks the timer has to reach in order to activate
     */
    public CommandZone(ListAcquirer<? extends CommandHandler> getter, Command c, int x, int y, int width, int height, Timer t, int tickActivationNumber) {
	zones.add(this);
	this.c = c;
	this.getter = getter;
	this.t = t;
	this.tick = tickActivationNumber;
    }
    
    public Timer getTimer() {
	return t;
    }
    
    public ListAcquirer<?> getGetter() {
	return getter;
    }
    
    public Command getCommand() {
	return c;
    }
    
    void refresh() {
	if(t != null) {
	    if(t.getCurrentTick() == tick)
		getter.get(x, y, width, height).forEach((CommandHandler h) -> h.handleCommand(c));
	}
    }
}
