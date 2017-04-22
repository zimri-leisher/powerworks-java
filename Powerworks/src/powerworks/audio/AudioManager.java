package powerworks.audio;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import powerworks.main.Game;

public class AudioManager {

    static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 4, 44100, false);
    static DataLine.Info outStreamInfo = new DataLine.Info(SourceDataLine.class, FORMAT);
    static final int HEARING_THRESHOLD = 150;
    static double soundVol = 1.0;
    static double musicVol = 1.0;
    static List<ActiveSound> sounds = new ArrayList<ActiveSound>();
    static int nextID = 0;
    static Thread audioOutputThread;
    static SourceDataLine outStream;
    static AudioOutputThread audioOutput;

    static class ActiveSound {

	boolean loop;
	int loopIterations = -1;
	int loopIteration = -1;
	int xPixel, yPixel;
	double vol;
	long currentPos = 0;
	long delayBetweenLoops = 0;
	Sound sound;
	int id = -1;

	private ActiveSound(Sound sound, int xPixel, int yPixel, double vol) {
	    this(sound, xPixel, yPixel, vol, false, -1, -1);
	}

	private ActiveSound(Sound s, int xPixel, int yPixel, double vol, boolean loop, int loopIterations, long delayBetweenLoops) {
	    this.sound = s;
	    this.xPixel = xPixel;
	    this.yPixel = yPixel;
	    this.vol = vol;
	    this.loop = loop;
	    this.delayBetweenLoops = delayBetweenLoops;
	    this.loopIterations = loopIterations;
	    if (loop) {
		id = nextID++;
		loopIteration = 0;
	    }
	}

	@Override
	public String toString() {
	    return sound + ", loop=" + loop + ", vol=" + vol + ", at " + xPixel + ", " + yPixel;
	}
    }

    public static Clip getClip(AudioInputStream input) {
	AudioFormat format = input.getFormat();
	DataLine.Info newInfo = new DataLine.Info(Clip.class, FORMAT);
	Clip outMem = null;
	try {
	    outMem = (Clip) AudioSystem.getLine(newInfo);
	    outMem.open(input);
	} catch (LineUnavailableException | IOException e) {
	    e.printStackTrace();
	}
	if (!AudioSystem.isLineSupported(newInfo)) {
	    Game.getLogger().error("Output audio line not supported");
	}
	return outMem;
    }

    public static AudioInputStream getAudioInputStream(URL url) {
	AudioInputStream input = null;
	try {
	    input = AudioSystem.getAudioInputStream(url);
	} catch (UnsupportedAudioFileException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	AudioFormat format = input.getFormat();
	AudioFormat mono16 = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, false);
	if (format.matches(FORMAT) || format.matches(mono16))
	    return input;
	else if (AudioSystem.isConversionSupported(FORMAT, format))
	    input = AudioSystem.getAudioInputStream(FORMAT, input);
	else if (AudioSystem.isConversionSupported(mono16, format))
	    input = AudioSystem.getAudioInputStream(mono16, input);
	else {
	    Game.getLogger().error("Unable to convert audio to compatible format");
	    try {
		input.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return null;
	}
	return input;
    }

    public AudioManager() {
	try {
	    outStream = (SourceDataLine) AudioSystem.getLine(outStreamInfo);
	    if (!AudioSystem.isLineSupported(outStreamInfo)) {
		Game.getLogger().error("Output audio line not supported");
	    }
	    outStream.open(FORMAT);
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	}
	audioOutput = new AudioOutputThread();
	audioOutputThread = new Thread(audioOutput);
	audioOutputThread.start();
    }

    public static void playMusic(Music m) {
	if (outStream.isActive()) {
	    outStream.stop();
	    outStream.flush();
	}
	// TODO
	outStream.start();
    }
    
    static class AudioOutputThread implements Runnable {

	private AtomicBoolean running;
	private SourceDataLine out;
	

	public void stop() {
	    running.set(false);
	}

	public synchronized void write(byte[] data) {
	}

	@Override
	public void run() {
	    running.set(true);
	    int bufSize = (int) FORMAT.getFrameRate() * FORMAT.getFrameSize();
	    byte[] audioBuffer = new byte[bufSize];
	    int maxFramesPerUpdate = (int) ((FORMAT.getFrameRate() / 1000) * 25);
	    int numBytesRead = 0;
	    double framesAccrued = 0;
	    long lastUpdate = System.nanoTime();
	    while (running.get()) {
		long currTime = System.nanoTime();
		double delta = currTime - lastUpdate;
		double secDelta = (delta / 1000000000L);
		framesAccrued += secDelta * FORMAT.getFrameRate();
		int framesToRead = (int) framesAccrued;
		int framesToSkip = 0;
		if (framesToRead > maxFramesPerUpdate) {
		    framesToSkip = framesToRead - maxFramesPerUpdate;
		    framesToRead = maxFramesPerUpdate;
		}
		if (framesToSkip > 0) {
		    int bytesToSkip = framesToSkip * FORMAT.getFrameSize();
		}
		if (framesToRead > 0) {
			int bytesToRead = framesToRead *
				FORMAT.getFrameSize();
			int tmpBytesRead = 0; //this.mixer.read(audioBuffer,
					//numBytesRead, bytesToRead);
			numBytesRead += tmpBytesRead; //mark how many read
			//fill rest with zeroes
			int remaining = bytesToRead - tmpBytesRead;
			for (int i = 0; i < remaining; i++) {
				audioBuffer[numBytesRead + i] = 0;
			}
			numBytesRead += remaining; //mark zeroes read
		}
	    }
	}
    }

    static byte[] getBytes(AudioInputStream stream) throws IOException {
	int bufSize = (int) FORMAT.getSampleRate() * FORMAT.getChannels() * FORMAT.getFrameSize();
	byte[] buf = new byte[bufSize];
	ByteList list = new ByteList(bufSize);
	int numRead = 0;
	while ((numRead = stream.read(buf)) > -1) {
	    for (int i = 0; i < numRead; i++) {
		list.add(buf[i]);
	    }
	}
	return list.asArray();
    }

    public static void close() {
	outStream.close();
	audioOutput.running.set(false);
	try {
	    audioOutputThread.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}