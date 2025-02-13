package com.example.ics4u___embear.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.ics4u___embear.R;
import com.example.ics4u___embear.SharedObjects;
import com.example.ics4u___embear.TrackOverListener;
import com.example.ics4u___embear.TrackPlayer;
import com.example.ics4u___embear.data.Track;

import java.io.IOException;


public class TrackActivity extends AppCompatActivity implements TrackOverListener {

    TextView trackNameView, onOffBox, totalTimeBox, currentTimeBox;
    SeekBar durationSeekbar;
    TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    ImageButton togglePlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        clearSystemUI();
        trackNameView = findViewById(R.id.trackNameView);
        totalTimeBox = findViewById(R.id.totalTime);
        currentTimeBox = findViewById(R.id.currentTime);
        durationSeekbar = findViewById(R.id.progressDuration);
        togglePlaying = findViewById(R.id.togglePlaying);
        togglePlaying.setTag(R.drawable.pause_track);

        trackPlayer.addTrackOverListener(this); // TODO set listeners everywhere?


        // remove device ui
        try {
            renderAudioData();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    //
    private void renderAudioData() throws IOException {
        trackNameView.setText(trackPlayer.getTrackPlaying().getName());
        totalTimeBox.setText(Track.formatMilliseconds(trackPlayer.getTrackPlaying().getDuration()));
        // TODO REMOVE INTENT
        initializeSeekbar();
    }


    // TRACK PLAYER CONTROLS ---------------------------------------------


    // how do i make this UNIVERSAL?
    public void initializeSeekbar() {
        durationSeekbar.setMin(0);
        durationSeekbar.setMax(trackPlayer.getTrackPlaying().getDuration());
        durationSeekbar.setProgress(trackPlayer.getCurrentPosition());
        durationSeekbar.setKeyProgressIncrement(1);

        // Initialize the currentTimeBox with the current progress of the seekbar
        currentTimeBox.setText(Track.formatMilliseconds(durationSeekbar.getProgress()));

        // Create a Handler to update the SeekBar and currentTimeBox every second
        Handler handler = new Handler();
        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                // Get the current position of the audio
                int currentPosition = trackPlayer.getCurrentPosition();

                // Update the SeekBar and the currentTimeBox every second
                durationSeekbar.setProgress(currentPosition);
                currentTimeBox.setText(Track.formatMilliseconds(currentPosition));

                // Schedule the Runnable to run again in 1 second
                handler.postDelayed(this, 1000);
            }
        };

        // Start the periodic update when audio starts playing
        handler.post(updateRunnable);

        // Set the SeekBar listener to handle user interaction
        durationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update currentTimeBox when SeekBar progress changes
                currentTimeBox.setText(Track.formatMilliseconds(progress));

                // If the user is dragging the SeekBar, update the audio player's position
                if (fromUser) {
                    trackPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                // Optionally, you can stop periodic updates when the user is dragging
//                currentTimeBox.setText(Track.formatMilliseconds(durationSeekbar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optionally, resume the periodic updates after the user stops dragging
            }
        });
    }


    public void togglePlaying(View view) {
        trackPlayer.togglePlaying();
//        onOffBox.setText(audioPlayer.isPlaying());

        if ((Integer) togglePlaying.getTag() == R.drawable.pause_track) {
            // pauses
            togglePlaying.setImageResource(R.drawable.play_track);
            togglePlaying.setBackgroundResource(R.drawable.play_track);
            togglePlaying.setTag(R.drawable.play_track);
        }
        else {
            togglePlaying.setImageResource(R.drawable.pause_track);
            togglePlaying.setBackgroundResource(R.drawable.pause_track);
            togglePlaying.setTag(R.drawable.pause_track);

        }

//        if (togglePlaying.getimageTa() = R.drawable.pause_track) {
//            togglePlaying.setImage(R.drawable.play_track)
//        } else {
//            togglePlaying.setImage(R.drawable.pause_track)
//        }
//        onOffBox.setText("");


    }


    public void playNext(View view) throws IOException {
        trackPlayer.playNextInQueue(this);
        renderAudioData();
    }

    public void playPrevious(View view) throws IOException {
        trackPlayer.playPreviousInQueue(this);
        renderAudioData();
    }

    // GO BACK

    public void goBack(View view) {
        finish();
    }

    @Override
    public void updateTrackUI() throws IOException {
        renderAudioData();
    }
}