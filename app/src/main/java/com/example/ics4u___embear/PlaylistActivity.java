package com.example.ics4u___embear;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PICK = 2;
    TextView playlistName;
    Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        loadPlaylistData();
    }

    private void loadPlaylistData() {
        playlist = PlayData.getPlaylist(getIntent().getIntExtra("PLAYLIST_INDEX", 0));
        playlistName = findViewById(R.id.playlistName);
        ArrayList<Audio> audioList = playlist.getAudioList();

        // Set playlist name
        playlistName.setText(playlist.getName());

        // Reference to LinearLayout that holds audio buttons
        LinearLayout audioContainer = findViewById(R.id.audioContainerLayout);
        audioContainer.removeAllViews();

        for (Audio audio : audioList) {
            Button button = new Button(this);
            button.setText(audio.getName());
            // Add click listener if needed to play the audio
            audioContainer.addView(button);
        }
    }

    public void addSongToPlaylist(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*"); // Only show audio files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_AUDIO_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AUDIO_PICK && resultCode == RESULT_OK && data != null) {
            Uri audioUri = data.getData(); // Get the selected file URI

            // Persist permission to access the file later
            getContentResolver().takePersistableUriPermission(audioUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Extract file name from URI
            String fileName = getFileNameFromUri(audioUri);

            // Create Audio object and add to playlist
            Log.d("PlaylistActivity", "about to add audio");
            Audio audioToAdd = new Audio(fileName, audioUri.toString());
            playlist.modifyPlaylist(Playlist.PROCEDURE_ADD, audioToAdd);

            // Refresh UI
            loadPlaylistData();
        }
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            String fileName = cursor.getString(nameIndex);
            cursor.close();
            return fileName;
        }
        return "Unknown File";
    }

    public void goBack(View view) {
        finish();
    }
}
