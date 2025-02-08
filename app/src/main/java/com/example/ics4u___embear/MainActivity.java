package com.example.ics4u___embear;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
//
    AudioPlayer audioPlayer;
    FileManager fileManager;
    TextView jsonOutput; // CANNOT INITIALIZE BEFORE ONCREATE!!!!!!!
    PlayData playData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearSystemUI();
        setContentView(R.layout.activity_main);

        // initialize audioplayer here too
        playData = PlayData.getPlayData();
        renderPlaylistButtons();

    }

    public void renderPlaylistButtons() {
        // Reference to the LinearLayout inside ScrollView
        LinearLayout playlistContainer = findViewById(R.id.playlistContainerLayout);

        // Clear existing views in the container (if any)
        playlistContainer.removeAllViews();

        // Iterate through each playlist in PlayData
        for (int playlistIndex = 0; playlistIndex < PlayData.getNumPlaylists(); playlistIndex++) {
            Playlist playlist = PlayData.getPlaylist(playlistIndex);
            Button button = new Button(this);
            button.setText(playlist.getName());  // Set the button text as the playlist name

            int carryablePlaylistIndex = playlistIndex;
            button.setOnClickListener(view -> {
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("PLAYLIST_INDEX", carryablePlaylistIndex);
                startActivity(intent);
//                overridePendingTransition(R.anim., R.anim.fade_in);
            });

            // Add the button to the container layout
            playlistContainer.addView(button);
        }
    }

    public void addPlaylistToPlayData(View view) {

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

        popupBuilder.setTitle("NEW PLAYLIST CREATION");
        popupBuilder.setMessage("PLEASE ENTER THE DESIRED NAME:");
        popupBuilder.setView(inputLine);

        // add buttons and what to do when theyre pressed


        // TODO DISABLE BUTTON UNTIL POSSIBLE (USE LISTENER)
        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // collect new name
            String newPlaylistName = inputLine.getText().toString().trim(); // trim gets rid of trailing and leading spaces
            // can do error checking here: not empty, not already in playdata

            if (!newPlaylistName.isEmpty()) {
                // add to playdata and reload
                PlayData.addPlaylist(new Playlist(newPlaylistName));
                renderPlaylistButtons();
            }
            else {
                Toast errorPopup = new Toast(this);
                errorPopup.setText("FAILED TO CREATE: EMPTY NAME GIVEN");
                errorPopup.show();
            }
        }));

        // when cancel is pressed
        popupBuilder.setNegativeButton("CANCEL", null);


        AlertDialog popup = popupBuilder.create();

//        LayoutInflater inflater = getLayoutInflater();
//View customView = inflater.inflate(R.layout.custom_dialog_layout, null);
//requestPopup.setView(customView);

        // show it (theres a method in builder too which just creates in it
        popup.show();

        // modifications to popup must occur after it is shown
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.BLACK);
//        popup.getButton(AlertDialog.BUTTON_NEGATIVE).setRa
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));  // Example with a custom color (hex code)
    }

    public void serializePlayData(View view) {

        jsonOutput = findViewById(R.id.jsonOutput);

        try {
            String jsonString = new Gson().toJson(playData);
            jsonOutput.setText(jsonString);

        } catch (Exception e) {
            jsonOutput.setText("Something went wrong.");
        }


//        Button button = findViewById()
//        playData.
    }

    public void deserializePlayData(View view) {
        jsonOutput.setText("This would be the deserialized object..");
    }

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

}