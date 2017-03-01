package powerworks.graphics;

import java.awt.Color;

public class ImageModifier {

    /**
     * Value = 0.0 -> Alpha = 0 Value = 1.0 -> Alpha = 255
     */
    public static final int SET_ALPHA = 0;
    /**
     * Any value will be accepted for args, it makes no difference on end result
     */
    public static final int TO_GREEN = 1;
    /**
     * Any value will be accepted for args, it makes no difference on end result
     */
    public static final int TO_RED = 2;
    /**
     * 
     */
    public static final int SCALE = 3;

    public static int[] modify(int[] pixels, int widthPixels, int heightPixels, int key, double value) {
	switch (key) {
	    case SET_ALPHA:
		return setAlpha(pixels, value);
	    case TO_GREEN:
		return getPixelsInGreen(pixels);
	    case TO_RED:
		return getPixelsInRed(pixels);
	    case SCALE:
		return scale(pixels, widthPixels, heightPixels, value);
	    default:
		throw new IllegalArgumentException("Invalid key");
	}
    }

    public static int[] modify(Image image, int key, double value) {
	return modify(image.getPixels(), image.getWidthPixels(), image.getHeightPixels(), key, value);
    }

    public static int[] scale(int[] pixels, int widthPixels, int heightPixels, double scale) {
	int[] newPixels = new int[(int) (pixels.length * scale * scale)];
	final double absoluteHeightPixels = scale * heightPixels;
	final double absoluteWidthPixels = scale * widthPixels;
	for (int y = 0; y < absoluteHeightPixels; y++) {
	    final int ya = (int) (y * absoluteWidthPixels);
	    final int yc = (int) (y / scale) * widthPixels;
	    for (int x = 0; x < absoluteWidthPixels; x++) {
		int xa = x;
		int xc = (int) (x / scale);
		final int coord2 = xa + ya;
		newPixels[coord2] = pixels[xc + yc];
	    }
	}
	return newPixels;
    }

    /**
     * Sets the alpha value of the image
     * 
     * @param pixels
     *            the pixels to modify
     * 
     * @param alpha
     *            the alpha value (1 being opaque, 0 being transparent) to set
     *            to
     */
    public static int[] setAlpha(int[] pixels, double alpha) {
	if (alpha > 1 || alpha < 0)
	    throw new IllegalArgumentException("Invalid key");
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255))).getRGB();
	}
	return newPixels;
    }

    /**
     * Sets the hue of the image to green
     * 
     * @param pixels
     *            the pixels to modify
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
     * Sets the hue of the image to red
     * 
     * @param pixels
     *            the pixels to modify
     * 
     */
    public static int[] getPixelsInRed(int[] pixels) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), 0, 0, c.getAlpha())).getRGB();
	}
	return newPixels;
    }
}
