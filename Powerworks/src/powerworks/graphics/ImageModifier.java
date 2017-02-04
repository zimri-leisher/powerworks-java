package powerworks.graphics;

import java.awt.Color;

public class ImageModifier {
    
    /**
     * Sets the alpha value of the image
     * 
     * @param pixels the pixels to modify
     * 
     * @param alpha
     *            the alpha value (255 being opaque, 0 being transparent) to set
     *            to
     */
    public static int[] setAlpha(int[] pixels, int alpha) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
	}
	return newPixels;
    }
    
    /**
     * Sets the alpha value of the image
     * 
     * @param objects the pixels to modify
     * 
     * @param alpha
     *            the alpha value (255 being opaque, 0 being transparent) to set
     *            to
     */
    public static int[] setAlpha(Image image, int alpha) {
	int[] newPixels = new int[image.getWidth() * image.getHeight()];
	for (int i = 0; i < image.getPixels().length; i++) {
	    Color c = new Color(image.getPixels()[i]);
	    newPixels[i] = (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
	}
	return newPixels;
    }
    
    /**
     * Sets the hue of the image to green
     * 
     * @param pixels the pixels to modify
     * 
     */
    public static int[] getPixelsInGreen(int[] pixels) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(0, c.getGreen(), 0, c.getAlpha())).getRGB();
	}
	return newPixels;
    }
    
    /**
     * Sets the hue of the image to green
     * 
     * @param objects the pixels to modify
     * 
     */
    public static int[] getPixelsInGreen(Image image) {
	int[] newPixels = new int[image.getWidth() * image.getHeight()];
	for (int i = 0; i < image.getPixels().length; i++) {
	    Color c = new Color(image.getPixels()[i]);
	    newPixels[i] = (new Color(0, c.getGreen(), 0, c.getAlpha())).getRGB();
	}
	return newPixels;
    }
    
    /**
     * Sets the hue of the image to red
     * 
     * @param pixels the pixels to modify
     * 
     * @param SIZE the size of the image (the square root of the length of the pixels array)
     */
    public static int[] getPixelsInRed(int[] pixels) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), 0, 0, c.getAlpha())).getRGB();
	}
	return newPixels;
    }
    
    /**
     * Sets the hue of the image to red
     * 
     * @param objects the pixels to modify
     * 
     * @param SIZE the size of the image (the square root of the length of the pixels array)
     */
    public static int[] getPixelsInRed(Image image) {
	int[] newPixels = new int[image.getWidth() * image.getHeight()];
	for (int i = 0; i < image.getPixels().length; i++) {
	    Color c = new Color(image.getPixels()[i]);
	    newPixels[i] = (new Color(c.getRed(), 0, 0, c.getAlpha())).getRGB();
	}
	return newPixels;
    }
}
