package powerworks.data;

@FunctionalInterface
public interface IntersectionChecker<T extends PhysicalObject> {
    public boolean intersects(int xPixel, int yPixel, int width, int height, T t);
}