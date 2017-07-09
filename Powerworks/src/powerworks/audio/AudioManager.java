package powerworks.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import com.adonax.audiocue.AudioCue;
import com.adonax.audiocue.AudioCueInstanceEvent;
import com.adonax.audiocue.AudioCueListener;
import powerworks.main.Game;
import powerworks.main.Setting;

public class AudioManager {

    private static double VOLUME_MULTIPLIER = 1;
    private static int MAX_HEARING_DISTANCE_PIXELS = 100;
    private static boolean SOUND_ENABLED = true;
    private static boolean LEVEL_SOUNDS_PAUSED = false;
    AudioHearer ears;
    CopyOnWriteArrayList<SoundSource> levelSounds = new CopyOnWriteArrayList<SoundSource>();
    CopyOnWriteArrayList<SoundSource> forceUpdate = new CopyOnWriteArrayList<SoundSource>();
    Map<Sound, Integer> otherSounds = new HashMap<Sound, Integer>();
    int lastXPixel, lastYPixel;

    public void load() {
	Sound.load();
    }

    public void close() {
	Sound.close();
    }

    public void closeSoundSources() {
	levelSounds.forEach(SoundSource::close);
    }

    public class SoundSource {

	int xPixel, yPixel, instance;
	Sound s;
	boolean loop, playing = true;

	SoundSource(int xPixel, int yPixel, int instance, Sound s, boolean loop) {
	    this.xPixel = xPixel;
	    this.yPixel = yPixel;
	    this.instance = instance;
	    this.loop = loop;
	    this.s = s;
	}

	public void setXPixel(int xPixel) {
	    this.xPixel = xPixel;
	    forceUpdate();
	}

	public void setYPixel(int yPixel) {
	    this.yPixel = yPixel;
	    forceUpdate();
	}

	public void stop() {
	    s.stop(instance);
	    playing = false;
	}

	public void start() {
	    s.start(instance);
	    playing = true;
	}

	public void close() {
	    s.close(instance);
	    levelSounds.remove(this);
	    forceUpdate.remove(this);
	    s = null;
	}

	private void forceUpdate() {
	    forceUpdate.add(this);
	}
	
	public boolean isLegitimate() {
	    return instance != -1;
	}
    }
    
    public void enableSound(boolean e) {
	SOUND_ENABLED = e;
    }
    
    public boolean isSoundEnabled() {
	return SOUND_ENABLED;
    }

    public void setAudioHearer(AudioHearer e) {
	ears = e;
    }

    /**
     * Plays a sound with full volume, no pan and no looping
     */
    public void play(Sound s) {
	if (!SOUND_ENABLED)
	    return;
	int i = s.play();
	if (i != -1)
	    otherSounds.put(s, i);
    }

    public SoundSource play(Sound s, int xPixel, int yPixel, boolean loop) {
	if (!SOUND_ENABLED)
	    return null;
	if (ears == null)
	    return null;
	SoundSource src = new SoundSource(xPixel, yPixel, s.play(
		getVolume(xPixel, yPixel),
		getPan(xPixel, yPixel), 1, (loop) ? -1 : 0), s, loop);
	if (src.instance != -1)
	    levelSounds.add(src);
	return (src.instance != -1) ? src : null;
    }

    private double getVolume(int xPixel, int yPixel) {
	double d = (Math.max(MAX_HEARING_DISTANCE_PIXELS - (Math.sqrt(Math.pow(xPixel - ears.getXPixel(), 2) + Math.pow(yPixel - ears.getYPixel(), 2))), 0) / MAX_HEARING_DISTANCE_PIXELS)
		* VOLUME_MULTIPLIER;
	return d;
    }

    private double getPan(int xPixel, int yPixel) {
	return Math.max(Math.min((float) (xPixel - ears.getXPixel()) / MAX_HEARING_DISTANCE_PIXELS, 1), -1);
    }

    public void update() {
	if (!SOUND_ENABLED)
	    return;
	if (ears == null)
	    return;
	if (Setting.PAUSE_IN_ESCAPE_MENU.getValue() && Game.isPaused() && !LEVEL_SOUNDS_PAUSED) {
	    LEVEL_SOUNDS_PAUSED = true;
	    for (SoundSource s : levelSounds)
		s.stop();
	    return;
	} else if(Setting.PAUSE_IN_ESCAPE_MENU.getValue() && !Game.isPaused() && LEVEL_SOUNDS_PAUSED) {
	    LEVEL_SOUNDS_PAUSED = false;
	    for(SoundSource s : levelSounds)
		s.start();
	}
	int xPixel = ears.getXPixel();
	int yPixel = ears.getYPixel();
	if (lastXPixel != xPixel || lastYPixel != yPixel) {
	    lastXPixel = xPixel;
	    lastYPixel = yPixel;
	    for (SoundSource s : levelSounds) {
		double vol = getVolume(s.xPixel, s.yPixel);
		if (vol != 0) {
		    if (!s.playing)
			s.start();
		    s.s.setVolume(getVolume(s.xPixel, s.yPixel), s.instance);
		    s.s.setPan(getPan(s.xPixel, s.yPixel), s.instance);
		} else {
		    if (s.playing)
			s.stop();
		}
	    }
	}
	for (SoundSource s : forceUpdate) {
	    s.s.setVolume(getVolume(s.xPixel, s.yPixel), s.instance);
	    s.s.setPan(getPan(s.xPixel, s.yPixel), s.instance);
	    forceUpdate.remove(s);
	}
    }
}