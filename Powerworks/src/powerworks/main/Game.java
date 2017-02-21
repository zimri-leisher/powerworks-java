package powerworks.main;

import java.awt.Canvas;
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
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import powerworks.chat.ChatCommandExecutor;
import powerworks.collidable.Collidable;
import powerworks.data.Quadtree;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.event.ZoomEvent;
import powerworks.graphics.Mouse;
import powerworks.graphics.Screen;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.io.ControlPressType;
import powerworks.io.InputManager;
import powerworks.io.KeyControlHandler;
import powerworks.io.KeyControlOption;
import powerworks.io.KeyPress;
import powerworks.io.Logger;
import powerworks.io.MouseWheelControlHandler;
import powerworks.io.MouseWheelControlOption;
import powerworks.io.MouseWheelPress;
import powerworks.level.Level;
import powerworks.moving.entity.Entity;
import powerworks.moving.entity.Player;
import powerworks.task.Task;

public final class Game extends Canvas implements Runnable, EventListener, KeyControlHandler, MouseWheelControlHandler {

    public static Game game;
    private static final long serialVersionUID = 1L;
    // CONSTANT
    public static final float UPDATES_PER_SECOND = 60.0f;
    // CHANGING
    public static final float FRAMES_PER_SECOND = 60.0f;
    public static final float MS_PER_UPDATE = 1000 / UPDATES_PER_SECOND;
    public static final float MS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
    public static final float NS_PER_UPDATE = 1000000000 / UPDATES_PER_SECOND;
    public static final float NS_PER_FRAME = 1000000000 / FRAMES_PER_SECOND;
    public static final int MAX_UPDATES_BEFORE_RENDER = 5;
    public static IntSummaryStatistics fps = new IntSummaryStatistics(), ups = new IntSummaryStatistics();
    public static int width = 300, zoomedWidth = width;
    public static int height = width / 16 * 9, zoomedHeight = height;
    public static int scale = 4;
    public static double zoomFactor = 1;
    private static Player player;
    public static int scrollHelperX1;
    public static int scrollHelperY1;
    private boolean showHitboxes = false;
    public static Thread gameThread;
    private static JFrame frame;
    public static Font font = null;
    private boolean running = false;
    private static BufferedImage layer1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    public static int[] objects = ((DataBufferInt) layer1.getRaster().getDataBuffer()).getData();
    private static BufferedImage layer2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    public static int[] overlay = ((DataBufferInt) layer2.getRaster().getDataBuffer()).getData();
    public static List<String> allPlayerNames;
    private static List<Player> allPlayers;
    public static InputManager input;
    public static Mouse mouse;
    public static boolean showRenderTimes = false;
    public static boolean showUpdateTimes = false;

    private Game() {
	setPreferredSize(new Dimension(width * scale, height * scale));
	frame = new JFrame();
	input = new InputManager();
	mouse = new Mouse();
	player = new Player(Level.level.getWidthPixels() / 2, Level.level.getHeightPixels() / 2);
	System.out.println(player.getTexture().getWidthPixels());
	scrollHelperX1 = (player.getXPixel() + (player.getTexture().getWidthPixels() / 2)) - Screen.screen.width / 2 + player.getTexture().getWidthPixels() / 2;
	scrollHelperY1 = player.getYPixel() - Screen.screen.height / 2 + player.getTexture().getHeightPixels() / 2;
	allPlayerNames = new ArrayList<String>();
	allPlayerNames.add(player.getName());
	allPlayers = new ArrayList<Player>();
	allPlayers.add(player);
	addKeyListener(input);
	addMouseWheelListener(input);
	addMouseListener(input);
	addMouseMotionListener(input);
	loadFont();
	setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
		new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
		new Point(0, 0), "null"));
	EventManager.registerEventListener(this);
	InputManager.registerKeyControlHandler(this, KeyControlOption.EXIT, KeyControlOption.SHOW_RENDER_TIMES, KeyControlOption.SHOW_UPDATE_TIMES);
	InputManager.registerMouseWheelControlHandler(this, MouseWheelControlOption.ZOOM_IN, MouseWheelControlOption.ZOOM_OUT);
    }

    private synchronized void start() {
	gameThread = new Thread(this, "Display");
	gameThread.start();
	running = true;
    }

    private synchronized void stop() {
	Logger.close();
	Level.level.saveLevel();
	System.exit(0);
	try {
	    gameThread.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
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
		}
		if (now - lastUpdateTime > NS_PER_UPDATE) {
		    lastUpdateTime = now - NS_PER_UPDATE;
		}
		render();
		frameCount++;
		lastRenderTime = now;
		int thisSecond = (int) (lastUpdateTime / 1000000000);
		if (thisSecond > lastSecondTime) {
		    frame.setTitle("Powerworks - " + frameCount + " fps, " + updateCount + " ups");
		    lastSecondTime = thisSecond;
		    ups.accept(updateCount);
		    fps.accept(frameCount);
		    frameCount = 0;
		    updateCount = 0;
		}
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
	long time = System.nanoTime();
	InputManager.update();
	if (showUpdateTimes)
	    System.out.println("----------");
	Task.update();
	Level.level.update();
	Quadtree.update();
	mouse.update();
	SynchronizedAnimatedTexture.update();
	if (showUpdateTimes) {
	    System.out.println("TOTAL:                       " + (System.nanoTime() - time) + " ns");
	}
	showUpdateTimes = false;
    }

    public void render() {
	BufferStrategy bufferStrat = getBufferStrategy();
	if (bufferStrat == null) {
	    createBufferStrategy(3);
	    return;
	}
	for (int i = 0; i < overlay.length; i++)
	    overlay[i] = 0;
	Graphics2D g2d = (Graphics2D) bufferStrat.getDrawGraphics();
	if (StateManager.state == StateManager.INGAME) {
	    long time = 0;
	    if (showRenderTimes) {
		System.out.println("----------");
		time = System.nanoTime();
	    }
	    Level.level.render(scrollHelperX1, scrollHelperY1, player);
	    if (showRenderTimes) {
		System.out.println("Rendering level took: " + (System.nanoTime() - time) + " ns");
	    }
	    g2d.drawImage(layer1, 0, 0, getWidth(), getHeight(), null);
	    player.hud.render(g2d);
	    mouse.render();
	    g2d.drawImage(layer2, 0, 0, getWidth(), getHeight(), null);
	    g2d.dispose();
	    bufferStrat.show();
	    if (showRenderTimes)
		System.out.println("TOTAL:                       " + (System.nanoTime() - time) + " ns");
	    showRenderTimes = false;
	} else if (StateManager.state == StateManager.MAIN_MENU) {
	}
    }

    private void loadFont() {
	try {
	    Font font = Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream("/font/MunroSmall.ttf")).deriveFont(Font.PLAIN, 28);
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(font);
	    Game.font = font;
	} catch (FontFormatException | IOException ex) {
	    ex.printStackTrace();
	}
    }

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
	System.out.println("Starting game...");
	game = new Game();
	try {
	    frame.setIconImage(ImageIO.read(Game.class.getResource("/textures/misc/logo.png")));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	frame.setResizable(false);
	frame.setTitle("Powerworks - Loading");
	frame.add(game);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocationRelativeTo(null);
	game.requestFocusInWindow();
	frame.setVisible(true);
	System.out.println("Welcome back, " + player.getName());
	game.start();
	Scanner scanner = new Scanner(System.in);
	while (scanner.hasNext()) {
	    String next = scanner.nextLine();
	    try {
		String[] commandArgs = next.substring(next.indexOf(" ") + 1).split(" ");
		for (int i = 0; i < commandArgs.length; i++) {
		    commandArgs[i].trim();
		}
		ChatCommandExecutor.executeCommand(next.substring(0, next.indexOf(" ")).trim(), commandArgs, player);
	    } catch (StringIndexOutOfBoundsException e) {
		System.out.println("Invalid command");
	    }
	}
	scanner.close();
    }

    public boolean showHitboxes() {
	return showHitboxes;
    }

    @EventHandler
    public void handleZoomEvent(ZoomEvent e) {
	zoomedWidth = (int) (width * e.zoomFactor);
	zoomedHeight = (int) (height * e.zoomFactor);
	layer1 = new BufferedImage(zoomedWidth, zoomedHeight, BufferedImage.TYPE_INT_ARGB);
	objects = ((DataBufferInt) layer1.getRaster().getDataBuffer()).getData();
	Screen.screen.set(zoomedWidth, zoomedHeight, objects);
	scrollHelperX1 = (player.getXPixel() + (player.getTexture().getWidthPixels() / 2)) - Screen.screen.width / 2 + player.getTexture().getWidthPixels() / 2;
	scrollHelperY1 = player.getYPixel() - Screen.screen.height / 2 + player.getTexture().getHeightPixels() / 2;
    }

    @EventHandler
    public void handleViewMoveEvent(ViewMoveEvent e) {
	scrollHelperX1 = (player.getXPixel() + (player.getTexture().getWidthPixels() / 2)) - Screen.screen.width / 2 + player.getTexture().getWidthPixels() / 2;
	scrollHelperY1 = player.getYPixel() - Screen.screen.height / 2 + player.getTexture().getHeightPixels() / 2;
    }

    @Override
    public void handleMouseWheelPress(MouseWheelPress p) {
	MouseWheelControlOption option = p.getOption();
	switch (option) {
	    case ZOOM_IN:
		if (zoomFactor > 0.89) {
		    zoomFactor /= 1.1;
		    zoomedWidth = (int) (width * zoomFactor);
		    zoomedHeight = (int) (height * zoomFactor);
		    layer1 = new BufferedImage(zoomedWidth, zoomedHeight, BufferedImage.TYPE_INT_ARGB);
		    objects = ((DataBufferInt) layer1.getRaster().getDataBuffer()).getData();
		    Screen.screen.set(zoomedWidth, zoomedHeight, objects);
		    scrollHelperX1 = (player.getXPixel() + (player.getTexture().getWidthPixels() / 2)) - Screen.screen.width / 2 + player.getTexture().getWidthPixels() / 2;
		    scrollHelperY1 = player.getYPixel() - Screen.screen.height / 2 + player.getTexture().getHeightPixels() / 2;
		}
		break;
	    case ZOOM_OUT:
		if (zoomFactor < 5) {
		    zoomFactor *= 1.1;
		    zoomedWidth = (int) (width * zoomFactor);
		    zoomedHeight = (int) (height * zoomFactor);
		    layer1 = new BufferedImage(zoomedWidth, zoomedHeight, BufferedImage.TYPE_INT_ARGB);
		    objects = ((DataBufferInt) layer1.getRaster().getDataBuffer()).getData();
		    Screen.screen.set(zoomedWidth, zoomedHeight, objects);
		    scrollHelperX1 = (player.getXPixel() + (player.getTexture().getWidthPixels() / 2)) - Screen.screen.width / 2 + player.getTexture().getWidthPixels() / 2;
		    scrollHelperY1 = player.getYPixel() - Screen.screen.height / 2 + player.getTexture().getHeightPixels() / 2;
		}
		break;
	    default:
		break;
	}
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
			System.out.println("Cols");
			Collidable.collidables.print();
			System.out.println("Entities");
			Entity.entities.print();
			//showUpdateTimes = true;
		    default:
			break;
		}
		break;
	    case SHOW_RENDER_TIMES:
		switch (pressType) {
		    case PRESSED:
			showRenderTimes = true;
		    default:
			break;
		}
		break;
	    default:
		break;
	}
    }
}
