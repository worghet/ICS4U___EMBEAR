package com.example.ics4u___embear;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// gets all the data needed
public class FileManager {

    // fields
    static FileManager fileManager = new FileManager();


    private String playdataFileContents;
    private Gson gson = new Gson();


    public static final String PLAYDATA_FILE = "playdata.txt";





    private FileManager() {}

    public static FileManager getFileManager() {
        return fileManager;
    }

//    public String getSerializedPlayData() {
//        Log.d("FileManager", "starting to getSerialized");
//        return gson.toJson(PlayData.getPlayData());
//    }

    public void deserializePlayData(View view) {
        // playdata set to (read file).deserialize
    }


    public boolean fileEmpty() throws FileNotFoundException {
        if (getFileContents().isEmpty()) {
            return true;
        }
        return false;
    }

    public String getFileContents() throws FileNotFoundException {

        Scanner fileReader = new Scanner(PLAYDATA_FILE);
        String contents = "";

        /*
        READ EMPTY (ONCREATE), SAVE SOMETHING IN IT FIRST (BUTTON), THEN READ IT
         */
        while (fileReader.hasNextLine()) {
            contents += fileReader.nextLine();
        }

        fileReader.close();
        return contents;
    }

    public void writeToFile(String dataToWrite) throws IOException {
        FileWriter fileWriter = new FileWriter(PLAYDATA_FILE);
        fileWriter.write(dataToWrite);
//         should close?
        fileWriter.close();
    }


    public void accessExternalStorage() {

    }

    public static void savePlayData(Activity activity, PlayData playData) {
        File file = activity.getFileStreamPath(PLAYDATA_FILE);
        // save to json file
    }

}
