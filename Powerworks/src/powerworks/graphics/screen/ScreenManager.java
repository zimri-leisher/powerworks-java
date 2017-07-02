package powerworks.graphics.screen;

import java.util.ArrayList;
import java.util.List;
import powerworks.data.GeometryHelper;
import powerworks.io.InputManager;
import powerworks.io.MouseEvent;

public class ScreenManager {

    List<ScreenObject> objects = new ArrayList<ScreenObject>();
    List<ClickableScreenObject> clickableObjects = new ArrayList<ClickableScreenObject>();
    ClickableScreenObject current;

    /**
     * @return true if a clickable screen object was clicked on
     */
    public boolean onMouseAction(MouseEvent mouse) {
	boolean used = false;
	int xPixel = mouse.getXPixel();
	int yPixel = mouse.getYPixel();
	List<ClickableScreenObject> o = new ArrayList<ClickableScreenObject>(clickableObjects);
	for (ClickableScreenObject cObj : o) {
	    if (cObj.isOpen()) {
		if (GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), xPixel, yPixel, 0, 0)) {
		    used = true;
		    cObj.onMouseActionOn(mouse);
		} else
		    cObj.onMouseActionOff(mouse);
	    }
	}
	
	return used;
    }

    public boolean onMouseScroll(int scroll) {
	boolean used = false;
	int xPixel = InputManager.getMouseXPixel();
	int yPixel = InputManager.getMouseYPixel();
	List<ClickableScreenObject> o = new ArrayList<ClickableScreenObject>(clickableObjects);
	for (ClickableScreenObject cObj : o) {
	    if (cObj.isOpen()) {
		if (GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), xPixel, yPixel, 0, 0)) {
		    used = true;
		    cObj.onScrollOn(scroll);
		}
	    }
	}
	return used;
    }

    /**
     * @return the screen object which is currently being interacted with
     */
    public ScreenObject getCurrentScreenObject() {
	return current;
    }

    public void update() {
	int mXPixel = InputManager.getMouseXPixel();
	int mYPixel = InputManager.getMouseYPixel();
	clickableObjects.stream().sorted((obj, obj2) -> obj.layer > obj2.layer ? 1 : obj.layer == obj2.layer ? 0 : -1).forEach(cObj -> {
	    if (cObj.isOpen()) {
		if (GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), mXPixel, mYPixel, 0, 0)) {
		    if (!cObj.isMouseOn()) {
			cObj.setMouseOn(true);
			cObj.onMouseEnter();
		    }
		} else if (cObj.isMouseOn()) {
		    cObj.setMouseOn(false);
		    cObj.onMouseLeave();
		}
	    }
	});
	objects.forEach(ScreenObject::update);
    }

    public void render() {
	ScreenObject.getNoParentObject().render();
    }

    public List<ClickableScreenObject> getClickableScreenObjectsAt(int xPixel, int yPixel) {
	List<ClickableScreenObject> returnObj = new ArrayList<ClickableScreenObject>();
	clickableObjects.stream().filter(
		(ClickableScreenObject cObj) -> cObj.isOpen() && GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), xPixel, yPixel, 0, 0))
		.forEach(returnObj::add);
	return returnObj;
    }

    public List<ScreenObject> getScreenObjects() {
	return objects;
    }

    public List<ClickableScreenObject> getClickableScreenObjects() {
	return clickableObjects;
    }
}
