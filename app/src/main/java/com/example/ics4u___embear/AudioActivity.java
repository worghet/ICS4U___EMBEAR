package com.example.ics4u___embear;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    TextView audioNameBox, onOffBox;
    AudioPlayer audioPlayer;
    Audio audio;
    
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
        audio = PlayData.getPlaylist(getIntent().getIntExtra("PLAYLIST_INDEX", 0)).getAudioList().get(getIntent().getIntExtra("AUDIO_INDEX", 0));
        audioNameBox.setText(audio.getName());
        audioPlayer = new AudioPlayer();
    }

    public void togglePlaying(View view) throws IOException {
//        audioPlayer.togglePlaying();
//        onOffBox.setText(audioPlayer.isPlaying());

        onOffBox.setText("PLAYING!");


    }

    public void goBack(View view) {
        finish();
    }



}