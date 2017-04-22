package powerworks.particle;

import powerworks.graphics.Texture;
import powerworks.world.level.LevelObject;

public class Particle extends LevelObject{

    int velX, velY;
    
    protected Particle(int xPixel, int yPixel) {
	super(xPixel, yPixel);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
	
    }

    @Override
    public void update() {
	
    }

    @Override
    public void remove() {
	
    }
}
