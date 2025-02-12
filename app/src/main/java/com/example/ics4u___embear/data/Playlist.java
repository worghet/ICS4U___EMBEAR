// == FILE LOCATION ===============
package com.example.ics4u___embear.data;

// == IMPORTS =============

import java.util.ArrayList;

/** PLAYLIST CLASS:
 * This is a data object responsible for playlist data: naming, as well as the tracks inside.
 */
public class Playlist {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private String name;
    private ArrayList<Track> allTracks;
//    private image icon

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

    public int getNumTracks() {
        return allTracks.size();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Track> getAllTracks() {
        return allTracks;
    }

    public int getPlaylistPlayTime() {

        int totalPlayTime = 0;
        // iterate through each audio, take sum.. format it
        for (Track track : allTracks) {
            totalPlayTime += track.getDuration();
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