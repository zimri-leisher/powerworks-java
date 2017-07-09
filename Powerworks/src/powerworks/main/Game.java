package powerworks.main;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import powerworks.audio.AudioManager;
import powerworks.chat.ChatCommandExecutor;
import powerworks.chat.ChatManager;
import powerworks.collidable.moving.living.Player;
import powerworks.data.Timer;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.graphics.Renderer;
import powerworks.graphics.SyncAnimation;
import powerworks.graphics.screen.HUD;
import powerworks.graphics.screen.Mouse;
import powerworks.graphics.screen.ScreenManager;
import powerworks.graphics.screen.gui.DebugInfoOverlay;
import powerworks.graphics.screen.gui.EscapeMenuGUI;
import powerworks.graphics.screen.gui.EscapeOptionsMenuGUI;
import powerworks.graphics.screen.gui.MainMenuGUI;
import powerworks.graphics.screen.gui.OptionsMenuGUI;
import powerworks.io.ControlMap;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.Logger;
import powerworks.io.Statistic;
import powerworks.task.Task;
import powerworks.world.WorldManager;
import powerworks.world.level.Chunk;
import powerworks.world.level.Level;
import powerworks.world.level.LevelManager;

public final class Game extends Canvas implements Runnable, EventListener, KeyControlHandler {

    private static Game game;
    boolean running = false;
    private static final long serialVersionUID = 1L;
    public static final float UPDATES_PER_SECOND = 60.0f;
    public static final float FRAMES_PER_SECOND = 100000000.0f;
    public static final float MS_PER_UPDATE = 1000 / UPDATES_PER_SECOND;
    public static final float MS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
    public static final float NS_PER_UPDATE = 1000000000 / UPDATES_PER_SECOND;
    public static final float NS_PER_FRAME = 1000000000 / FRAMES_PER_SECOND;
    public static final int MAX_UPDATES_BEFORE_RENDER = 5;
    public static final String VERSION = "0.0.2";
    private static boolean FPS_MODE = false;
    private static boolean PAUSE_IN_ESCAPE_MENU = true;
    static boolean paused = false;
    static boolean showHitboxes = false;
    static int width = 300, zoomedWidth = width;
    static int height = width / 16 * 9, zoomedHeight = height;
    static int scale = 4;
    static int secondCount = 0;
    static long updateCount = 0;
    static long frameCount = 0;
    int prevFrameWidth = 0;
    int prevFrameHeight = 0;
    static Thread gameThread;
    static JFrame frame;
    static Renderer render;
    static Mouse mouse;
    static HUD hud;
    static Cursor defCursor;
    static ScreenManager screen;
    static MainMenuGUI mainMenu;
    static OptionsMenuGUI optionsMenu;
    static EscapeMenuGUI escapeMenu;
    static EscapeOptionsMenuGUI escapeOptionsMenu;
    static DebugInfoOverlay debugInfo;
    static GraphicsConfiguration gConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    static Font mainFont = null;
    static HashMap<Integer, Font> fonts = new HashMap<Integer, Font>();
    static AudioManager audio;
    static List<String> allPlayerNames;
    static List<Player> allPlayers;
    static Player player;
    static WorldManager world;
    static LevelManager level;
    // Required to have listeners
    static InputManager input;
    static Logger logger;
    static ChatCommandExecutor chatCmdExecutor;
    static ChatManager chatManager;
    static boolean showRenderTimes = false;
    static boolean showUpdateTimes = false;

    private Game() {
	loadFont();
	setPreferredSize(new Dimension(width * scale, height * scale));
	frame = new JFrame();
	logger = new Logger();
	input = new InputManager();
	chatCmdExecutor = new ChatCommandExecutor();
	render = new Renderer(width, height);
	screen = new ScreenManager();
	mouse = new Mouse();
	mainMenu = new MainMenuGUI();
	optionsMenu = new OptionsMenuGUI();
	escapeMenu = new EscapeMenuGUI();
	escapeOptionsMenu = new EscapeOptionsMenuGUI();
	audio = new AudioManager();
	audio.load();
	debugInfo = new DebugInfoOverlay();
	addKeyListener(input);
	addMouseWheelListener(input);
	addMouseListener(input);
	addMouseMotionListener(input);
	defCursor = Cursor.getDefaultCursor();
	setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
		new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
		new Point(0, 0), "null"));
	EventManager.registerEventListener(this);
	InputManager.registerKeyControlHandler(this, ControlMap.DEFAULT_INGAME, KeyControlOption.EXIT, KeyControlOption.SHOW_RENDER_TIMES, KeyControlOption.SHOW_UPDATE_TIMES,
		KeyControlOption.RENDER_HITBOX,
		KeyControlOption.DEBUG_INFO);
	InputManager.registerKeyControlHandler(this, ControlMap.MAIN_MENU, KeyControlOption.EXIT, KeyControlOption.DEBUG_INFO);
    }

    private synchronized void start() {
	gameThread = new Thread(this, "Display");
	gameThread.start();
	running = true;
    }

    private synchronized void stop() {
	logger.close();
	System.exit(0);
	try {
	    gameThread.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Should not normally be used, only necessary for things that have to do
     * with the Component part of the game (i.e. mouse icons, frame size, etc.)
     */
    public static Game getInstance() {
	return game;
    }

    public static boolean isPaused() {
	return paused;
    }

    /**
     * Sets the cursor to the default windows cursor
     */
    public void resetMouseIcon() {
	setCursor(defCursor);
    }

    public void clearMouseIcon() {
	setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
		new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
		new Point(0, 0), "null"));
    }
    
    public static void setPaused(boolean val) {
	paused = val;
    }

    /**
     * Enables or disables thread waiting every cycle of the main loop
     */
    static void setFPSMode(boolean mode) {
	FPS_MODE = mode;
    }

    static void setPauseInEscapeMenu(boolean val) {
	PAUSE_IN_ESCAPE_MENU = val;
    }

    public static MainMenuGUI getMainMenuGUI() {
	return mainMenu;
    }

    public static OptionsMenuGUI getOptionsMenuGUI() {
	return optionsMenu;
    }

    public static EscapeMenuGUI getEscapeMenuGUI() {
	return escapeMenu;
    }

    public static EscapeOptionsMenuGUI getEscapeOptionsMenuGUI() {
	return escapeOptionsMenu;
    }

    public static void exit() {
	game.running = false;
    }

    static void setShowHitboxes(boolean show) {
	showHitboxes = show;
    }

    public static int getSecondsSinceStart() {
	return secondCount;
    }

    public static long getUpdatesSinceStart() {
	return updateCount;
    }

    public static long getFramesSinceStart() {
	return frameCount;
    }

    public static AudioManager getAudioManager() {
	return audio;
    }

    /**
     * For generating, loading and unloading worlds
     */
    public static WorldManager getWorldManager() {
	return world;
    }

    /**
     * For sending input to be processed by GUIs and rendering and updating
     * GUIs/HUD
     */
    public static ScreenManager getScreenManager() {
	return screen;
    }

    /**
     * For sending and accessing chat messages
     */
    public static ChatManager getChatManager() {
	return chatManager;
    }

    public static LevelManager getLevelManager() {
	return level;
    }

    /**
     * The HUD includes the hotbar, healthbar and chatbar
     */
    public static HUD getHUD() {
	return hud;
    }

    /**
     * For setting the texture. To get coordinates one should really use
     * InputManager appropriately
     */
    public static Mouse getMouse() {
	return mouse;
    }

    /**
     * For drawing textures, setting clips, scaling, getting width, height, etc.
     * 
     * @return
     */
    public static Renderer getRenderEngine() {
	return render;
    }

    public static List<String> getAllPlayerNames() {
	return allPlayerNames;
    }

    /**
     * Default font size is 28
     */
    public static Font getFont(int size) {
	if (fonts.containsKey(size))
	    return fonts.get(size);
	Font newFont = mainFont.deriveFont(Font.PLAIN, size);
	fonts.put(size, newFont);
	return newFont;
    }

    public static GraphicsConfiguration getGraphicsConf() {
	return gConf;
    }

    public static Player getMainPlayer() {
	return player;
    }

    public static Player getPlayer(String name) {
	for (Player player : allPlayers) {
	    if (player.getUsername().equals(name))
		return player;
	}
	return null;
    }

    /**
     * The main game level as set by the current
     */
    public static Level getLevel() {
	return level.getLevel();
    }

    /**
     * The main logger for the game. Used for statistic/data gathering, error
     * saving for debugging, etc.
     */
    public static Logger getLogger() {
	return logger;
    }

    /**
     * Same as <tt> RenderEngine.getWidthPixels</tt>, just for convenience
     */
    public static int getScreenWidth() {
	return render.getWidthPixels();
    }
    
    /**
     * Same as <tt> RenderEngine.getHeightPixels</tt>, just for convenience
     */
    public static int getScreenHeight() {
	return render.getHeightPixels();
    }

    public static int getScreenScale() {
	return scale;
    }

    public static ChatCommandExecutor getCommandExecutor() {
	return chatCmdExecutor;
    }

    public static InputManager getInputManager() {
	return input;
    }

    public static boolean showUpdateTimes() {
	return showUpdateTimes;
    }

    public static boolean showRenderTimes() {
	return showRenderTimes;
    }

    @Override
    public void run() {
	double lastUpdateTime = System.nanoTime();
	double lastRenderTime = System.nanoTime();
	int lastSecondTime = (int) (lastUpdateTime / 1000000000);
	int frameCount = 0;
	int updateCount = 0;
	while (running) {
	    double now = System.nanoTime();
	    int updates = 0;
	    while (now - lastUpdateTime > NS_PER_UPDATE && updates < MAX_UPDATES_BEFORE_RENDER) {
		update();
		lastUpdateTime += NS_PER_UPDATE;
		updates++;
		updateCount++;
		Game.updateCount++;
	    }
	    if (now - lastUpdateTime > NS_PER_UPDATE) {
		lastUpdateTime = now - NS_PER_UPDATE;
	    }
	    render();
	    Game.frameCount++;
	    frameCount++;
	    lastRenderTime = now;
	    int thisSecond = (int) (lastUpdateTime / 1000000000);
	    if (thisSecond > lastSecondTime) {
		frame.setTitle("Powerworks Industries - " + frameCount + " fps, " + updateCount + " ups");
		lastSecondTime = thisSecond;
		secondCount++;
		logger.addData(Statistic.FPS, frameCount);
		logger.addData(Statistic.UPS, updateCount);
		frameCount = 0;
		updateCount = 0;
		Dimension d = getSize();
		int newFrameWidth = (int) d.getWidth();
		int newFrameHeight = (int) d.getHeight();
		if (newFrameWidth != prevFrameWidth || newFrameHeight != prevFrameHeight) {
		    render.setSize(newFrameWidth / scale, newFrameHeight / scale);
		    prevFrameWidth = newFrameWidth;
		    prevFrameHeight = newFrameHeight;
		    if (State.getState() == State.INGAME)
			render.setOffset(player.getXPixel() - getRenderEngine().getWidthPixels() / 2, player.getYPixel() - getRenderEngine().getHeightPixels() / 2);
		}
	    }
	    if (!FPS_MODE)
		while (now - lastRenderTime < NS_PER_FRAME && now - lastUpdateTime < NS_PER_UPDATE) {
		    Thread.yield();
		    try {
			Thread.sleep(1);
		    } catch (Exception e) {
		    }
		    now = System.nanoTime();
		}
	}
	stop();
    }

    public void update() {
	State s = State.getState();
	if (s == State.INGAME) {
	    InputManager.update();
	    Task.update();
	    screen.update();
	    if (!paused)
		level.update();
	    audio.update();
	    Timer.update();
	    if (!paused)
		SyncAnimation.update();
	} else if (s == State.MAIN_MENU) {
	    InputManager.update();
	    Task.update();
	    screen.update();
	    Timer.update();
	    SyncAnimation.update();
	}
	State.update();
	showUpdateTimes = false;
    }

    public void render() {
	State s = State.getState();
	BufferStrategy bufferStrat = getBufferStrategy();
	if (bufferStrat == null) {
	    createBufferStrategy(3);
	    return;
	}
	do {
	    do {
		Graphics2D g2d = (Graphics2D) bufferStrat.getDrawGraphics();
		render.feed(g2d);
		if (s == State.INGAME) {
		    level.render();
		    screen.render();
		} else if (s == State.MAIN_MENU) {
		    screen.render();
		}
		g2d.dispose();
		bufferStrat.show();
	    } while (bufferStrat.contentsRestored());
	    showRenderTimes = false;
	} while (bufferStrat.contentsLost());
    }

    private void loadFont() {
	try {
	    Font font = Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream("/font/MunroSmall.ttf")).deriveFont(Font.PLAIN, 28);
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(font);
	    mainFont = font;
	    fonts.put(28, font);
	} catch (FontFormatException | IOException ex) {
	    ex.printStackTrace();
	}
    }

    @SuppressWarnings("unused")
    private String getDeviceConfigurationString(GraphicsConfiguration gc) {
	return "Bounds: " + gc.getBounds() + "\n" +
		"Buffer Capabilities: " + gc.getBufferCapabilities() + "\n" +
		"   Back Buffer Capabilities: " + gc.getBufferCapabilities().getBackBufferCapabilities() + "\n" +
		"      Accelerated: " + gc.getBufferCapabilities().getBackBufferCapabilities().isAccelerated() + "\n" +
		"      True Volatile: " + gc.getBufferCapabilities().getBackBufferCapabilities().isTrueVolatile() + "\n" +
		"   Flip Contents: " + gc.getBufferCapabilities().getFlipContents() + "\n" +
		"   Front Buffer Capabilities: " + gc.getBufferCapabilities().getFrontBufferCapabilities() + "\n" +
		"      Accelerated: " + gc.getBufferCapabilities().getFrontBufferCapabilities().isAccelerated() + "\n" +
		"      True Volatile: " + gc.getBufferCapabilities().getFrontBufferCapabilities().isTrueVolatile() + "\n" +
		"   Is Full Screen Required: " + gc.getBufferCapabilities().isFullScreenRequired() + "\n" +
		"   Is MultiBuffer Available: " + gc.getBufferCapabilities().isMultiBufferAvailable() + "\n" +
		"   Is Page Flipping: " + gc.getBufferCapabilities().isPageFlipping() + "\n" +
		"Device: " + gc.getDevice() + "\n" +
		"   Available Accelerated Memory: " + gc.getDevice().getAvailableAcceleratedMemory() + "\n" +
		"   ID String: " + gc.getDevice().getIDstring() + "\n" +
		"   Type: " + gc.getDevice().getType() + "\n" +
		"   Display Mode: " + gc.getDevice().getDisplayMode() + "\n" +
		"Image Capabilities: " + gc.getImageCapabilities() + "\n" +
		"      Accelerated: " + gc.getImageCapabilities().isAccelerated() + "\n" +
		"      True Volatile: " + gc.getImageCapabilities().isTrueVolatile() + "\n";
    }

    public static void main(String[] args) {
	try {
	    System.setProperty("sun.java2d.opengl", "true");
	    System.setProperty("sun.java2d.translaccel", "true");
	    System.setProperty("sun.java2d.ddforcevram", "true");
	    System.out.println("Starting game...");
	    game = new Game();
	    frame.setIconImage(ImageIO.read(Game.class.getResource("/textures/misc/logo.png")));
	    frame.setResizable(true);
	    frame.setTitle("Powerworks - Loading");
	    frame.add(game);
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    game.requestFocusInWindow();
	    frame.setVisible(true);
	    mainMenu.open();
	    game.start();
	    Scanner scanner = new Scanner(System.in);
	    while (scanner.hasNext()) {
		chatCmdExecutor.executeCommand(scanner.nextLine(), player);
	    }
	    scanner.close();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    logger.close();
	    audio.close();
	}
    }

    public static boolean showHitboxes() {
	return showHitboxes;
    }

    public static Chunk highlightedChunk = null;
    
    @Override
    public void handleKeyControlPress(KeyPress p) {
	KeyControlOption option = p.getOption();
	ControlPressType pressType = p.getType();
	switch (option) {
	    case EXIT:
		switch (pressType) {
		    case PRESSED:
			if (State.getState() == State.INGAME) {
			    if (escapeMenu.isOpen()) {
				escapeMenu.close();
				if (PAUSE_IN_ESCAPE_MENU)
				    paused = false;
			    } else {
				escapeMenu.open();
				if (PAUSE_IN_ESCAPE_MENU)
				    paused = true;
			    }
			} else
			    exit();
			break;
		    default:
			break;
		}
		break;
	    case SHOW_UPDATE_TIMES:
		switch (pressType) {
		    case PRESSED:
			showUpdateTimes = true;
		    default:
			break;
		}
		break;
	    case SHOW_RENDER_TIMES:
		switch (pressType) {
		    case PRESSED:
			// showRenderTimes = true;
		    default:
			break;
		}
		break;
	    case RENDER_HITBOX:
		switch (pressType) {
		    case PRESSED:
			Setting.SHOW_HITBOXES.setValue(!Setting.SHOW_HITBOXES.getValue());
			chatManager.sendMessage("Hitbox rendering toggled to: " + Setting.SHOW_HITBOXES.getValue());
			break;
		    default:
			break;
		}
		break;
	    case DEBUG_INFO:
		switch (pressType) {
		    case PRESSED:
			//highlightedChunk = level.getLevel().getChunkAtPixel(player.getXPixel(), player.getYPixel());
			debugInfo.toggle();
			break;
		    default:
			break;
		}
		break;
	    default:
		break;
	}
    }
}
