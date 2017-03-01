package powerworks.graphics.gui;

import powerworks.graphics.StaticTexture;
import powerworks.main.Game;

public class PlayerInventoryGUI extends InventoryGUI{

    public PlayerInventoryGUI() {
	super(86, 11, 20, 20, 0, 0, "Inventory", 0xFFFFFF, 5, 30, StaticTexture.PLAYER_INVENTORY);
    }
}