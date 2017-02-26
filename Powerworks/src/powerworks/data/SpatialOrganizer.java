package powerworks.data;

import java.util.ArrayList;
import java.util.List;

public class SpatialOrganizer<T extends PhysicalObject> {
    ArrayList<T> objects = new ArrayList<T>();
    
    public List<T> getIntersecting(int x, int y, int width, int height) {
	List<T> returnObj = new ArrayList<T>();
	objects.stream().filter((T t) -> intersects(x, y, width, height, t.getXPixel(), t.getYPixel(), t.getWidthPixels(), t.getHeightPixels())).forEach(returnObj::add);
	return returnObj;
    }
    
    public boolean anyIntersecting(int x, int y, int width, int height) {
	for(T t : objects)
	    if(intersects(x, y, width, height, t.getXPixel(), t.getYPixel(), t.getWidthPixels(), t.getHeightPixels()))
		return true;
	return false;
    }
    
    public List<T> getContainedBy(int x, int y, int width, int height) {
	List<T> returnObj = new ArrayList<T>();
	objects.stream().filter((T t) -> contains(x, y, width, height, t.getXPixel(), t.getYPixel(), t.getWidthPixels(), t.getHeightPixels())).forEach(returnObj::add);
	return returnObj;
    }
    
    public List<T> getAll() {
	return objects;
    }
    
    public void add(T t) {
	objects.add(t);
    }

    public void remove(T t) {
	objects.remove(t);
    }
    
    boolean intersects(int xPixel, int yPixel, int width, int height, int xPixel2, int yPixel2, int width2, int height2) {
	if (xPixel + width < xPixel2 || yPixel + height < yPixel2 || xPixel >= xPixel2 + width2 || yPixel >= yPixel2 + height2)
	    return false;
	return true;
    }

    boolean contains(int xPixel, int yPixel, int width, int height, int xPixelIn, int yPixelIn, int widthIn, int heightIn) {
	if (xPixelIn >= xPixel && yPixelIn >= yPixel && xPixelIn + widthIn < xPixel + width && yPixelIn + heightIn < yPixel + height)
	    return true;
	return false;
    }
}
