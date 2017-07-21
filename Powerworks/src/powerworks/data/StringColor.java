package powerworks.data;

import java.awt.Color;
import java.util.regex.Pattern;

public class StringColor {
    public static String removeColors(String s) {
	return s.replaceAll(Pattern.quote("&*&") + ".*" + Pattern.quote("&*&"), "");
    }
    
    /**
     * Gets the first color from a string
     */
    public static Color getColor(String c) {
	 return Color.decode(c.substring(c.indexOf("&*&") + 3, c.indexOf("&*&", c.indexOf("&*&") + 3)));
    }
    
    public static String getText(Color c) {
	return "&*&" + String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()) + "&*&"; 
    }
}
