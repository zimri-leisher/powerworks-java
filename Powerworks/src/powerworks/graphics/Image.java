package powerworks.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.ImageIO;
import powerworks.main.Game;

public class Image implements Texture {

    public static final Image ERROR = new Image("/textures/misc/error.png");
    public static final Image BLOCK_PLACEABLE = new Image("/textures/level/block/placeable.png");
    public static final Image BLOCK_NOT_PLACEABLE = new Image("/textures/level/block/not_placeable.png");
    public static final Image HOTBAR_SLOT = new Image("/textures/screen/gui/hotbar_slot.png");
    public static final Image HOTBAR_SLOT_SELECTED = new Image("/textures/screen/gui/hotbar_slot_selected.png");
    public static final Image CURSOR_DEFAULT = new Image("/textures/screen/cursor/cursor_default.png");
    public static final Image CURSOR_LEFT_CLICK = new Image("/textures/screen/cursor/cursor_left_click.png");
    public static final Image IRON_ORE_ITEM = new Image("/textures/screen/item/iron_ore_raw.png");
    public static final Image CONVEYOR_BELT_ITEM = new Image("/textures/screen/item/conveyor_belt.png");
    public static final Image IRON_INGOT = new Image("/textures/screen/item/iron_ingot.png");
    public static final Image GUI_DEFAULT_CORNER_TOP_RIGHT = new Image("/textures/screen/gui/default/top_right_corner.png");
    public static final Image GUI_DEFAULT_CORNER_TOP_LEFT = new Image("/textures/screen/gui/default/top_left_corner.png");
    public static final Image GUI_DEFAULT_CORNER_BOTTOM_RIGHT = new Image("/textures/screen/gui/default/bottom_right_corner.png");
    public static final Image GUI_DEFAULT_CORNER_BOTTOM_LEFT = new Image("/textures/screen/gui/default/bottom_left_corner.png");
    public static final Image GUI_DEFAULT_EDGE_TOP = new Image("/textures/screen/gui/default/top_edge.png");
    public static final Image GUI_DEFAULT_EDGE_BOTTOM = new Image("/textures/screen/gui/default/bottom_edge.png");
    public static final Image GUI_DEFAULT_EDGE_RIGHT = new Image("/textures/screen/gui/default/right_edge.png");
    public static final Image GUI_DEFAULT_EDGE_LEFT = new Image("/textures/screen/gui/default/left_edge.png");
    public static final Image GUI_DEFAULT_BACKGROUND = new Image("/textures/screen/gui/default/background.png");
    public static final Image ITEM_SLOT = new Image("/textures/screen/gui/item_slot.png");
    public static final Image ITEM_SLOT_HIGHLIGHT = new Image("/textures/screen/gui/item_slot_highlight.png");
    public static final Image ITEM_SLOT_DISPLAY = new Image("/textures/screen/gui/item_slot_display.png");
    public static final Image ITEM_SLOT_CLICK = new Image("/textures/screen/gui/item_slot_click.png");
    public static final Image CHAT_BAR = new Image(Utils.modify(Utils.modify(new Image("/textures/screen/gui/chat_bar.png"), Utils.SCALE_WIDTH, 180), Utils.SCALE_HEIGHT, 10));
    public static final Image ARROW = new Image(Utils.modify(new Image("/textures/misc/arrow.png"), Utils.SET_ALPHA, 50));
    public static final Image VOID = new Image(new Color(0));
    public static final Image ORE_MINER = new Image(Utils.modify(new Image("/textures/level/block/machine/ore_miner.png"), Utils.SCALE, 1));
    public static final Image GUI_BUTTON = new Image(Utils.genRectangle(64, 16));
    public static final Image GUI_BUTTON_HIGHLIGHT = new Image(Utils.modify(GUI_BUTTON, Utils.ADJUST_BRIGHTNESS, 1.2));
    public static final Image GUI_BUTTON_CLICK = new Image(Utils.modify(GUI_BUTTON, Utils.ROTATE, 2));
    public static final Image GUI_DRAG_GRIP = new Image("/textures/screen/gui/drag_grip.png");
    public static final Image GUI_SCROLL_BAR_TOP = new Image("/textures/screen/gui/scroll_bar_top.png");
    public static final Image GUI_SCROLL_BAR_MIDDLE = new Image("/textures/screen/gui/scroll_bar_middle.png");
    public static final Image GUI_SCROLL_BAR_BOTTOM = new Image("/textures/screen/gui/scroll_bar_bottom.png");
    public static final Image GUI_SCROLL_BAR_UNHIGHLIGHT_TOP = new Image("/textures/screen/gui/scroll_bar_unhighlight_top.png");
    public static final Image GUI_SCROLL_BAR_UNHIGHLIGHT_MIDDLE = new Image("/textures/screen/gui/scroll_bar_unhighlight_mid.png");
    public static final Image GUI_SCROLL_BAR_UNHIGHLIGHT_BOTTOM = new Image("/textures/screen/gui/scroll_bar_unhighlight_bottom.png");
    public static final Image GUI_SCROLL_BAR_HIGHLIGHT_TOP = new Image("/textures/screen/gui/scroll_bar_highlight_top.png");
    public static final Image GUI_SCROLL_BAR_HIGHLIGHT_MIDDLE = new Image("/textures/screen/gui/scroll_bar_highlight_mid.png");
    public static final Image GUI_SCROLL_BAR_HIGHLIGHT_BOTTOM = new Image("/textures/screen/gui/scroll_bar_highlight_bottom.png");
    public static final Image GUI_SCROLL_BAR_CLICK_TOP = new Image("/textures/screen/gui/scroll_bar_click_top.png");
    public static final Image GUI_SCROLL_BAR_CLICK_MIDDLE = new Image("/textures/screen/gui/scroll_bar_click_mid.png");
    public static final Image GUI_SCROLL_BAR_CLICK_BOTTOM = new Image("/textures/screen/gui/scroll_bar_click_bottom.png");
    public static final Image MAIN_MENU_BACKGROUND = new Image(new Color(0x5B5B5B));
    public static final Image MAIN_MENU_BUTTON_BOX = new Image("/textures/screen/gui/main_menu.png");
    public static final Image OPTIONS_MENU_BACKGROUND = new Image(new Color(0x999999));
    public static final Image ESCAPE_MENU_BACKGROUND = new Image("/textures/screen/gui/escape_menu_background.png");
    
    private BufferedImage image;

    public static class Utils {

	public static final int SCALE = 0;
	public static final int SCALE_WIDTH = 1;
	public static final int SCALE_HEIGHT = 2;
	/**
	 * Sets alpha for entire image, value is from 0-255, 0 being fully
	 * transparent, 255 being opaque
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
	 * Value from 0-3. Multiply by 90 to get degrees clockwise
	 */
	public static final int ROTATE = 10;
	/**
	 * Multiplies existing brightness by value, meaning 1 has no effect, 0.5
	 * halves brightness, and 2 doubles it.
	 */
	public static final int ADJUST_BRIGHTNESS = 11;

	/**
	 * Modifies an image based on the key and value pair inputted
	 * 
	 * @param image
	 *            image to modify
	 * @param key
	 *            the key from ImageModifier. Ex:
	 *            <tt> ImageModifier.SCALE </tt>
	 * @param value
	 *            the value. What it does depends on key, check key
	 *            description for more information
	 * @param interpolationType
	 *            key from the AffineTransformOp class
	 *            (<tt>TYPE_BICUBIC, TYPE_BILINEAR </tt>and<tt> TYPE_NEAREST_NEIGHBOR</tt>)
	 * @return the new BufferedImage in compatible form to the default
	 *         graphics configuration
	 */
	public static BufferedImage modify(Image image, int key, double value, int interpolationType) {
	    return modify(image.getImage(), key, value, interpolationType);
	}

	/**
	 * Modifies an image based on the key and value pair inputted
	 * 
	 * @param image
	 *            image to modify
	 * @param key
	 *            the key from ImageModifier. Ex:
	 *            <tt> ImageModifier.SCALE </tt>
	 * @param value
	 *            the value. What it does depends on key, check key
	 *            description for more information. If it involves
	 *            scaling/other such transforms, nearest neighbor will be
	 *            used
	 * @return the new BufferedImage in compatible form to the default
	 *         graphics configuration
	 */
	public static BufferedImage modify(Image image, int key, double value) {
	    return modify(image, key, value, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}

	/**
	 * Modifies an image based on the key and value pair inputted
	 * 
	 * @param image
	 *            image to modify
	 * @param key
	 *            the key from ImageModifier. Ex:
	 *            <tt> ImageModifier.SCALE </tt>
	 * @param value
	 *            the value. What it does depends on key, check key
	 *            description for more information. If it involves
	 *            scaling/other such transforms, nearest neighbor will be
	 *            used
	 * @return the new BufferedImage in compatible form to the default
	 *         graphics configuration
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
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y));
			Color newC = new Color(c.getRed(), 0, 0, (c.getRGB() >> 24) & 0xFF);
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == TO_GREEN) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y), true);
			Color newC = new Color(0, c.getGreen(), 0, c.getAlpha());
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == TO_BLUE) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y));
			Color newC = new Color(0, 0, c.getBlue(), (c.getRGB() >> 24) & 0xFF);
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == SET_RED) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y));
			Color newC = new Color((int) value, c.getGreen(), c.getBlue(), (c.getRGB() >> 24) & 0xFF);
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == SET_GREEN) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y));
			Color newC = new Color(c.getRed(), (int) value, c.getBlue(), (c.getRGB() >> 24) & 0xFF);
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == SET_BLUE) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y));
			Color newC = new Color(c.getRed(), c.getGreen(), (int) value, (c.getRGB() >> 24) & 0xFF);
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == SET_ALPHA) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
			Color c = new Color(image.getRGB(x, y), true);
			Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (value * (c.getAlpha() / 255)));
			newImg.setRGB(x, y, newC.getRGB());
		    }
		}
	    } else if (key == ROTATE) {
		if (value == 0)
		    return image;
		if (value == 1 || value == 3) {
		    newImg = Game.getGraphicsConf().createCompatibleImage(image.getHeight(), image.getWidth(), Transparency.TRANSLUCENT);
		    Graphics2D g2d = newImg.createGraphics();
		    g2d.rotate(Math.toRadians(value * 90), image.getWidth() / 2, image.getHeight() / 2);
		    g2d.drawImage(image, 0, 0, null);
		    g2d.dispose();
		} else {
		    newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		    Graphics2D g2d = newImg.createGraphics();
		    g2d.rotate(Math.toRadians(180), image.getWidth() / 2, image.getHeight() / 2);
		    g2d.drawImage(image, 0, 0, null);
		    g2d.dispose();
		}
	    } else if (key == ADJUST_BRIGHTNESS) {
		newImg = Game.getGraphicsConf().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		newImg.createGraphics().drawImage(image, 0, 0, null);
		WritableRaster wr = newImg.getRaster();
		int[] pixel = new int[4];
		for (int i = 0; i < wr.getWidth(); i++) {
		    for (int j = 0; j < wr.getHeight(); j++) {
			wr.getPixel(i, j, pixel);
			pixel[0] = (int) (pixel[0] * value);
			pixel[1] = (int) (pixel[1] * value);
			pixel[2] = (int) (pixel[2] * value);
			wr.setPixel(i, j, pixel);
		    }
		}
	    }
	    return newImg;
	}

	public static Image getImage(BufferedImage image) {
	    return new Image(image);
	}

	public static BufferedImage genRectangle(int widthPixels, int heightPixels) {
	    return genRectangle(Image.GUI_DEFAULT_EDGE_TOP, Image.GUI_DEFAULT_EDGE_BOTTOM, Image.GUI_DEFAULT_EDGE_LEFT, Image.GUI_DEFAULT_EDGE_RIGHT, Image.GUI_DEFAULT_CORNER_TOP_RIGHT,
		    Image.GUI_DEFAULT_CORNER_TOP_LEFT, Image.GUI_DEFAULT_CORNER_BOTTOM_RIGHT, Image.GUI_DEFAULT_CORNER_BOTTOM_LEFT, Image.GUI_DEFAULT_BACKGROUND, widthPixels, heightPixels);
	}

	public static BufferedImage genRectangle(Texture topEdge, Texture bottomEdge, Texture leftEdge, Texture rightEdge, Texture topRightCorner, Texture topLeftCorner, Texture bottomRightCorner,
		Texture bottomLeftCorner, Texture background, int widthPixels, int heightPixels) {
	    BufferedImage dest = Game.getGraphicsConf().createCompatibleImage(widthPixels, heightPixels, Transparency.TRANSLUCENT);
	    Graphics2D g = dest.createGraphics();
	    g.setComposite(AlphaComposite.SrcOver);
	    for (int x = 0; x < widthPixels;) {
		for (int y = 0; y < heightPixels;) {
		    g.drawImage(background.getImage(), x, y, null);
		    y += background.getHeightPixels();
		}
		x += background.getWidthPixels();
	    }
	    for (int i = topLeftCorner.getWidthPixels(); i < widthPixels - topRightCorner.getWidthPixels();) {
		g.drawImage(topEdge.getImage(), i, 0, null);
		i += topEdge.getWidthPixels();
	    }
	    for (int i = topRightCorner.getHeightPixels(); i < heightPixels - bottomRightCorner.getHeightPixels();) {
		g.drawImage(rightEdge.getImage(), widthPixels - rightEdge.getWidthPixels(), i, null);
		i += rightEdge.getHeightPixels();
	    }
	    for (int i = bottomLeftCorner.getWidthPixels(); i < widthPixels - bottomRightCorner.getWidthPixels();) {
		g.drawImage(bottomEdge.getImage(), i, heightPixels - bottomEdge.getHeightPixels(), null);
		i += bottomEdge.getWidthPixels();
	    }
	    for (int i = topLeftCorner.getHeightPixels(); i < heightPixels - bottomLeftCorner.getHeightPixels();) {
		g.drawImage(leftEdge.getImage(), 0, i, null);
		i += leftEdge.getHeightPixels();
	    }
	    g.drawImage(topLeftCorner.getImage(), 0, 0, null);
	    g.drawImage(topRightCorner.getImage(), widthPixels - topRightCorner.getWidthPixels(), 0, null);
	    g.drawImage(bottomRightCorner.getImage(), widthPixels - bottomRightCorner.getWidthPixels(), heightPixels - bottomRightCorner.getHeightPixels(), null);
	    g.drawImage(bottomLeftCorner.getImage(), 0, heightPixels - bottomLeftCorner.getHeightPixels(), null);
	    g.dispose();
	    return dest;
	}
    }

    private Image(String path) {
	try {
	    BufferedImage src = ImageIO.read(Image.class.getResource(path));
	    BufferedImage dest = Game.getGraphicsConf().createCompatibleImage(src.getWidth(), src.getHeight(), Transparency.TRANSLUCENT);
	    Graphics2D g = dest.createGraphics();
	    g.setComposite(AlphaComposite.SrcOver);
	    g.drawImage(src, 0, 0, null);
	    g.dispose();
	    image = dest;
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private Image(BufferedImage image) {
	this.image = image;
    }

    /**
     * 1x1 pixel of the color
     */
    private Image(Color c) {
	image = Game.getGraphicsConf().createCompatibleImage(1, 1);
	Graphics2D g2d = image.createGraphics();
	g2d.setColor(c);
	g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
	g2d.dispose();
    }

    public BufferedImage getImage() {
	return image;
    }

    public int getWidthPixels() {
	return image.getWidth();
    }

    public int getHeightPixels() {
	return image.getHeight();
    }
}
