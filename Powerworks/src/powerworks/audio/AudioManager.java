package powerworks.audio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import powerworks.main.Game;

public class AudioManager {

    static List<SoundSource> queue = new ArrayList<SoundSource>();
    static final double HEARING_THRESHOLD = 100;
    static Music m;
    static double musicVol = 1.0;
    static double soundVol = 1.0;

    static class SoundSource {

	Sound s;
	int xPixel, yPixel;
	double vol;
	boolean loop;

	private SoundSource(Sound s, int xPixel, int yPixel, double vol, boolean loop) {
	    this.s = s;
	    this.xPixel = xPixel;
	    this.yPixel = yPixel;
	    this.vol = vol;
	    this.loop = loop;
	}
    }

    public static void playSound(Sound s, int xPixel, int yPixel, double vol) {
	queue.add(new SoundSource(s, xPixel, yPixel, vol, false));
    }
    
    public static void loopSound(Sound s, int xPixel, int yPixel, double vol) {
	queue.add(new SoundSource(s, xPixel, yPixel, vol, true));
    }
    
    public static void stopLoop(Sound s, int xPixel, int yPixel) {
	Iterator<SoundSource> i = queue.iterator();
	while(i.hasNext()) {
	    SoundSource sSrc = i.next();
	    if(sSrc.loop && sSrc.s == s && sSrc.xPixel == xPixel && sSrc.yPixel == yPixel)
		i.remove();
	}
    }
    
    public static void stopLoop(Sound s) {
	Iterator<SoundSource> i = queue.iterator();
	while(i.hasNext()) {
	    SoundSource sSrc = i.next();
	    if(sSrc.loop && sSrc.s == s)
		i.remove();
	}
    }

    public static void update() {
	Iterator<SoundSource> i = queue.iterator();
	while(i.hasNext()) {
	    SoundSource s = i.next();
	    int playerXPixel = Game.getMainPlayer().getXPixel();
	    int playerYPixel = Game.getMainPlayer().getYPixel();
	    double dist = Math.sqrt(Math.pow(s.xPixel - playerXPixel, 2) + Math.pow(s.yPixel - playerYPixel, 2));
	    double adjDist = dist * s.vol * soundVol;
	    if (adjDist < HEARING_THRESHOLD) {
		double pan = 0;
		double ratio = (HEARING_THRESHOLD - adjDist) / HEARING_THRESHOLD;
		s.s.play(s.vol * soundVol * ratio, 0);
	    }
	    if(!s.loop)
		i.remove();
	}
	queue.clear();
    }

    public static void playMusic(Music m) {
	AudioManager.m = m;
	m.play(musicVol);
    }

    public static void setMusicVol(double vol) {
	musicVol = vol;
    }

    public static void setSoundVol(double vol) {
	soundVol = vol;
    }
}
