package powerworks.moving.living;

import powerworks.ai.AI;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.screen.gui.InventoryGUI;
import powerworks.inventory.Inventory;
import powerworks.main.Game;
import powerworks.moving.Moving;

public abstract class Living extends Moving {

    protected AI ai;
    protected Inventory inv;
    protected InventoryGUI invGUI;
    protected Equipment equips;
    protected double health, armor;
    
    protected Living(int xPixel, int yPixel, Hitbox hitbox, Inventory inv) {
	this(xPixel, yPixel, hitbox, 20);
	this.inv = inv;
	invGUI = new InventoryGUI(10, 10, 180, 55, inv, Image.PLAYER_INVENTORY, "test");
    }
    
    protected Living(int xPixel, int yPixel, Hitbox hitbox, int footstepDistance) {
	super(xPixel, yPixel, hitbox);
	Game.getLevel().getLivingEntities().add(this);
    }
}