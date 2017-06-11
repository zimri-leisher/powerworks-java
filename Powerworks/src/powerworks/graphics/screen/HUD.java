package powerworks.graphics.screen;

import powerworks.graphics.Image;
import powerworks.main.Game;

public class HUD {
    Hotbar hotbar;
    Healthbar healthbar;
    Chatbar chatbar;
    
    public HUD() {
	hotbar = new Hotbar(Game.getRenderEngine().getWidthPixels() / 2 - 8 * (Image.HOTBAR_SLOT.getWidthPixels() / 2), Game.getRenderEngine().getHeightPixels() - Image.HOTBAR_SLOT.getHeightPixels(), 8);
	healthbar = new Healthbar(0, 0);
	chatbar = new Chatbar(0, 0);
    }
    
    public Hotbar getHotbar() {
	return hotbar;
    }
    
    public Chatbar getChatbar() {
	return chatbar;
    }
    
    public Healthbar getHealthbar() {
	return healthbar;
    }
}