// == FILE PACKAGE =====================
package com.example.ics4u___embear.data;

// == IMPORTS =============
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// == PLAYDATA ========
public class PlayData {

    public static PlayData playData; // shared between activities;   TODO don't touch TODO move to sharedObjects

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private List<Playlist> allPlaylists = new ArrayList<>();

    // ==================================
    // == MUTATOR METHODS ===========
    // ==================================

    // Parameters: (Playlist) playlist.
    // Description: Adds given playlist to list of playlists.
    public void addPlaylist(Playlist playlist) {
        allPlaylists.add(playlist);
    }

    // Parameters: (Playlist) playlist.
    // Description : Removes the given playlist from the list of playlists.
    public void removePlaylist(Playlist playlist) {
        allPlaylists.remove(playlist);
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    // Parameters: None | Uses access to class variables.
    // Description: Returns the number of playlists.
    public int getNumPlaylists() {
        return allPlaylists.size();
    }

    // Parameters: (int) Playlist index.
    // Description: Returns the playlist associated with the index within the playlist list.
    public Playlist getPlaylist(int index) {
        return allPlaylists.get(index);
    }

}
