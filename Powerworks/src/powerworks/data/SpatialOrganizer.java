package powerworks.data;

import java.util.ArrayList;
import java.util.List;
import powerworks.collidable.Collidable;

public class SpatialOrganizer<T extends Collidable> {
    ArrayList<T> objects = new ArrayList<T>();
    
    public List<T> getIntersecting(int x, int y, int width, int height) {
	List<T> returnObj = new ArrayList<T>();
	objects.stream().filter((T t) -> GeometryHelper.intersects(x, y, width, height, t.getXPixel() + t.getHitbox().xStart, t.getYPixel() + t.getHitbox().yStart, t.getHitbox().width, t.getHitbox().height)).forEach(returnObj::add);
	return returnObj;
    }
    
    public boolean anyIntersecting(int x, int y, int width, int height) {
	for(T t : objects)
	    if(GeometryHelper.intersects(x, y, width, height, t.getXPixel() + t.getHitbox().xStart, t.getYPixel() + t.getHitbox().yStart, t.getHitbox().width, t.getHitbox().height))
		return true;
	return false;
    }
    
    public List<T> getContainedBy(int x, int y, int width, int height) {
	List<T> returnObj = new ArrayList<T>();
	objects.stream().filter((T t) -> GeometryHelper.contains(x, y, width, height, t.getXPixel() + t.getHitbox().xStart, t.getYPixel() + t.getHitbox().yStart, t.getHitbox().width, t.getHitbox().height)).forEach(returnObj::add);
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
    
}
