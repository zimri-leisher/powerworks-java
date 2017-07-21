package powerworks.audio;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.adonax.audiocue.AudioCue;

public enum Sound {
    GRASS_FOOTSTEP("/sounds/footstep/grass.wav", 6), CONVEYOR_BELT("/sounds/block/machine/conveyor_belt.wav", 4);

    AudioCue a;

    private Sound(String path, int maxConcurrent) {
	try {
	    a = AudioCue.makeStereoCue(Sound.class.getResource(path), maxConcurrent);
	} catch (UnsupportedAudioFileException | IOException e) {
	    e.printStackTrace();
	}
    }

    static void load() {
	for (Sound s : values())
	    try {
		s.a.open();
	    } catch (IllegalStateException | LineUnavailableException e) {
		e.printStackTrace();
	    }
    }

    static void close() {
	for (Sound s : values()) {
	    s.a.close();
	}
    }

    int play() {
	int s = a.play();
	if (s != -1)
	    a.setRecycleWhenDone(s, true);
	return a.play();
    }

    int play(double vol) {
	int s = a.play(vol);
	if (s != -1)
	    a.setRecycleWhenDone(s, true);
	return s;
    }

    int play(double vol, double pan, double speed, int loops) {
	int s = a.play(vol, pan, speed, loops);
	if (s != -1)
	    a.setRecycleWhenDone(s, true);
	return s;
    }

    int play(double vol, int loop) {
	int s = a.play(vol, 0, 1, loop);
	if (s != -1)
	    a.setRecycleWhenDone(s, true);
	return s;
    }

    void setVolume(double vol, int instance) {
	a.setVolume(instance, vol);
    }

    void setPan(double pan, int instance) {
	a.setPan(instance, pan);
    }

    void setLoop(int loops, int instance) {
	a.setLooping(instance, loops);
    }

    void close(int instance) {
	a.stop(instance);
	a.releaseInstance(instance);
    }

    void stop(int instance) {
	a.stop(instance);
    }

    void start(int instance) {
	a.start(instance);
    }
}