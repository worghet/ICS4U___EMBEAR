// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS ===================

import android.media.MediaPlayer;
import android.content.Context;

import java.io.IOException;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

/**
 * TRACKPLAYER CLASS:
 * This is essentially the audio player of the project.
 * It manages everything relating to the actual playing: queues, sound, etc.
 */
public class TrackPlayer extends MediaPlayer { // extends MediaPlayer..?

    // ==================================
    // == CLASS CONSTANTS (UTILITY) =====
    // ==================================

    // Constants used in traversing queue.
    final public int NEXT = 0;
    final public int PREV = 1;

    // ==================================
    // == CLASS VARIABLES [FIELDS] =====
    // ==================================

    private Track trackPlaying;
    private int timeProgress;
    private boolean queueLooping;

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public TrackPlayer() {
        super();
        timeProgress = 0;
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public Track getTrackPlaying() {
        return trackPlaying;
    }

    // ==================================
    // == MANIPULATOR METHODS ===========
    // ==================================

    public void setTrackPlaying(Track trackPlaying) {
        this.trackPlaying = trackPlaying;
    }

    public int getTimeProgress() {
        return timeProgress;
    }

    public void setTimeProgress(int timeProgress) {
        this.timeProgress = timeProgress;
    }


    // ==================================
    // == PSEUDO MEDIA PLAYER METHODS ===
    // ==================================

    // -- AUDIO PLAYING ------------------------------------------------------------------

    // Parameters: (Context) context of the activity | (Track) which track to play.
    // Description: Plays the specified track (context used for loading the audio itself.
    public void playAudio(Context context, Track audio) throws IOException {

        // Stop playing any music that is on already.
        if (isPlaying()) {
            togglePlaying();
        }

        // Clear / Reset the player.
        reset();

        // Set the data source for the given audio.
        setDataSource(context, Uri.parse(audio.getFilePath()));

        // Update object variable.
        trackPlaying = audio;

        // Prepare the player, then start it.
        prepare();
        start();
    }

    // Parameters: None.
    // Description: Pauses/Unpauses the track.
    public void togglePlaying() {

        // If a track is being played.
        if (isPlaying()) {

            // Save the current position to be used later.
            timeProgress = getCurrentPosition();

            // Stop playing audio.
            pause();

        }

        // If track has been paused.
        else {

            // Start playing audio.
            start();


            // Set the progress to where it was paused.
            seekTo(timeProgress);


        }
    }

    // -- QUEUE MANAGEMENT -------------------------------------------------------------

    // Parameters: None.
    // Description: Sets the queue to a by-index list
    public void generateIndexQueue() {

    }

    // Parameters: None.
    // Description: Sets the queue to a randomized list.
    public void generateShuffleQueue() {
        // while true; (get random track) if randTrack is in newQueue, ignore, else, add; if lengthAllList and newQueue is same, quit.
    }

    // Parameters: None.
    // Description: Sets the given track next in queue. MIGHT REMOVE
    public void setNextInQueue(Track track) {
        //
    }

    // Parameters: None.
    // Description: Will set / stop looping playlist.
    public void toggleLooping() {

    }

    // OTHER METHODS --------------------------------------------------------

//    public void initializeSeekbar(SeekBar durationSeekbar) {
//        durationSeekbar.setMin(0);
//        durationSeekbar.setMax(getTrackPlaying().getLengthTime());
//        durationSeekbar.setProgress(getCurrentPosition());
//        durationSeekbar.setKeyProgressIncrement(1);
//
//        // Initialize the currentTimeBox with the current progress of the seekbar
////        currentTimeBox.setText(Track.formatMilliseconds(durationSeekbar.getProgress()));
//
//        // Create a Handler to update the SeekBar and currentTimeBox every second
//        Handler handler = new Handler();
//        Runnable updateRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // Get the current position of the audio
//                int currentPosition = getCurrentPosition();
//
//                // Update the SeekBar and the currentTimeBox every second
//                durationSeekbar.setProgress(currentPosition);
////                currentTimeBox.setText(Track.formatMilliseconds(currentPosition));
//
//                // Schedule the Runnable to run again in 1 second
//                handler.postDelayed(this, 1000);
//            }
//        };
//
//        // Start the periodic update when audio starts playing
//        handler.post(updateRunnable);
//
//        // Set the SeekBar listener to handle user interaction
//        durationSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // Update currentTimeBox when SeekBar progress changes
////                currentTimeBox.setText(Track.formatMilliseconds(progress));
//
//                // If the user is dragging the SeekBar, update the audio player's position
//                if (fromUser) {
//                    seekTo(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                // Optionally, you can stop periodic updates when the user is dragging
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // Optionally, resume the periodic updates after the user stops dragging
//            }
//        });
//    }

//    @Override
//    public void setOnCompletionListener(OnCompletionListener listener) {
//        super.setOnCompletionListener(listener);
//
//        Log.d("TRACKPLAYER", "FINISHED PLAYING" + trackPlaying.getName());
//
//    }
}
