package powerworks.data;

@FunctionalInterface
public interface IndexGetter<T extends PhysicalObject> {
    public int getIndex(T t);
}
