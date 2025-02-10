// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS =============

import java.util.ArrayList;
import java.util.List;

/**
 * PLAYDATA CLASS:
 * This is a data object responsible for the storage of all data used in the application.
 */
public class PlayData {

    public static PlayData playData; // shared between activities;   TODO don't

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    // List of all playlists.
    private List<Playlist> allPlaylists = new ArrayList<>();

    // Because this is a singleton class, it holds its own object.
    // private static PlayData playData = generateDemoPlayData();

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    // TEMPORARY: WILL REPLACE WITH DEFAULT CONSTRUCTOR AFTER
    private static PlayData generateDemoPlayData() {
        PlayData demoData = new PlayData();

        Playlist playlist1 = new Playlist("PLAYLIST #1");
        demoData.allPlaylists.add(playlist1);

        return demoData;
    }

//    // Parameters: None.
//    // Description: Private constructor; singleton class.
//    private PlayData() {
//
//    }

    // ==================================
    // == MANIPULATOR METHODS ===========
    // ==================================

    // Parameters: (Playlist) playlist
    // Description: Adds given playlist to list of playlists.
    public void addPlaylist(Playlist playlist) {
        allPlaylists.add(playlist);
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
