package powerworks.graphics.gui;

import powerworks.graphics.StaticTexture;
import powerworks.main.Game;

public class PlayerInventoryGUI extends InventoryGUI{

    public PlayerInventoryGUI() {
	super(82, 11, 4, 32, 16, 16, "Inventory", 0xFFFFFF, 5, 30, StaticTexture.PLAYER_INVENTORY);
    }
}