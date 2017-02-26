package powerworks.collidable;

import powerworks.data.PhysicalObject;
import powerworks.data.SpatialOrganizer;

public interface Collidable extends PhysicalObject {

    public static SpatialOrganizer<Collidable> collidables = new SpatialOrganizer<Collidable>();

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
