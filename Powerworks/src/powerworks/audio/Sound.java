package powerworks.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import kuusisto.tinysound.TinySound;

public enum Sound {
    TEST("/sounds/misc/test.wav"), 
    GRASS_FOOTSTEP("/sounds/footstep/grass.wav"),
    CONVEYOR_BELT("/sounds/block/machine/conveyor_belt.wav");
    
    kuusisto.tinysound.Sound tsSound;
    /**
     * In 60ths of second
     */
    double length;
    /**
     * In 60ths of second
     */
    double currentTime;
    boolean playing;
    boolean loop = false;
    int loopIterations = -1;
    int loopIteration = -1;

    private Sound(String path) {
	//tsSound = TinySound.loadSound(path);
	AudioInputStream audioInputStream;
	try {
	    File f = new File(Sound.class.getResource(path).getFile());
	    audioInputStream = AudioSystem.getAudioInputStream(f);
	    AudioFormat format = audioInputStream.getFormat();
	    long audioFileLength = f.length();
	    int frameSize = format.getFrameSize();
	    float frameRate = format.getFrameRate();
	    float durationInSeconds = (audioFileLength / (frameSize * frameRate));
	} catch (UnsupportedAudioFileException | IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Note - does not play the sound.
     */
    void setLoop(boolean loop) {
	this.loop = loop;
	loopIterations = -1;
	loopIteration = -1;
    }

    void setLoop(boolean loop, int iterations) {
	this.loop = loop;
	this.loopIterations = (loop) ? iterations : -1;
	this.loopIteration = 0;
    }

    void play() {
	playing = true;
	tsSound.play();
    }

    void stop() {
	playing = false;
	tsSound.stop();
    }

    void play(double vol) {
	tsSound.play(vol);
    }

    void play(double vol, double pan) {
	tsSound.play(vol, pan);
    }

    public static void update() {
	for (Sound s : values()) {
	    if (s.playing) {
		s.currentTime++;
		if (s.currentTime == s.length) {
		    if (s.loop) {
			if (s.loopIterations != -1 && s.loopIteration != s.loopIterations) {
			    s.loopIteration++;
			    s.tsSound.stop();
			    s.tsSound.play();
			} else {
			    s.tsSound.stop();
			    s.tsSound.play();
			}
		    } else {
			s.playing = false;
			s.stop();
		    }
		}
	    }
	}
    }
}