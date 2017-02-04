package powerworks.collidable;

import powerworks.data.PhysicalObject;
import powerworks.data.Quadtree;
import powerworks.level.Level;

public interface Collidable extends PhysicalObject{
    
    public static Quadtree<Collidable> collidables = new Quadtree<Collidable>(0, 0, Level.level.getWidthPixels(), Level.level.getHeightPixels());
    
    /**
     * Gets the hitbox of the collidable
     * 
     * @return the hitbox
     */
    public Hitbox getHitbox();

    /**
     * Renders the hitbox
     */
    public void renderHitbox();
}
