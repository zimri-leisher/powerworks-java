package powerworks.event;


public class ViewMoveEvent extends Event{
    public int x, y;
    
    public ViewMoveEvent(int x, int y) {
	this.x = x;
	this.y = y;
    }
}
