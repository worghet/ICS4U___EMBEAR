// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS =============
import java.util.ArrayList;

/** PLAYLIST CLASS:
 * This is a data object responsible for playlist data: naming, as well as the tracks inside.
 */
public class Playlist {

    // ==================================
    // == CLASS CONSTANTS (UTILITY ======
    // ==================================

    // Constants used in modifyPlaylist();
    final static int PROCEDURE_DELETE = 0;
    final static int PROCEDURE_ADD = 1;

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private String name;
    private ArrayList<Track> tracks;
//    private int numSongs;
//    private long totalPlaytime;
//    private image icon

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public Playlist(String playlistName) {
        tracks = new ArrayList<>();
        name = playlistName;
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public int getNumTracks() {
        return tracks.size();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    // TODO
    public long getPlaylistLength() {
        return 100000;
    }

    // TODO
    public int getNumberTracks() {
        return 0;
    }

    // ==================================
    // == MANIPULATOR METHODS ===========
    // ==================================

    public void setName(String name) {
        this.name = name;
    }

    // TODO
    public void deletePlaylist() {

    }

    // Parameters: (int) PROCEDURE | (Track) track.
    // Description: Either adds or removes the given track.
    public void modifyPlaylist(int PROCEDURE, Track track) {
        if (PROCEDURE == PROCEDURE_ADD) {
            // check that not exists already?
            tracks.add(track);
        }
        // if delete ...
    }
}