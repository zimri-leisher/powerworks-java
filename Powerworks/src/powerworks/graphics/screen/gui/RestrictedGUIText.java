package powerworks.graphics.screen.gui;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import powerworks.graphics.screen.ScreenObject;
import powerworks.main.Game;

public class RestrictedGUIText extends GUIText {

    public RestrictedGUIText(ScreenObject parent, int xPixel, int yPixel, int widthPixelLimit, int layer, String text) {
	super(parent, xPixel, yPixel, layer, formatText(text, widthPixelLimit));
    }

    private static String formatText(String text, int widthPixelLimit) {
	FontRenderContext frc = new FontRenderContext(null, false, false);
	int s = Game.getScreenScale();
	Font f = Game.getFont(28);
	String[] words = text.split(" ");
	String current = "";
	String newS = "";
	for (int i = 0; i < words.length; i++) {
	    newS += words[i] + " ";
	    if (f.getStringBounds(newS, frc).getWidth() / s >= widthPixelLimit) {
		current += newS + "\n";
		newS = "";
	    }
	}
	return current;
    }
}
