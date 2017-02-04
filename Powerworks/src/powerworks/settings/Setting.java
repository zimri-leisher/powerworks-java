package powerworks.settings;


public abstract class Setting<T> {
    
    String name;
    String desc;
    T currentValue;

    protected Setting(String name, String desc) {
	this.name = name;
	this.desc = desc;
    }
    
    public String getName() {
	return name;
    }
    
    public String getDesc() {
	return desc;
    }
    
    public T getCurrentValue() {
	return currentValue;
    }
}
