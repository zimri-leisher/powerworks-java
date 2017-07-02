package powerworks.collidable.moving.living;

import powerworks.ai.AI;
import powerworks.collidable.Hitbox;
import powerworks.graphics.ImageCollection;

public class LivingType {
    
    public static final LivingType PLAYER = new LivingType("Player", 100, null, 8, 4, true, Hitbox.PLAYER, ImageCollection.PLAYER);
    
    String name;
    double health;
    AI ai;
    int invWidth, invHeight;
    boolean createInvGUI;
    Hitbox hitbox;
    ImageCollection textures;
    
    /**
     * @param createInvGUI whether or not to create and load appropriate images for this inventory (may save memory if the inventory goes unopened normally)
     */
    private LivingType(String name, double health, AI ai, int invWidth, int invHeight, boolean createInvGUI, Hitbox hitbox, ImageCollection textures) {
	this.name = name;
	this.health = health;
	this.ai = ai;
	this.invWidth = invWidth;
	this.invHeight = invHeight;
	this.createInvGUI = createInvGUI;
	this.hitbox = hitbox;
	this.textures = textures;
    }
    
    public ImageCollection getTextures() {
	return textures;
    }
    
    public String getName() {
	return name;
    }
    
    public double getMaxHealth() {
	return health;
    }
    
    public AI getAI() {
	return ai;
    }
    
    public int getInvWidth() {
	return invWidth;
    }
    
    public int getInvHeight() {
	return invHeight;
    }
    
    public boolean shouldCreateInvGUI() {
	return createInvGUI;
    }
    
    public Hitbox getHitbox() {
	return hitbox;
    }
}