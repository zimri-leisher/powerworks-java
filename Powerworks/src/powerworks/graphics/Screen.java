package powerworks.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import powerworks.collidable.Collidable;
import powerworks.main.Game;

public class Screen {

    public static Screen screen = new Screen(Game.width, Game.height, Game.objects, Game.overlay);
    final int originalWidth, originalHeight;
    public int width;
    public int height;
    public int[] objects;
    public int[] overlay;
    public int xOffset, yOffset;

    public Screen(int width, int height, int[] objects, int[] overlay) {
	this.originalHeight = height;
	this.originalWidth = width;
	this.width = width;
	this.height = height;
	this.objects = objects;
	this.overlay = overlay;
    }

    public void set(int width, int height, int[] pixels) {
	this.width = width;
	this.height = height;
	this.objects = pixels;
    }

    /**
     * Sets offset for the screen (used for movement of camera)
     * 
     * @param xOffset
     *            the x pixel to offset the screen
     * @param yOffset
     *            the y pixel to offset the screen
     */
    public void setOffset(int xOffset, int yOffset) {
	if(xOffset - width / 2 >= 0) this.xOffset = xOffset;
	if(yOffset - height / 2 >= 0) this.yOffset = yOffset;
    }

    public void renderHitbox(Collidable col) {
	int xPixel = col.getXPixel() - xOffset + col.getHitbox().xStart;
	int yPixel = col.getYPixel() - yOffset + col.getHitbox().yStart;
	for (int y = 0; y < col.getHitbox().height; y++) {
	    int ya = (yPixel + y) * width;
	    for (int x = 0; x < col.getHitbox().width; x++) {
		int xa = xPixel + x;
		if (xa < -(col.getHitbox().width - col.getHitbox().xStart) || xa >= width || ya < 0 || ya >= objects.length)
		    break;
		if (xa < 0)
		    xa = 0;
		objects[xa + ya] = 0xFFFF0000;
	    }
	}
    }

    public void renderTexture(Texture tex, int xPixel, int yPixel, int widthTiles, int heightTiles, double scale, boolean affectedByZoom, boolean affectedByCamera) {
	int[] objPixels = tex.getPixels();
	final int widthPixels = tex.getWidthPixels();
	final int heightPixels = tex.getHeightPixels();
	final double absoluteHeightPixels = scale * heightPixels * heightTiles;
	final double absoluteWidthPixels = scale * widthPixels * widthTiles;
	final double widthScaleFactor = widthTiles * scale;
	final double heightScaleFactor = heightTiles * scale;
	if(affectedByCamera) {
	    xPixel -= xOffset;
	    yPixel -= yOffset;
	}
	if (!affectedByZoom) {
	    for (int y = 0; y < absoluteHeightPixels; y++) {
		final int ya = (yPixel + y) * this.originalWidth;
		final int yc = (int) (y / heightScaleFactor) * widthPixels;
		for (int x = 0; x < absoluteWidthPixels; x++) {
		    int xa = xPixel + x;
		    int xc = (int) (x / widthScaleFactor);
		    if (xa < -widthPixels || xa >= this.originalWidth || ya < 0 || ya >= overlay.length)
			break;
		    if (xa < 0)
			xa = 0;
		    final int coord2 = xa + ya;
		    final int pixel = objPixels[xc + yc];
		    final int alpha = (pixel >> 24) & 0xFF;
		    if (alpha == 255)
			overlay[coord2] = pixel;
		    else if (alpha != 0)
			overlay[coord2] = combineColors(pixel, overlay[coord2]);
		}
	    }
	} else {
	    for (int y = 0; y < absoluteHeightPixels; y++) {
		final int ya = (yPixel + y) * width;
		final int yc = (int) (y / heightScaleFactor) * widthPixels;
		for (int x = 0; x < absoluteWidthPixels; x++) {
		    int xa = xPixel + x;
		    int xc = (int) (x / widthScaleFactor);
		    if (xa < -widthPixels || xa >= width || ya < 0 || ya >= objects.length)
			break;
		    if (xa < 0)
			xa = 0;
		    final int coord2 = xa + ya;
		    final int pixel = objPixels[xc + yc];
		    final int alpha = (pixel >> 24) & 0xFF;
		    if (alpha == 255)
			objects[coord2] = pixel;
		    else if (alpha != 0)
			objects[coord2] = combineColors(pixel, objects[coord2]);
		}
	    }
	}
    }

    public void renderTexture(Texture tex, int xPixel, int yPixel, boolean affectedByZoom, boolean affectedByCamera) {
	int[] objPixels = tex.getPixels();
	final int widthPixels = tex.getWidthPixels();
	final int heightPixels = tex.getHeightPixels();
	if(affectedByCamera) {
	    xPixel -= xOffset;
	    yPixel -= yOffset;
	}
	if (!affectedByZoom) {
	    for (int y = 0; y < heightPixels; y++) {
		int ya = (yPixel + y) * originalWidth;
		int yc = y * heightPixels;
		for (int x = 0; x < widthPixels; x++) {
		    int xa = xPixel + x;
		    int xc = x;
		    if (xa < -widthPixels || xa >= originalWidth || ya < 0 || ya >= overlay.length)
			break;
		    if (xa < 0)
			xa = 0;
		    int pixel = objPixels[xc + yc];
		    int coord = xa + ya;
		    int alpha = (pixel >> 24) & 0xFF;
		    if (alpha == 255)
			overlay[coord] = pixel;
		    else if (alpha != 0)
			overlay[coord] = combineColors(pixel, overlay[coord]);
		}
	    }
	} else {
	    for (int y = 0; y < heightPixels; y++) {
		int ya = (yPixel + y) * width;
		int yc = y * heightPixels;
		for (int x = 0; x < widthPixels; x++) {
		    int xa = xPixel + x;
		    int xc = x;
		    if (xa < -widthPixels || xa >= width || ya < 0 || ya >= objects.length)
			break;
		    if (xa < 0)
			xa = 0;
		    int pixel = objPixels[xc + yc];
		    int coord = xa + ya;
		    int alpha = (pixel >> 24) & 0xFF;
		    if (alpha == 255)
			objects[coord] = pixel;
		    else if (alpha != 0)
			objects[coord] = combineColors(pixel, objects[coord]);
		}
	    }
	}
    }

    public void renderTexturedObject(TexturedObject obj) {
	int xPixel = obj.getXPixel() - xOffset;
	int yPixel = obj.getYPixel() - yOffset;
	int[] objPixels = obj.getTexture().getPixels();
	double scale = obj.getScale();
	int widthPixels = obj.getTexture().getWidthPixels();
	int heightPixels = obj.getTexture().getHeightPixels();
	double absoluteHeightPixels = obj.getScale() * heightPixels;
	double absoluteWidthPixels = obj.getScale() * widthPixels;
	for (int y = 0; y < absoluteHeightPixels; y++) {
	    int ya = (yPixel + y) * width;
	    int yc = (int) (y / scale);
	    for (int x = 0; x < absoluteWidthPixels; x++) {
		int xa = xPixel + x;
		int xc = (int) (x / scale);
		if (xa < -widthPixels || xa >= width || ya < 0 || ya >= objects.length)
		    break;
		if (xa < 0)
		    xa = 0;
		int coord2 = xa + ya;
		int pixel = 0;
		if(obj.getRotation() == 0)
		    pixel = objPixels[xc + yc * widthPixels];
		else if(obj.getRotation() == 1)
		    pixel = objPixels[(15 - yc) * widthPixels + xc];
		else if(obj.getRotation() == 2)
		    pixel = objPixels[xc * widthPixels + yc];
		else
		    pixel = objPixels[(15 - xc) * widthPixels + yc];
		int alpha = (pixel >> 24) & 0xFF;
		
		if (alpha == 255)
		    objects[coord2] = pixel;
		else if (alpha != 0)
		    objects[coord2] = combineColors(pixel, objects[coord2]);
	    }
	}
    }

    public void renderText(String text, int color, int xPixel, int yPixel, Graphics2D g2d) {
	g2d.setFont(Game.font);
	g2d.setColor(new Color(color));
	g2d.drawString(text, xPixel * Game.scale, yPixel * Game.scale);
    }

    public static int combineColors(int foregroundColor, int backgroundColor) {
	final double ap1 = (backgroundColor >> 24 & 0xff) / 255d;
	final double ap2 = (foregroundColor >> 24 & 0xff) / 255d;
	final double h = (ap1 * (1 - ap2));
	final double ap = ap2 + h;
	final double amount1 = h / ap;
	final double amount2 = amount1 / ap;
	int a = ((int) (ap * 255d)) & 0xff;
	int r = ((int) (((backgroundColor >> 16 & 0xff) * amount1) +
		((foregroundColor >> 16 & 0xff) * amount2))) & 0xff;
	int g = ((int) (((backgroundColor >> 8 & 0xff) * amount1) +
		((foregroundColor >> 8 & 0xff) * amount2))) & 0xff;
	int b = ((int) (((backgroundColor & 0xff) * amount1) +
		((foregroundColor & 0xff) * amount2))) & 0xff;
	return a << 24 | r << 16 | g << 8 | b << 0;
    }
}
