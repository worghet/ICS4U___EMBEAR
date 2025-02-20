// == FILE PACKAGE =========================
package com.example.ics4u___embear.activity;

// == IMPORTS ======================================
import com.example.ics4u___embear.TrackOverListener;
import com.example.ics4u___embear.SharedObjects;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ics4u___embear.TrackPlayer;
import com.example.ics4u___embear.data.Track;
import androidx.core.view.WindowCompat;
import com.example.ics4u___embear.R;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;
import java.io.IOException;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

// == PLAYLIST SCREEN =============================================================
public class TrackActivity extends AppCompatActivity implements TrackOverListener {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    TextView trackNameView, totalTimeBox, currentTimeBox;
    TextView trackArtistNameView;
    ImageView trackIconView;
    SeekBar durationSeekbar;
    TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    ImageButton togglePlaying;

    // ==================================
    // == SCREEN BUILDER (ON_CREATE) ====
    // ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        clearSystemUI();

        // Initialize the modifiable views.
        trackNameView = findViewById(R.id.trackNameView);
        trackArtistNameView = findViewById(R.id.artistNameView);
        trackIconView = findViewById(R.id.trackIconView);
        totalTimeBox = findViewById(R.id.totalTime);
        currentTimeBox = findViewById(R.id.currentTime);
        durationSeekbar = findViewById(R.id.progressDuration);
        togglePlaying = findViewById(R.id.togglePlaying);
        togglePlaying.setTag(R.drawable.pause_track);

        // Register this activity as a 'track-ends' listener.
        trackPlayer.addTrackOverListener(this);

        // Display Track data.
        try { renderTrackData(); }
        catch (IOException e) { throw new RuntimeException(e); }

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
    // Description: Sets the text views to the data of the currently playing track.
    private void renderTrackData() throws IOException {

        // Set the track data (Name, Artist, Duration, Icon).
        trackNameView.setText(trackPlayer.getTrackPlaying().getTrackName());
        trackArtistNameView.setText(trackPlayer.getTrackPlaying().getArtistName());
        totalTimeBox.setText(Track.formatMilliseconds(trackPlayer.getTrackPlaying().getDurationInMilliseconds()));
        trackIconView.setImageBitmap(trackPlayer.getTrackPlaying().getIconFromMetadata(this));

        // Check if the track is playing. Some of the cases this is needed for include:
        // 1. The user skipped while paused,
        // 2, Or they entered the activity when the song is paused.
        if (!trackPlayer.isPlaying()) {
            togglePlaying.setImageResource(R.drawable.play_track);
            togglePlaying.setBackgroundResource(R.drawable.play_track);
        }
        else {
            togglePlaying.setImageResource(R.drawable.pause_track);
            togglePlaying.setBackgroundResource(R.drawable.pause_track);
        }

        // Get the bar to actually.. work.
        initializeSeekbar();
    }

    // Parameters: (View) view.
    // Description: closes the activity; goes back to the playlist menu.
    public void goBack(View view) {
        finish();
    }

    // ==================================
    // == TRACK CONTROL METHODS =========
    // ==================================

    // Parameters: None.
    // Description: Sets all the seekbar values and functions.
    public void initializeSeekbar() {

        // Set basic things: minimum, maximum values, increments, and current progress.
        durationSeekbar.setMin(0);
        durationSeekbar.setMax(trackPlayer.getTrackPlaying().getDurationInMilliseconds());
        durationSeekbar.setKeyProgressIncrement(1);
        durationSeekbar.setProgress(trackPlayer.getCurrentPosition());

        // Initialize the currentTimeBox with the current progress of the seekbar.
        currentTimeBox.setText(Track.formatMilliseconds(durationSeekbar.getProgress()));

        // Create a Handler to update the SeekBar and currentTimeBox every second.
        Handler handler = new Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {

                // Get the current position of the audio.
                int currentPosition = trackPlayer.getCurrentPosition();

                // Update the bar and text every second.
                durationSeekbar.setProgress(currentPosition);
                currentTimeBox.setText(Track.formatMilliseconds(currentPosition));

                // Schedule the runnable object to run again in 1 second.
                handler.postDelayed(this, 1000);
            }
        };

        // Start the "periodic" update when audio starts playing.
        handler.post(updateRunnable);

        // Set the listener to handle user interaction.
        durationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Update current time when the bar progress changes.
                currentTimeBox.setText(Track.formatMilliseconds(progress));

                // If the user is dragging the bar, update the audio's position.
                if (fromUser) {
                    trackPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Parameters: (View) Object which called this method.
    // Description: Toggles the playing of the track.
    public void togglePlaying(View view) {

        // Perform the toggle. (Might want to rename this method..)
        trackPlayer.togglePlaying();

        // Change the icon to pause if playing, playing if pause.
        if (!trackPlayer.isPlaying()) {
            togglePlaying.setImageResource(R.drawable.play_track);
            togglePlaying.setBackgroundResource(R.drawable.play_track);
        }
        else {
            togglePlaying.setImageResource(R.drawable.pause_track);
            togglePlaying.setBackgroundResource(R.drawable.pause_track);
        }
    }

    // Parameters: (View) Object which called this method.
    // Description: Plays the next track in the queue.
    public void playNext(View view) throws IOException {
        trackPlayer.playNextInQueue(this);
        renderTrackData();
    }

    // Parameters: (View) Object which called this method.
    // Description: Plays the previous track in the queue.
    public void playPrevious(View view) throws IOException {
        trackPlayer.playPreviousInQueue(this);
        renderTrackData();
    }

    // ==================================
    // == BACKGROUND UPDATER METHODS ====
    // ==================================

    // Parameters: None.
    /** EXPLANATION:
     When an activity is opened, it sets the current activity in to a paused state.
     This method performs an action when it is unpaused (resumed); when the screen is returned to.
     */
    @Override
    public void updateTrackUI() throws IOException {
        renderTrackData();
    }
}