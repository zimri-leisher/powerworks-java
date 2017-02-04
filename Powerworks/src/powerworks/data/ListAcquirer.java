package powerworks.data;

import java.util.List;

@FunctionalInterface
public interface ListAcquirer<T> {
    public List<T> get(int x, int y, int width, int height);
}
