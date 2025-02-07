package com.example.ics4u___embear;

public class AudioPlayer {

    // constants
    final public int NEXT = 0;
    final public int PREV = 1;

    // fields
    private Audio audioPlaying;
    private long timeProgress;


    // methods (getters / setters)
    public Audio getAudioPlaying() {
        return audioPlaying;
    }

    public void setAudioPlaying(Audio audioPlaying) {
        this.audioPlaying = audioPlaying;
    }

    public long getTimeProgress() {
        return timeProgress;
    }

    public void setTimeProgress(long timeProgress) {
        this.timeProgress = timeProgress;
    }

    // custom methods

    // "play THIS song"
    public void playSong(int desiredSong) {
        if (desiredSong == NEXT) {
            // get next song in queue and put it into "PlaySong"
        }
        else if (desiredSong == PREV) {
            // get prev song in queue and put it into "PlaySong"
        }

    }

    public void playSong(Audio audio) {

    }

    public void togglePlaying() {

    }

    // queue management (use arrays?)

    public void generateIndexQueue() {

    }

    public void generateShuffleQueue() {
        // creates an
    }

    public void setNextInQueue(Audio audio) {
        //
    }

    // sum static thing for formatting
    public static String longToFormatted(long milliseconds) {
        return "not complete yet";
    }


}
