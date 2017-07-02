package powerworks.world.level;

import powerworks.block.Block;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class LevelManager {

    public Level genLevel(LevelType type, int w, int h, long s) {
	return type.createInstance(w, h, s);
    }
    
    public boolean onMouseAction(MouseEvent e) {
	int xPixel = InputManager.getMouseLevelXPixel();
	int yPixel = InputManager.getMouseLevelYPixel();
	Block b = Game.getLevel().getBlockFromPixel(xPixel, yPixel);
	if(b != null) {
	    b.onMouseActionOn(e);
	    return true;
	}
	return false;
    }
}
