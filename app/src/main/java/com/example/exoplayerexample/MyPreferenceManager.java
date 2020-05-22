package com.example.exoplayerexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.example.exoplayerexample.Constants.LAST_ARTIST;
import static com.example.exoplayerexample.Constants.LAST_ARTIST_IMAGE;
import static com.example.exoplayerexample.Constants.LAST_CATEGORY;
import static com.example.exoplayerexample.Constants.LAST_PLAYED_SONG_RUNNING_STATE;
import static com.example.exoplayerexample.Constants.MEDIA_QUEUE_POSITION;
import static com.example.exoplayerexample.Constants.NOW_PLAYING;
import static com.example.exoplayerexample.Constants.PLAYLIST_ID;

public class MyPreferenceManager {

    private static final String TAG = "MyPreferenceManager";

    private SharedPreferences mPreferences;

    public MyPreferenceManager(Context mContext) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public String getPlaylistId() {
        return mPreferences.getString(PLAYLIST_ID, "");
    }

    public void savePlaylistId(String playlistId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PLAYLIST_ID, playlistId);
        editor.apply();
    }

    public void saveQueuePosition(int position) {
        Log.d(TAG, "saveQueuePosition: SAVING QUEUE INDEX: " + position);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(MEDIA_QUEUE_POSITION, position);
        editor.apply();
    }

    public int getQueuePosition() {
        return mPreferences.getInt(MEDIA_QUEUE_POSITION, -1);
    }

    public void saveLastPlayedArtistImage(String url) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LAST_ARTIST_IMAGE, url);
        editor.apply();
    }

    public void setLastPlayedMediaId(String mediaId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(NOW_PLAYING, mediaId);
        editor.apply();
    }

    public void setIsLastPlayedSongRunning(boolean state) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(LAST_PLAYED_SONG_RUNNING_STATE, state);
        editor.apply();
    }

    public Boolean isLastPlayedSongRunning(){
        return mPreferences.getBoolean(LAST_PLAYED_SONG_RUNNING_STATE,false);
    }

    public String getLastPlayedMediaId() {
        return mPreferences.getString(NOW_PLAYING, "");
    }

    public void saveLastPlayedArtist(String artist) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LAST_ARTIST, artist);
        editor.apply();
    }

}


















