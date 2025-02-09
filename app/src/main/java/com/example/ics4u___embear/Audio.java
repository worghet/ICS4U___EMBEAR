package com.example.ics4u___embear;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

// Data object for Audio (can rename to Track if preferred)
public class Audio {

    // Fields
    private long lengthTime; // Duration in milliseconds
    private String name, artist;
    private String filePath;
    private MediaMetadataRetriever metadataRetriever= new MediaMetadataRetriever();

    // Constructor
    public Audio(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
//        metadataRetriever.setDataSource(filePath);
    }


    public Audio(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
//        this.lengthTime = getAudioDuration(Uri.parse(filePath)); // Update duration if file path changes
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getLengthTime() {
        return lengthTime;
    }

    // MM:SS
    static public String millisecondsToSeconds(long milliseconds) {
        int minutes, seconds;

        seconds = (int) milliseconds / 1000;
        minutes = seconds / 60;
        seconds %= 60;

        return (minutes + ":" + seconds);
    }
}
