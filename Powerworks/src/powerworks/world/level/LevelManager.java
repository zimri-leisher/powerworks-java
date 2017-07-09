package powerworks.world.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import powerworks.collidable.Collidable;
import powerworks.collidable.block.Block;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class LevelManager {

    List<Collidable> mouseOn = new ArrayList<Collidable>();
    Block lastBlock;
    Level currentLevel;

    public Level genLevel(LevelType type, int w, int h, long s) {
	return type.createInstance(w, h, s);
    }

    public Level getLevel() {
	return currentLevel;
    }

    public void setCurrentLevel(Level l) {
	currentLevel = l;
    }

    public boolean onMouseAction(MouseEvent e) {
	int xPixel = InputManager.getMouseLevelXPixel();
	int yPixel = InputManager.getMouseLevelYPixel();
	List<Collidable> cols = Game.getLevel().getIntersectingCollidables(xPixel, yPixel, 0, 0, c -> !(c instanceof Block));
	Block b = Game.getLevel().getBlockFromPixel(xPixel, yPixel);
	boolean used = false;
	if (b != null) {
	    b.onMouseActionOn(e);
	    used = true;
	}
	for (Collidable c : cols) {
	    c.onMouseActionOn(e);
	    used = true;
	}
	return used;
    }

    public void update() {
	int xPixel = InputManager.getMouseLevelXPixel();
	int yPixel = InputManager.getMouseLevelYPixel();
	List<Collidable> cols = Game.getLevel().getIntersectingCollidables(xPixel, yPixel, 0, 0, c -> !(c instanceof Block));
	Block b = Game.getLevel().getBlockFromPixel(xPixel, yPixel);
	if (b != null) {
	    if (!b.isMouseOn()) {
		b.setMouseOn(true);
	    }
	}
	for (Collidable c : cols) {
	    if (!c.isMouseOn()) {
		c.setMouseOn(true);
	    }
	}
	for (Collidable c : mouseOn) {
	    if (!cols.contains(c)) {
		c.setMouseOn(false);
	    }
	}
	if (lastBlock != b) {
	    if (lastBlock != null)
		lastBlock.setMouseOn(false);
	    lastBlock = b;
	}
	currentLevel.update();
	mouseOn = cols;
    }

    public void render() {
	currentLevel.render();
    }

}
