package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.graphics.RenderParams;
import powerworks.graphics.Renderer;
import powerworks.graphics.Texture;
import powerworks.inventory.item.Item;
import powerworks.io.InputManager;
import powerworks.main.Game;

public class Mouse extends ScreenObject {

    Texture texture = Image.CURSOR_DEFAULT;
    Item heldItem = null;

    public Mouse() {
	super(InputManager.getMouseXPixel(), InputManager.getMouseYPixel(), /*
									     * Always
									     * on
									     * top
									     */ Integer.MAX_VALUE);
	open();
    }

    @Override
    public float getScale() {
	return 0.5f;
    }

    @Override
    public void render() {
	Renderer r = Game.getRenderEngine();
	if (heldItem != null) {
	    r.renderTexture(heldItem.getTexture(), xPixel - 9, yPixel);
	    r.renderText(heldItem.getQuantity(), xPixel - 8, yPixel + 4);
	}
	r.renderTexture(texture, xPixel, yPixel, new RenderParams().setScale(0.5f));
    }

    public void setTexture(Texture texture) {
	this.texture = texture;
    }

    public Item getHeldItem() {
	return heldItem;
    }

    public void setHeldItem(Item item) {
	this.heldItem = item;
    }

    @Override
    public void update() {
	xPixel = InputManager.getMouseXPixel();
	yPixel = InputManager.getMouseYPixel();
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public void remove() {
	super.remove();
	texture = null;
	heldItem = null;
    }
}
