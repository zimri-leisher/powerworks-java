package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ClickableScreenObject;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;

public class GUIOutline extends GUIElement {

    public GUIOutline(ScreenObject parent, int widthPixels, int heightPixels, int color) {
	super(parent, 0, 0, widthPixels, heightPixels, parent.getLayer() + 1);
    }

    public GUIOutline(ClickableScreenObject parent, int color) {
	super(parent, 0, 0, parent.getWidthPixels(), parent.getHeightPixels(), parent.getLayer() + 1);
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void render() {
	super.render();
    }

    @Override
    public Texture getTexture() {
	return null;
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
