package com.example.ics4u___embear;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

        for (int audioIndex = 0; audioIndex < playlist.getNumAudio(); audioIndex++) {
            Button audioButton = new Button(this);
            audioButton.setText(playlist.getAudioList().get(audioIndex).getName());
            // Add click listener if needed to play the audio

            audioButton.setOnClickListener(view -> {

            });

            audioContainer.addView(audioButton);
        }
    }

    public void goToFullscreenAudio(View view) {
        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra("PLAYLIST_INDEX", getIntent().getIntExtra("PLAYLIST_INDEX", 0));
//        intent.putExtra("AUDIO_INDEX", playlist.getAudioList().indexOf());
        startActivity(intent);
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

    public void renamePlaylist(View view) {

        Log.d("PlaylistActivity", "Rename was pressed!");

        // edit text is an input getter
        EditText inputLine = new EditText(this); // created in This activity
        inputLine.setHint("NEW PLAYLIST NAME:"); // sets what should be entered

        // formatting
        // TODO import own colors, try to make rounded stuff
        inputLine.setPadding(70, 0, 0, 30);
//        inputLine.setBackgroundColor(); // can use images as background. ex Color.BLACK
        // setTextSize,

        // alert dialogue is the actual UI popup
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this); // again, to create in this activity

        popupBuilder.setTitle("PLAYLIST RENAMING");
        popupBuilder.setMessage("PLEASE ENTER THE DESIRED NAME:");
        popupBuilder.setView(inputLine);

        // add buttons and what to do when theyre pressed


        // TODO DISABLE BUTTON UNTIL POSSIBLE (USE LISTENER)
        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // collect new name
            String newPlaylistName = inputLine.getText().toString().trim(); // trim gets rid of trailing and leading spaces
            // can do error checking here: not empty, not already in playdata

            if (!newPlaylistName.isEmpty()) {
                playlist.setName(newPlaylistName);
                loadPlaylistData();
                // TODO re render playlist buttons
            }
            else {
                Toast errorPopup = new Toast(this);
                errorPopup.setText("FAILED TO RENAME: EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        // when cancel is pressed
        popupBuilder.setNegativeButton("CANCEL", null);


        AlertDialog popup = popupBuilder.create();


        // show it (theres a method in builder too which just creates in it
        popup.show();

        // modifications to popup must occur after it is shown
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.BLACK);
//        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setRa
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));  // Example with a custom color (hex code)


    }

    public void goBack(View view) {
        finish();
    }
}
