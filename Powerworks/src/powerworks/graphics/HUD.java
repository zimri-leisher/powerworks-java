package powerworks.graphics;

public class HUD {

    int selectedSlotNum;
    int numberOfSlots = 8;
    
    public void render() {
	for(int slotNum = 0; slotNum < numberOfSlots; slotNum++) {
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
