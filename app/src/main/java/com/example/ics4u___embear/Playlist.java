package com.example.ics4u___embear;

// data object
public class Playlist {

    // constants
    final static int PROCEDURE_DELETE = 0;
    final static int PROCEDURE_ADD = 1;

    // fields
    private int numSongs;
    private String name;
    private long totalPlaytime;
    // private image icon

    // methods (getters and setters)
    public int getNumSongs() {
        return numSongs;
    }

    public void setNumSongs(int numSongs) {
        this.numSongs = numSongs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(long totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }

    // custom

    public void deletePlaylist() {

    }

    // replace with 2 dif methods?             just string?
    public void modifyPlaylist(int procedure, Audio audio) {

    }

}
