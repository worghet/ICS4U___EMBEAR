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

import android.widget.LinearLayout;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.EditText;
import android.util.TypedValue;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;
import android.view.Gravity;
import android.view.Window;

import java.io.IOException;

import android.view.View;
import android.os.Bundle;

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

    // Parameters: None.
    // Description: Loads all Playlists as buttons onto the screen.
    private void renderPlaylistButtons() {

        // initializes the container to store all the playlist information.
        LinearLayout playlistContainer = findViewById(R.id.containerLayout);

        // Remove everything inside.
        playlistContainer.removeAllViews();

        // Go through each playlist.
        for (int playlistIndex = 0; playlistIndex < playData.getNumPlaylists(); playlistIndex++) {

            // Get the playlist information.
            Playlist playlist = playData.getPlaylist(playlistIndex);

            // Create the main row layout.
            LinearLayout playlistItemLayout = new LinearLayout(this);
            playlistItemLayout.setOrientation(LinearLayout.HORIZONTAL);
            playlistItemLayout.setPadding(30, 30, 30, 30);
            playlistItemLayout.setBackgroundColor(getResources().getColor(R.color.pickled));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            playlistItemLayout.setLayoutParams(params);
            playlistItemLayout.setGravity(Gravity.CENTER_VERTICAL);

            // Create a layout for playlist name / text.
            LinearLayout playlistContentLayout = new LinearLayout(this);
            playlistContentLayout.setOrientation(LinearLayout.VERTICAL);
            playlistContentLayout.setPadding(0, 0, 30, 0);
            playlistContentLayout.setGravity(Gravity.LEFT);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            playlistContentLayout.setLayoutParams(textParams);

            // Playlist title.
            TextView playlistNameTextView = new TextView(this);
            playlistNameTextView.setText(playlist.getName());
            playlistNameTextView.setTextSize(18);
            playlistNameTextView.setPadding(0, 0, 0, 10);
            playlistNameTextView.setTextColor(getResources().getColor(R.color.cadet));
            playlistNameTextView.setTypeface(null, Typeface.BOLD);

            // Playlist description.
            TextView playlistDescriptionTextView = new TextView(this);
            playlistDescriptionTextView.setText(playlist.getNumberOfTracks() + " TRACKS | " + Track.formatMilliseconds(playlist.getPlaylistPlayTime()));
            playlistDescriptionTextView.setPadding(0, 10, 0, 0);
            playlistDescriptionTextView.setTextColor(getResources().getColor(R.color.cadet));
            playlistDescriptionTextView.setTypeface(null, Typeface.BOLD);

            // Add views to content layout.
            playlistContentLayout.addView(playlistNameTextView);
            playlistContentLayout.addView(playlistDescriptionTextView);

            // Create delete icon.
            ImageView deleteIcon = new ImageView(this);
            deleteIcon.setImageResource(R.drawable.delete);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    dpToPx(40), dpToPx(40));
            deleteIcon.setLayoutParams(iconParams);
            deleteIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            deleteIcon.setPadding(0, 0, 0, 0);

            // Add functionality for the delete icon.
            int finalPlaylistIndex = playlistIndex;
            deleteIcon.setOnClickListener(view -> {
                new AlertDialog.Builder(this)
                        .setTitle("DELETE PLAYLIST")
                        .setMessage("Are you sure you want to delete this playlist?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            playData.removePlaylist(playData.getPlaylist(finalPlaylistIndex));
                            FileManager.savePlayData(PlayData.playData);
                            renderPlaylistButtons();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            // Add items to playlist layout.
            playlistItemLayout.addView(playlistContentLayout);
            playlistItemLayout.addView(deleteIcon);

            // Add functionality to the playlist button.
            playlistItemLayout.setOnClickListener(v -> {
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("PLAYLIST_INDEX", finalPlaylistIndex);
                startActivity(intent);
            });

            // Add to container.
            playlistContainer.addView(playlistItemLayout);
        }

        // Create add playlist button.
        LinearLayout addPlaylistLayout = new LinearLayout(this);
        addPlaylistLayout.setOrientation(LinearLayout.HORIZONTAL);
        addPlaylistLayout.setPadding(30, 30, 30, 30);
        addPlaylistLayout.setBackgroundColor(getResources().getColor(R.color.pickled));
        addPlaylistLayout.setGravity(Gravity.CENTER);

        // Set the layout.
        LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addParams.setMargins(0, 10, 0, 10);
        addPlaylistLayout.setLayoutParams(addParams);

        // Create ImageView for the add button.
        ImageView addButton = new ImageView(this);
        addButton.setImageResource(R.drawable.add_track); // Ensure you have this drawable

        // Set fixed size for the add icon.
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                dpToPx(48), dpToPx(48)); // Adjust size as needed
        addButton.setLayoutParams(iconParams);
        addButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Add listener to add playlist button.
        addPlaylistLayout.setOnClickListener(view -> {
            addPlaylistToPlayData();
        });

        // Add the button to the layout.
        addPlaylistLayout.addView(addButton);

        // Add button to container.
        playlistContainer.addView(addPlaylistLayout);
    }

    // Parameters: (int) density-independent pixels.
    // Description: coverts dp to to pixel measurement.
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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

    // Parameters: (View) Object which called this method.
    // Description: Makes a popup which requests a name for the new playlist.
    public void addPlaylistToPlayData() {

        // Create the text line itself.
        EditText inputLine = new EditText(this);
        inputLine.setHint("Enter new playlist name");
        inputLine.setTextColor(Color.BLACK);
        inputLine.setPadding(70, 0, 0, 30);
        inputLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);  // Example: Set text size in sp

        // Build the popup itself.
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
        popupBuilder.setTitle("CREATING PLAYLIST");
        popupBuilder.setMessage("Please enter the new name below.");
        popupBuilder.setView(inputLine);

        // Create the positive button ("CREATE")
        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // Get the entered name.
            String newPlaylistName = inputLine.getText().toString().trim();

            // Check if the entered name isn't empty.
            if (!newPlaylistName.isEmpty()) {

                // Check that a playlist with the same name doesn't exist.
                boolean noMatchingName = true;
                for (int playIndex = 0; playIndex < playData.getNumPlaylists(); playIndex++) {
                    if (playData.getPlaylist(playIndex).getName().equals(newPlaylistName)) {
                        noMatchingName = false;
                        break;
                    }
                }

                // If there is no playlist with the same name, make it.
                if (noMatchingName) {

                    // Add new playlist to playData.
                    playData.addPlaylist(new Playlist(newPlaylistName));

                    // Save the playdata object.
                    FileManager.savePlayData(PlayData.playData);

                    // Re-render the playlists to show the new playlist.
                    renderPlaylistButtons();
                }

                // If the name is already used, show message.
                else {
                    Toast errorPopup = new Toast(this);
                    errorPopup.setText("THAT NAME IS ALREADY IN USE");
                    errorPopup.show();
                }
            }

            // If string entered is empty, show message.
            else {
                // Show an error message if something is wrong.
                Toast errorPopup = new Toast(this);
                errorPopup.setText("EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        // Set negative ("CANCEL") button to do nothing.
        popupBuilder.setNegativeButton("CANCEL", null);

        AlertDialog popup = popupBuilder.create();

        // Set the background color for the popup.
        Window window = popup.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cadet)));
        }

        // Show the popup.
        popup.show();
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
        else {
            playData = new PlayData();
        }

        // Update global variable.
        PlayData.playData = playData;
    }

    // =================================================================
    // == TRACKPLAYER METHODS (NOT COMPLETE FOR THE PROJECT) ===========
    // =================================================================

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

    // =================================================================
    // == UPDATER METHODS (NOT COMPLETE FOR THE PROJECT) ===============
    // =================================================================

    // Parameters: None.
    // Description: Will perform an action when it receives a "end track" signal from the trackPlayer.
    @Override
    public void updateTrackUI() throws IOException {
        //
    }

    // Parameters: None.

    /**
     * EXPLANATION:
     * When an activity is opened, it sets the current activity in to a paused state.
     * This method performs an action when it is unpaused (resumed); when the screen is returned to.
     */
    @Override
    protected void onResume() {
        super.onResume();
        renderPlaylistButtons();
    }

}