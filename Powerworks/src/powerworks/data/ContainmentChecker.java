package powerworks.data;

@FunctionalInterface
public interface ContainmentChecker<T extends PhysicalObject> {
    public boolean containedBy(int xPixel, int yPixel, int width, int height, T t);
}
