package powerworks.data;

import java.util.ArrayList;
import java.util.List;
import powerworks.main.Game;

public class Quadtree<T extends PhysicalObject> {

    static List<Quadtree<?>> trees = new ArrayList<Quadtree<?>>();
    int width, height, x, y;
    Node<T>[] children;
    int currentLevels = 0;
    int numObjects = 0;
    int maxObjects = 8;
    int maxLevels = 5;
    List<T> remove = new ArrayList<T>();

    public static void update() {
	long time = 0;
	if (Game.showUpdateTimes)
	    time = System.nanoTime();
	trees.forEach((Quadtree<?> q) -> q.refresh());
	if (Game.showUpdateTimes)
	    System.out.println("Updating quadtrees took:     " + (System.nanoTime() - time) + " ns");
    }

    @SuppressWarnings("unchecked")
    public Quadtree(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	children = new Node[4];
	int newWidth = width / 2;
	int newHeight = height / 2;
	int x2 = x + newWidth;
	int y2 = y + newHeight;
	children[0] = new Node<T>(x2, y, newWidth, newHeight, null);
	children[1] = new Node<T>(x2, y2, newWidth, newHeight, null);
	children[2] = new Node<T>(x, y2, newWidth, newHeight, null);
	children[3] = new Node<T>(x, y, newWidth, newHeight, null);
	trees.add(this);
    }

    public List<T> retrieveAll() {
	List<T> returnObj = new ArrayList<T>();
	for (int i = 0; i < 4; i++)
	    children[i].retrieveAll(returnObj);
	return returnObj;
    }

    private int localIndex(T t) {
	return localIndex(t.getXPixel(), t.getYPixel(), t.getWidthPixels(), t.getHeightPixels());
    }

    private int localIndex(int x, int y, int width, int height) {
	int xMid = this.width / 2;
	int yMid = this.height / 2;
	boolean top = y < yMid;
	boolean left = x < xMid;
	int index = -1;
	if (top)
	    if (left)
		index = 3;
	    else
		index = 0;
	else if (left)
	    index = 2;
	else
	    index = 1;
	return index;
    }

    public void print() {
	List<String> lines = new ArrayList<String>();
	for (int u = 0; u < children.length; u++)
	    children[u].print(u, lines);
	lines.sort((String o1, String o2) -> o1.compareTo(o2));
	lines.forEach((String s) -> System.out.println(s));
	System.out.println("Total levels: " + currentLevels + ", number of objects: " + numObjects);
    }

    public void put(T t) {
	children[localIndex(t)].put(t);
	numObjects++;
    }

    public void refresh() {
	currentLevels = 0;
	numObjects = 0;
	List<T> reinsert = new ArrayList<T>();
	for (int i = 0; i < 4; i++) {
	    children[i].refresh(reinsert);
	}
	reinsert.forEach((T t) -> {
	    if (!remove.contains(t))
		put(t);
	    else
		numObjects--;
	});
	remove.clear();
    }

    public void remove(T t) {
	children[localIndex(t)].remove(t);
	numObjects--;
    }

    public void removeNextRefresh(T t) {
	remove.add(t);
    }

    public List<T> retrievePossible(T t) {
	List<T> returnObj = new ArrayList<T>();
	children[localIndex(t)].retrievePossible(t, returnObj);
	return returnObj;
    }

    public List<T> retrieveIn(int x, int y, int width, int height) {
	List<T> returnObj = new ArrayList<T>();
	children[localIndex(x, y, width, height)].retrieveIn(x, y, width, height, returnObj);
	return returnObj;
    }

    public boolean anyIn(int x, int y, int width, int height) {
	return children[localIndex(x, y, width, height)].anyIn(x, y, width, height);
    }

    public void setMaxObjects(int m) {
	maxObjects = m;
    }

    public void setMaxLevels(int m) {
	maxLevels = m;
    }

    @SuppressWarnings("hiding")
    class Node<T extends PhysicalObject> {

	int x, y, width, height;
	Node<T>[] children;
	List<T> objects = new ArrayList<T>();
	Node<T> parent;
	int level = 0;

	Node(int x, int y, int width, int height, Node<T> parent) {
	    this.x = x;
	    this.y = y;
	    this.width = width;
	    this.height = height;
	    this.parent = parent;
	    if (parent != null) {
		level = parent.level + 1;
	    }
	}

	public void retrieveAll(List<T> returnObj) {
	    returnObj.addAll(objects);
	    if (!hasChildren())
		return;
	    for (int i = 0; i < 4; i++)
		children[i].retrieveAll(returnObj);
	}

	public boolean anyIn(int x, int y, int width, int height) {
	    for (T t : objects) {
		if (x < t.getXPixel() + t.getWidthPixels() && x + width > t.getXPixel() && y < t.getYPixel() + t.getHeightPixels() && y + height > t.getYPixel()) {
		    return true;
		}
	    }
	    if (!hasChildren())
		return false;
	    return children[localIndex(x, y, width, height)].anyIn(x, y, width, height);
	}

	boolean contains(T t) {
	    return (t.getXPixel() >= x) && (t.getYPixel() >= y) &&
		    (t.getXPixel() + t.getWidthPixels() <= x + width) && (t.getYPixel() + t.getHeightPixels() <= y + height);
	}

	boolean hasChildren() {
	    return children != null;
	}

	boolean immediateChildrenAreEmpty() {
	    for (int i = 0; i < 4; i++)
		if (children[i].objects.size() != 0)
		    return false;
	    return true;
	}

	private int localIndex(T t) {
	    return localIndex(t.getXPixel(), t.getYPixel(), t.getWidthPixels(), t.getHeightPixels());
	}

	private int localIndex(int x, int y, int width, int height) {
	    int xMid = this.width / 2;
	    int yMid = this.height / 2;
	    boolean top = y < yMid;
	    boolean left = x < xMid;
	    int index = -1;
	    if (top)
		if (left)
		    index = 3;
		else
		    index = 0;
	    else if (left)
		index = 2;
	    else
		index = 1;
	    return index;
	}

	public void print(int coord, List<String> lines) {
	    if (objects.size() != 0)
		lines.add("Level " + level + ", coord " + coord + ": " + objects.size());
	    if (hasChildren())
		for (int i = 0; i < 4; i++)
		    children[i].print(i, lines);
	}

	void put(T t) {
	    int localIndex = localIndex(t);
	    if (hasChildren()) {
		if (children[localIndex].contains(t)) {
		    children[localIndex].put(t);
		} else {
		    objects.add(t);
		}
	    } else {
		if (objects.size() == maxObjects) {
		    if (level < maxLevels) {
			split();
			if (children[localIndex].contains(t)) {
			    children[localIndex].put(t);;
			} else {
			    objects.add(t);
			}
		    } else {
			objects.add(t);
		    }
		} else {
		    objects.add(t);
		}
	    }
	}

	void refresh(List<T> reinsert) {
	    if (hasChildren())
		for (int i = 0; i < 4; i++)
		    children[i].refresh(reinsert);
	    reinsert.addAll(objects);
	    children = null;
	    parent = null;
	    objects.clear();
	}

	void remove(T t) {
	    int localIndex = localIndex(t);
	    if (children == null || !children[localIndex].contains(t)) {
		objects.remove(t);
	    } else
		children[localIndex].remove(t);
	}

	void retrieveIn(int x, int y, int width, int height, List<T> returnObj) {
	    for (T t : objects) {
		if (!(x >= t.getXPixel() + t.getWidthPixels()) && !(x + width <= t.getXPixel() && !(t.getYPixel() + t.getHeightPixels() >= y) && !(t.getYPixel() <= y + height))) {
		    System.out.println(t.getXPixel() + ", " + t.getYPixel() + ", " + t.getWidthPixels() + ", " + t.getHeightPixels() + " is inside " + x + ", " + y + ", " + width + ", " + height);
		    returnObj.add(t);
		}
	    }
	    if (hasChildren())
		children[localIndex(x, y, width, height)].retrieveIn(x, y, width, height, returnObj);
	}

	void retrievePossible(T t, List<T> returnObj) {
	    returnObj.addAll(objects);
	    if (hasChildren())
		children[localIndex(t)].retrievePossible(t, returnObj);
	}

	@SuppressWarnings("unchecked")
	void split() {
	    if (level >= currentLevels)
		currentLevels++;
	    children = new Node[4];
	    int newWidth = width / 2;
	    int newHeight = height / 2;
	    int x2 = x + newWidth;
	    int y2 = y + newHeight;
	    children[0] = new Node<T>(x2, y, newWidth, newHeight, this);
	    children[1] = new Node<T>(x2, y2, newWidth, newHeight, this);
	    children[2] = new Node<T>(x, y2, newWidth, newHeight, this);
	    children[3] = new Node<T>(x, y, newWidth, newHeight, this);
	}
    }
}