package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.task.Task;

public class OptionsMenuGUI extends GUI {

    @SuppressWarnings("unused")
    private GUITexturePane background;
    private GUIElementList options;
    @SuppressWarnings("unused")
    private GUIButton back;

    public OptionsMenuGUI() {
	super(0, 0, 1);
	int width = Game.getScreenWidth();
	int height = Game.getScreenHeight();
	background = new GUITexturePane(this, 0, 0, width, height, 0, Image.MAIN_MENU_BACKGROUND);
	GUIGroup g = new GUIGroup(null, 0, 0, 2);
	g.addChild(new GUITexturePane(null, 2, 2, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels(), 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 2, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 3, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 4, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 5, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 6, 3, Image.GUI_BUTTON));
	g.addChild(new GUITexturePane(null, 2, 2 + Image.GUI_BUTTON.getHeightPixels() * 7, 3, Image.GUI_BUTTON));
	new GUITexturePane(this, 5, 5, width - 10, height - 24, 2, Image.OPTIONS_MENU_BACKGROUND);
	options = new GUIElementList(this, 5, 5, width - 10, height - 24, 3, g);
	g.setParent(options);
	back = new GUIButton(this, 5, height - Image.GUI_BUTTON.getHeightPixels() - 2, Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), 4, "Back", true, new Task() {

	    @Override
	    public void run() {
	    }
	}, new Task() {

	    @Override
	    public void run() {
		close();
		Game.getMainMenuGUI().open();
	    }
	});
    }
}
