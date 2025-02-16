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

        loopToggleView = findViewById(R.id.loopToggler);
        if (trackPlayer.isQueueLooping()) {
            loopToggleView.setBackgroundResource(R.drawable.looping_true);
        }

        shuffleToggleView = findViewById(R.id.shuffleToggler);
        if (trackPlayer.isShuffle()) {
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

            // Create the parent container for the track and delete card
            LinearLayout trackDeleteContainer = new LinearLayout(this);
            trackDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
            trackDeleteContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            trackDeleteContainer.setPadding(0, 40, 0, 0); // Adds spacing between cards

            // Create the CardView for the track
            CardView trackCardView = new CardView(this);
            LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                    0, // Set 0 width to allow it to expand and take up available space
                    200 // height of the CardView
            );
            cardLayoutParams.weight = 1; // This ensures that the trackCard takes up the majority of the space
            trackCardView.setLayoutParams(cardLayoutParams);


            trackCardView.setCardBackgroundColor(getResources().getColor(R.color.pickled)); // Set background color


            trackCardView.setRadius(20); // Rounded corners
            trackCardView.setUseCompatPadding(true); // Adds padding for shadow

            // Create the horizontal LinearLayout inside the track CardView
            LinearLayout trackLayout = new LinearLayout(this);
            trackLayout.setOrientation(LinearLayout.HORIZONTAL);
            trackLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            );
            trackLayout.setPadding(24, 0, 0, 0);

            // Left container: track icon (ImageView)
            LinearLayout leftContainer = new LinearLayout(this);
            leftContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    100, // Slightly larger width for more space around the image
                    LinearLayout.LayoutParams.MATCH_PARENT)
            );
            leftContainer.setGravity(Gravity.CENTER);

            ImageView trackImageView = new ImageView(this);
            trackImageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            trackImageView.setImageBitmap(playlist.getAllTracks().get(trackIndex).getIconFromMetadata(this));  // Set a default icon (replace with track icon if available)
            trackImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            leftContainer.addView(trackImageView);

            // Right container: track name, artist, and playtime
            LinearLayout rightContainer = new LinearLayout(this);
            rightContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1)
            );
            rightContainer.setGravity(Gravity.CENTER_VERTICAL);
            rightContainer.setOrientation(LinearLayout.VERTICAL);
            rightContainer.setPadding(30, 0, 0, 0);

            // Track Name TextView
            TextView trackNameTextView = new TextView(this);
            trackNameTextView.setText(playlist.getAllTracks().get(trackIndex).getTrackName());
            trackNameTextView.setTextColor(getResources().getColor(R.color.cadet));
            trackNameTextView.setTextSize(14); // Or use sp for scaling
            trackNameTextView.setTypeface(null, Typeface.BOLD);

            // Artist and Playtime TextView
            TextView trackInfoTextView = new TextView(this);
            trackInfoTextView.setText(
                    playlist.getAllTracks().get(trackIndex).getArtistName() + " | " +
                            Track.formatMilliseconds(playlist.getAllTracks().get(trackIndex).getDurationInMilliseconds()));
            trackInfoTextView.setTextColor(getResources().getColor(R.color.cadet));
            trackInfoTextView.setTextSize(10); // Or use sp for scaling
            trackInfoTextView.setTypeface(null, Typeface.BOLD);

            // Add TextViews to right container
            rightContainer.addView(trackNameTextView);
            rightContainer.addView(trackInfoTextView);

            // Add the left and right containers to the trackLayout
            trackLayout.addView(leftContainer);
            trackLayout.addView(rightContainer);

            // Add trackLayout to the trackCardView
            trackCardView.addView(trackLayout);

            // Set OnClickListener for CardView to play the track
            int finalTrackIndex = trackIndex;
            trackCardView.setOnClickListener(view -> {
                try {
                    // Play the track
                    if (trackPlayer.getTrackPlaying() == playlist.getAllTracks().get(finalTrackIndex)) {
                        goToFullscreenTrackPlayer();
                    }
                    else {
                        trackPlayer.playTrack(this, playlist.getAllTracks().get(finalTrackIndex));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });



            // Create the delete CardView and button
            CardView deleteCardView = new CardView(this);
            deleteCardView.setLayoutParams(new LinearLayout.LayoutParams(
                    200, // Set both width and height to 100 to make the card square
                    200 // Same as width to ensure it's square
            ));
            deleteCardView.setRadius(10); // Rounded corners for the delete card
            deleteCardView.setUseCompatPadding(true); // Adds padding for shadow

            // Inside the delete card, add the delete button
            // Inside the delete card, add the delete button
            ImageButton deleteButton = new ImageButton(this);
            deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
            );

// Set the background color to 'pickled'
//            if
            deleteButton.setBackgroundColor(getResources().getColor(R.color.pickled));
//            deleteButton.setRadius

// Set the delete image
            deleteButton.setImageResource(R.drawable.delete);

// Make the image fit inside the button
            deleteButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // OnClickListener for the delete button
            // OnClickListener for the delete button
            final int carryableTrackIndex = trackIndex;
            deleteButton.setOnClickListener(view -> {
                if (trackPlayer.isPlaying() && trackPlayer.getTrackPlaying() == playlist.getAllTracks().get(carryableTrackIndex)) {
                    trackPlayer.togglePlaying();
                }

                // Remove the track from the playlist
                playlist.removeTrack(playlist.getAllTracks().get(carryableTrackIndex));

                // Remove the specific track's CardView from the container
                trackContainer.removeViewAt(carryableTrackIndex + 1);
                // TODO use a tag system to delete the correct view

                // Save the playlist data
                FileManager.savePlayData(PlayData.playData);

                // Optionally, if you want to update the remaining track views
                renderPlaylistData(); // Or re-render selectively if necessary
            });

            deleteCardView.addView(deleteButton); // Add delete button to the delete card
            deleteCardView.setRadius(25); // Rounded corners for the delete card


            // Add the delete card to the parent container
            trackDeleteContainer.addView(trackCardView);
            trackDeleteContainer.addView(deleteCardView); // Add the delete button card to the same container


            // Add the trackDeleteContainer to the trackContainer
            trackContainer.addView(trackDeleteContainer);

        }
    }


    // Parameters: (View) Object which called this method.
    // Description: Opens (the currently playing track in) the fullscreen player.
    public void goToFullscreenTrackPlayer() {
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
            Log.d("saved", "saved");

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
        EditText inputLine = new EditText(this);
        inputLine.setHint("Enter new playlist name");

        inputLine.setHintTextColor(getResources().getColor(R.color.cadet));

        inputLine.setPadding(70, 0, 0, 30);

        inputLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);  // Example: Set text size in sp

        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
        popupBuilder.setTitle("RENAMING PLAYLIST");
        popupBuilder.setMessage("Please enter the new name below.");
        popupBuilder.setView(inputLine); // Set the EditText as the view

        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {
            String newPlaylistName = inputLine.getText().toString().trim();

            if (!newPlaylistName.isEmpty()) {
                playlist.setName(newPlaylistName);
                FileManager.savePlayData(PlayData.playData);
                renderPlaylistData();
            } else {
                Toast errorPopup = new Toast(this);
                errorPopup.setText("EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        popupBuilder.setNegativeButton("CANCEL", null);

        AlertDialog popup = popupBuilder.create();

        // Set the background color for the popup (dialog)
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
    // Description: Starts playing quque (based on the status of shuffle).
    public void playQueue(View view) throws IOException {
        if (trackPlayer.isShuffle()) {
            trackPlayer.generateShuffleQueue(playlist);
        } else {
            trackPlayer.generateIndexQueue(playlist);
        }

        trackPlayer.startPlayingQueue(this);
    }

    // Parameters: (View) view.
    // Description: Button function to enable/disable queue looping.
    public void toggleLooping(View view) {
        trackPlayer.toggleQueueLooping();
        if (trackPlayer.isQueueLooping()) {
            loopToggleView.setBackgroundResource(R.drawable.looping_true);
        } else {
            loopToggleView.setBackgroundResource(R.drawable.looping_false);
        }
    }

    // Parameters: (View) view.
    // Description: Button function to enable/disable shuffle.
    public void toggleShuffle(View view) {
        trackPlayer.toggleShuffle();
        if (trackPlayer.isShuffle()) {
            shuffleToggleView.setBackgroundResource(R.drawable.shuffle_true);
        } else {
            shuffleToggleView.setBackgroundResource(R.drawable.shuffle_false);
        }
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
