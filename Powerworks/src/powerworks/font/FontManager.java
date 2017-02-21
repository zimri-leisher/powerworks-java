package powerworks.font;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class FontManager {
    static HashMap<Character, Integer[]> chars = new HashMap<Character, Integer[]>();
    
    public static void load(String path) {
	
	try {
	    BufferedImage image = ImageIO.read(FontManager.class.getResourceAsStream(path));
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
