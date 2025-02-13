// == FILE PACKAGE ================
package com.example.ics4u___embear;

// == IMPORTS =============
import java.io.IOException;

// == TRACK_OVER_LISTENER ==========
/** EXPLANATION:
 * Whenever a track finishes in trackPlayer, it broadcasts
 * a message to all activities (screens) to update their UI;
 * since there's likely a new song being played now (if a queue is setup).
 */
public interface TrackOverListener {
    void updateTrackUI() throws IOException;
}
