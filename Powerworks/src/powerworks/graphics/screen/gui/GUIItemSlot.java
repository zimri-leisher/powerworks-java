package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.Mouse;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class GUIItemSlot extends GUIElement {

    boolean display = false;
    int index;
    Inventory inv;

    GUIItemSlot(GUI parent, int xPixel, int yPixel, int layer, int index, Inventory inv) {
	super(parent, xPixel, yPixel, 16, 16, layer);
	this.index = index;
	this.inv = inv;
    }

    GUIItemSlot(GUI parent, int xPixel, int yPixel, int layer, int index, Inventory inv, boolean display) {
	super(parent, xPixel, yPixel, 16, 16, layer);
	this.index = index;
	this.display = display;
	this.inv = inv;
    }

    @Override
    public void render() {
	Item item = getItem();
	if (item != null) {
	    Game.getRenderEngine().renderTexture(item.getTexture(), xPixel, yPixel);
	    Game.getRenderEngine().renderText(item.getQuantity(), xPixel + 1, yPixel + 4);
	}
	if (mouseOn && !display)
	    Game.getRenderEngine().renderTexture(Image.ITEM_SLOT_HIGHLIGHT, xPixel, yPixel);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
	if(display)
	    return;
	Mouse m = Game.getMouse();
	Item i = getItem();
	Item mI = m.getHeldItem();
	if (i != null) {
	    if (mI == null) {
		m.setHeldItem(new Item(i.getType(), i.getQuantity()));
		inv.takeItem(i);
	    } else if (m.getHeldItem().getType() == i.getType()) {
		if (mI.getQuantity() + i.getQuantity() > i.getMaxStack()) {
		    mI.setQuantity(mI.getQuantity() - (i.getMaxStack() - i.getQuantity()));
		    i.setQuantity(i.getMaxStack());
		} else {
		    int quant = i.getQuantity();
		    i.setQuantity(i.getQuantity() + mI.getQuantity());
		    mI.setQuantity(mI.getQuantity() - quant);
		}
	    } else {
		Item temp = mI;
		m.setHeldItem(i);
		inv.takeItem(i);
		inv.giveItem(temp);
	    }
	} else {
	    if (mI != null) {
		inv.giveItem(mI);
		m.setHeldItem(null);
	    }
	}
    }

    public Item getItem() {
	return inv.getItem(index);
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void onScreenSizeChange() {
    }

    @Override
    public void update() {
    }

    @Override
    public void onClickOff() {
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
    }

    @Override
    public String toString() {
	return "GUI item slot at " + xPixel + ", " + yPixel + ", width: " + widthPixels + ", height: " + heightPixels + ", open: " + open + ", parent GUI: " + parent.toString();
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }
}
