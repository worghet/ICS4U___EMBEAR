package com.example.ics4u___embear;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

// extends mediaplayer?
public class AudioPlayer {

    // constants
    final public int NEXT = 0;
    final public int PREV = 1;

    // fields

    private Audio audioPlaying;
    private long timeProgress;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    public AudioPlayer() {
        isPlaying = false;
        mediaPlayer = new MediaPlayer();
        timeProgress = 0;
    }



    // methods (getters / setters)
//    public AudioPlayer getAudioPlayer() {
//        return audioPlayer;
//    }

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

    public String isPlaying() {
        if (isPlaying) {
            return "ON";
        }
        return "OFF";

    }

    // custom methods

    // "play THIS song"
    public void playAudio(int RELATIVE_AUDIO) {
        if (RELATIVE_AUDIO == NEXT) {
            // get next song in queue and put it into "PlaySong"
        }
        else if (RELATIVE_AUDIO == PREV) {
            // get prev song in queue and put it into "PlaySong"
        }

    }

    public void playAudio(Context context, Audio audio) throws IOException {
        if (isPlaying) {
            togglePlaying();
        }

        mediaPlayer.reset();  // Reset the MediaPlayer to clear the previous state

        mediaPlayer.setDataSource(context, Uri.parse(audio.getFilePath()));  // Set the new audio source

        mediaPlayer.prepare();  // Prepare the MediaPlayer to start playing
        mediaPlayer.start();  // Start playing the audio
//        togglePlaying();
    }

    // pause / resume
    public void togglePlaying() {
        if (isPlaying) {
            // turn off
            mediaPlayer.pause();

        }
        else {
//            mediaPlayer.
            // turn on
        }

        isPlaying = !isPlaying;
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
