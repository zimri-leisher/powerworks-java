package powerworks.io;


public class MouseMovementDetector {
    boolean screen = true, level = true;
    
    public boolean hasMovedRelativeToScreen() {
	if(screen) {
	    screen = false;
	    return true;
	}
	return false;
    }
    
    public boolean hasMovedRelativeToLevel() {
	if(level) {
	    level = false;
	    return true;
	}
	return false;
    }
    
    void setScreen(boolean screen) {
	this.screen = screen;
    }
    
    void setLevel(boolean level) {
	this.level = level;
    }
}