package powerworks.event;


public class ZoomEvent extends Event{
    public double zoomFactor;
    
    public ZoomEvent(double zoomFactor) {
	this.zoomFactor = zoomFactor;
    }
}
