package powerworks.graphics;

import java.awt.image.BufferedImage;

public interface Texture {
    public BufferedImage getImage();
    public int getWidthPixels();
    public int getHeightPixels();
}
