package com.example.ics4u___embear;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class TrackActivity extends AppCompatActivity {

    TextView audioNameBox, onOffBox, totalTimeBox, currentTimeBox;
    SeekBar durationSeekbar;
    TrackPlayer trackPlayer = SharedObjects.trackPlayer;
    Button togglePlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        audioNameBox = findViewById(R.id.audioName);
//        onOffBox = findViewById(R.id.playingStatus);
        totalTimeBox = findViewById(R.id.totalTime);
        currentTimeBox = findViewById(R.id.currentTime);
        durationSeekbar = findViewById(R.id.progressDuration);
        togglePlaying = findViewById(R.id.togglePlaying);

        // remove device ui
        try {
            renderAudioData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
////            audioPlayer.playSong(audio);
//            Log.d("AudioActivity", "started playing" + audio.getName());
//        } catch (Exception e) {
//            Log.d("AudioActivity", "couldnt play" + audio.getName());
//        }

    }
//
    private void renderAudioData() throws IOException {
        audioNameBox.setText(getIntent().getStringExtra("AUDIO_NAME"));
        totalTimeBox.setText(Track.formatMilliseconds(trackPlayer.getTrackPlaying().getLengthTime()));
        initializeSeekbar();
    }

    // how do i make this UNIVERSAL?
    public void initializeSeekbar() {
        durationSeekbar.setMin(0);
        durationSeekbar.setMax(trackPlayer.getTrackPlaying().getLengthTime());
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
        if (togglePlaying.getText().equals(">")) {
            togglePlaying.setText("| |");
        }
        else {
            togglePlaying.setText(">");
        }
//        onOffBox.setText("");


    }

    public void goBack(View view) {
        finish();
    }



}