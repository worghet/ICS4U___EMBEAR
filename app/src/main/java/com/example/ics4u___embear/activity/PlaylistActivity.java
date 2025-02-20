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
import androidx.cardview.widget.CardView;
import android.provider.OpenableColumns;
import androidx.core.view.WindowCompat;
import com.example.ics4u___embear.R;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.TypedValue;
import android.database.Cursor;
import android.widget.EditText;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;
import java.io.IOException;
import android.view.Window;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.net.Uri;

// == PLAYLIST SCREEN ================================================================
public class PlaylistActivity extends AppCompatActivity implements TrackOverListener {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private static final int REQUEST_AUDIO_PICK = 2;
    private TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    private TextView playlistNameView, numberOfTracksView, playTimeView;
    private ImageButton loopToggleView, shuffleToggleView;
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
        playlistNameView = findViewById(R.id.playlistName);
        numberOfTracksView = findViewById(R.id.numberOfTracksView);
        playTimeView = findViewById(R.id.playTimeView);

        // Initialize the loop button; set icon color depending on whether loop is on or off.
        loopToggleView = findViewById(R.id.loopToggler);
        if (trackPlayer.isQueueLooping()) {

            // Naturally it is disabled (in xml file).
            loopToggleView.setBackgroundResource(R.drawable.looping_true);
        }

        // Initialize the shuffle button; set color depending on whether shuffle is on or off.
        shuffleToggleView = findViewById(R.id.shuffleToggler);
        if (trackPlayer.isShuffle()) {

            // Naturally it is disabled (in xml file).
            shuffleToggleView.setBackgroundResource(R.drawable.shuffle_true);
        }

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
        numberOfTracksView.setText(playlist.getNumberOfTracks() + " TRACKS");
        playTimeView.setText(Track.formatMilliseconds(playlist.getPlaylistPlayTime()));

        // Display the playlist name.
        playlistNameView.setText(playlist.getName());

        // Get the container where the tracks will be added.
        LinearLayout trackContainer = findViewById(R.id.audioContainerLayout);

        // Clear the container (in case it is not the first render).
        int childCount = trackContainer.getChildCount();
        for (int i = childCount - 1; i > 0; i--) {
            trackContainer.removeViewAt(i);  // Remove view at index i
        }

        // Load the container with the tracks.
        for (int trackIndex = 0; trackIndex < playlist.getNumberOfTracks(); trackIndex++) {

            // Create the parent container for the track and delete card.
            LinearLayout trackDeleteContainer = new LinearLayout(this);
            trackDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
            trackDeleteContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add padding / spacing between cards.
            trackDeleteContainer.setPadding(0, 40, 0, 0);

            // Create the CardView for the track.
            CardView trackCardView = new CardView(this);
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(0, 200); // (width isn't actually 0; it just allows the layout to expand as much as it can).

            // Add layout and set card weight.
            cardLayoutParams.weight = 1;
            trackCardView.setLayoutParams(cardLayoutParams);

            // Add rounding and shadow.
            trackCardView.setRadius(20);
            trackCardView.setUseCompatPadding(true);

            // Create a layout for the track info.
            LinearLayout trackLayout = new LinearLayout(this);
            trackLayout.setOrientation(LinearLayout.HORIZONTAL);
            trackLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // Add padding.
            trackLayout.setPadding(24, 0, 0, 0);

            // Left container: track icon.
            LinearLayout leftContainer = new LinearLayout(this);
            leftContainer.setLayoutParams(new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT));

            // Add centering.
            leftContainer.setGravity(Gravity.CENTER);

            // Add the track icon.
            ImageView trackImageView = new ImageView(this);
            trackImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            trackImageView.setImageBitmap(playlist.getAllTracks().get(trackIndex).getIconFromMetadata(this));
            trackImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            leftContainer.addView(trackImageView);

            // Right container: track name, artist, and playtime.
            LinearLayout rightContainer = new LinearLayout(this);
            rightContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            rightContainer.setGravity(Gravity.CENTER_VERTICAL);
            rightContainer.setOrientation(LinearLayout.VERTICAL);
            rightContainer.setPadding(30, 0, 0, 0);

            // Track name.
            TextView trackNameTextView = new TextView(this);
            trackNameTextView.setText(playlist.getAllTracks().get(trackIndex).getTrackName());
            trackNameTextView.setTextColor(getResources().getColor(R.color.cadet));
            trackNameTextView.setTextSize(14);
            trackNameTextView.setTypeface(null, Typeface.BOLD);

            // Artist and Playtime.
            TextView trackInfoTextView = new TextView(this);
            trackInfoTextView.setText(playlist.getAllTracks().get(trackIndex).getArtistName() + " | " + Track.formatMilliseconds(playlist.getAllTracks().get(trackIndex).getDurationInMilliseconds()));
            trackInfoTextView.setTextColor(getResources().getColor(R.color.cadet));
            trackInfoTextView.setTextSize(10);
            trackInfoTextView.setTypeface(null, Typeface.BOLD);

            // Add the TextViews.
            rightContainer.addView(trackNameTextView);
            rightContainer.addView(trackInfoTextView);

            // Add the left and right containers to the trackLayout.
            trackLayout.addView(leftContainer);
            trackLayout.addView(rightContainer);

            // Add trackLayout to the trackCardView.
            trackCardView.addView(trackLayout);

            // Make a listener for CardView to play track.
            int finalTrackIndex = trackIndex;
            trackCardView.setOnClickListener(view -> {
                try {

                    // Check if the user wants to go to fullscreen or just play the song.

                    // If the track is already the one playing, then open that track in fullscreen.
                    if (trackPlayer.getTrackPlaying() == playlist.getAllTracks().get(finalTrackIndex)) {
                        goToFullscreenTrackPlayer();
                    }

                    // If the track is not the playing track (or is null), just play the selected song.
                    else {
                        trackPlayer.playTrack(this, playlist.getAllTracks().get(finalTrackIndex));
                    }

                    // Reload the ui so that the track shows up as playing.
                    // Note: if a playlist has many tracks, the loading / rendering process is very slow.
                    renderPlaylistData();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            // Create the delete track card.
            CardView deleteCardView = new CardView(this);
            deleteCardView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

            // Add rounding / shadows (even if unnecessary).
            deleteCardView.setRadius(10);
            deleteCardView.setUseCompatPadding(true);


            // Inside the delete card, add the delete button.
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            // Show which track is playing; on reload this will change what is shown as green.
            if (trackPlayer.getTrackPlaying() == playlist.getAllTracks().get(trackIndex)) {
                deleteCardView.setBackgroundColor(getResources().getColor(R.color.lynch));
                trackCardView.setBackgroundColor(getResources().getColor(R.color.lynch));
                deleteButton.setBackgroundColor(getResources().getColor(R.color.lynch));
            }
            else {
                trackCardView.setBackgroundColor(getResources().getColor(R.color.pickled));
                deleteCardView.setBackgroundColor(getResources().getColor(R.color.pickled));
                deleteButton.setBackgroundColor(getResources().getColor(R.color.pickled));
            }

            // Set the delete image.
            deleteButton.setImageResource(R.drawable.delete);

            // Make the image fit inside the button.
            deleteButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // Set listener for the delete button.
            final int carryableTrackIndex = trackIndex;
            deleteButton.setOnClickListener(view -> {

                // Pause the button before deleting it.
                if (trackPlayer.isPlaying() && trackPlayer.getTrackPlaying() == playlist.getAllTracks().get(carryableTrackIndex)) {
                    trackPlayer.togglePlaying();
                }

                // Remove the track from the playlist.
                playlist.removeTrack(playlist.getAllTracks().get(carryableTrackIndex));

                // Save the playlist data.
                FileManager.savePlayData(PlayData.playData);

                // Re-render playlist data.
                renderPlaylistData();
            });

            // Add delete button to view.
            deleteCardView.addView(deleteButton);
            deleteCardView.setRadius(25);

            // Add the delete card to the parent container
            trackDeleteContainer.addView(trackCardView);
            trackDeleteContainer.addView(deleteCardView);

            // Add the trackDeleteContainer to the trackContainer
            trackContainer.addView(trackDeleteContainer);
        }
    }

    // Parameters: (View) Object which called this method.
    // Description: Opens (the currently playing track in) the fullscreen player.
    public void goToFullscreenTrackPlayer() {
        Intent intent = new Intent(this, TrackActivity.class);
        startActivity(intent);
    }

    // Parameters: (View) Object which called this method.
    // Description: Closes the current screen.
    public void goBack(View view) {
        finish();
    }

    // ==================================
    // == FUNCTIONALITY METHODS =========
    // ==================================

    // Parameters:  (View) view.
    // Description: Opens the menu for selecting audio files.
    public void addAudioFile(View view) {

        // Create an intent which opens the file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Tells the file ui to only show audio files.
        intent.setType("audio/*");

        // Tells the file ui to only show audio files that are tangible (so no hidden or virtual files).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Allows the user to select multiple files at once.
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        // Starts the intent / opens the screen.
        startActivityForResult(intent, REQUEST_AUDIO_PICK);
    }

    // Parameters: (int) requestCode: intent identifier | (int) resultCode: either CANCELLED, or OK | (Intent) data: the Uris of the selected files.
    // Description: Processes the file selected in addAudioFile().
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Create basic logic using parent to process.
        super.onActivityResult(requestCode, resultCode, data);

        // Check that everything went through fine.
        if (requestCode == REQUEST_AUDIO_PICK && resultCode == RESULT_OK && data != null) {

            // Check if multiple files were selected (getClipData).
            if (data.getClipData() != null) {

                // Go through all data selected.
                int count = data.getClipData().getItemCount();
                for (int trackIndex = 0; trackIndex < count; trackIndex++) {

                    // Get the uri from the data.
                    Uri trackUri = data.getClipData().getItemAt(trackIndex).getUri();

                    // Grant the app long term permission (persist permission).
                    getContentResolver().takePersistableUriPermission(trackUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Get file name from URI.
                    String fileName = getFileNameFromUri(trackUri);

                    // Create Track object and add to playlist.
                    Track trackToAdd = new Track(fileName, trackUri.toString(), this);
                    playlist.addTrack(trackToAdd);
                }
            }
            // Check if data is single object (getData).
            else if (data.getData() != null) {

                // Get track Uri.
                Uri trackUri = data.getData();

                // Get long term file access.
                getContentResolver().takePersistableUriPermission(trackUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Extract file name from URI.
                String fileName = getFileNameFromUri(trackUri);

                // Create Track object and add to playlist.
                Track trackToAdd = new Track(fileName, trackUri.toString(), this);
                playlist.addTrack(trackToAdd);
            }

            // Update playData to include selected files.
            FileManager.savePlayData(PlayData.playData);

            // Load ui for new selected tracks.
            renderPlaylistData();
        }
    }

    // Parameters: (Uri) file uri. (ex. content://media/external/audio/media/something)
    // Description: Gets the file name through uri.
    private String getFileNameFromUri(Uri uri) {

        // The uri does not have the file name on its own; it holds the metadata that has the file name.
        // This cursor object is used to access that name.
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        // Check that the process of accessing metadata went through, then try moving the results to the first row (returns 1 if successful).
        if (cursor != null && cursor.moveToFirst()) {

            // Get the display name from the data.
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            // Set it to string.
            String fileName = cursor.getString(nameIndex);

            // Close the cursor.
            cursor.close();

            // Get a substring without the file extension and return.
            fileName = fileName.substring(0, fileName.indexOf('.'));
            return fileName;
        }

        // If something goes wrong in the search, return Unknown File.
        return "Unknown File";
    }

    // ==================================
    // == PLAYLIST CHANGE METHODS =======
    // ==================================

    // Parameters: (View) Object which called this method.
    // Description: Opens a popup, then renames the working playlist.
    public void renamePlaylist(View view) {

        // This popup is pretty much the exact same as the new playlist popup in main activity, so im not commenting it.

        EditText inputLine = new EditText(this);
        inputLine.setHint("Enter new playlist name");
        inputLine.setHintTextColor(getResources().getColor(R.color.cadet));
        inputLine.setPadding(70, 0, 0, 30);
        inputLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
        popupBuilder.setTitle("RENAMING PLAYLIST");
        popupBuilder.setMessage("Please enter the new name below.");
        popupBuilder.setView(inputLine);

        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {
            String newPlaylistName = inputLine.getText().toString().trim();

            if (!newPlaylistName.isEmpty()) {

                // TODO check that playlist with that name doesnt exist.
                playlist.setName(newPlaylistName);
                FileManager.savePlayData(PlayData.playData);
                renderPlaylistData();
            }
            else {
                Toast errorPopup = new Toast(this);
                errorPopup.setText("EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        popupBuilder.setNegativeButton("CANCEL", null);
        AlertDialog popup = popupBuilder.create();

        Window window = popup.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cadet)));
        }

        // Show the popup
        popup.show();
    }

    // ==================================
    // == QUEUE MANAGEMENT METHODS ======
    // ==================================

    // Parameters: (View) view.w
    // Description: Starts playing queue (based on the status of shuffle).
    public void playQueue(View view) throws IOException {

        // Generate playlist queue based of shuffle status.
        if (trackPlayer.isShuffle()) {
            trackPlayer.generateShuffleQueue(playlist);
        }
        else {
            trackPlayer.generateIndexQueue(playlist);
        }

        // Start the queue.
        trackPlayer.startPlayingQueue(this);

        // Reload ui to show the current song playing.
        renderPlaylistData();
    }

    // Parameters: (View) view.
    // Description: Button function to enable/disable queue looping.
    public void toggleLooping(View view) {

        // Toggle looping.
        trackPlayer.toggleQueueLooping();

        // Change icon.
        if (trackPlayer.isQueueLooping()) {
            loopToggleView.setBackgroundResource(R.drawable.looping_true);
        }
        else {
            loopToggleView.setBackgroundResource(R.drawable.looping_false);
        }
    }

    // Parameters: (View) view.
    // Description: Button function to enable/disable shuffle.
    public void toggleShuffle(View view) {

        // Toggle shuffle.
        trackPlayer.toggleShuffle();

        // Change icon.
        if (trackPlayer.isShuffle()) {
            shuffleToggleView.setBackgroundResource(R.drawable.shuffle_true);
        }
        else {
            shuffleToggleView.setBackgroundResource(R.drawable.shuffle_false);
        }
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
    public void updateTrackUI() {
        // render the player
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
        renderPlaylistData();
    }
}
