package com.example.ics4u___embear;

public class AudioPlayer {

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
    public void initializeAudioPlaying(Audio audio) {
        // start playing this audio file
    }

    public void togglePlaying() {

    }

    // queue management (use arrays?)

    public void generateIndexQueue() {

    }

    public void generateShuffleQueue() {

    }

    // sum static thing for formatting
    public static String longToFormatted(long milliseconds) {
        return "not complete yet";
    }


}
