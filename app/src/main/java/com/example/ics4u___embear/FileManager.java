package com.example.ics4u___embear;

import android.util.Log;
import android.view.View;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// gets all the data needed
public class FileManager {

    // fields
    static FileManager fileManager = new FileManager();
//    BufferedReader bufferedReader = new BufferedReader(fileInputStream);
    static String PLAYDATA_FILE_PATH = "playdata.txt";
    private String jsonString;
    private Gson gson = new Gson();

    private FileManager() {
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    public String getSerializedPlayData() {
        Log.d("FileManager", "starting to getSerialized");
        jsonString = gson.toJson(PlayData.getPlayData());
        return jsonString;
    }

    public void deserializePlayData(View view) {
        // playdata set to (read file).deserialize
    }

    // use buffered reader too?
    public String getPlayDataFileContents() {
        // Initialize the reading resources.
        Scanner fileReader = new Scanner(PLAYDATA_FILE_PATH);
        String fileContents = "";

        // Read the file.
        while (fileReader.hasNextLine()) {
            fileContents += fileReader.nextLine();
        }

        // Close reader.
        fileReader.close(); // should use try finally or try with resource
        return fileContents;
    }

    public void updatePlayDataFileContents() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PLAYDATA_FILE_PATH));
        bufferedWriter.write(getSerializedPlayData());
        bufferedWriter.flush();
        bufferedWriter.close();

    }


    public void accessExternalStorage() {

    }

}
