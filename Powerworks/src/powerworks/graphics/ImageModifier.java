package powerworks.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import powerworks.main.Game;

public class ImageModifier {

    public static final int SCALE = 0;
    public static final int SCALE_WIDTH = 1;
    public static final int SCALE_HEIGHT = 2;
    /**
     * Sets alpha for entire image, value is from 0-255, 0 being fully transparent, 255 being opaque
     */
    public static final int SET_ALPHA = 3;
    /**
     * Converts entire image entirely to red. Value is ignored
     */
    public static final int TO_RED = 4;
    /**
     * Converts entire image entirely to green. Value is ignored
     */
    public static final int TO_GREEN = 5;
    /**
     * Converts entire image entirely to blue. Value is ignored
     */
    public static final int TO_BLUE = 6;
    /**
     * Value is from 0-255, 0 being no red, 255 being full red
     */
    public static final int SET_RED = 7;
    /**
     * Value is from 0-255, 0 being no green, 255 being full green
     */
    public static final int SET_GREEN = 8;
    /**
     * Value is from 0-255, 0 being no blue, 255 being full blue
     */
    public static final int SET_BLUE = 9;

    /**
     * Modifies an image based on the key and value pair inputted
     * @param image image to modify
     * @param key the key from ImageModifier. Ex: <tt> ImageModifier.SCALE </tt>
     * @param value the value. What it does depends on key, check key description for more information
     * @param interpolationType key from the AffineTransformOp class (<tt>TYPE_BICUBIC, TYPE_BILINEAR </tt>and<tt> TYPE_NEAREST_NEIGHBOR</tt>)
     * @return the new BufferedImage in compatible form to the default graphics configuration
     */
    public static BufferedImage modify(Image image, int key, double value, int interpolationType) {
	return modify(image.getImage(), key, value, interpolationType);
    }
    
    /**
     * Modifies an image based on the key and value pair inputted
     * @param image image to modify
     * @param key the key from ImageModifier. Ex: <tt> ImageModifier.SCALE </tt>
     * @param value the value. What it does depends on key, check key description for more information. If it involves scaling/other such transforms, nearest neighbor will be used
     * @return the new BufferedImage in compatible form to the default graphics configuration
     */
    public static BufferedImage modify(Image image, int key, double value) {
	return modify(image, key, value, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }
    
    /**
     * Modifies an image based on the key and value pair inputted
     * @param image image to modify
     * @param key the key from ImageModifier. Ex: <tt> ImageModifier.SCALE </tt>
     * @param value the value. What it does depends on key, check key description for more information. If it involves scaling/other such transforms, nearest neighbor will be used
     * @return the new BufferedImage in compatible form to the default graphics configuration
     */
    public static BufferedImage modify(Texture texture, int key, double value) {
	return modify(texture.getImage(), key, value, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }
    
    public static BufferedImage modify(BufferedImage image, int key, double value) {
	return modify(image, key, value, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    }
    
    /**
     * @param interpolationType
     *            the key from the AffineTransformOp class
     */
    public static BufferedImage modify(BufferedImage image, int key, double value, int interpolationType) {
	BufferedImage newImg = null;
	if (key == SCALE) {
	    newImg = Game.getGraphicsConf().createCompatibleImage((int) (image.getWidth() * value), (int) (image.getHeight() * value), Transparency.TRANSLUCENT);
	    Graphics2D g2d = newImg.createGraphics();
	    g2d.drawImage(image, 0, 0, (int) (image.getWidth() * value), (int) (image.getHeight() * value), null);
	    g2d.dispose();
	} else if (key == SCALE_WIDTH) {
	    newImg = Game.getGraphicsConf().createCompatibleImage((int) (image.getWidth() * value), image.getHeight(), Transparency.TRANSLUCENT);
	    Graphics2D g2d = newImg.createGraphics();
	    g2d.drawImage(image, 0, 0, (int) (image.getWidth() * value), image.getHeight(), null);
	    g2d.dispose();
	} else if (key == SCALE_HEIGHT) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), (int) (image.getHeight() * value), Transparency.TRANSLUCENT);
	    Graphics2D g2d = newImg.createGraphics();
	    g2d.drawImage(image, 0, 0, image.getWidth(), (int) (image.getHeight() * value), null);
	    g2d.dispose();
	} else if (key == TO_RED) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y));
		    Color newC = new Color(c.getRed(), 0, 0, (c.getRGB() >> 24) & 0xFF);
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if (key == TO_GREEN) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y), true);
		    Color newC = new Color(0, c.getGreen(), 0, c.getAlpha());
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if (key == TO_BLUE) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y));
		    Color newC = new Color(0, 0, c.getBlue(), (c.getRGB() >> 24) & 0xFF);
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if(key == SET_RED) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y));
		    Color newC = new Color((int) value, c.getGreen(), c.getBlue(), (c.getRGB() >> 24) & 0xFF);
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if(key == SET_GREEN) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y));
		    Color newC = new Color(c.getRed(), (int) value, c.getBlue(), (c.getRGB() >> 24) & 0xFF);
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if(key == SET_BLUE) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y));
		    Color newC = new Color(c.getRed(), c.getGreen(), (int) value, (c.getRGB() >> 24) & 0xFF);
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	} else if(key == SET_ALPHA) {
	    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    for(int y = 0; y < image.getHeight(); y++) {
		for(int x = 0; x < image.getWidth(); x++) {
		    Color c = new Color(image.getRGB(x, y), true);
		    Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (value * (c.getAlpha() / 255)));
		    newImg.setRGB(x, y, newC.getRGB());
		}
	    }
	}
	return newImg;
    }
}