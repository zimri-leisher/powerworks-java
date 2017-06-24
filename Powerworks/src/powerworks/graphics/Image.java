package powerworks.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import powerworks.main.Game;

public class Image implements Texture {

    public static final Image ERROR = new Image("/textures/misc/error.png");
    public static final Image BLOCK_PLACEABLE = new Image("/textures/block/placeable.png");
    public static final Image BLOCK_NOT_PLACEABLE = new Image("/textures/block/not_placeable.png");
    public static final Image HOTBAR_SLOT = new Image("/textures/gui/hotbar_slot.png");
    public static final Image HOTBAR_SLOT_SELECTED = new Image("/textures/gui/hotbar_slot_selected.png");
    public static final Image CURSOR_DEFAULT = new Image("/textures/cursor/cursor_default.png");
    public static final Image CURSOR_LEFT_CLICK = new Image("/textures/cursor/cursor_left_click.png");
    public static final Image IRON_ORE_ITEM = new Image("/textures/item/iron_ore_raw.png");
    public static final Image CONVEYOR_BELT_ITEM = new Image("/textures/item/conveyor_belt.png");
    public static final Image IRON_INGOT = new Image("/textures/item/iron_ingot.png");
    public static final Image ITEM_SLOT = new Image("/textures/gui/item_slot.png");
    public static final Image ITEM_SLOT_HIGHLIGHT = new Image("/textures/gui/item_slot_highlight.png");
    public static final Image ITEM_SLOT_DISPLAY = new Image("/textures/gui/item_slot_display.png");
    public static final Image ITEM_SLOT_CLICK = new Image("/textures/gui/item_slot_click.png");
    public static final Image PLAYER_INVENTORY = new Image("/textures/gui/player_inventory.png");
    public static final Image CHAT_BAR = new Image(ImageModifier.modify(ImageModifier.modify(new Image("/textures/gui/chat_bar.png"), ImageModifier.SCALE_WIDTH, 180), ImageModifier.SCALE_HEIGHT, 10));
    public static final Image ARROW = new Image(ImageModifier.modify(new Image("/textures/misc/arrow.png"), ImageModifier.SET_ALPHA, 50));
    public static final Image VOID = new Image(new Color(0));
    public static final Image ORE_MINER = new Image(ImageModifier.modify(new Image("/textures/block/ore_miner.png"), ImageModifier.SCALE, 1));
    public static final Image GUI_BUTTON = new Image("/textures/gui/button.png");
    public static final Image GUI_BUTTON_HIGHLIGHT = new Image("/textures/gui/button_highlight.png");
    public static final Image GUI_BUTTON_CLICK = new Image("/textures/gui/button_click.png");
    public static final Image GUI_DRAG_GRIP = new Image("/textures/gui/drag_grip.png");
    public static final Image GUI_SCROLL_BAR_TOP = new Image("/textures/gui/scroll_bar_top.png");
    public static final Image GUI_SCROLL_BAR_MIDDLE = new Image("/textures/gui/scroll_bar_middle.png");
    public static final Image GUI_SCROLL_BAR_BOTTOM = new Image("/textures/gui/scroll_bar_bottom.png");
    public static final Image GUI_SCROLL_BAR = new Image("/textures/gui/scroll_bar.png");
    public static final Image GUI_SCROLL_BAR_HIGHLIGHT = new Image("/textures/gui/scroll_bar_highlight.png");
    public static final Image GUI_SCROLL_BAR_CLICK = new Image("/textures/gui/scroll_bar_click.png");
    public static final Image MAIN_MENU_BACKGROUND = new Image(new Color(0x666666));
    public static final Image MAIN_MENU_BUTTON_BOX = new Image("/textures/gui/main_menu.png");
    public static final Image OPTIONS_MENU_BACKGROUND = new Image(new Color(0x999999));
    public static final Image ESCAPE_MENU_BACKGROUND = new Image("/textures/gui/escape_menu_background.png");
    
    private BufferedImage image;

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
