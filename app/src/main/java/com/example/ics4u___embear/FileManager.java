// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS ==============
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import android.util.Log;
import java.io.File;

// == FILE MANAGER =======
public class FileManager {

    // ==================================
    // == CLASS VARIABLES [STATIC] ======
    // ==================================

    public static final String PLAYDATA_FILE_NAME = "playdata.txt";
    static File PLAYDATA_FILE;
    static Gson gson = new Gson();

    // ==================================
    // == UTILITY METHODS ===============
    // ==================================

    // Parameters: None | Uses access to static File.
    // Description: Check if the given file exists.
    public static boolean playDataFileExists() {
        return PLAYDATA_FILE.exists();
    }

    // Parameters: (PlayData) updatedPlayData.
    // Description: Overwrites the current save with a serialized updatedPlayData.
    public static void savePlayData(PlayData updatedPlayData) {

        // Catch any dropped exceptions.
        try {

            // Try with resources; write into file.
            try (FileWriter writer = new FileWriter(PLAYDATA_FILE)) {

                // write + toJson(Object object) methods.
                // --------------------------------------
                // 1. Serialize the object into json.
                // 2. Write it (overwrite) into file.
                writer.write(gson.toJson(updatedPlayData));

            }
        }

        // If anything goes wrong; log the error.
        catch (Exception e) {
            Log.d("ERROR", e.toString());
        }
    }

    // Parameters: None | Uses access to static File.
    // Description: Get all data from the file and return a data-filled PlayData object.
    public static PlayData loadPlayData() {

        // Catch any dropped exceptions.
        try {

            // Try with resources; read from the file.
            try (FileReader reader = new FileReader(PLAYDATA_FILE)) {

                // fromJson(FileReader (To read file), Class (Object Blueprints)) method.
                // -------------------------------------------------------------
                // 1. Read file (if syntax is JSON, it's fine as a text file).
                // 2. Parse and build a PlayData object.
                return gson.fromJson(reader, PlayData.class);

            }

        }

        // If anything goes wrong; just make a new PlayData.
        catch (Exception e) {
            return new PlayData();
        }

    }

}
