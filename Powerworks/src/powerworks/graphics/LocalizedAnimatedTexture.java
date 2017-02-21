package powerworks.graphics;

public class LocalizedAnimatedTexture implements Texture{

    SynchronizedAnimatedTexture anim;
    int currentFrame = 0;
    int currentTick = 0;
    boolean playing = false;
    boolean cancelOnFinish = false;

    public LocalizedAnimatedTexture(SynchronizedAnimatedTexture anim, boolean playing) {
	this.anim = anim;
	this.playing = playing;
    }

    public LocalizedAnimatedTexture(SynchronizedAnimatedTexture anim) {
	this.anim = anim;
    }

    public void update() {
	if (playing) {
	    currentTick++;
	    if (currentTick == anim.frameTimes[currentFrame]) {
		if (currentFrame == anim.frameTimes.length - 1) {
		    reset();
		    if(cancelOnFinish)  {
			cancelOnFinish = false;
			playing = false;
		    }
		} else {
		    currentTick = 0;
		    currentFrame++;
		}
	    }
	}
    }
    /**
     * Sets the animation back to the start
     */
    public void reset() {
	currentFrame = 0;
	currentTick = 0;
    }

    /**
     * Goes through the entire animation once from the start
     */
    public void playOnce() {
	cancelOnFinish = true;
	reset();
	playing = true;
    }
    
    /**
     * Plays the animation on a continuous loop until stop() is executed
     */
    public void loop() {
	reset();
	playing = true;
    }
    
    /**
     * Stops the animation wherever it is
     */
    public void stop() {
	playing = false;
    }
    
    /**
     * Starts the animation wherever it is
     */
    public void start() {
	playing = true;
    }

    @Override
    public int[] getPixels() {
	return anim.textures.getTextures()[currentFrame].getPixels();
    }

    @Override
    public boolean hasTransparency() {
	return anim.textures.getTextures()[currentFrame].hasTransparency();
    }

    @Override
    public int getWidthPixels() {
	return anim.textures.getTextures()[currentFrame].getWidthPixels();
    }

    @Override
    public int getHeightPixels() {
	return anim.textures.getTextures()[currentFrame].getHeightPixels();
    }
    
    
}
