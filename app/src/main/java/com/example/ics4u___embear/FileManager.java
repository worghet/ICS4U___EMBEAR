// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS =======================

import java.io.FileNotFoundException;

import com.google.gson.Gson;

import android.app.Activity;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import android.content.Context;
import android.view.View;

import java.util.Scanner;
import java.io.File;

// == FILE MANAGER =======
public class FileManager {

    public static final String PLAYDATA_FILE = "playdata.txt";

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private String playdataFileContents;
    static Gson gson = new Gson();
    // private File PLAYDATA_FILE?

    // ==================================
    // == UTILITY METHODS ===============
    // ==================================

    public static void savePlayData(Context context, PlayData newPlaydata) {

        // Get file using context.
        File file = context.getFileStreamPath(FileManager.PLAYDATA_FILE); // file in the app context storage dir (ONLY INSTANFCE OF CONTEXT NEED FOR FILE)

        // Catch any dropped exceptions.
        try {

            // Try with resources; write into the file.
            try (FileWriter writer = new FileWriter(file)) {

                // Get serialized playData object.
                String serializedPlayData = gson.toJson(newPlaydata);

                // Overwrite anything inside the file with the new serialized data.
                writer.write(serializedPlayData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Parameters: Context | Where to grab the file from.
    // Description: Get all data from the file and return a data-filled PlayData object.
    public static PlayData loadPlayData(Context context) {

        // Get file using context.
        File file = context.getFileStreamPath(FileManager.PLAYDATA_FILE); // file in the app context storage dir (ONLY INSTANFCE OF CONTEXT NEED FOR FILE)

        // Catch any dropped exceptions.
        try {

            // Try with resources; read from the file.
            try (FileReader reader = new FileReader(file)) {

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

    // Parameters: File | File which to check exists.
    // Description: Check if the given file exists.
    public static boolean fileExists(File file) {
        return (file == null || !file.exists());
    }
}
