package powerworks.collidable.moving.living;

import powerworks.ai.AI;
import powerworks.collidable.Hitbox;
import powerworks.collidable.moving.Moving;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.gui.InventoryGUI;
import powerworks.inventory.Inventory;
import powerworks.main.Game;

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
	invGUI = new InventoryGUI(inv, Image.PLAYER_INVENTORY, "Inventory");
    }
    
    protected Living(int xPixel, int yPixel, Hitbox hitbox, int footstepDistance) {
	super(xPixel, yPixel, hitbox);
	Game.getLevel().getLivingEntities().add(this);
    }
    
    public Inventory getInventory() {
	return inv;
    }
    
    public AI getAI() {
	return ai;
    }
    
    public InventoryGUI getInvGUI() {
	return invGUI;
    }
    
    public Equipment getEquipment() {
	return equips;
    }
    
    public double getHealth() {
	return health;
    }
    
    public double getArmor() {
	return armor;
    }
}