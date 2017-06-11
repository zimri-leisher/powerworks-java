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

public enum State {
    MAIN_MENU(new Task() {

	@Override
	public void run() {
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
	    Game.world = WorldManager.genWorld(256, 256, seed);
	    Game.player = new Player(500, 500);
	    Game.render.setOffset(Game.player.getXPixel() - Game.width / 2, Game.player.getYPixel() - Game.height / 2);
	    Game.allPlayerNames = new ArrayList<String>();
	    Game.allPlayerNames.add(Game.player.getName());
	    Game.allPlayers = new ArrayList<Player>();
	    Game.allPlayers.add(Game.player);
	    Game.hud = new HUD();
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

    //http://imgur.com/gallery/PRjAC
    
    public static void setState(State s) {
	CURRENT_STATE.off.run();
	CURRENT_STATE = s;
	s.on.run();
    }

    public static State getState() {
	return CURRENT_STATE;
    }
}
