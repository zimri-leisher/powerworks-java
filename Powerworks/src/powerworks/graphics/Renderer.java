package powerworks.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import powerworks.data.GeometryHelper;
import powerworks.io.InputManager;
import powerworks.main.Game;
import powerworks.world.level.LevelObject;

public class Renderer {

    List<ScreenObject> objects = new ArrayList<ScreenObject>();
    private Graphics2D g2d = null;
    private int widthPixels, heightPixels;
    private int xPixelOffset, yPixelOffset;
    private double zoom = 1.0, scaleWidth = 1.0, scaleHeight = 1.0;

    public Renderer(int widthPixels, int heightPixels) {
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
    }

    /**
     * @return true if a clickable screen object was clicked on
     */
    public boolean onClick(int xPixel, int yPixel) {
	boolean used = false;
	for (ScreenObject obj : objects) {
	    if (obj instanceof ClickableScreenObject) {
		ClickableScreenObject cObj = (ClickableScreenObject) obj;
		if (GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), xPixel, yPixel, 0, 0)) {
		    used = true;
		    cObj.onClick(xPixel, yPixel);
		} else
		    cObj.onClickOff();
	    }
	}
	return used;
    }

    public void onRelease(int xPixel, int yPixel) {
	for (ScreenObject obj : objects) {
	    if (obj instanceof ClickableScreenObject) {
		ClickableScreenObject cObj = (ClickableScreenObject) obj;
		cObj.onRelease(xPixel, yPixel);
	    }
	}
    }

    public void update() {
	int mXPixel = InputManager.getMouseXPixel();
	int mYPixel = InputManager.getMouseYPixel();
	objects.forEach((ScreenObject obj) -> {
	    if (obj instanceof ClickableScreenObject) {
		ClickableScreenObject cObj = (ClickableScreenObject) obj;
		System.out.println(cObj.getXPixel() + ", " + cObj.getYPixel() + ", " + mXPixel + ", " + mYPixel);
		if (GeometryHelper.contains(cObj.getXPixel(), cObj.getYPixel(), cObj.getWidthPixels(), cObj.getHeightPixels(), mXPixel, mYPixel, 0, 0))
		    cObj.whileMouseOver();
	    }
	});
	objects.forEach(ScreenObject::update);
    }

    public List<ScreenObject> getScreenObjects() {
	return objects;
    }

    public int getWidthPixels() {
	return widthPixels;
    }

    public int getHeightPixels() {
	return heightPixels;
    }

    public int getXPixelOffset() {
	return xPixelOffset;
    }

    public int getYPixelOffset() {
	return yPixelOffset;
    }

    public double getZoom() {
	return zoom;
    }

    public void setOffset(int xPixel, int yPixel) {
	boolean used = false;
	if (xPixel - widthPixels / 2 >= 0 && xPixel + widthPixels < Game.getLevel().getWidthPixels()) {
	    if (this.xPixelOffset != xPixel)
		used = true;
	    this.xPixelOffset = xPixel;
	}
	if (yPixel - heightPixels / 2 >= 0 && yPixel + heightPixels < Game.getLevel().getHeightPixels()) {
	    if (this.yPixelOffset != yPixel)
		used = true;
	    this.yPixelOffset = yPixel;
	}
	if (used)
	    InputManager.screenMoved();
    }

    public void setSize(int widthPixels, int heightPixels) {
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
	objects.forEach(ScreenObject::onScreenSizeChange);
    }

    public void setZoom(double zoom) {
	this.zoom = zoom;
    }

    public Rectangle getCurrentViewArea() {
	int xPixel = xPixelOffset;
	int yPixel = yPixelOffset;
	return new Rectangle(xPixel, yPixel, (int) (widthPixels / zoom), (int) (heightPixels / zoom));
    }

    /**
     * Must be called at beginning of render cycle
     */
    public void feed(Graphics2D g2d) {
	this.g2d = g2d;
    }

    /**
     * Checks if the Graphics2D object is not null. Note, if it has been
     * "disposed" it will still return true, so we have to hope that it isn't
     */
    public boolean isHungry() {
	return g2d == null;
    }

    public void renderSquare(int color, int xPixel, int yPixel, int width, int height) {
	g2d.setColor(new Color(color));
	xPixel -= xPixelOffset;
	yPixel -= yPixelOffset;
	g2d.fillRect(xPixel * Game.getScreenScale(), yPixel * Game.getScreenScale(), width * Game.getScreenScale(), height * Game.getScreenScale());
    }

    /**
     * Renders a texture to the screen, relative to the screen i.e. no zoom or
     * offset based on camera is applied
     * 
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen
     * @param yPixel
     *            the y pixel to render at relative to screen
     */
    public void renderTexture(Texture texture, int xPixel, int yPixel) {
	renderTexture(texture, xPixel, yPixel, 1);
    }

    /**
     * Renders a texture to the screen, relative to the screen i.e. no zoom or
     * offset based on camera is applied
     * 
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen
     * @param yPixel
     *            the y pixel to render at relative to screen
     * @param alpha
     *            the alpha multiplier of the image (retains original alpha, 1 =
     *            no modifier, 0 = invisible)
     */
    public void renderTexture(Texture texture, int xPixel, int yPixel, float alpha) {
	renderTexture(texture, xPixel, yPixel, alpha, true);
    }

    /**
     * Renders a texture
     * 
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen if @param
     *            screenObject is true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if @param
     *            screenObject is true. Otherwise it will be relative to level
     * @param screenObject
     *            whether to render relative to level (false) or relative to
     *            screen (true)
     * @param alpha
     *            the alpha multiplier of the image (retains original alpha, 1 =
     *            no modifier, 0 = invisible)
     */
    public void renderTexture(Texture texture, int xPixel, int yPixel, float alpha, boolean screenObject) {
	renderTexture(texture, xPixel, yPixel, 1, 1, 1, 0, alpha, screenObject);
    }

    /**
     * Renders a texture
     * 
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen if @param
     *            screenObject is true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if @param
     *            screenObject is true. Otherwise it will be relative to level
     * @param scale
     *            the scale of the texture
     * @param widthScale
     *            the width scale of the texture
     * @param heightScale
     *            the height scale of the texture
     * @param rotation
     *            the rotation of the texture (0 = 0 degrees, 1 = 90 degrees,
     *            etc., 3 = 270 degrees)
     * @param screenObject
     *            whether to render relative to level (false) or relative to
     *            screen (true)
     */
    public void renderTexture(Texture texture, int xPixel, int yPixel, float scale, float widthScale, float heightScale, int rotation, float alpha, boolean screenObject) {
	if (!screenObject) {
	    xPixel -= xPixelOffset;
	    yPixel -= yPixelOffset;
	}
	int mainScale = Game.getScreenScale();
	BufferedImage image = texture.getImage();
	int absoluteXPixel = xPixel * mainScale;
	int absoluteYPixel = yPixel * mainScale;
	int absoluteWidth = (int) (widthScale * image.getWidth() * scaleWidth * scale * mainScale);
	int absoluteHeight = (int) (heightScale * image.getHeight() * scaleHeight * scale * mainScale);
	if (!screenObject) {
	    absoluteWidth *= zoom;
	    absoluteHeight *= zoom;
	    absoluteXPixel *= zoom;
	    absoluteYPixel *= zoom;
	}
	AffineTransform old = null;
	Composite oldC = g2d.getComposite();
	if (alpha != 1) {
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
	}
	if (rotation != 0) {
	    old = g2d.getTransform();
	    g2d.rotate(Math.toRadians(rotation * 90), absoluteXPixel + absoluteWidth / 2, absoluteYPixel + absoluteHeight / 2);
	}
	g2d.drawImage(image, absoluteXPixel, absoluteYPixel, absoluteWidth, absoluteHeight, null);
	if (rotation != 0)
	    g2d.setTransform(old);
	if (alpha != 1)
	    g2d.setComposite(oldC);
    }

    /**
     * Renders text to the screen, no zoom or offset is applied, the color of
     * the text is white, and its font size is the default of 28
     * 
     * @param o
     *            the object of the string (can be a non string object, it will
     *            just use toString())
     * @param xPixel
     *            the x pixel to render at relative to the screen
     * @param yPixel
     *            the y pixel to render at relative to the screen
     */
    public void renderText(Object o, int xPixel, int yPixel) {
	renderText(o, xPixel, yPixel, 28);
    }

    /**
     * Renders text to the screen, no zoom or offset is applied, the color of
     * the text is white
     * 
     * @param o
     *            the object of the string (can be a non string object, it will
     *            just use toString())
     * @param xPixel
     *            the x pixel to render at relative to the screen
     * @param yPixel
     *            the y pixel to render at relative to the screen
     * @param size
     *            the size of the font
     */
    public void renderText(Object o, int xPixel, int yPixel, int size) {
	renderText(o, xPixel, yPixel, size, 0xFFFFFF);
    }

    /**
     * Renders text to the screen, no zoom or offset is applied
     * 
     * @param o
     *            the object of the string (can be a non string object, it will
     *            just use toString())
     * @param xPixel
     *            the x pixel to render at relative to the screen
     * @param yPixel
     *            the y pixel to render at relative to the screen
     * @param color
     *            the color (in hexadecimal) of the text
     * @param size
     *            the size of the font
     */
    public void renderText(Object o, int xPixel, int yPixel, int size, int color) {
	renderText(o, xPixel, yPixel, 1, 1, 1, size, color);
    }

    /**
     * Renders text to the screen, no zoom or offset is applied
     * 
     * @param o
     *            the object of the string (can be a non string object, it will
     *            just use toString())
     * @param xPixel
     *            the x pixel to render at relative to screen
     * @param yPixel
     *            the y pixel to render at relative to screen
     * @param scale
     *            the scale of the text
     * @param widthScale
     *            the width scale of the text
     * @param heightScale
     *            the height scale of the text
     * @param color
     *            the color (in hexadecimal) of the text
     * @param size
     *            the size of the font
     */
    public void renderText(Object o, int xPixel, int yPixel, float scale, float widthScale, float heightScale, int size, int color) {
	int mainScale = Game.getScreenScale();
	int absoluteXPixel = xPixel * mainScale;
	int absoluteYPixel = yPixel * mainScale;
	AffineTransform old = g2d.getTransform();
	g2d.setFont(Game.getFont(size));
	g2d.setColor(new Color(color));
	g2d.scale(scale + widthScale - 1, scale + heightScale - 1);
	g2d.drawString(o.toString(), absoluteXPixel, absoluteYPixel);
	g2d.setTransform(old);
    }

    /**
     * It is recommended that you execute all screen object renderings
     * <i>after</i> rendering all level objects for performance reasons
     */
    public void renderScreenObject(ScreenObject o) {
	int xPixel = o.getXPixel();
	int yPixel = o.getYPixel();
	int rotation = o.getRotation();
	double oScale = o.getScale();
	int mainScale = Game.getScreenScale();
	BufferedImage image = o.getTexture().getImage();
	int absoluteXPixel = xPixel * mainScale;
	int absoluteYPixel = yPixel * mainScale;
	int absoluteWidth = (int) (o.getWidthScale() * image.getWidth() * scaleWidth * oScale * mainScale);
	int absoluteHeight = (int) (o.getHeightScale() * image.getHeight() * scaleHeight * oScale * mainScale);
	AffineTransform old = g2d.getTransform();
	if (rotation != 0) {
	    g2d.rotate(Math.toRadians(rotation * 90), absoluteXPixel + absoluteWidth / 2, absoluteYPixel + absoluteHeight / 2);
	}
	g2d.drawImage(image, absoluteXPixel, absoluteYPixel, absoluteWidth, absoluteHeight, null);
	if (rotation != 0)
	    g2d.setTransform(old);
    }

    public void renderLevelObject(LevelObject o) {
	renderLevelObject(o, 0, 0);
    }

    public void renderLevelObject(LevelObject o, int xOffset, int yOffset) {
	int xPixel = o.getXPixel() - xPixelOffset - xOffset + o.getTextureXPixelOffset();
	int yPixel = o.getYPixel() - yPixelOffset - yOffset + o.getTextureYPixelOffset();
	int rotation = o.getRotation();
	float oScale = o.getScale();
	int mainScale = Game.getScreenScale();
	BufferedImage image = o.getTexture().getImage();
	int absoluteXPixel = (int) (xPixel * mainScale * zoom);
	int absoluteYPixel = (int) (yPixel * mainScale * zoom);
	int absoluteWidth = (int) (o.getWidthScale() * image.getWidth() * scaleWidth * oScale * mainScale * zoom);
	int absoluteHeight = (int) (o.getHeightScale() * image.getHeight() * scaleHeight * oScale * mainScale * zoom);
	AffineTransform old = g2d.getTransform();
	if (rotation != 0) {
	    g2d.rotate(Math.toRadians(rotation * 90), absoluteXPixel + absoluteWidth / 2, absoluteYPixel + absoluteHeight / 2);
	}
	g2d.drawImage(image, absoluteXPixel, absoluteYPixel, absoluteWidth, absoluteHeight, null);
	if (rotation != 0)
	    g2d.setTransform(old);
    }
}
