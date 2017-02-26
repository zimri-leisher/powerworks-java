package powerworks.moving.entity;

import powerworks.collidable.Hitbox;
import powerworks.data.SpatialOrganizer;
import powerworks.graphics.StaticTextureCollection;
import powerworks.graphics.Texture;
import powerworks.graphics.TexturedObject;
import powerworks.moving.Moving;

public abstract class Entity extends Moving implements TexturedObject {

    public static final SpatialOrganizer<Entity> entities = new SpatialOrganizer<Entity>();
    int dir;
    StaticTextureCollection textures;

    protected Entity(Hitbox hitbox) {
	super(hitbox);
	entities.add(this);
    }
    
    @Override
    protected void move() {
	if (velX > 0 && dir != 1)
	    dir = 1;
	if (velX < 0 && dir != 3)
	    dir = 3;
	if (velY > 0 && dir != 2)
	    dir = 2;
	if (velY < 0 && dir != 0)
	    dir = 0;
	super.move();
    }

    @Override
    public Texture getTexture() {
	return textures.get(dir);
    }
}