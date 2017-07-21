package powerworks.graphics.screen.gui;

import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.inventory.Inventory;
import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class InventoryGUI extends GUI {

    Inventory inv;
    GUIItemSlot[] items;
    GUITexturePane background;
    GUIText name;
    GUIDragGrip grip;
    
    public InventoryGUI(Inventory inv, Texture background, String name) {
	super((Game.getScreenWidth() - background.getWidthPixels()) / 2, (Game.getScreenHeight() - background.getHeightPixels()) / 2, 1);
	int width = inv.getWidth() * 18 + 10;
	int height = inv.getHeight() * 18 + 10;
	items = new GUIItemSlot[inv.getWidth() * inv.getHeight()];
	for(int y = 0; y < inv.getHeight(); y++) {
	    for(int x = 0; x < inv.getWidth(); x++) {
		items[x + y * inv.getWidth()] = new GUIItemSlot(this, 5 + 18 * x, 5 + 18 * y, 16, 16, this.layer + 2, inv, x + y * inv.getWidth(), false);
	    }
	}
	this.background = new GUITexturePane(this, 0, 0, width, height, this.layer + 1, background);
	this.grip = new GUIDragGrip(this, width - 5, 1, this.layer + 2);
	this.name = new GUIText(this, 2, 6, this.layer + 2, name);
    }
    
    @Override
    public void onClose() {
	super.onClose();
	if(!Game.getScreenManager().getScreenObjects().stream().filter(ScreenObject::isOpen).anyMatch(c -> c instanceof InventoryGUI)) {
	    Item i = Game.getMouse().getHeldItem();
	    if(i != null) {
		Game.getMainPlayer().getInventory().giveItem(i);
		Game.getMouse().setHeldItem(null);
	    }
	}
    }
    
    @Override
    public void remove() {
	super.remove();
	inv = null;
	items = null;
	background = null;
	name = null;
	grip = null;
    }
}
