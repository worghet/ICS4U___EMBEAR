package com.example.ics4u___embear;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

// == PLAYLIST SCREEN ===================================
public class PlaylistActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PICK = 2;
    TextView playlistName, playlistContentData;
    TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlistName = findViewById(R.id.playlistName);
        playlistContentData = findViewById(R.id.totalDuration);

        renderPlaylistData();
    }

    private void renderPlaylistData() {
        playlist = PlayData.playData.getPlaylist(getIntent().getIntExtra("PLAYLIST_INDEX", 0));

        playlistContentData.setText(playlist.getNumberOfTracks() + " TRACKS | " + Track.formatMilliseconds(playlist.getPlaylistPlayTime()) + " TOTAL");

        // Set playlist name
        playlistName.setText(playlist.getName());

        // Reference to LinearLayout that holds audio buttons
        LinearLayout trackContainer = findViewById(R.id.audioContainerLayout);
        trackContainer.removeAllViews();

        for (int trackIndex = 0; trackIndex < playlist.getNumTracks(); trackIndex++) {

            LinearLayout trackAndDeleteContainer = new LinearLayout(this);
            trackAndDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
            trackAndDeleteContainer.setGravity(Gravity.CENTER);
            trackAndDeleteContainer.setPadding(16, 16, 16, 16);


            Button trackButton = new Button(this);
            trackButton.setText(playlist.getAllTracks().get(trackIndex).getName());
            // Add click listener if needed to play the audio

            int carryableTrackIndex = trackIndex;
            trackButton.setOnClickListener(view -> {
                try {
                    trackPlayer.playAudio(this, playlist.getAllTracks().get(carryableTrackIndex));


                    TextView lenSelected = findViewById(R.id.metadata);
                    lenSelected.setText(playlist.getAllTracks().get(carryableTrackIndex).getArtist());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // ===========

            Button removeTrackButton = new Button(this);
            removeTrackButton.setText("del");
            removeTrackButton.setOnClickListener(del -> {

                if (trackPlayer.isPlaying()) {
                    trackPlayer.togglePlaying();
                }
                playlist.removeTrack(playlist.getAllTracks().get(carryableTrackIndex));
                FileManager.savePlayData(PlayData.playData);
                renderPlaylistData();
            });



            trackAndDeleteContainer.addView(trackButton);
            trackAndDeleteContainer.addView(removeTrackButton);
            trackContainer.addView(trackAndDeleteContainer);
        }
    }

    public void goToFullscreenTrackPlayer(View view) {
        if (trackPlayer.isPlaying()) {
            Intent intent = new Intent(this, TrackActivity.class);
            intent.putExtra("AUDIO_NAME", trackPlayer.getTrackPlaying().getName());
            intent.putExtra("AUDIO_LENGTH", trackPlayer.getTrackPlaying().getLengthTime());
            startActivity(intent);
        }
    }

    // ADD TO FILEMANAGER
    public void addAudioFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*"); // Only show audio files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple file selection
        startActivityForResult(intent, REQUEST_AUDIO_PICK);
    }


    // MOVE TO FILEMANAGER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_AUDIO_PICK && resultCode == RESULT_OK && data != null) {
            // Check if multiple files were selected
            if (data.getClipData() != null) {
                // Multiple files selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri trackUri = data.getClipData().getItemAt(i).getUri(); // Get each URI

                    // Persist permission to access the file
                    getContentResolver().takePersistableUriPermission(trackUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Extract file name from URI
                    String fileName = getFileNameFromUri(trackUri);

                    // Create Audio object and add to playlist
                    Track trackToAdd = new Track(fileName, trackUri.toString(), this);

                    // GET METADATA HERE
//                    audioToAdd.intializeMetadata(this, Uri.parse(audioToAdd.getFilePath()));

                    playlist.addTrack(trackToAdd);
                }
            } else if (data.getData() != null) {
                // Single file selected
                Uri trackUri = data.getData(); // Get the selected file URI

                // Persist permission to access the file
                getContentResolver().takePersistableUriPermission(trackUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Extract file name from URI
                String fileName = getFileNameFromUri(trackUri);

                // Create Audio object and add to playlist
                Track trackToAdd = new Track(fileName, trackUri.toString(), this);
                playlist.addTrack(trackToAdd);
            }

            // Refresh UI
            FileManager.savePlayData(PlayData.playData);
            renderPlaylistData();
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
                FileManager.savePlayData(PlayData.playData);
                renderPlaylistData();
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

    public void deletePlaylist(View view) {

        if (trackPlayer.isPlaying()) {
            trackPlayer.togglePlaying();
        }

        PlayData.playData.removePlaylist(playlist);
        FileManager.savePlayData(PlayData.playData);

        // refresh
//        MainActivity.
        finish();
    }



    @Override
    protected void onResume() {
        super.onResume();
        renderPlaylistData();
    }

    public void goBack(View view) {
        finish();
    }
}
