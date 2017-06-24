package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.main.Game;
import powerworks.main.Setting;
import powerworks.task.Task;

public class OptionsMenuGUI extends GUI {

    @SuppressWarnings("unused")
    private GUITexturePane background;
    private GUIElementList options;
    private AutoFormatGUIGroup elements;
    @SuppressWarnings("unused")
    private GUIButton back;

    public OptionsMenuGUI() {
	super(0, 0, 1);
	int width = Game.getScreenWidth();
	int height = Game.getScreenHeight();
	background = new GUITexturePane(this, 0, 0, width, height, 0, Image.MAIN_MENU_BACKGROUND);
	elements = new AutoFormatGUIGroup(null, 0, 0, 2, 2, 2);
	for(GUIElement el : Setting.genOptionsList())
	    elements.addChild(el);
	new GUITexturePane(this, 5, 5, width - 10, height - 24, 2, Image.OPTIONS_MENU_BACKGROUND);
	options = new GUIElementList(this, 5, 5, width - 10, height - 24, 3, elements);
	back = new GUIButton(this, 5, height - Image.GUI_BUTTON.getHeightPixels() - 2, 4, "Back", true, new Task() {

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

    public GUIElementList getOptions() {
	return options;
    }
}
