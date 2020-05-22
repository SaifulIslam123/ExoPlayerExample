package com.example.exoplayerexample.audio;

import android.support.v4.media.MediaMetadataCompat;

import com.example.exoplayerexample.MyApplication;
import com.example.exoplayerexample.MyPreferenceManager;
import com.example.exoplayerexample.model.Artist;

public interface IHlsAudioStreamingActivity {

    void hideProgressBar();

    void showPrgressBar();

    default void onCategorySelected(String category) {
    }

    default void onArtistSelected(String category, Artist artist) {
    }

    default void setActionBarTitle(String title) {
    }

    void playPause();

    void playNextMedia();

    void playPreviousMedia();

    MyApplication getMyApplicationInstance();

    void onMediaSelected(String playlistId, MediaMetadataCompat mediaItem, int position);

    MyPreferenceManager getMyPreferenceManager();
}
