package powerworks.graphics.screen;

import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import powerworks.data.Timer;
import powerworks.graphics.Image;
import powerworks.graphics.ScreenObject;
import powerworks.graphics.Texture;
import powerworks.io.ControlMap;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.TextListener;
import powerworks.main.Game;
import powerworks.task.Task;

public class Chatbar extends ScreenObject {

    boolean open = false;
    boolean underscoreShown = true;
    float chatbarWidthScale = 1.0f;

    protected Chatbar(int xPixel, int yPixel) {
	super(xPixel, yPixel);
    }

    public void setOpen(boolean open) {
	this.open = open;
    }
    
    public boolean isOpen() {
	return open;
    }
    
    public void showUnderscore(boolean show) {
	this.underscoreShown = show;
    }
    
    public boolean showingUnderscore() {
	return underscoreShown;
    }
    
    public void setChatbarSize(int pixels) {
	chatbarWidthScale = (float) pixels / (float) Image.CHAT_BAR.getWidthPixels();
    }
    
    public int getChatbarWidthPixels() {
	return (int) (Image.CHAT_BAR.getWidthPixels() * chatbarWidthScale);
    }

    @Override
    public Texture getTexture() {
	return null;
    }

    @Override
    public void render() {
	Map<Timer, String> messages = Game.getChatManager().getMessagesWithTimers();
	String text = Game.getChatManager().getCurrentText();
	if (open) {
	    Game.getRenderEngine().renderTexture(Image.CHAT_BAR, xPixel, yPixel, 1.0f, chatbarWidthScale, 1.0f, 0, 1.0f, true);
	    String newText = text;
	    if(underscoreShown) newText += "_";
	    Game.getRenderEngine().renderText(newText, xPixel + 2, yPixel + 7, 36);
	}
	Iterator<Entry<Timer, String>> i = messages.entrySet().iterator();
	int d = messages.size();
	while (i.hasNext()) {
	    Entry<Timer, String> e = null;
	    try {
		e = i.next();
		int yPixelL = yPixel + d * Image.CHAT_BAR.getHeightPixels();
		Game.getRenderEngine().renderTexture(Image.CHAT_BAR, xPixel, yPixelL, 1.0f, chatbarWidthScale, 1.0f, 0, (float) Math.min(1, 1.5 - (e.getKey().getCurrentUpdate() / 700.0f)), true);
		Game.getRenderEngine().renderText(e.getValue(), xPixel + 2, yPixelL + 7, 36);
		d--;
	    } catch (ConcurrentModificationException e1) {
	    }
	}
    }

    @Override
    public void update() {
    }

    @Override
    public void onScreenSizeChange() {
    }
}
