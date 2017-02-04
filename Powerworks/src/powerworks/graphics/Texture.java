package powerworks.graphics;

public interface Texture {

    public int[] getPixels();

    public int getWidthPixels();
    
    public int getHeightPixels();
    
    public boolean hasTransparency();
    
}