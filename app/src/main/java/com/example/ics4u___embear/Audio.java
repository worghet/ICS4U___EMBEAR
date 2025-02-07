package com.example.ics4u___embear;

// data object
public class Audio {

    // fields
    private long lengthTime;
    private String name, artist;
    private String filePath;
    // private image icon

    // methods (just getters / setters)

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }


    public long getLengthTime() {
        return lengthTime;
    }

    public void setLengthTime(long lengthTime) {
        this.lengthTime = lengthTime;
    }

    // static intent to audio file converter here

}
