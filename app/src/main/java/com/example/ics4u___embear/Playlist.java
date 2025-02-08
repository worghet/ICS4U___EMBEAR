package com.example.ics4u___embear;

import java.util.ArrayList;

// data object
public class Playlist {
// extend serializable
    // constants
    final static int PROCEDURE_DELETE = 0;
    final static int PROCEDURE_ADD = 1;

    // fields
//    private int numSongs;
    private String name;
//    private long totalPlaytime;
    private ArrayList<Audio> audioList;
    // private image icon
    // queue
//    Gson

    // constructor
    public Playlist(String playlistName) {
        audioList = new ArrayList<>();
        name = playlistName;
    }



    // methods (getters and setters)
    public int getNumAudio() {
        return audioList.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPlaytime() {
        return 0;
    }

    public ArrayList<Audio> getAudioList() {
        return audioList;
    }

    // custom

    public void deletePlaylist() {

    }

    // replace with 2 dif methods?             just string?
    public void modifyPlaylist(int procedure, Audio audio) {
        if (procedure == PROCEDURE_ADD) {
            // check that not exists already?
            audioList.add(audio);
        }
        // if delete ...
    }

    public long calculatePlaylistLengthMillis() {
        return 100000;
    }

    public int howManyAudio() {
        return 0;
    }
}
