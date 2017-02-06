package powerworks.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import powerworks.chat.ChatCommandExecutor;
import powerworks.data.Quadtree;
import powerworks.event.EventHandler;
import powerworks.event.EventListener;
import powerworks.event.EventManager;
import powerworks.event.ViewMoveEvent;
import powerworks.event.ZoomEvent;
import powerworks.graphics.Screen;
import powerworks.graphics.SynchronizedAnimatedTexture;
import powerworks.input.KeyControlHandler;
import powerworks.input.KeyControlOption;
import powerworks.input.KeyControlPress;
import powerworks.input.ControlPressType;
import powerworks.input.InputManager;
import powerworks.level.Level;
import powerworks.moving.entity.Player;
import powerworks.task.Task;

public final class Game extends Canvas implements Runnable, EventListener, KeyControlHandler {

    public static Game game;
    private static final long serialVersionUID = 1L;
    public static final double UPDATES_PER_SECOND = 60.0;
    public static final double FRAMES_PER_SECOND = 60.0;
    public static int width = 600, zoomedWidth = width;
    public static int height = width / 16 * 9, zoomedHeight = height;
    public static int scale = 4;
    public static double zoomFactor = 1.0;
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
    public static boolean showRenderTimes = false;
    public static boolean showUpdateTimes = false;

    private Game() {
	setPreferredSize(new Dimension(width * scale, height * scale));
	frame = new JFrame();
	input = new InputManager();
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
	InputManager.registerKeyControlHandler(this, KeyControlOption.EXIT);
    }

    private synchronized void start() {
	gameThread = new Thread(this, "Display");
	gameThread.start();
	running = true;
    }

    private synchronized void stop() {
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
	long lastTime = System.nanoTime();
	long timer = System.currentTimeMillis();
	double updateProgress = 0;
	double frameProgress = 0;
	final double nsU = 1000000000.0 / UPDATES_PER_SECOND;
	final double nsF = 1000000000.0 / FRAMES_PER_SECOND;
	int updates = 0;
	int frames = 0;
	while (running) {
	    long now = System.nanoTime();
	    long diff = now - lastTime;
	    lastTime = now;
	    updateProgress += diff / nsU;
	    while (updateProgress >= 1) {
		update();
		updates++;
		updateProgress--;
	    }
	    frameProgress += diff / nsF;
	    if (frameProgress >= 1) {
		render();
		frames++;
		frameProgress = 0;
	    }
	    if (System.currentTimeMillis() - timer >= 1000) {
		timer += 1000;
		frame.setTitle("Powerworks - " + frames + " fps, " + updates + " ups");
		updates = 0;
		frames = 0;
	    }
	    synchronized (gameThread) {
		try {
		    gameThread.wait(0, 5);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
	stop();
    }

    public void update() {
	long time = System.nanoTime();
	if (showUpdateTimes)
	    System.out.println("----------");
	Task.update();
	InputManager.update();
	player.update();
	Level.level.update();
	Quadtree.update();
	SynchronizedAnimatedTexture.update();
	if (showUpdateTimes) {
	    System.out.println("Updating everything took: " + (System.nanoTime() - time) + " ns");
	}
	showUpdateTimes = false;
    }

    public void render() {
	BufferStrategy bufferStrat = getBufferStrategy();
	if (bufferStrat == null) {
	    createBufferStrategy(3);
	    return;
	}
	if (objects.length > overlay.length) {
	    for (int i = 0; i < objects.length; i++) {
		objects[i] = 0xFF000000;
		if (i < overlay.length)
		    overlay[i] = 0x00000000;
	    }
	} else {
	    for (int i = 0; i < overlay.length; i++) {
		overlay[i] = 0x00000000;
		if (i < objects.length)
		    objects[i] = 0xFF000000;
	    }
	}
	Graphics2D g2d = (Graphics2D) bufferStrat.getDrawGraphics();
	if (StateManager.state == StateManager.INGAME) {
	    long time = 0;
	    if (showRenderTimes) {
		System.out.println("----------");
		time = System.nanoTime();
	    }
	    Level.level.render(scrollHelperX1, scrollHelperY1, player);
	    g2d.drawImage(layer1, 0, 0, getWidth(), getHeight(), null);
	    if (showRenderTimes) {
		System.out.println("Rendering level took: " + (System.nanoTime() - time) + " ns");
	    }
	    g2d.drawImage(layer2, 0, 0, getWidth(), getHeight(), null);
	    g2d.dispose();
	    bufferStrat.show();
	    showRenderTimes = false;
	} else if (StateManager.state == StateManager.MAIN_MENU) {
	}
    }

    private void loadFont() {
	try {
	    Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(Game.class.getResource("/font/MunroSmall.ttf").getFile())).deriveFont(Font.PLAIN, 28);
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(font);
	    Game.font = font;
	} catch (FontFormatException | IOException ex) {
	    ex.printStackTrace();
	}
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
    public void handleKeyControlPress(KeyControlPress p) {
	KeyControlOption control = p.getControl();
	ControlPressType pressType = p.getPressType();
	switch(control) {
	    case EXIT:
		switch(pressType) {
		    case PRESSED:
			running = false;
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
