package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.graphics.ScreenObject;
import powerworks.graphics.SyncAnimation;
import powerworks.graphics.Texture;
import powerworks.inventory.item.Item;
import powerworks.io.InputManager;
import powerworks.main.Game;

public class Mouse extends ScreenObject {

    Texture texture = Image.CURSOR_DEFAULT;
    Item heldItem = null;
    
    protected Mouse() {
	super(InputManager.getMouseXPixel(), InputManager.getMouseYPixel());
    }

    @Override
    public float getScale() {
	return 0.5f;
    }
    
    @Override
    public void render() {
	Game.getRenderEngine().renderScreenObject(this);
    }

    @Override
    public Texture getTexture() {
	return texture;
    }

    @Override
    public void update() {
	xPixel = InputManager.getMouseXPixel();
	yPixel = InputManager.getMouseYPixel();
	if(InputManager.getMouseButton() == 3) {
	    texture = SyncAnimation.CURSOR_RIGHT_CLICK;
	    SyncAnimation.CURSOR_RIGHT_CLICK.play();
	} else if(InputManager.getMouseButton() == 1) {
	    texture = Image.CURSOR_LEFT_CLICK;
	    SyncAnimation.CURSOR_RIGHT_CLICK.reset();
	} else {
	    SyncAnimation.CURSOR_RIGHT_CLICK.reset();
	    texture = Image.CURSOR_DEFAULT;
	}
    }

    @Override
    public void onScreenSizeChange() {}
}
