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
import java.io.IOException;
import android.view.View;
import android.os.Bundle;
import android.util.Log;

// == PLAYLISTS SCREEN ==============================
public class MainActivity extends AppCompatActivity {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    TextView jsonOutput;
    PlayData playData;

    // ==================================
    // == SCREEN BUILDER (ON_CREATE) ====
    // ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default layout creation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        clearSystemUI();

        // Check that file exists; read from it if it does.
        jsonOutput = findViewById(R.id.textOutput);

        // make file exist
        Log.d("checking contents", FileManager.getFileManager().getPlayDataFileContents());

        // initialize audioplayer here too
        playData = PlayData.getPlayData();
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
        for (int playlistIndex = 0; playlistIndex < PlayData.getNumPlaylists(); playlistIndex++) {

            // For convenience: copy the playlist here.
            Playlist playlist = PlayData.getPlaylist(playlistIndex);

            // Create a button dedicated to this playlist.
            Button playlistButton = new Button(this);

            // Set the button text as the playlist name.
            playlistButton.setText(playlist.getName());

            int carryablePlaylistIndex = playlistIndex;
            playlistButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, PlaylistActivity.class);
                intent.putExtra("PLAYLIST_INDEX", carryablePlaylistIndex);
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
//        inputLine.setBackgroundColor(); // can use images as background. ex Color.BLACK
        // setTextSize,

        // alert dialogue is the actual UI popup
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this); // again, to create in this activity

        popupBuilder.setTitle("NEW PLAYLIST CREATION");
        popupBuilder.setMessage("PLEASE ENTER THE DESIRED NAME:");
        popupBuilder.setView(inputLine);

        // ------------------------------------|
        // 2. Setup "CANCEL" and "ADD" Buttons |
        // ------------------------------------|

        popupBuilder.setPositiveButton("ADD", ((dialog, which) -> {

            // collect new name
            String newPlaylistName = inputLine.getText().toString().trim(); // trim gets rid of trailing and leading spaces
            // can do error checking here: not empty, not already in playdata

            if (!newPlaylistName.isEmpty()) {
                // add to playdata and reload
                PlayData.addPlaylist(new Playlist(newPlaylistName));
                renderPlaylistButtons();
                try {
                    Log.d("FileManager", "trying to update contents");
                    FileManager.getFileManager().updatePlayDataFileContents();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

    // TEMPORARY, WILL REMOVE LATER
    public void showFileContents(View view) {
        jsonOutput.setText(FileManager.getFileManager().getPlayDataFileContents());
    }
}