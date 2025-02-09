package com.example.ics4u___embear;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    TextView audioNameBox, onOffBox, totalTimeBox, durationBox;
    AudioPlayer audioPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
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
        audioNameBox = findViewById(R.id.audioName);
        onOffBox = findViewById(R.id.playingStatus);
        totalTimeBox = findViewById(R.id.totalTime);
        audioPlayer = AudioPlayer.getAudioPlayer();
        audioNameBox.setText(getIntent().getStringExtra("AUDIO_NAME"));
        totalTimeBox.setText(Audio.millisecondsToSeconds(getIntent().getLongExtra("AUDIO_LENGTH", 0)));
    }

    public void togglePlaying(View view) throws IOException {
        audioPlayer.togglePlaying();
//        onOffBox.setText(audioPlayer.isPlaying());

        onOffBox.setText("TOGGLED!");


    }

    public void goBack(View view) {
        finish();
    }



}