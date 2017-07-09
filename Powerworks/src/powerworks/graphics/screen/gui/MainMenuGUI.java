package powerworks.graphics.screen.gui;

import java.util.Arrays;
import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.main.Game;
import powerworks.main.State;
import powerworks.task.Task;

public final class MainMenuGUI extends GUI {

    @SuppressWarnings("unused")
    private GUITexturePane background, box, logo;
    private GUIGroup buttons;
    private GUIButton play, options, exit;
    @SuppressWarnings("unused")
    private GUIDragGrip grip;
    private GUIText text;
    
    public MainMenuGUI() {
	super(0, 0, 1);
	int width = Game.getScreenWidth();
	int height = Game.getScreenHeight();
	background = new GUITexturePane(this, 0, 0, width, height, 1, Image.MAIN_MENU_BACKGROUND);
	logo = new GUITexturePane(this, (width - Image.MAIN_MENU_LOGO.getWidthPixels()) / 2 - 1, (height - Image.MAIN_MENU_LOGO.getHeightPixels()) / 10, 2, Image.MAIN_MENU_LOGO);
	box = new GUITexturePane(this, (width - Image.MAIN_MENU_BUTTON_BOX.getWidthPixels()) / 2, (height - Image.MAIN_MENU_BUTTON_BOX.getHeightPixels()) / 2, 1, Image.MAIN_MENU_BUTTON_BOX);
	play = new GUIButton(null, 0, 0, 2, "Play", true, new Task() {

		    @Override
		    public void run() {
		    }
		}, new Task() {

		    @Override
		    public void run() {
			close();
			State.setState(State.INGAME);
		    }
		});
	options = new GUIButton(null, 0, 0, 2, "Options", true, new Task() {

		    @Override
		    public void run() {
		    }
		}, new Task() {

		    @Override
		    public void run() {
			close();
			Game.getOptionsMenuGUI().open();
		    }
		});
	exit = new GUIButton(null, 0, 0, 2, "Exit", true,
		new Task() {

		    @Override
		    public void run() {
		    }
		}, new Task() {

		    @Override
		    public void run() {
			Game.exit();
		    }
		});
	buttons = new AutoFormatGUIGroup(box, 0, 0, 2, 0, 0, Arrays.asList(play, options, exit));
	buttons.setRelXPixel((Image.MAIN_MENU_BUTTON_BOX.getWidthPixels() - buttons.getWidthPixels()) / 2);
	buttons.setRelYPixel((Image.MAIN_MENU_BUTTON_BOX.getHeightPixels() - buttons.getHeightPixels()) / 2);
	grip = new GUIDragGrip(box, Image.MAIN_MENU_BUTTON_BOX.getWidthPixels() - Image.GUI_DRAG_GRIP.getWidthPixels() - 1, Image.MAIN_MENU_BUTTON_BOX.getHeightPixels() - Image.GUI_DRAG_GRIP.getHeightPixels() - 1, 2);
	text = new GUIText(this, 4, height - 4, 2, "Version " + Game.VERSION + " private build");
    }

    @Override
    public void update() {
    }

    @Override
    protected void onOpen() {
    }

    @Override
    protected void onClose() {
    }

    @Override
    public void onScreenSizeChange(int oldWidthPixels, int oldHeightPixels) {
    }
}
