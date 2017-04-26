package powerworks.graphics.screen.gui;

import java.util.ArrayList;
import java.util.List;
import powerworks.graphics.ClickableScreenObject;
import powerworks.main.Game;

public abstract class GUI {

    List<GUIElement> elements = new ArrayList<GUIElement>();
    int xPixel, yPixel, widthPixels, heightPixels;
    boolean open;

    protected GUI(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	this.xPixel = xPixel;
	this.yPixel = yPixel;
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
    }

    public void open() {
	Game.getGUIManager().open(this);
	elements.forEach(GUIElement::open);
	open = true;
    }

    public void close() {
	open = false;
	elements.forEach(GUIElement::close);
	Game.getGUIManager().close(this);
    }

    public void toggle() {
	if (open)
	    close();
	else
	    open();
    }

    public int getXPixel() {
	return xPixel;
    }

    public int getYPixel() {
	return yPixel;
    }

    public int getWidthPixels() {
	return widthPixels;
    }

    public int getHeightPixels() {
	return heightPixels;
    }

    void add(GUIElement element) {
	elements.add(element);
    }

    void remove(GUIElement element) {
	elements.remove(element);
    }

    public void render() {
	elements.stream().sorted((GUIElement e1, GUIElement e2) -> (e1.layer > e2.layer) ? 1 : (e1.layer == e2.layer) ? 0 : -1).forEach(GUIElement::render);
    }
}
