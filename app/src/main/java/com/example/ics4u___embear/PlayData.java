package com.example.ics4u___embear;

import java.util.ArrayList;
import java.util.List;

// singleton class
public class PlayData {

    private List<Playlist> allPlaylists = new ArrayList<>();
    static PlayData playData = generateDemoPlayData();

    // read from file if it were real, etc
    private static PlayData generateDemoPlayData() {
        PlayData demoData = new PlayData();

        Playlist playlist1 = new Playlist("PLAYLIST #1");
//        playlist1.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongA"));
//        playlist1.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongB"));
//
//        Playlist playlist2 = new Playlist("PLAYLIST #2");
//        playlist2.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongC"));
//        playlist2.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongD"));
//        playlist2.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongE"));
//
//        Playlist playlist3 = new Playlist("PLAYLIST #3");
//        playlist3.modifyPlaylist(Playlist.PROCEDURE_ADD, new Audio("SongF"));


        // make add playlist method
        demoData.allPlaylists.add(playlist1);
//        demoData.allPlaylists.add(playlist2);
//        demoData.allPlaylists.add(playlist3);


        return demoData;
    }

    public static void addPlaylist(Playlist playlist) {
        playData.allPlaylists.add(playlist);
    }

    public static int getNumPlaylists() {
        return playData.allPlaylists.size();
    }

    public static Playlist getPlaylist(int index) {
        return playData.allPlaylists.get(index);
    }


    private PlayData() {

    }

    public static PlayData getPlayData() {
        return playData;
    }



}
