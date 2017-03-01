package powerworks.graphics;

import powerworks.inventory.item.Item;
import powerworks.main.Game;

public class HUD {

    int selectedSlotNum;
    int numberOfSlots = 8;
    
    public void render() {
	for(int slotNum = 0; slotNum < numberOfSlots; slotNum++) {
	    Item item = Game.getMainPlayer().getInv().getItem(slotNum);
	    int xPixel = Screen.screen.originalWidth / 2 - (numberOfSlots / 2) * 16 + slotNum * 16;
	    int yPixel = Screen.screen.originalHeight - 16;
	    if(item == null || Game.getMainPlayer().getInv().getItem(slotNum).getTexture().hasTransparency()) {
		Screen.screen.renderTexture((selectedSlotNum == slotNum) ? StaticTexture.HOTBAR_SLOT_SELECTED : StaticTexture.HOTBAR_SLOT, xPixel, yPixel, false, false);
	    }
	    if(item != null) {
		Screen.screen.renderTexture(item.getTexture(), xPixel, yPixel, false, false);
		if(item.quantity > 1) {
		    Screen.screen.renderText(Integer.toString(item.quantity), 0xFFFFFFE8, xPixel, yPixel);
		}
	    }
	}
    }
    
    public int getSelectedSlotNum() {
	return selectedSlotNum;
    }
    
    public int getNumOfSlots() {
	return numberOfSlots;
    }
    
    public void setSelectedSlotNum(int num) {
	selectedSlotNum = num;
    }
}
