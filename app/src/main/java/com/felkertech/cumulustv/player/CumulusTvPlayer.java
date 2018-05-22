package com.felkertech.cumulustv.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import com.felkertech.cumulustv.player.source.PositionReference;
import com.felkertech.cumulustv.player.utils.TrickPlayController;
import com.felkertech.cumulustv.player.extractor.TvExtractor;
import com.google.android.media.tv.companionlibrary.TvPlayer;


import java.util.ArrayList;
import java.util.List;


public class CumulusTvPlayer implements TvPlayer, com.google.android.exoplayer2.Player.EventListener {

    private static final String TAG = CumulusTvPlayer.class.getSimpleName();

    private static final boolean DEBUG = false;

    private static final int DEFAULT_MIN_BUFFER_MS = 3000;
    private static final int DEFAULT_MAX_BUFFER_MS = 5000;
    private static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 1000;
    private static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 2000;

    public interface Listener  {

        void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        void onPlayerError(Exception e);

        void onDisconnect();

        void onTracksChanged(StreamBundle bundle);

        void onAudioTrackChanged(Format format);

        void onVideoTrackChanged(Format format);

        void onRenderedFirstFrame();

        void onStreamError(int status);
    }

    private Listener listener;
    private Handler handler;

    private List<ErrorListener> mErrorListeners = new ArrayList<>();
    private List<Callback> mTvCallbacks = new ArrayList<>();
    final private SimpleExoPlayer player;
    final private PositionReference position;
    final private TrickPlayController trickPlayController;
    private float mPlaybackSpeed;
    private Context mContext;

    public CumulusTvPlayer(Context context) {
        this(context,  new DefaultTrackSelector(),                 new DefaultLoadControl(
                        new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
                        DEFAULT_MIN_BUFFER_MS,
                        DEFAULT_MAX_BUFFER_MS,
                        DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                        DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                        C.LENGTH_UNSET,
                        false
                )
        );
    }

    public CumulusTvPlayer(Context context, DefaultTrackSelector trackSelector, LoadControl loadControl) {
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        mContext = context;
        player.addListener(this);
        position = new PositionReference();
        trickPlayController = new TrickPlayController(handler, position, player);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
    }

    public void setSurface(Surface surface) {
        player.setVideoSurface(surface);
    }

    public void setStreamVolume(float volume) {
        player.setVolume(volume);
    }

    public void seek(long position) {
        long p = this.position.timeUsFromPosition(Math.max(position, this.position.getStartPosition()));
        player.seekTo(p / 1000);
    }

    @TargetApi(23)
    public void setPlaybackParams(PlaybackParams params) {
        Log.d(TAG, "speed: " + params.getSpeed());
        trickPlayController.start(params.getSpeed());
    }

    public float getPlaybackSpeed() {
        return mPlaybackSpeed;
    }

    public long getStartPosition() {
        return position.getStartPosition();
    }

    public long getEndPosition() {
        return position.getEndPosition();
    }

    public long getCurrentPosition() {
        long timeUs = player.getCurrentPosition() * 1000;
        long startPos = position.getStartPosition();
        long endPos = position.getEndPosition();

        long pos = Math.max(position.positionFromTimeUs(timeUs), startPos);

        // clamp to end position (if we already have a valid endposition)
        if(endPos > startPos) {
            return Math.min(pos, endPos);
        }

        return pos;
    }

    public long getBufferedPosition() {
        long timeUs = player.getBufferedPosition() * 1000;
        return position.positionFromTimeUs(timeUs);
    }

    public long getDurationSinceStart() {
        return getCurrentPosition() - getStartPosition();
    }

    public long getDuration() {
        return position.getDuration();
    }

    public void play() {
        trickPlayController.stop();
        player.setPlayWhenReady(true);
    }

    public void pause() {
        trickPlayController.stop();
        player.setPlayWhenReady(false);
    }

    @Override
    public void setVolume(float volume) {
        player.setVolume(volume);
    }

    public boolean isPaused() {
        return !player.getPlayWhenReady();
    }

    public void stop() {
        player.stop();
    }

    public void release() {
        player.removeListener(this);
        player.release();
    }

    @Override
    public void registerCallback(Callback callback) {
        mTvCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mTvCallbacks.remove(callback);
    }

    public void registerErrorListener(ErrorListener callback) {
        mErrorListeners.add(callback);
    }

    public void unregisterErrorListener(ErrorListener callback) {
        mErrorListeners.remove(callback);
    }

    public void startPlaying(Uri mediaUri) {
        // This is the MediaSource representing the media to be played.
        try {
            MediaSource videoSource = MediaSourceFactory.getMediaSourceFor(mContext, mediaUri);
            // Prepare the player with the source.
            player.prepare(videoSource);
        } catch (MediaSourceFactory.NotMediaException e) {
            for (ErrorListener listener : mErrorListeners) {
                listener.onError(e);
            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        for (Callback tvCallback : mTvCallbacks) {
            if (playWhenReady && playbackState == player.STATE_ENDED) {
                tvCallback.onCompleted();
            } else if (playWhenReady && playbackState == player.STATE_READY) {
                tvCallback.onStarted();
            }
        }
        Log.d(TAG, "Player state changed to " + playbackState + ", PWR: " + playWhenReady);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        for (Callback tvCallback : mTvCallbacks) {
            tvCallback.onError();
        }
        for (ErrorListener listener : mErrorListeners) {
            listener.onError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onSeekProcessed() {
    }

    public interface ErrorListener {
        void onError(Exception error);
    }
}
