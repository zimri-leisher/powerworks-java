package powerworks.graphics;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageCollection {

    public static final ImageCollection CURSOR_RIGHT_CLICK = new ImageCollection("/textures/screen/cursor/cursor_right_click_anim.png", 8); 
    public static final ImageCollection GRASS_TILE = new ImageCollection("/textures/level/tile/grass.png", 4); 
    public static final ImageCollection GRASS_IRON_ORE_TILE = new ImageCollection("/textures/level/tile/grass_iron_ore.png", 3); 
    public static final ImageCollection CONVEYOR_BELT_CONNECTED_LEFT = new ImageCollection("/textures/level/block/machine/conveyor_belt_connected_left_anim.png", 2); 
    public static final ImageCollection CONVEYOR_BELT_CONNECTED_UP = new ImageCollection("/textures/level/block/machine/conveyor_belt_connected_up_anim.png", 4);
    public static final ImageCollection PLAYER = new ImageCollection("/textures/level/moving/living/player/player.png", 4);
    
    private Texture[] textures;
    private int width, height;
    
    private ImageCollection(String path, int numberOfImages) {
	try {
	    BufferedImage image = ImageIO.read(ImageCollection.class.getResource(path));
	    int width = image.getWidth() / numberOfImages;
	    GraphicsConfiguration conf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	    textures = new ImageWrapper[numberOfImages];
	    for(int i = 0; i < numberOfImages; i++) {
		BufferedImage sub = image.getSubimage(width * i, 0, width, image.getHeight());
		BufferedImage newImage = new BufferedImage(sub.getColorModel(), sub.getRaster().createCompatibleWritableRaster(width, image.getHeight()), image.isAlphaPremultiplied(), null);
		BufferedImage dest = conf.createCompatibleImage(newImage.getWidth(), newImage.getHeight(), newImage.getTransparency());
		sub.copyData(newImage.getRaster());
		Graphics2D g = dest.createGraphics();
		g.drawImage(newImage, 0, 0, null);
		g.dispose();
		textures[i] = new ImageWrapper(dest);
	    }
	    this.width = width;
	    this.height = image.getHeight();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Assumes all are the same dimensions
     */
    private ImageCollection(Texture[] textures) {
	this.textures = textures;
	width = textures[0].getWidthPixels();
	height = textures[0].getHeightPixels();
    }
    
    /**
     * Assumes all are the same dimensions
     */
    public static ImageCollection createCollection(Texture...textures) {
	return new ImageCollection(textures);
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
	return textures[index];
    }
    
    public int getWidthPixels() {
	return width;
    }
    
    public int getHeightPixels() {
	return height;
    }
    
    public Texture[] getTextures() {
	return textures;
    }
    
    public int size() {
	return textures.length;
    }
}
