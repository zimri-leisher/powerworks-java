package powerworks.graphics.gui;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI {

    List<GUIElement> elements = new ArrayList<GUIElement>();
    int xPixel, yPixel;

    protected GUI(int xPixel, int yPixel) {
	this.xPixel = xPixel;
	this.yPixel = yPixel;
    }

    public void setXPixel(int xPixel) {
	this.xPixel = xPixel;
    }

    public void setYPixel(int yPixel) {
	this.yPixel = yPixel;
    }

    public void render() {
	elements.sort((GUIElement e, GUIElement e1) -> {
	    return (e.level > e1.level) ? 1 : (e.level == e1.level) ? 0 : -1;
	});
	elements.forEach((GUIElement e) -> {
	    if (e.active)
		e.render();
	});
    }
}