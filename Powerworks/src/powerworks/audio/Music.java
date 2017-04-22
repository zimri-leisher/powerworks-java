package powerworks.audio;

import kuusisto.tinysound.TinySound;

public enum Music {
    //TEST("/sounds/misc/test2.wav");
    ;

    kuusisto.tinysound.Music tsMusic;

    private Music(String path) {
	tsMusic = TinySound.loadMusic(path, true);
    }

    public void play() {
	tsMusic.play(false);
    }

    public void loop() {
	tsMusic.play(true);
    }

    public void play(double vol) {
	tsMusic.play(false, vol);
    }

    public void stop() {
	tsMusic.stop();
    }

    public void pause() {
	tsMusic.pause();
    }

    public void setVolume(double vol) {
	tsMusic.setVolume(vol);
    }

    public void setPan(double pan) {
	tsMusic.setPan(pan);
    }
}
