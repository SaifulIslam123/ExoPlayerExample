/*
 * Copyright (Utils) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exoplayerexample.audio;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;

import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.Nullable;

import com.example.exoplayerexample.R;
import com.example.exoplayerexample.Utils;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.gson.Gson;

import static com.example.exoplayerexample.Utils.PLAYBACK_CHANNEL_ID;
import static com.example.exoplayerexample.Utils.PLAYBACK_NOTIFICATION_ID;
import static com.example.exoplayerexample.Utils.PREF_NAME;
import static com.example.exoplayerexample.audio.Samples.SAMPLES;

public class AudioPlayerService extends Service {

    private SingletonAudio singletonAudio;
    private PlayerNotificationManager playerNotificationManager;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = getApplicationContext();

        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        singletonAudio = SingletonAudio.getSingletonAudioInstance(getApplicationContext());

        /*player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, getString(R.string.application_name)));
        cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(context),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        concatenatingMediaSource = new ConcatenatingMediaSource();*/
        for (Samples.Sample sample : SAMPLES) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(singletonAudio.getCacheDataSourceFactory())
                    .createMediaSource(sample.uri);
            singletonAudio.getConcatenatingMediaSource().addMediaSource(mediaSource);
        }
        singletonAudio.getSimpleExoPlayer().prepare(singletonAudio.getConcatenatingMediaSource());
        singletonAudio.getSimpleExoPlayer().setPlayWhenReady(true);

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,
                PLAYBACK_NOTIFICATION_ID,
                new MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return SAMPLES[player.getCurrentWindowIndex()].title;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return SAMPLES[player.getCurrentWindowIndex()].description;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
                        return Samples.getBitmap(
                                context, SAMPLES[player.getCurrentWindowIndex()].bitmapResource);
                    }
                }
        );
        playerNotificationManager.setNotificationListener(new NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(singletonAudio.getSimpleExoPlayer());

        //mediaSession = new MediaSessionCompat(context, MEDIA_SESSION_TAG);
        singletonAudio.getMediaSession().setActive(true);
        playerNotificationManager.setMediaSessionToken(singletonAudio.getMediaSession().getSessionToken());

        // mediaSessionConnector = new MediaSessionConnector(mediaSession);
        singletonAudio.getMediaSessionConnector().setQueueNavigator(new TimelineQueueNavigator(singletonAudio.getMediaSession()) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return Samples.getMediaDescription(context, SAMPLES[windowIndex]);
            }
        });
        singletonAudio.getMediaSessionConnector().setPlayer(singletonAudio.getSimpleExoPlayer(), null);


        /*Gson gson = new Gson();
        String json = gson.toJson(singletonAudio);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Utils.AUDIO_SINGLETON_OBJ, json);
        editor.commit();*/
    }

    @Override
    public void onDestroy() {
        singletonAudio.getMediaSession().release();
        singletonAudio.getMediaSessionConnector().setPlayer(null, null);
        playerNotificationManager.setPlayer(null);
        singletonAudio.getSimpleExoPlayer().release();
        singletonAudio.setSimpleExoPlayer(null);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
