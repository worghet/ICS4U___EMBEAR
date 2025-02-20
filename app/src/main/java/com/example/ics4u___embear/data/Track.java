// == FILE PACKAGE ===============
package com.example.ics4u___embear.data;

// == IMPORTS ==============================
import android.media.MediaMetadataRetriever;
import android.graphics.BitmapFactory;
import com.example.ics4u___embear.R;
import com.example.ics4u___embear.SharedObjects;

import android.media.MediaPlayer;
import android.graphics.Bitmap;
import android.content.Context;
import java.io.IOException;
import android.util.Log;
import android.net.Uri;

// == TRACK ========
public class Track {

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private final String trackName, artistName;
    private final int durationInMilliseconds;
    private final String filePath;

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public Track(String name, String filePath, Context context) {
        this.filePath = filePath;
        this.trackName = name;

        // Context is required to deal with Uris and metadata access.
        this.durationInMilliseconds = calculatePlaytime(context);
        this.artistName = getArtistFromMetadata(context);

    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDurationInMilliseconds() {
        return durationInMilliseconds;
    }

    // ==================================
    // == FUNCTIONALITY METHODS =========
    // ==================================

    // Parameters: (Context) context.
    // Description: Calculates the playtime of the track.
    private int calculatePlaytime(Context context) {

        // Using a new mediaplayer because we don't want to disturb the source for the trackplayer.
        MediaPlayer durationCalculator = MediaPlayer.create(context, Uri.parse(filePath));
        int calculatedDuration = durationCalculator.getDuration();

        // Close and return playtime.
        durationCalculator.release();
        return calculatedDuration;
    }

    // Parameters: (Context) context.
    // Description: Gets the icon metadata (if exists).
    public Bitmap getIconFromMetadata(Context context) {

        // Outside try to catch any falling exceptions.
        try {

            // Try with resources: Uses metadata retriever to get the audio file metadata.
            try (MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever()) {

                // Set the source.
                metadataRetriever.setDataSource(context, Uri.parse(filePath));

                // Get the data.
                byte[] imageIcon = metadataRetriever.getEmbeddedPicture();

                // Begin decryption process.

                Bitmap bitmap;

                // If there is metadata for an image.
                if (imageIcon != null) {
                    bitmap = BitmapFactory.decodeByteArray(imageIcon, 0, imageIcon.length);

                }

                // Otherwise, use the default custom one.
                else {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_track_icon);
                }

                // The metadata retriever closes automatically.
                return bitmap;
            }
        } catch (IOException ioException) {
            Log.d("METADATA RETRIEVER: ICON", "Something went wrong..");
        }

        // If anything goes wrong, just return the default.
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_track_icon);

    }

    // Parameters: (Context) context.
    // Description: Gets the artist metadata (if exists).
    private String getArtistFromMetadata (Context context){

        // Outside try to catch any falling exceptions.
        try {

            // Try with resources: Uses metadata retriever to get the audio file metadata.
            try (MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever()) {

                // Set data source.
                metadataRetriever.setDataSource(context, Uri.parse(filePath));

                // Get the data itself.
                String metadataArtist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

                // The metadata retriever closes automatically.

                // Return the data retrieved if it exists.
                if (metadataArtist != null) {
                    return metadataArtist;
                }

                // Otherwise, say unknown artist.
                else {
                    return "UNKNOWN ARTIST";
                }
            }
            catch (IOException ioException) {
                return "UNKNOWN ARTIST";
            }
        } catch (Exception e) {
            Log.d("METADATA RETRIEVER: ARTIST", "Something went wrong.");
        }

        return "UNKNOWN ARTIST";
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

        // Add extra cushioning for readability.
        if (seconds < 10) {
            formattedText += "0";
        }

        formattedText += seconds;

        return formattedText;

    }
}
