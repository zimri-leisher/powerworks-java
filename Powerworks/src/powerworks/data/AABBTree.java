package powerworks.data;

import java.util.ArrayList;
import java.util.List;

public class AABBTree<T extends PhysicalObject> {

    Node left, right;

    public void getIntersecting(Node n, AABB box, List<Node> returnObj) {
	if (n == null)
	    return;
	if (intersects(box.x, box.y, box.width, box.height, n.box.x, n.box.y, n.box.width, n.box.height)) {
	    if (!n.hasChildren())
		returnObj.add(n);
	    else {
		getIntersecting(n.left, box, returnObj);
		getIntersecting(n.right, box, returnObj);
	    }
	    	
	}
    }
    
    public Node insert(Node root, Node n) {
	n.left = null;
	n.right = null;
	n.parent = root;
	n.level = 0;
	if(root == null)
	    root = n;
	else if(!root.hasChildren()) {
	    //Node nP = new Node(null,)
	}
	return null;
    }

    class Node {

	AABB box;
	Node parent, left, right;
	int level;

	private Node(AABB box, Node parent) {
	    this.box = box;
	}

	boolean hasChildren() {
	    if (left == null && right == null)
		return false;
	    return true;
	}

	void getIntersecting(int x, int y, int width, int height, List<T> returnObj) {
	}
    }
    
    class AABB {

	int x, y, width, height;

	private AABB(int x, int y, int width, int height) {
	    this.x = x;
	    this.y = y;
	    this.width = width;
	    this.height = height;
	}

	boolean intersects(AABB other) {
	    return intersects(other.x, other.y, other.width, other.height);
	}

	boolean intersects(int x2, int y2, int width2, int height2) {
	    if (x + width < x2 || y + height < y2 || x >= x2 + width2 || y >= y2 + height2)
		return false;
	    return true;
	}
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
