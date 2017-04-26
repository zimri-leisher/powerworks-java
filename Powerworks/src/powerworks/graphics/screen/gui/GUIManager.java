package powerworks.graphics.screen.gui;

import java.util.ArrayList;
import java.util.List;
import powerworks.data.GeometryHelper;
import powerworks.io.InputManager;

public class GUIManager {

    List<GUI> guis = new ArrayList<GUI>();

    void open(GUI gui) {
	guis.add(gui);
    }

    void close(GUI gui) {
	guis.remove(gui);
    }

    public List<GUI> getOpenGUIsAt(int xPixel, int yPixel) {
	List<GUI> returnObj = new ArrayList<GUI>();
	guis.stream().filter((GUI gui) -> {
	    return GeometryHelper.contains(gui.getXPixel(), gui.getYPixel(), gui.getWidthPixels(), gui.getHeightPixels(), xPixel, yPixel, 0, 0);
	}).forEach(returnObj::add);
	return returnObj;
    }

    public List<GUI> getAllOpenGUIs() {
	return guis;
    }
    
    public List<GUIElement> getGUIElementsAt(int xPixel, int yPixel) {
	List<GUIElement> returnObj = new ArrayList<GUIElement>();
	for (GUI gui : guis) {
	    if (GeometryHelper.contains(gui.getXPixel(), gui.getYPixel(), gui.getWidthPixels(), gui.getHeightPixels(), xPixel, yPixel, 0, 0)) {
		for (GUIElement el : gui.elements) {
		    if (GeometryHelper.contains(el.getXPixel() + gui.getXPixel(), el.getYPixel() + gui.getYPixel(), el.getWidthPixels(), el.getHeightPixels(), xPixel, yPixel, 0, 0))
			returnObj.add(el);
		}
	    }
	}
	return returnObj;
    }

    public void render() {
	guis.forEach(GUI::render);
    }
}