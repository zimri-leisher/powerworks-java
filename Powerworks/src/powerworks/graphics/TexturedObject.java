package powerworks.graphics;

import powerworks.data.PhysicalObject;

public interface TexturedObject extends PhysicalObject {
    public Texture getTexture();
    public int getRotation();
    public double getScale();
}