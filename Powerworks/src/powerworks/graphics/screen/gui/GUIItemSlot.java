package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class GUIItemSlot extends GUIElement {

    boolean highlighted = false;
    boolean highlightOnMouseOver = true;
    int index;
    Inventory inv;

    GUIItemSlot(GUI parent, int xPixel, int yPixel, int layer, int index, Inventory inv) {
	super(parent, xPixel, yPixel, 16, 16, layer);
	this.index = index;
	this.inv = inv;
    }

    GUIItemSlot(GUI parent, int xPixel, int yPixel, int layer, int index, Inventory inv, boolean highlightOnMouseOver) {
	super(parent, xPixel, yPixel, 16, 16, layer);
	this.highlightOnMouseOver = highlightOnMouseOver;
	this.index = index;
	this.inv = inv;
    }

    @Override
    public void render() {
	Item item = inv.getItem(index);
	Game.getRenderEngine().renderTexture(Image.ERROR, xPixel + parent.xPixel, yPixel + parent.yPixel);
	if (item != null) {
	    Game.getRenderEngine().renderTexture(item.getTexture(), xPixel + parent.xPixel, yPixel + parent.yPixel);
	    Game.getRenderEngine().renderText(item.getQuantity(), xPixel + parent.xPixel + 1, yPixel + 4 + parent.yPixel);
	}
	if (highlighted)
	    Game.getRenderEngine().renderTexture(Image.ITEM_SLOT_HIGHLIGHT, xPixel + parent.xPixel, yPixel + parent.yPixel);
    }

    @Override
    public void onClick(int xPixel, int yPixel) {
    }

    @Override
    public void whileMouseOver() {
	if (highlightOnMouseOver)
	    highlighted = true;
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
	highlighted = false;
    }

    @Override
    public void onClickOff() {
    }

    @Override
    public void onRelease(int xPixel, int yPixel) {
	
    }
}
