package com.felkertech.cumulustv.player;

/**
 * The listener for buffer events occurred during playback.
 */
public interface PlaybackBufferListener {

    /**
     * Invoked when the start position of the buffer has been changed.
     *
     * @param startTimeMs the new start time of the buffer in millisecond
     */
    void onBufferStartTimeChanged(long startTimeMs);

    /**
     * Invoked when the state of the buffer has been changed.
     *
     * @param available whether the buffer is available or not
     */
    void onBufferStateChanged(boolean available);

    /**
     * Invoked when the disk speed is too slow to write the buffers.
     */
    void onDiskTooSlow();
}
