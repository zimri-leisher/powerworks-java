package powerworks.graphics.screen.gui;

import powerworks.graphics.Image;
import powerworks.graphics.Texture;
import powerworks.graphics.screen.ScreenObject;
import powerworks.main.Game;
import powerworks.main.State;
import powerworks.task.Task;

public final class MainMenuGUI extends GUI {

    @SuppressWarnings("unused")
    private GUITexturePane background, box;
    @SuppressWarnings("unused")
    private GUIButton play, options;
    private GUIDragGrip grip;

    public MainMenuGUI() {
	super(0, 0, 1);
	int width = Game.getScreenWidth();
	int height = Game.getScreenHeight();
	background = new GUITexturePane(this, 0, 0, width, height, 0, Image.MAIN_MENU_BACKGROUND);
	box = new GUITexturePane(this, (width - Image.MAIN_MENU_BUTTON_BOX.getWidthPixels()) / 2, (height - Image.MAIN_MENU_BUTTON_BOX.getHeightPixels()) / 2, 1, Image.MAIN_MENU_BUTTON_BOX);
	play = new GUIButton(box, (Image.MAIN_MENU_BUTTON_BOX.getWidthPixels() - Image.GUI_BUTTON.getWidthPixels()) / 2, Image.MAIN_MENU_BUTTON_BOX.getHeightPixels() / 2 - Image.GUI_BUTTON.getHeightPixels(),
		Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), 2, "Play", true, new Task() {

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
	options = new GUIButton(box, (Image.MAIN_MENU_BUTTON_BOX.getWidthPixels() - Image.GUI_BUTTON.getWidthPixels()) / 2, Image.MAIN_MENU_BUTTON_BOX.getHeightPixels() / 2,
		Image.GUI_BUTTON.getWidthPixels(), Image.GUI_BUTTON.getHeightPixels(), 2, "Options", true, new Task() {

		    @Override
		    public void run() {
		    }
		}, new Task() {

		    @Override
		    public void run() {
		    }
		});
	grip = new GUIDragGrip(box, Image.MAIN_MENU_BUTTON_BOX.getWidthPixels() - Image.GUI_DRAG_GRIP.getWidthPixels(), 0, 2);
	new GUITextField(this, 10, 10, 8, 30, 3, "tt");
    }

    @Override
    public Texture getTexture() {
	return null;
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
