package powerworks.graphics;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import powerworks.exception.InvalidResourceException;
import powerworks.main.Game;

public enum ImageCollection {
    CURSOR_RIGHT_CLICK("/textures/cursor/cursor_right_click_anim.png", 8), 
    GRASS_TILE("/textures/tiles/grass.png", 4), 
    GRASS_IRON_ORE_TILE("/textures/tiles/grass_iron_ore.png",3), 
    CONVEYOR_BELT_CONNECTED_LEFT("/textures/blocks/conveyor_belt_connected_left_anim.png", 2), 
    CONVEYOR_BELT_CONNECTED_UP("/textures/blocks/conveyor_belt_connected_up_anim.png", 4), 
    PLAYER("/textures/player/player.png", 4);

    int width, height;
    int[][] pixels;
    boolean[] hasTransparency;

    private ImageCollection(String path, int numberOfFrames) {
	try {
	    load(path, numberOfFrames);
	} catch (InvalidResourceException e) {
	    e.printStackTrace();
	}
    }
    
    public int getWidth() {
	return width;
    }
    
    public int getHeight() {
	return height;
    }
    
    public int[][] getPixels() {
	return pixels;
    }
    
    private void load(String path, int numberOfFrames) throws InvalidResourceException {
	System.out.println("Loading ImageCollection " + toString());
	try {
	    hasTransparency = new boolean[numberOfFrames];
	    BufferedImage image = ImageIO.read(ImageCollection.class.getResource(path));
	    if((image.getWidth() / numberOfFrames) % 1 != 0) {
		Game.logger.error("ImageCollection " + toString() + " may not be sized properly, as the image cannot be divided equally");
		throw new InvalidResourceException();
	    }
	    width = image.getWidth() / numberOfFrames;
	    height = image.getHeight();
	    pixels = new int[numberOfFrames][width * height];
	    while (numberOfFrames > 0) {
		numberOfFrames--;
		image.getRGB(width * numberOfFrames, 0, width, height, pixels[numberOfFrames], 0, width);
		hasTransparency[numberOfFrames] = image.getTransparency() != Transparency.OPAQUE;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}