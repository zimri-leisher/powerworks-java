package powerworks.graphics.screen.gui;

import powerworks.collidable.moving.Moving;
import powerworks.graphics.Renderer;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.MouseEvent;
import powerworks.main.Game;

public class GUICamera extends GUIElement {

    private Moving camera;
    
    protected GUICamera(ScreenObject parent, int xPixel, int yPixel, int widthPixels, int heightPixels, int layer) {
	super(parent, xPixel, yPixel, widthPixels, heightPixels, layer);
    }

    @Override
    public void onMouseActionOn(MouseEvent mouse) {
    }

    @Override
    public void onMouseActionOff(MouseEvent mouse) {
    }

    @Override
    public void render() {
	Renderer r = Game.getRenderEngine();
	r.setOffset(camera.getXPixel() - r.getWidthPixels() / 2, camera.getYPixel() - r.getHeightPixels() / 2);
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
