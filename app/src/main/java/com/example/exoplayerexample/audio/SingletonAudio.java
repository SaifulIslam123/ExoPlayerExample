package com.example.exoplayerexample.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.exoplayerexample.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;

import static com.example.exoplayerexample.Utils.MEDIA_SESSION_TAG;

public class SingletonAudio implements Serializable {

    private SimpleExoPlayer player;

    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private DefaultDataSourceFactory dataSourceFactory;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private ConcatenatingMediaSource concatenatingMediaSource;

    private Context context;

    private static SingletonAudio singletonAudio = null;


    public static SingletonAudio getSingletonAudioInstance(Context context) {

        if (singletonAudio == null) {
            singletonAudio = new SingletonAudio(context);
        }


        return singletonAudio;
    }

    private SingletonAudio(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer getSimpleExoPlayer() {

        if (player == null)
            player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

        return player;
    }

    public void setSimpleExoPlayer(SimpleExoPlayer player) {
        this.player = player;
    }

    public MediaSessionCompat getMediaSession() {

        if (mediaSession == null)
            mediaSession = new MediaSessionCompat(context, MEDIA_SESSION_TAG);

        return mediaSession;
    }

    public MediaSessionConnector getMediaSessionConnector() {

        if (mediaSessionConnector == null) {
            mediaSessionConnector = new MediaSessionConnector(mediaSession);
        }

        return mediaSessionConnector;
    }

    public DefaultDataSourceFactory getDataSourceFactory() {

        if (dataSourceFactory == null) {
            dataSourceFactory = new DefaultDataSourceFactory(
                    context, Util.getUserAgent(context, context.getString(R.string.application_name)));
        }

        return dataSourceFactory;
    }

    public CacheDataSourceFactory getCacheDataSourceFactory() {

        if (cacheDataSourceFactory == null) {
            cacheDataSourceFactory = new CacheDataSourceFactory(
                    DownloadUtil.getCache(context),
                    getDataSourceFactory(),
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        }

        return cacheDataSourceFactory;
    }

    public ConcatenatingMediaSource getConcatenatingMediaSource() {
        if (concatenatingMediaSource == null) {
            concatenatingMediaSource = new ConcatenatingMediaSource();
        }

        return concatenatingMediaSource;
    }

}
