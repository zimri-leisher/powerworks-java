package powerworks.graphics.screen.gui;

import powerworks.data.GeometryHelper;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ClickableScreenObject;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;

public class GUIElementList extends GUIElement {

    private int scrollPos;

    protected GUIElementList(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
	for (ScreenObject c : children) {
	    if (c instanceof ClickableScreenObject) {
		ClickableScreenObject cObj = (ClickableScreenObject) c;
		if (GeometryHelper.intersects(xPixel, yPixel, widthPixels, heightPixels, cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels())) {
		    cObj.render();
		}
	    }
	}
    }

    @Override
    public void update() {
    }

    @Override
    protected void onOpen() {
    }

    @Override
    protected void onClose() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }
}
