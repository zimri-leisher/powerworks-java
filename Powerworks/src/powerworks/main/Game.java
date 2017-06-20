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
import kuusisto.tinysound.TinySound;
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
import powerworks.graphics.screen.ScreenObject;
import powerworks.graphics.screen.gui.GUI;
import powerworks.graphics.screen.gui.MainMenuGUI;
import powerworks.graphics.screen.gui.OptionsMenuGUI;
import powerworks.io.ControlMap;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.Logger;
import powerworks.io.MouseWheelControlHandler;
import powerworks.io.MouseWheelControlOption;
import powerworks.io.MouseWheelPress;
import powerworks.io.Statistic;
import powerworks.task.Task;
import powerworks.world.World;
import powerworks.world.level.Level;

public final class Game extends Canvas implements Runnable, EventListener, KeyControlHandler, MouseWheelControlHandler {

    private static Game game;
    private static final long serialVersionUID = 1L;
    public static final float UPDATES_PER_SECOND = 60.0f;
    public static final float FRAMES_PER_SECOND = 1000000.0f;
    public static final float MS_PER_UPDATE = 1000 / UPDATES_PER_SECOND;
    public static final float MS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
    public static final float NS_PER_UPDATE = 1000000000 / UPDATES_PER_SECOND;
    public static final float NS_PER_FRAME = 1000000000 / FRAMES_PER_SECOND;
    public static boolean FPS_MODE = false;
    public static final int MAX_UPDATES_BEFORE_RENDER = 5;
    static int width = 300, zoomedWidth = width;
    static int height = width / 16 * 9, zoomedHeight = height;
    static int scale = 4;
    static int secondCount = 0;
    static long updateCount = 0;
    static long frameCount = 0;
    int prevFrameWidth = 0;
    int prevFrameHeight = 0;
    static Player player;
    static boolean showHitboxes = false;
    static Thread gameThread;
    static JFrame frame;
    static Renderer render;
    static Font mainFont = null;
    static HashMap<Integer, Font> fonts = new HashMap<Integer, Font>();
    boolean running = false;
    static GraphicsConfiguration gConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    static List<String> allPlayerNames;
    static List<Player> allPlayers;
    static World world;
    static ScreenManager screen;
    static HUD hud;
    static MainMenuGUI mainMenu;
    static OptionsMenuGUI optionsMenu;
    static Mouse mouse;
    static InputManager input;
    static ChatCommandExecutor chatCmdExecutor;
    static ChatManager chatManager;
    static Logger logger;
    static boolean showRenderTimes = false;
    static boolean showUpdateTimes = false;
    static Cursor defCursor;

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
		KeyControlOption.TOGGLE_FPS_MODE);
	InputManager.registerKeyControlHandler(this, ControlMap.MAIN_MENU, KeyControlOption.EXIT, KeyControlOption.TOGGLE_FPS_MODE);
	InputManager.registerMouseWheelControlHandler(this, ControlMap.DEFAULT_INGAME, MouseWheelControlOption.ZOOM_IN, MouseWheelControlOption.ZOOM_OUT);
    }

    private synchronized void start() {
	gameThread = new Thread(this, "Display");
	gameThread.start();
	running = true;
    }

    private synchronized void stop() {
	logger.close();
	TinySound.shutdown();
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

    public static MainMenuGUI getMainMenuGUI() {
	return mainMenu;
    }

    public static OptionsMenuGUI getOptionsMenuGUI() {
	return optionsMenu;
    }

    public static void exit() {
	game.running = false;
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

    public static ScreenManager getScreenManager() {
	return screen;
    }

    public static ChatManager getChatManager() {
	return chatManager;
    }

    public static HUD getHUD() {
	return hud;
    }

    public static Mouse getMouse() {
	return mouse;
    }

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
	    if (player.getName().equals(name))
		return player;
	}
	return null;
    }

    /**
     * The main game level. More may well be implemented, but for now this is
     * where the magic happens
     */
    public static Level getLevel() {
	return world.getLevel();
    }

    /**
     * The main logger for the game. Used for statistic/data gathering, error
     * saving for debugging, etc.
     */
    public static Logger getLogger() {
	return logger;
    }

    public static int getScreenWidth() {
	return render.getWidthPixels();
    }

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
	    world.update();
	    Timer.update();
	    SyncAnimation.update();
	} else if (s == State.MAIN_MENU) {
	    InputManager.update();
	    Task.update();
	    screen.update();
	    Timer.update();
	    SyncAnimation.update();
	}
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
		    world.render();
		    screen.render();
		    mouse.render();
		} else if (s == State.MAIN_MENU) {
		    screen.render();
		    mouse.render();
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
	    System.setProperty("sun.java2d.translaccel", "true");
	    System.setProperty("sun.java2d.ddforcevram", "true");
	    System.out.println("Starting game...");
	    // TinySound.init();
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
	}
    }

    public static boolean showHitboxes() {
	return showHitboxes;
    }

    @Override
    public void handleKeyControlPress(KeyPress p) {
	KeyControlOption option = p.getOption();
	ControlPressType pressType = p.getType();
	switch (option) {
	    case EXIT:
		switch (pressType) {
		    case PRESSED:
			running = false;
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
			// render.setZoom(render.getZoom() + 0.1);
			// System.out.println(render.getCurrentViewArea());
			chatManager.sendMessage("Hitbox rendering toggled to: " + !showHitboxes);
			showHitboxes = !showHitboxes;
			break;
		    default:
			break;
		}
		break;
	    case TOGGLE_FPS_MODE:
		switch (pressType) {
		    case PRESSED:
			logger.log("test");
			// FPS_MODE = !FPS_MODE;
			break;
		    default:
			break;
		}
		break;
	    default:
		break;
	}
    }

    @Override
    public void handleMouseWheelPress(MouseWheelPress press) {
    }
}
