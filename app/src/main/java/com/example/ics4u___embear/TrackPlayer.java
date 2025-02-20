// == FILE LOCATION ===============
package com.example.ics4u___embear;

// == IMPORTS ==================================
import com.example.ics4u___embear.data.Playlist;
import com.example.ics4u___embear.data.Track;
import android.media.MediaPlayer;
import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import android.net.Uri;

// == TRACKPLAYER ============================
public class TrackPlayer extends MediaPlayer {

    // ==================================
    // == CLASS VARIABLES [FIELDS] ======
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
        super(); // Media player capabilities.
        timeProgress = 0;
        queueLooping = false;
        isShuffle = false;
    }

    // For interface implementation.
    public void addTrackOverListener(TrackOverListener trackOverListener) {
        allTrackOverListeners.add(trackOverListener);
    }

    // ==================================
    // == GETTER METHODS ================
    // ==================================

    public Track getTrackPlaying() {
        return trackPlaying;
    }

    public boolean isQueueLooping() {
        return queueLooping;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    // ==================================
    // == PSEUDO MEDIA PLAYER METHODS ===
    // ==================================

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

    // ==================================
    // == QUEUE MANAGEMENT ==============
    // ==================================

    // Parameters: (Context) context.
    // Description: Starts playing the queue from the beginning.
    public void startPlayingQueue(Context context) throws IOException {
      currentTrackIndex = -1;
      playNextInQueue(context);
    }

    // Parameters: (Context) context.
    // Description: Plays the next track in the queue.
    public void playNextInQueue(Context context) throws IOException {

        // Make sure that this method is not called when a queue hasn't been initialized yet.
        if (queue != null) {

            // If it's the last track in the queue.
            if ((queue.length - 1) == currentTrackIndex) {

                // Loop the queue if desired.
                if (queueLooping) {

                    // Re-shuffle queue to avoid repetitiveness.
                    if (isShuffle) {
                        generateShuffleQueue(playingFrom);
                    }

                    // Restart queue playing from starting index.
                    startPlayingQueue(context);
                }

            }

            // If the track isn't the last one.
            else {

                // Play the next track.
                currentTrackIndex++;
                playTrack(context, queue[currentTrackIndex]);

                // Upon completion of the track, start playing the next in queue, and update the ui.
                this.setOnCompletionListener(mp -> {
                    try {
                        // Play next in queue.
                        playNextInQueue(context);

                        // Update UI in every activity (that is, every one that implements the listener, which is all).
                        for (TrackOverListener trackOverListener : allTrackOverListeners) {
                            trackOverListener.updateTrackUI();
                        }
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    // Parameters: (Context) context.
    // Description: Plays the previous track in the queue.
    public void playPreviousInQueue(Context context) throws IOException {

        // Make sure that this method is not called when a queue hasn't been initialized yet.
        if (queue != null) {

            // Check that the current track isn't the first in the queue.
            if (currentTrackIndex > 0) {

                // Play the previous index in the queue.
                currentTrackIndex--;
                playTrack(context, queue[currentTrackIndex]);

                // Update UI in every activity (that is, every one that implements the listener, which is all).
                this.setOnCompletionListener(mp -> {
                    try {
                        playNextInQueue(context);

                        // Update UI in every activity (that is, every one that implements the listener, which is all).
                        for (TrackOverListener trackOverListener : allTrackOverListeners) {
                            trackOverListener.updateTrackUI();
                        }
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    // Parameters: (Playlist) playlist to generate from.
    // Description: Sets the queue to a by-index list
    public void generateIndexQueue(Playlist playlist) {

        // Set the queue length to the number of tracks in the playlist.
        queue = new Track[playlist.getNumberOfTracks()];

        // Go through each track as per index and set equal.
        for (int trackIndex = 0; trackIndex < playlist.getNumberOfTracks(); trackIndex++) {
            queue[trackIndex] = playlist.getAllTracks().get(trackIndex);
        }

        // Set info variables.
        playingFrom = playlist;
    }

    // Parameters: (Playlist) playlist to generate from.
    // Description: Sets the queue to a randomized list.
    public void generateShuffleQueue(Playlist playlist) {

        // Set queue length to playlist number of tracks.
        queue = new Track[playlist.getNumberOfTracks()];

        // Create random object; used to randomly shuffle.
        Random random = new Random();

        // Go through each index of the currently un-initialized queue.
        for (int queueIndex = 0; queueIndex < playlist.getNumberOfTracks(); queueIndex++) {
            while (true) {
                Track randomTrack = playlist.getAllTracks().get(random.nextInt(playlist.getNumberOfTracks()));
                if (!Arrays.asList(queue).contains(randomTrack)) {
                    queue[queueIndex] = randomTrack;
                    break;
                }
            }
        }

        // Set info variables.
        playingFrom = playlist;

    }

    // Parameters: None.
    // Description: Sets / stops looping playlist.
    public void toggleQueueLooping() {
        queueLooping = !queueLooping;
    }

    public void toggleShuffle() {
        isShuffle = !isShuffle;
    }

    // OTHER METHODS - NOT COMPLETE FOR THIS SUBMISSION

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

