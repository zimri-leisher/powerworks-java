package powerworks.moving.living;

import powerworks.ai.AI;
import powerworks.collidable.Hitbox;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
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
    
    /**
     * @param name the name of the inventory
     * @param background the background of the inventory
     * @param stretchToFit whether or not to stretch the background to fit the inventory width and height
     */
    protected Living(int xPixel, int yPixel, Hitbox hitbox, Inventory inv, String name, Texture background, boolean stretchToFit) {
	this(xPixel, yPixel, hitbox, 20);
	this.inv = inv;
	int width = inv.getWidth() * 18 + 10;
	int height = inv.getHeight() * 18 + 10;
	invGUI = new InventoryGUI((Game.getScreenWidth() - width) / 2, 10, width, height, inv, background, name, true);
    }
    
    protected Living(int xPixel, int yPixel, Hitbox hitbox, int footstepDistance) {
	super(xPixel, yPixel, hitbox);
	Game.getLevel().getLivingEntities().add(this);
    }
}