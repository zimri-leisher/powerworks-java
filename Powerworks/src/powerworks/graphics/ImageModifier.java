package powerworks.graphics;

import java.awt.Color;

public class ImageModifier {

    /**
     * Value = 0.0 -> alpha = 0, value = 1.0 -> alpha = 255
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
     * Value = 0.5 -> width and height are halved, value = 2 -> width and height
     * are doubled
     */
    public static final int SCALE = 3;

    public static ModImage modify(int[] pixels, int widthPixels, int heightPixels, boolean hasTransparency, int key, double value) {
	switch (key) {
	    case SET_ALPHA:
		return setAlpha(pixels, widthPixels, heightPixels, hasTransparency, value);
	    case TO_GREEN:
		return getPixelsInGreen(pixels, widthPixels, heightPixels, hasTransparency);
	    case TO_RED:
		return getPixelsInRed(pixels, widthPixels, heightPixels, hasTransparency);
	    case SCALE:
		return scale(pixels, widthPixels, heightPixels, hasTransparency, value);
	    default:
		throw new IllegalArgumentException("Invalid key");
	}
    }

    public static ModImage modify(ModImage m, int key, double value) {
	return modify(m.pixels, m.widthPixels, m.heightPixels, m.hasTransparency, key, value);
    }

    public static ModImage modify(Image image, int key, double value) {
	return modify(image.getPixels(), image.getWidthPixels(), image.getHeightPixels(), image.hasTransparency(), key, value);
    }

    static class ModImage {

	int[] pixels;
	int widthPixels, heightPixels;
	boolean hasTransparency;

	private ModImage(int[] pixels, int width, int height, boolean hasTransparency) {
	    this.pixels = pixels;
	    this.widthPixels = width;
	    this.heightPixels = height;
	    this.hasTransparency = hasTransparency;
	}
    }

    public static ModImage scale(int[] pixels, int widthPixels, int heightPixels, boolean hasTransparency, double scale) {
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
	ModImage m = new ModImage(newPixels, (int) (widthPixels * scale), (int) (heightPixels * scale), hasTransparency);
	return m;
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
    public static ModImage setAlpha(int[] pixels, int widthPixels, int heightPixels, boolean hasTransparency, double alpha) {
	if (alpha > 1 || alpha < 0)
	    throw new IllegalArgumentException("Invalid key");
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255))).getRGB();
	}
	ModImage m = new ModImage(newPixels, widthPixels, heightPixels, hasTransparency);
	return m;
    }

    /**
     * Sets the hue of the image to green
     * 
     * @param pixels
     *            the pixels to modify
     * 
     */
    public static ModImage getPixelsInGreen(int[] pixels, int widthPixels, int heightPixels, boolean hasTransparency) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(0, c.getGreen(), 0, c.getAlpha())).getRGB();
	}
	ModImage m = new ModImage(newPixels, widthPixels, heightPixels, hasTransparency);
	return m;
    }

    /**
     * Sets the hue of the image to red
     * 
     * @param pixels
     *            the pixels to modify
     * 
     */
    public static ModImage getPixelsInRed(int[] pixels, int widthPixels, int heightPixels, boolean hasTransparency) {
	int[] newPixels = new int[pixels.length];
	for (int i = 0; i < pixels.length; i++) {
	    Color c = new Color(pixels[i]);
	    newPixels[i] = (new Color(c.getRed(), 0, 0, c.getAlpha())).getRGB();
	}
	ModImage m = new ModImage(newPixels, widthPixels, heightPixels, hasTransparency);
	return m;
    }
}
