// == FILE PACKAGE =========================
package com.example.ics4u___embear.activity;

// == IMPORTS ======================================
import com.example.ics4u___embear.TrackOverListener;
import com.example.ics4u___embear.SharedObjects;
import com.example.ics4u___embear.data.PlayData;
import com.example.ics4u___embear.data.Playlist;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.ColorDrawable;
import com.example.ics4u___embear.TrackPlayer;
import com.example.ics4u___embear.FileManager;
import com.example.ics4u___embear.data.Track;
import androidx.appcompat.app.AlertDialog;
import android.provider.OpenableColumns;
import androidx.core.view.WindowCompat;
import com.example.ics4u___embear.R;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.EditText;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.view.Gravity;
import android.widget.Toast;
import java.io.IOException;
import android.view.View;
import android.os.Bundle;
import android.net.Uri;

// == PLAYLIST SCREEN ================================================================
public class PlaylistActivity extends AppCompatActivity implements TrackOverListener {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private static final int REQUEST_AUDIO_PICK = 2;
    private TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    private TextView playlistName, playlistContentData;
    private Playlist playlist;

    // ==================================
    // == SCREEN BUILDER (ON_CREATE) ====
    // ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        clearSystemUI();

        // Read the playlist from the carry over variable PLAYLIST_INDEX; set that to the working playlist.
        playlist = PlayData.playData.getPlaylist(getIntent().getIntExtra("PLAYLIST_INDEX", 0));

        // Register this activity as a 'track-ends' listener.
        trackPlayer.addTrackOverListener(this);

        // Initialize the modifiable views.
        playlistName = findViewById(R.id.playlistName);
        playlistContentData = findViewById(R.id.totalDuration);
        // TODO add scrollbar here?

        // Display the playlist data.
        renderPlaylistData();
    }

    // ==================================
    // == UI HELPER METHODS =============
    // ==================================

    // Parameters: None.
    // Description: Gets rid of SYSTEM UI.
    private void clearSystemUI() {

        // [CLARITY] Gets rid of the top status bar.
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // [CLARITY] Gets rid of the bottom navigation bar.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // Parameters: None.
    // Description: Updates the displays with the playlist data.
    private void renderPlaylistData() {

        // Display the number of tracks in playlist, and the total time of the playlist.
        playlistContentData.setText(playlist.getNumberOfTracks() + " TRACKS | " + Track.formatMilliseconds(playlist.getPlaylistPlayTime()) + " TOTAL");

        // Display the playlist name.
        playlistName.setText(playlist.getName());

        // TODO Card Layout.
        LinearLayout trackContainer = findViewById(R.id.audioContainerLayout);

        // Clear the container (in case it is not the first render).
        trackContainer.removeAllViews();

        // Load the container with the tracks.
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
                    trackPlayer.playTrack(this, playlist.getAllTracks().get(carryableTrackIndex));
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

    // Parameters: (View) Object which called this method.
    // Description: Opens (the currently playing track in) the fullscreen player.
    public void goToFullscreenTrackPlayer(View view) {
        if (trackPlayer.isPlaying()) {
            Intent intent = new Intent(this, TrackActivity.class);
            startActivity(intent);
        }
    }

    // Parameters: (View) Object which called this method.
    // Description: Closes the current screen.
    public void goBack(View view) {
        finish();
    }

    // ==================================
    // == FUNCTIONALITY METHODS =========
    // ==================================

    // WRITTEN BY CHATGPT.
    // Description: Opens the menu for selecting audio files.
    public void addAudioFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_AUDIO_PICK);
    }

    // MOSTLY WRITTEN BY CHATGPT.
    // Description: Processes the file selected in addAudioFile().
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

                    // TODO possibly add getIcon or sum
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

            // Update playData, refresh UI.
            FileManager.savePlayData(PlayData.playData);
            renderPlaylistData();
        }
    }

    // MOSTLY WRITTEN BY CHATGPT.
    // Description: Gets the file name through uri.
    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            String fileName = cursor.getString(nameIndex);
            cursor.close();
            fileName = fileName.substring(0, fileName.indexOf('.'));
            return fileName;
        }
        return "Unknown File";
    }

    // ==================================
    // == PLAYLIST CHANGE METHODS =======
    // ==================================

    // Parameters: (View) Object which called this method.
    // Description: Opens a popup, then renames the working playlist.
    public void renamePlaylist(View view) {


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

    // Parameters: (View) Object which called this method.
    // Description: Removes the working playlist from playData, closes screen.
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

    // ==================================
    // == QUEUE MANAGEMENT METHODS ======
    // ==================================

    // Parameters: (View) Object which called this method.
    // Description: Generates and starts playing tracks by index.
    public void playInOrder(View view) throws IOException {
        trackPlayer.generateIndexQueue(playlist);
        trackPlayer.startPlayingQueue(this);
    }

    // Parameters: (View) Object which called this method.
    // Description: Generates random queue order and plays it.
    public void playShuffle(View view) throws IOException {
        trackPlayer.generateShuffleQueue(playlist);
        trackPlayer.startPlayingQueue(this);
    }

    // ==================================
    // == TRACKPLAYER METHODS ===========
    // ==================================

    // Parameters: (View) Object which called this method.
    // Description: Toggles the playing of the track.
    public void togglePlaying(View view) {

        // Perform the toggle.
        trackPlayer.togglePlaying();

        // Change the icon.
//        view = (ImageButton) view;
//        CHECK TAG
//        ((ImageButton) view).setImageResource();
//        view.setBackgroundResource();
    }

    // Parameters: (View) Object which called this method.
    // Description: Plays the next track in the queue.
    public void playNext(View view) throws IOException {
        trackPlayer.playNextInQueue(this);
    }

    // Parameters: (View) Object which called this method.
    // Description: Plays the previous track in the queue.
    public void playPrevious(View view) {

    }

    // ==================================
    // == BACKGROUND UPDATER METHODS ====
    // ==================================

    // Parameters: None.
    // Description: Will perform an action when it receives a "end track" signal from the trackPlayer.
    @Override
    public void updateTrackUI() {
        // render the player
    }

    // Parameters: None.
    /** EXPLANATION:
     When an activity is opened, it sets the current activity in to a paused state.
     This method performs an action when it is unpaused (resumed); when the screen is returned to.
     */
    @Override
    protected void onResume() {
        super.onResume();
        renderPlaylistData();
    }
}
