package com.example.ics4u___embear;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    TextView playlistName;
    Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        // clear ui
        loadPlaylistData();
    }

    private void loadPlaylistData() {
        playlist = PlayData.getPlaylist(getIntent().getIntExtra("PLAYLIST_INDEX", 0));
        playlistName = findViewById(R.id.playlistName);
        ArrayList<Audio> audioList = playlist.getAudioList();

        // set text
        playlistName.setText(playlist.getName());

        // --------------
//
//        // Reference to the LinearLayout inside ScrollView
//        LinearLayout buttonContainer = findViewById(R.id.buttonContainerLayout);

        LinearLayout audioContainer = findViewById(R.id.audioContainerLayout);
        audioContainer.removeAllViews();

        for (int i = 0; i < playlist.getNumAudio(); i++) {
            Audio audio = audioList.get(i);
            Button button = new Button(this);
            button.setText(audio.getName());

            // listener (to open audio in audioplayer / play it)

            audioContainer.addView(button);
        }


//        // Clear existing views in the container (if any)
//        buttonContainer.removeAllViews();
//
//        // Iterate through each playlist in PlayData
//        for (int playlistIndex = 0; playlistIndex < PlayData.getNumPlaylists(); playlistIndex++) {
//            Playlist playlist = PlayData.getPlaylist(playlistIndex);
//            Button button = new Button(this);
//            button.setText(playlist.getName());  // Set the button text as the playlist name
//
//            int carryablePlaylistIndex = playlistIndex;
//            button.setOnClickListener(view -> {
//                Intent intent = new Intent(this, PlaylistActivity.class);
//                intent.putExtra("PLAYLIST_INDEX", carryablePlaylistIndex);
//                startActivity(intent);
////                overridePendingTransition(R.anim., R.anim.fade_in);
//            });
//
//            // Add the button to the container layout
//            buttonContainer.addView(button);

    }

    public void goBack(View view) {
        // save everything
        finish();
    }
}