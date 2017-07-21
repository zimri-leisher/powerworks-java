package powerworks.graphics.screen;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import powerworks.chat.ChatManager;
import powerworks.data.Timer;
import powerworks.graphics.Image;
import powerworks.graphics.RenderParams;
import powerworks.graphics.Renderer;
import powerworks.main.Game;

public class Chatbar extends ScreenObject {
    
    private boolean active = false;
    private boolean underscoreShown = true;
    private float chatbarWidthScale = 1.0f;

    protected Chatbar(int xPixel, int yPixel) {
	super(xPixel, yPixel, 1);
    }

    public void showUnderscore(boolean show) {
	this.underscoreShown = show;
    }

    public boolean showingUnderscore() {
	return underscoreShown;
    }
    
    /**
     * True if text is being listened for
     */
    public boolean isActive() {
	return active;
    }
    
    public void setActive(boolean active) {
	this.active = active;
    }

    public void setChatbarSize(int pixels) {
	chatbarWidthScale = (float) pixels / (float) Image.CHAT_BAR.getWidthPixels();
    }

    public int getChatbarWidthPixels() {
	return (int) (Image.CHAT_BAR.getWidthPixels() * chatbarWidthScale);
    }

    @Override
    public void render() {
	Renderer r = Game.getRenderEngine();
	ChatManager c = Game.getChatManager();
	Map<Timer, String> messages = c.getMessagesWithTimers();
	String text = c.getCurrentText();
	if (active) {
	    r.renderTexture(Image.CHAT_BAR, xPixel, yPixel, new RenderParams().setWidthScale(chatbarWidthScale));
	    String newText = text;
	    if (underscoreShown)
		newText += "_";
	    r.renderText(newText, xPixel + 2, yPixel + 7, 36);
	}
	Iterator<Entry<Timer, String>> i = messages.entrySet().iterator();
	int d = messages.size();
	while (i.hasNext()) {
	    Entry<Timer, String> e = null;
	    try {
		e = i.next();
		int yPixelL = yPixel + d * Image.CHAT_BAR.getHeightPixels();
		r.renderTexture(Image.CHAT_BAR, xPixel, yPixelL, new RenderParams().setWidthScale(chatbarWidthScale).setAlpha((float) Math.min(1, 2 - (e.getKey().getCurrentUpdate() / 500.0f))));
		r.renderText(e.getValue(), xPixel + 2, yPixelL + 7, 36);
		d--;
	    } catch (ConcurrentModificationException e1) {
	    }
	}
    }

    @Override
    public void update() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }

    @Override
    public void onOpen() {
	
    }

    @Override
    public void onClose() {
	
    }
    
    @Override
    public String toString() {
	return "Chatbar at " + xPixel + ", " + yPixel + " with id " + id;
    }

}