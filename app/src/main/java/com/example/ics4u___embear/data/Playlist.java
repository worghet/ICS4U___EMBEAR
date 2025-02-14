// == FILE PACKAGE =====================
package com.example.ics4u___embear.data;

// == IMPORTS =============
import java.util.ArrayList;

// == PLAYLIST ========
public class Playlist {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private String name;
    private ArrayList<Track> allTracks;
//    TODO private image icon

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public Playlist(String playlistName) {
        allTracks = new ArrayList<>();
        name = playlistName;
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================


    public String getName() {
        return name;
    }

    public ArrayList<Track> getAllTracks() {
        return allTracks;
    }

    public int getPlaylistPlayTime() {

        int totalPlayTime = 0;

        // Iterate through each track, take sum; format it.
        for (Track track : allTracks) {
            totalPlayTime += track.getDurationInMilliseconds();
        }

        return totalPlayTime;
    }

    public int getNumberOfTracks() {
        return allTracks.size();
    }

    // ==================================
    // == MANIPULATOR METHODS ===========
    // ==================================

    public void setName(String name) {
        this.name = name;
    }

    public void addTrack(Track track) {
        allTracks.add(track);
    }

    public void removeTrack(Track track) {
        allTracks.remove(track);
    }
}