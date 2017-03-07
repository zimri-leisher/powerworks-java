package powerworks.graphics.gui;

public abstract class GUIElement {

    int level = 0;
    boolean active = true, requiresUpdate = true;
    int xPixel, yPixel;
    GUI parent;

    protected GUIElement(int xPixel, int yPixel, GUI parent) {
	this.parent = parent;
	parent.elements.add(this);
	this.xPixel = xPixel;
	this.yPixel = yPixel;
    }
    
    public void setLevel(int level) {
	this.level = level;
    }

    public void setActive(boolean active) {
	this.active = active;
    }
    
    public void setRequiresUpdate(boolean requiresUpdate) {
	this.requiresUpdate = requiresUpdate;
    }

    public abstract void render();
    
    public abstract void update();
}
