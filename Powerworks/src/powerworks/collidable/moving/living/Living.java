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

    protected Inventory inv;
    protected InventoryGUI invGUI;
    protected Equipment equips;
    protected LivingType type;
    protected double health, armor;

    /**
     * @param name
     *            the name of the inventory
     * @param background
     *            the background of the inventory
     * @param stretchToFit
     *            whether or not to stretch the background to fit the inventory
     *            width and height
     */
    protected Living(LivingType type, int xPixel, int yPixel) {
	super(xPixel, yPixel, type.getHitbox(), type.getTextures());
	this.type = type;
	inv = new Inventory(type.getInvWidth(), type.getInvHeight());
	if (type.shouldCreateInvGUI()) {
	    invGUI = new InventoryGUI(inv, Image.Utils.getImage(Image.Utils.genRectangle(type.getInvWidth() * 18 + 10, type.getInvHeight() * 18 + 10)), type.getName());
	}
    }
    
    @Override
    public void addToLevel() {
	super.addToLevel();
	System.out.println("adding living to " + currentChunk.toString());
	currentChunk.getLivingEntities().add(this);
    }

    public Inventory getInventory() {
	return inv;
    }

    public AI getAI() {
	return type.getAI();
    }

    public InventoryGUI getInvGUI() {
	return (invGUI == null) ? invGUI = new InventoryGUI(inv, Image.Utils.getImage(Image.Utils.genRectangle(type.getInvWidth() * 18 + 20, type.getInvHeight() * 18 + 20)), type.getName()) : invGUI;
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
    
    @Override
    public void removeFromLevel() {
	super.removeFromLevel();
	currentChunk.getLivingEntities().remove(this);
    }

    @Override
    public void remove() {
	super.remove();
	inv.unload();
	inv = null;
	invGUI.remove();
	invGUI = null;
	equips.unload();
	equips = null;
	type = null;
    }

    @Override
    public String toString() {
	return "Living object at " + xPixel + ", " + yPixel + ", with a health value of " + health + "/" + type.getMaxHealth() + ", with equipment "
		+ equips.toString() + " and AI " + getAI().toString();
    }
}