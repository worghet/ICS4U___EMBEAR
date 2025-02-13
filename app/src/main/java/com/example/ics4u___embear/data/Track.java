// == FILE PACKAGE ===============
package com.example.ics4u___embear.data;

// == IMPORTS ===================
import android.media.MediaPlayer;
import android.content.Context;
import android.net.Uri;

// == TRACK ========
public class Track {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private String name;
    private int durationInMilliseconds; // Duration in milliseconds.
    private String filePath;

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public Track(String name, String filePath, Context context) {
        this.filePath = filePath;
        this.name = name.substring(0, name.indexOf('.'));
        this.durationInMilliseconds = calculatePlaytime(context);
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDurationInMilliseconds() {
        return durationInMilliseconds;
    }

    // ==================================
    // == FUNCTIONALITY METHODS ==========
    // ==================================

    // Parameters: (Context) context.
    // Description: Calculates the playtime of the track.
    private int calculatePlaytime(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
        int calculatedDuration = mediaPlayer.getDuration();
        mediaPlayer.release();
        return calculatedDuration;
    }

    // Parameters: (int) milliseconds.
    // Description: Returns a formatted string MM:SS.
    static public String formatMilliseconds(int milliseconds) {
        int minutes, seconds, hours;
        String formattedText = "";

        seconds = milliseconds / 1000;

        if (seconds > 60) {
            minutes = seconds / 60;
            seconds %= 60;

            if (minutes > 60) {
                hours = minutes / 60;
                minutes %= 60;

                formattedText += hours + ":";
                if (minutes < 10) {
                    formattedText += "0";
                }

                formattedText += minutes + ":";


            }
            else {
                formattedText += minutes + ":";
            }
        }
        else {
            formattedText += "0:";
        }
        if (seconds < 10) {
            formattedText += "0";
        }
        formattedText += seconds;

        return formattedText;



    }
}
