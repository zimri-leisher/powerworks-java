package powerworks.graphics;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum ImageCollection {

    CURSOR_RIGHT_CLICK("/textures/cursor/cursor_right_click_anim.png", 8), 
    GRASS_TILE("/textures/tiles/grass.png", 4), 
    GRASS_IRON_ORE_TILE("/textures/tiles/grass_iron_ore.png", 3), 
    CONVEYOR_BELT_CONNECTED_LEFT("/textures/block/conveyor_belt_connected_left_anim.png", 2), 
    CONVEYOR_BELT_CONNECTED_UP("/textures/block/conveyor_belt_connected_up_anim.png", 4), 
    PLAYER("/textures/player/player.png", 4);
    
    private ImageWrapper[] texture;
    private int width, height;
    
    private ImageCollection(String path, int numberOfImages) {
	try {
	    BufferedImage image = ImageIO.read(ImageCollection.class.getResource(path));
	    int width = image.getWidth() / numberOfImages;
	    GraphicsConfiguration conf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	    texture = new ImageWrapper[numberOfImages];
	    for(int i = 0; i < numberOfImages; i++) {
		BufferedImage sub = image.getSubimage(width * i, 0, width, image.getHeight());
		BufferedImage newImage = new BufferedImage(sub.getColorModel(), sub.getRaster().createCompatibleWritableRaster(width, image.getHeight()), image.isAlphaPremultiplied(), null);
		BufferedImage dest = conf.createCompatibleImage(newImage.getWidth(), newImage.getHeight(), newImage.getTransparency());
		sub.copyData(newImage.getRaster());
		Graphics2D g = dest.createGraphics();
		g.drawImage(newImage, 0, 0, null);
		g.dispose();
		texture[i] = new ImageWrapper(dest);
	    }
	    this.width = width;
	    this.height = image.getHeight();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    static class ImageWrapper implements Texture{
	BufferedImage image;
	
	private ImageWrapper(BufferedImage image) {
	    this.image = image;
	}

	@Override
	public BufferedImage getImage() {
	    return image;
	}

	@Override
	public int getWidthPixels() {
	    return image.getWidth();
	}

	@Override
	public int getHeightPixels() {
	    return image.getHeight();
	}
    }
    
    public Texture getTexture(int index) {
	return texture[index];
    }
    
    public int getWidthPixels() {
	return width;
    }
    
    public int getHeightPixels() {
	return height;
    }
    
    public Texture[] getTextures() {
	return texture;
    }
}
