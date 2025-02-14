/*
[NAME] ----- BUSHKOV
[TASK] CODE REVIEW (ICS4U-01)
[DATE] 02 / 10 / 2025
[DESCRIPTION] Local audio file organizer and player.
 */

// == FILE PACKAGE =========================
package com.example.ics4u___embear.activity;

// == IMPORTS ======================================
import com.example.ics4u___embear.TrackOverListener;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ics4u___embear.SharedObjects;
import com.example.ics4u___embear.data.Playlist;
import com.example.ics4u___embear.data.PlayData;
import android.graphics.drawable.ColorDrawable;
import com.example.ics4u___embear.TrackPlayer;
import com.example.ics4u___embear.FileManager;
import com.example.ics4u___embear.data.Track;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowCompat;
import com.example.ics4u___embear.R;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import android.os.Handler;
import android.view.View;
import android.os.Bundle;
import java.io.File;

// == PLAYLISTS SCREEN ===========================================================
public class MainActivity extends AppCompatActivity implements TrackOverListener {

    // ==================================
    // == CLASS VARIABLES [FIELDS] ======
    // ==================================

    private PlayData playData;
    private TrackPlayer trackPlayer = SharedObjects.trackPlayer;

    // ==================================
    // == SCREEN BUILDER (ON_CREATE) ====
    // ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearSystemUI();

        // Register this activity as a 'track-ends' listener.
        trackPlayer.addTrackOverListener(this);

        // Load PlayData if there is anything saved.
        catchDataOnOpen();

        // Make each playlist into a button.
        renderPlaylistButtons();

    }

    // ==================================
    // == UI HELPER METHODS =============
    // ==================================

    // TODO: CHANGE COMPOSITION OF BUTTONS (INCLUDES REMOVE, NAME, ETC). | CARDVIEW?
    // Parameters: None.
    // Description: Loads all Playlists as buttons onto the screen.
    public void renderPlaylistButtons() {

        // Create a reference to the LinearLayout inside the ScrollView.
        LinearLayout playlistContainer = findViewById(R.id.playlistContainerLayout);

        // Clear existing views in the container (if any).
        playlistContainer.removeAllViews();

        // Iterate through each playlist in PlayData.
        for (int playlistIndex = 0; playlistIndex < playData.getNumPlaylists(); playlistIndex++) {

            // For convenience: copy the playlist here.
            Playlist playlist = playData.getPlaylist(playlistIndex);

            // =================================================
            // == PLAYLIST BUTTON CREATION =====================
            // =================================================

            // Create a button dedicated to this playlist.
            Button playlistButton = new Button(this);

            // Set the button text as the playlist name.
            playlistButton.setText(playlist.getName());

            // Set up a listener which will redirect the user to that playlist.
            int carryablePlaylistIndex = playlistIndex;
            playlistButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("PLAYLIST_INDEX", carryablePlaylistIndex);
                startActivity(intent);
            });

            // Add the button to the container layout
            playlistContainer.addView(playlistButton);
        }
    }

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

    // ==================================
    // == FUNCTIONALITY METHODS =========
    // ==================================

    // TODO: recolor dis
    // Parameters: (View) Object which called this method.
    // Description: Makes a popup which requests a name for the new playlist.
    public void addPlaylistToPlayData(View view) {

        // ----------------------------------------------------------------|
        // 1. Create the popup. TODO: CHANGE COMPOSITION OF POPUP (COLOR). |
        // ----------------------------------------------------------------|

        // Create the input line where the user will write the desired name.
        EditText inputLine = new EditText(this);
        inputLine.setHint("NEW PLAYLIST NAME:"); // sets what should be entered

        // TODO import own colors, try to make rounded stuff
        inputLine.setPadding(70, 0, 0, 30);

        // alert dialogue is the actual UI popup
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this); // again, to create in this activity

        popupBuilder.setTitle("NEW PLAYLIST CREATION");
        popupBuilder.setMessage("PLEASE ENTER THE DESIRED NAME:");
        popupBuilder.setView(inputLine);

        // ------------------------------------|
        // 2. Setup "CANCEL" and "ADD" Buttons |
        // ------------------------------------|

        // When the add button is pressed.
        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // Read what is in the entered line.
             String newPlaylistName = inputLine.getText().toString().trim(); // trim gets rid of trailing and leading spaces

            // ERROR CHECKING ----

            // Check if String entered is empty.
            if (!newPlaylistName.isEmpty()) {

                // Compare the new name with all of the existing names.
                boolean nameHasBeenUsed = false;
                for (int playlistIndex = 0; playlistIndex < playData.getNumPlaylists(); playlistIndex++) {
                    if (newPlaylistName.equals(playData.getPlaylist(playlistIndex))) {
                        nameHasBeenUsed = true;
                        break;
                    }
                }

                // If it hasn't been used, make the new playlist.
                if (!nameHasBeenUsed) {

                    // Add the new playlist to playData.
                    playData.addPlaylist(new Playlist(newPlaylistName));

                    File file = getFileStreamPath(FileManager.PLAYDATA_FILE_NAME);

                    FileManager.savePlayData(playData);

                    renderPlaylistButtons();

                }
                // If the name has been used; do nothing.
                else {
                    Toast errorPopup = new Toast(this);
                    errorPopup.setText("ERROR: NAME ALREADY USED");
                    errorPopup.show();
                }

            }
            else {
                Toast errorPopup = new Toast(this);
                errorPopup.setText("ERROR: EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        // when cancel is pressed
        popupBuilder.setNegativeButton("CANCEL", null);

        // Build the popup itself.
        AlertDialog popup = popupBuilder.create();

        // Display the popup to the user.
        popup.show();

        // modifications to popup must occur after it is shown
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.BLACK);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));  // Example with a custom color (hex code)
    }

    // Parameters: None.
    // Description: Load playData from file (if exists). Otherwise, make it empty.
    private void catchDataOnOpen() {

        // Get file using context of Activity; save it in the FileManager class.
        FileManager.PLAYDATA_FILE = getFileStreamPath(FileManager.PLAYDATA_FILE_NAME);

        // Check that the file exists; if it does, load data.
        if (FileManager.playDataFileExists()) {
            playData = FileManager.loadPlayData();
        }


        // If the file does not exist; assign empty one.
        else { playData = new PlayData(); }

        PlayData.playData = playData; // TODO globalize ts
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
    public void updateTrackUI() throws IOException {
        //
    }

    // Parameters: None.
    /** EXPLANATION:
    When an activity is opened, it sets the current activity in to a paused state.
    This method performs an action when it is unpaused (resumed); when the screen is returned to.
     */
    @Override
    protected void onResume() {
        super.onResume();
        renderPlaylistButtons();
    }

}