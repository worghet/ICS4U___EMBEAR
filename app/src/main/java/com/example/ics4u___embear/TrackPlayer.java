// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS ===================

import android.media.MediaPlayer;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.net.Uri;
import android.util.Log;

import com.example.ics4u___embear.data.Playlist;
import com.example.ics4u___embear.data.Track;

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
    private int currentTrackIndex;
    private boolean isShuffle;
    private Playlist playingFrom;
    private Track[] queue;

    private ArrayList<TrackOverListener> allTrackOverListeners = new ArrayList<>();

    // ==================================
    // == CONSTRUCTORS ==================
    // ==================================

    public TrackPlayer() {
        super();
//        this.setOnCompletionListener(mp -> onTrackFinished());
        timeProgress = 0;
        queueLooping = true;
//        toggleQueueLooping();
//        setLooping(true); SONG LOOPING
    }

    public void addTrackOverListener(TrackOverListener trackOverListener) {
        allTrackOverListeners.add(trackOverListener);
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
    // Description: Plays the specified track (context used for loading the audio itself).
    public void playTrack(Context context, Track track) throws IOException {

        // Stop playing any music that is on already.
        if (isPlaying()) {
            togglePlaying();
        }

        // Clear / Reset the player.
        reset();

        // Set the data source for the given track.
        setDataSource(context, Uri.parse(track.getFilePath()));

        // Update object variable.
        trackPlaying = track;

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

    public void startPlayingQueue(Context context) throws IOException {
      currentTrackIndex = -1;
      playNextInQueue(context);
    }

    public void playNextInQueue(Context context) throws IOException {
        if ((queue.length - 1) == currentTrackIndex) {
            Log.d("LOOPER", "checking looping status!");
            if (queueLooping) {
                Log.d("LOOPER", "trying to loop!");
                if (isShuffle) {
                    generateShuffleQueue(playingFrom);
                }
                startPlayingQueue(context);
            }

        }
        else {
            currentTrackIndex++;
            playTrack(context, queue[currentTrackIndex]);

            this.setOnCompletionListener(mp -> {
                try {
                    playNextInQueue(context);
                    for (TrackOverListener trackOverListener : allTrackOverListeners) {

                        trackOverListener.updateTrackUI();
                    }
                    Log.d("UPDATING TRACK UI NATURALLY", "tapping it now..");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    public void playPreviousInQueue(Context context) throws IOException {
        if (currentTrackIndex > 0) {
            currentTrackIndex--;
            playTrack(context, queue[currentTrackIndex]);

            this.setOnCompletionListener(mp -> {
                try {
                    playNextInQueue(context);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    // Parameters: (Playlist) playlist to generate from.
    // Description: Sets the queue to a by-index list
    public void generateIndexQueue(Playlist playlist) {
        queue = new Track[playlist.getNumTracks()];
        String logStr = "";
        for (int trackIndex = 0; trackIndex < playlist.getNumTracks(); trackIndex++) {
            queue[trackIndex] = playlist.getAllTracks().get(trackIndex);
            logStr += playlist.getAllTracks().get(trackIndex).getName() + ", ";
        }

        playingFrom = playlist;
        isShuffle = false;
        Log.d("QUEUE GEN", logStr);
    }

    // Parameters: (Playlist) playlist to generate from.
    // Description: Sets the queue to a randomized list.
    public void generateShuffleQueue(Playlist playlist) {
        queue = new Track[playlist.getNumTracks()];
        Random random = new Random();

        for (int queueIndex = 0; queueIndex < playlist.getNumTracks(); queueIndex++) {

            while (true) {
                Track randomTrack = playlist.getAllTracks().get(random.nextInt(playlist.getNumTracks()));
                if (!Arrays.asList(queue).contains(randomTrack)) {
                    queue[queueIndex] = randomTrack;
                    break;
                }
            }
        }
        playingFrom = playlist;
        isShuffle = true;

    }

    // Parameters: None.
    // Description: Will set / stop looping playlist.
    public void toggleQueueLooping() {
        queueLooping = !queueLooping;
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

}

