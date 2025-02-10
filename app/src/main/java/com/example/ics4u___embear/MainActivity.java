/*
[NAME] ----- BUSHKOV
[TASK] CODE REVIEW (ICS4U-01)
[DATE] 02 / 10 / 2025
[DESCRIPTION] Local audio organizer and player.
 */

// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS ==================================

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowCompat;

import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;

import java.io.File;


// == PLAYLISTS SCREEN ==============================
public class MainActivity extends AppCompatActivity {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    TextView textOutput;
    PlayData playData;

    // ==================================
    // == SCREEN BUILDER (ON_CREATE) ====
    // ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearSystemUI();

        // Check that file exists; read from it if it does.
        textOutput = findViewById(R.id.textOutput);

        // make file exist
        catchDataOnOpen();

        // initialize audioplayer here too

        renderPlaylistButtons();

    }

    // ==================================
    // == UI HELPER METHODS =============
    // ==================================

    // TODO: CHANGE COMPOSITION OF BUTTONS (INCLUDES REMOVE, NAME, ETC).
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

            // Create a button dedicated to this playlist.
            Button playlistButton = new Button(this);

            // Set the button text as the playlist name.
            playlistButton.setText(playlist.getName());

            int carryablePlaylistIndex = playlistIndex;
            playlistButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("PLAYLIST_INDEX", carryablePlaylistIndex);
//                intent.putExtra("PLAYLIST")
                startActivity(intent);
//                overridePendingTransition(R.anim., R.anim.fade_in);
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

    // Parameters: None.
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
        // TODO: MAKE UNCLICKABLE FOR CERTAIN CONDITIONS?
        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // Read what is in the entered line.
             String newPlaylistName = inputLine.getText().toString().trim(); // trim gets rid of trailing and leading spaces

            // ERROR CHECKING ----

            // Check if String entered is empty.
            if (!newPlaylistName.isEmpty()) {

                // TODO CHECK THAT THE PLAYLIST NAME DOESNT ALREADY EXIST

                // Add the new playlist to playData.
                playData.addPlaylist(new Playlist(newPlaylistName));

                File file = getFileStreamPath(FileManager.PLAYDATA_FILE_NAME);

                FileManager.savePlayData(playData);

                renderPlaylistButtons();


               // FileManager.savePlayData(this, playData);
                // SAVE PLAYDATA
//                File file = getFileStreamPath(FileManager.PLAYDATA_FILE); // file in the app context storage dir (ONLY INSTANFCE OF CONTEXT NEED FOR FILE)


            } else {
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


    // Parameters: None.
    // Description: Load playData from file (if exists). Otherwise, make it empty.
    public void catchDataOnOpen() {

        // Get file using context of Activity; save it in the FileManager class.
        FileManager.PLAYDATA_FILE = getFileStreamPath(FileManager.PLAYDATA_FILE_NAME);

        // Check that the file exists; if it does, load data.
        if (FileManager.playDataFileExists()) { playData = FileManager.loadPlayData(); }

        // If the file does not exist; assign empty one.
        else { playData = new PlayData(); }

        // Set local playData to universal one.
        // TODO access universal always
        PlayData.playData = playData;
    }
}