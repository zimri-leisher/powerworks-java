package powerworks.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import powerworks.graphics.screen.ScreenObject;
import powerworks.io.InputManager;
import powerworks.main.Game;
import powerworks.world.level.LevelObject;

public class Renderer {

    private Graphics2D g2d = null;
    private int widthPixels, heightPixels;
    private int xPixelOffset, yPixelOffset;
    private double zoom = 1.0, scaleWidth = 1.0, scaleHeight = 1.0;
    private Rectangle clip, defClip;

    public Renderer(int widthPixels, int heightPixels) {
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
	defClip = new Rectangle(0, 0, widthPixels, heightPixels);
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
    
    public Rectangle getClip() {
	return clip;
    }
    
    /**
     * All drawing operations will not do anything outside this rectangle on the screen. It can be reset by Renderer.resetClip()
     */
    public void setClip(int xPixel, int yPixel, int widthPixels, int heightPixels) {
	int s = Game.getScreenScale();
	this.clip = new Rectangle(xPixel * s, yPixel * s, widthPixels * s, heightPixels * s);
	g2d.setClip(clip);
    }
    
    public void resetClip() {
	clip = defClip;
	g2d.setClip(clip);
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
	int w = this.widthPixels;
	int h = this.heightPixels;
	this.widthPixels = widthPixels;
	this.heightPixels = heightPixels;
	defClip = new Rectangle(0, 0, widthPixels * Game.getScreenScale(), heightPixels * Game.getScreenScale());
	Game.getScreenManager().getScreenObjects().forEach((ScreenObject obj) -> obj.onScreenSizeChange(w, h));
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
     *            the x pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
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
     *            the x pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param scale
     *            the scale of the texture
     * @param widthScale
     *            the width scale of the texture
     * @param heightScale
     *            the height scale of the texture
     * @param rotation
     *            the rotation of the texture (0 = 0 degrees, 1 = 90 degrees,
     *            etc., 3 = 270 degrees)
     * @param alpha
     *            the alpha multiplier of the image (retains original alpha, 1 =
     *            no modifier, 0 = invisible)
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
     * Renders a texture
     * 
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param rotation
     *            the rotation of the texture (0 = 0 degrees, 1 = 90 degrees,
     *            etc., 3 = 270 degrees)
     * @param alpha
     *            the alpha multiplier of the image (retains original alpha, 1 =
     *            no modifier, 0 = invisible)
     * @param screenObject
     *            whether to render relative to level (false) or relative to
     *            screen (true)
     * @param clip
     *            the clipping rectangle, relative to the screen
     * 
     */
    public void renderTexture(Texture texture, int xPixel, int yPixel, int rotation, float alpha, boolean screenObject, Rectangle clip) {
	if (!screenObject) {
	    xPixel -= xPixelOffset;
	    yPixel -= yPixelOffset;
	}
	int mainScale = Game.getScreenScale();
	BufferedImage image = texture.getImage();
	int absoluteXPixel = xPixel * mainScale;
	int absoluteYPixel = yPixel * mainScale;
	int absoluteWidth = (int) (image.getWidth() * scaleWidth * mainScale);
	int absoluteHeight = (int) (image.getHeight() * scaleHeight * mainScale);
	if (!screenObject) {
	    absoluteWidth *= zoom;
	    absoluteHeight *= zoom;
	    absoluteXPixel *= zoom;
	    absoluteYPixel *= zoom;
	}
	AffineTransform old = null;
	Composite oldC = g2d.getComposite();
	Rectangle oldClip = g2d.getClipBounds();
	g2d.setClip((int) clip.getX() * mainScale, (int) clip.getY() * mainScale, (int) clip.getWidth() * mainScale, (int) clip.getHeight() * mainScale);
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
	g2d.setClip(oldClip);
    }

    /**
     * This method should be used for performance when you know the exact width
     * and height pixels already
     * 
     * @param flag
     *            does nothing, just differentiates between overloads
     * @param texture
     *            the texture to render
     * @param xPixel
     *            the x pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param yPixel
     *            the y pixel to render at relative to screen if screenObject is
     *            true. Otherwise it will be relative to level
     * @param widthPixels
     *            the width pixels of the area to draw. Will automatically scale
     *            the texture to fit this area
     * @param heightPixels
     *            the height pixels of the area to draw. Will automatically
     *            scale the texture to fit this area
     * @param rotation
     *            the rotation of the texture (0 = 0 degrees, 1 = 90 degrees,
     *            etc., 3 = 270 degrees)
     * @param alpha
     *            the alpha multiplier of the image (retains original alpha, 1 =
     *            no modifier, 0 = invisible)
     * @param screenObject
     *            whether to render relative to level (false) or relative to
     *            screen (true)
     */
    public void renderTexture(boolean flag, Texture texture, int xPixel, int yPixel, int widthPixels, int heightPixels, int rotation, float alpha, boolean screenObject) {
	if (!screenObject) {
	    xPixel -= xPixelOffset;
	    yPixel -= yPixelOffset;
	}
	BufferedImage image = texture.getImage();
	int mainScale = Game.getScreenScale();
	int absoluteXPixel = xPixel * mainScale;
	int absoluteYPixel = yPixel * mainScale;
	int absoluteWidthPixels = widthPixels * mainScale;
	int absoluteHeightPixels = heightPixels * mainScale;
	if (!screenObject) {
	    absoluteXPixel *= zoom;
	    absoluteYPixel *= zoom;
	    absoluteWidthPixels *= zoom;
	    absoluteHeightPixels *= zoom;
	}
	AffineTransform old = null;
	Composite oldC = g2d.getComposite();
	if (alpha != 1) {
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
	}
	if (rotation != 0) {
	    old = g2d.getTransform();
	    g2d.rotate(Math.toRadians(rotation * 90), absoluteXPixel + absoluteWidthPixels / 2, absoluteYPixel + absoluteHeightPixels / 2);
	}
	g2d.drawImage(image, absoluteXPixel, absoluteYPixel, absoluteWidthPixels, absoluteHeightPixels, null);
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
