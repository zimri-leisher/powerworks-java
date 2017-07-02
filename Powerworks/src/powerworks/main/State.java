package powerworks.main;

import java.util.ArrayList;
import java.util.Random;
import powerworks.chat.ChatManager;
import powerworks.collidable.moving.living.Player;
import powerworks.graphics.screen.HUD;
import powerworks.io.ControlMap;
import powerworks.task.Task;
import powerworks.io.InputManager;
import powerworks.world.WorldManager;
import powerworks.world.level.LevelManager;

public enum State {
    MAIN_MENU(new Task() {

	@Override
	public void run() {
	    Game.hud.close();
	    Game.mainMenu.open();
	    Game.paused = false;
	    InputManager.setMapping(ControlMap.MAIN_MENU);
	}
    }, new Task() {

	@Override
	public void run() {
	    System.out.println("Loading game...");
	}
    }), INGAME(new Task() {

	@Override
	public void run() {
	    InputManager.setMapping(ControlMap.DEFAULT_INGAME);
	    long seed = (new Random()).nextInt(4096);
	    Game.worldManager = new WorldManager();
	    Game.levelManager = new LevelManager();
	    Game.world = Game.worldManager.genWorld(256, 256, seed);
	    Game.player = new Player("Player");
	    Game.render.setOffset(Game.player.getXPixel() - Game.width / 2, Game.player.getYPixel() - Game.height / 2);
	    Game.allPlayerNames = new ArrayList<String>();
	    Game.allPlayerNames.add(Game.player.getUsername());
	    Game.allPlayers = new ArrayList<Player>();
	    Game.allPlayers.add(Game.player);
	    if(Game.hud == null) {
		Game.hud = new HUD();
	    }
	    Game.hud.open();
	    Game.chatManager = new ChatManager();
	}
    }, new Task() {

	@Override
	public void run() {
	}
    });

    Task on, off;

    private State(Task on, Task off) {
	this.on = on;
	this.off = off;
    }

    private static State CURRENT_STATE = MAIN_MENU;

    // http://imgur.com/gallery/PRjAC
    public static void setState(State s) {
	CURRENT_STATE.off.run();
	CURRENT_STATE = s;
	s.on.run();
    }

    public static State getState() {
	return CURRENT_STATE;
    }
}
