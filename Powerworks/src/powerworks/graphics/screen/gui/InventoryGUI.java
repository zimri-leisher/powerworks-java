package powerworks.graphics.screen.gui;

import java.awt.font.FontRenderContext;
import powerworks.graphics.Texture;
import powerworks.inventory.Inventory;
import powerworks.main.Game;

public class InventoryGUI extends GUI {

    Inventory inv;
    GUITexturePane background;
    GUIText name;
    GUIItemSlot[] items;

    public InventoryGUI(int xPixel, int yPixel, int widthPixels, int heightPixels, Inventory inv, Texture background, String name, boolean stretchToFit) {
	super(xPixel, yPixel, widthPixels, heightPixels);
	this.inv = inv;
	this.background = new GUITexturePane(this, 0, 0, widthPixels, heightPixels, 0, background, stretchToFit);
	this.name = new GUIText(this, 2, 5, (int) Game.getFont(28).getStringBounds(name, new FontRenderContext(null, false, false)).getWidth() / Game.getScreenScale(), 9, 3, name, 0xFFFFFF);
	items = new GUIItemSlot[inv.getSize()];
	for (int y = 0; y < inv.getHeight(); y++) {
	    for (int x = 0; x < inv.getWidth(); x++) {
		items[x + y * inv.getWidth()] = new GUIItemSlot(this, 6 + x * 18, 6 + y * 18, 1, x + y * inv.getWidth(), inv);
	    }
	}
    }

    @Override
    public String toString() {
	return "Inventory GUI at " + xPixel + ", " + yPixel + ", width: " + widthPixels + ", height: " + heightPixels + ", inventory: " + inv.toString();
    }
}