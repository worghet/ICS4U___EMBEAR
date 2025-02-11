package com.example.ics4u___embear;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

// extends mediaplayer?
public class trackPlayer {

    // constants
    final public int NEXT = 0;
    final public int PREV = 1;

    // fields

    private static trackPlayer trackPlayer = new trackPlayer();
    private Track audioPlaying;
    private int timeProgress;
    private MediaPlayer mediaPlayer;

    private trackPlayer() {
        mediaPlayer = new MediaPlayer();
        timeProgress = 0;
    }



    // methods (getters / setters)
    public static trackPlayer getTrackPlayer() {
        return trackPlayer;
    }

    public Track getAudioPlaying() {
        return audioPlaying;
    }

    public void setAudioPlaying(Track audioPlaying) {
        this.audioPlaying = audioPlaying;
    }

    public long getTimeProgress() {
        return timeProgress;
    }

    public void setTimeProgress(int timeProgress) {
        this.timeProgress = timeProgress;
    }

    public String isPlaying() {
        if (mediaPlayer.isPlaying()) {
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playAudio(Context context, Track audio) throws IOException {
        if (mediaPlayer.isPlaying()) {
            togglePlaying();
        }

        mediaPlayer.reset();  // Reset the MediaPlayer to clear the previous state

        audioPlaying = audio;
        mediaPlayer.setDataSource(context, Uri.parse(audio.getFilePath()));  // Set the new audio source

        mediaPlayer.prepare();  // Prepare the MediaPlayer to start playing
        mediaPlayer.start();  // Start playing the audio
        // if song fully plays (ends) then what? then we check mediaplaying.isplayimg
    }

    // pause / resume
    public void togglePlaying() {
        if (mediaPlayer.isPlaying()) {
            // turn off
            timeProgress = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();

        }
        else {
            mediaPlayer.start();
            mediaPlayer.seekTo(timeProgress);
            // turn on
        }

    }

    // queue management (use arrays?)

    public void generateIndexQueue() {

    }

    public void generateShuffleQueue() {
        // creates an
    }

    public void setNextInQueue(Track audio) {
        //
    }



}
