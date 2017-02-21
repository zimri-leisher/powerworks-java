package powerworks.graphics;

import powerworks.main.Game;

public enum SynchronizedAnimatedTexture implements Texture {
    CONVEYOR_BELT_CONNECTED_UP(StaticTextureCollection.CONVEYOR_BELT_CONNECTED_UP, new int[] { 2, 2, 2, 2 }), 
    CURSOR_RIGHT_CLICK(StaticTextureCollection.CURSOR_RIGHT_CLICK, new int[] { 12, 12, 12, 12, 12, 12, 12, 12 }, true);

    StaticTextureCollection textures;
    int[] frameTimes;
    int currentFrame = 0;
    int currentTick = 0;
    boolean playing = true;
    boolean stopOnFinish;

    private SynchronizedAnimatedTexture(StaticTextureCollection col, int[] frameTimes) {
	this(col, frameTimes, false);
    }

    private SynchronizedAnimatedTexture(StaticTextureCollection col, int[] frameTimes, boolean defaultStopOnFinish) {
	this.textures = col;
	this.frameTimes = frameTimes;
	this.stopOnFinish = defaultStopOnFinish;
    }

    public static void update() {
	long time = 0;
	if(Game.showUpdateTimes)
	    time = System.nanoTime();
	for (SynchronizedAnimatedTexture anim : values()) {
	    if (anim.playing) {
		anim.currentTick++;
		if (anim.frameTimes[anim.currentFrame] == anim.currentTick) {
		    if (anim.currentFrame + 1 == anim.frameTimes.length)
			if (!anim.stopOnFinish)
			    anim.currentFrame = 0;
			else {
			    anim.setStopOnFinish(false);
			    anim.stop();
			}
		    else {
			anim.currentFrame++;
			anim.currentTick = 0;
		    }
		}
	    }
	}
	if(Game.showUpdateTimes)
	    System.out.println("Updating animations took:    " + (System.nanoTime() - time) + " ns");
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
     * Sets the frame and the tick to 0
     */
    public void reset() {
	currentFrame = 0;
	currentTick = 0;
    }

    public void setStopOnFinish(boolean stopOnFinish) {
	this.stopOnFinish = stopOnFinish;
    }

    @Override
    public int[] getPixels() {
	return textures.getTextures()[currentFrame].getPixels();
    }

    @Override
    public int getWidthPixels() {
	return textures.getTextures()[currentFrame].getWidthPixels();
    }
    
    @Override
    public int getHeightPixels() {
	return textures.getTextures()[currentFrame].getHeightPixels();
    }

    @Override
    public boolean hasTransparency() {
	return textures.getTextures()[currentFrame].hasTransparency();
    }
}
