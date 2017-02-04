package powerworks.event;


public enum EventPriority {
    MAX(5), HIGH(4), MED(3), LOW(2), MIN(1);
    
    int p;
    
    private EventPriority(int p) {
	this.p = p;
    }
}
