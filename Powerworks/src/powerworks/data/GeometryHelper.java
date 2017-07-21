package powerworks.data;


public class GeometryHelper {
    public static boolean intersects(int xPixel, int yPixel, int width, int height, int xPixel2, int yPixel2, int width2, int height2) {
	if (xPixel + width <= xPixel2 || yPixel + height <= yPixel2 || xPixel >= xPixel2 + width2 || yPixel >= yPixel2 + height2)
	    return false;
	return true;
    }

    public static boolean contains(int xPixel, int yPixel, int width, int height, int xPixelIn, int yPixelIn, int widthIn, int heightIn) {
	if (xPixelIn >= xPixel && yPixelIn >= yPixel && xPixelIn + widthIn < xPixel + width && yPixelIn + heightIn < yPixel + height)
	    return true;
	return false;
    }
    
    public static int addDegrees(int deg1, int deg2) {
	return (deg1 + deg2) % 360;
    }
}
