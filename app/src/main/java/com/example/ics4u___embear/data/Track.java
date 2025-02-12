// == FILE LOCATION ===============
package com.example.ics4u___embear.data;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/** TRACK CLASS:
 * This is a data object responsible for track (audio) data.
 */
public class Track {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    // Track details.
    private String name, artist;
    private int duration; // Duration in milliseconds.
    private String filePath;

//    private Uri fileUri;

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public Track(String name, String filePath, Context context) {
        this.filePath = filePath;
        this.name = name.substring(0, name.indexOf('.'));
        this.duration = calculatePlaytime(context);


        // TODO
        // getArtist --> if none; UNKNOWN ARTIST
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDuration() {
        return duration;
    }

    // ==================================
    // == OTHER USEFUL METHODS ==========
    // ==================================

    public int calculatePlaytime(Context context) {

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
        int calculatedDuration = mediaPlayer.getDuration();
        mediaPlayer.release();
        return calculatedDuration;

    }

    // Parameters: (long) milliseconds.
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
            // mm:ss
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
