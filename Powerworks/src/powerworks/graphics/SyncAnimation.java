package powerworks.graphics;

import java.awt.image.BufferedImage;
import powerworks.main.Game;

public enum SyncAnimation implements Texture {
    CONVEYOR_BELT_CONNECTED_UP(ImageCollection.CONVEYOR_BELT_CONNECTED_UP, new int[] { 2, 2, 2, 2 }, true, 1.0, true), 
    CURSOR_RIGHT_CLICK(ImageCollection.CURSOR_RIGHT_CLICK,
	    new int[] { 12, 12, 12, 12, 12, 12, 12, 12 });

    ImageCollection images;
    int[] frameTimes;
    int currentFrame = 0;
    int currentTick = 0;
    boolean playing = false;
    boolean loop = false;
    double speed = 1.0;

    private SyncAnimation(ImageCollection images, int[] frameTimes) {
	this.images = images;
	this.frameTimes = frameTimes;
    }

    private SyncAnimation(ImageCollection images, int[] frameTimes, boolean loop, double speed) {
	this.images = images;
	this.frameTimes = frameTimes;
	this.loop = loop;
	this.speed = speed;
    }
    
    private SyncAnimation(ImageCollection images, int[] frameTimes, boolean loop, double speed, boolean startOnLoad) {
	this(images, frameTimes, loop, speed);
	this.playing = startOnLoad;
    }

    public void setSpeed(double speed) {
	this.speed = speed;
    }

    public double getSpeed() {
	return speed;
    }

    /**
     * Starts playing from wherever it left off
     */
    public void play() {
	playing = true;
    }

    /**
     * Stops playing, keeps all timings
     */
    public void stop() {
	playing = false;
    }

    /**
     * Sets the frame, tick and speed to starting values
     */
    public void reset() {
	currentFrame = 0;
	currentTick = 0;
	speed = 1.0;
    }

    public void loop(boolean loop) {
	this.loop = loop;
    }

    public static void update() {
	long time = 0;
	if (Game.showUpdateTimes())
	    time = System.nanoTime();
	for (SyncAnimation anim : values()) {
	    if (anim.playing) {
		anim.currentTick++;
		if (anim.frameTimes[anim.currentFrame] / anim.speed <= anim.currentTick) {
		    if (anim.currentFrame + 1 == anim.frameTimes.length)
			if (anim.loop) {
			    anim.currentFrame = 0;
			    anim.currentTick = 0;
			} else {
			    anim.loop(false);
			    anim.stop();
			}
		    else {
			anim.currentFrame++;
			anim.currentTick = 0;
		    }
		}
	    }
	}
	if (Game.showUpdateTimes())
	    System.out.println("Updating animations took:    " + (System.nanoTime() - time) + " ns");
    }

    @Override
    public BufferedImage getImage() {
	return images.getTexture(currentFrame).getImage();
    }

    @Override
    public int getWidthPixels() {
	return getImage().getWidth();
    }

    @Override
    public int getHeightPixels() {
	return getImage().getHeight();
    }
}
