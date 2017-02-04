package powerworks.graphics.gui;

import powerworks.graphics.StaticTexture;

public class GUIPane {

    StaticTexture background;
    StaticTexture edges;
    StaticTexture corners;
    /**
     * In tiles
     */
    int width, height;
    /**
     * In pixels
     */
    int x, y;

    public GUIPane(int x, int y, int width, int height) {
    }

    public void render() {
    }
}
