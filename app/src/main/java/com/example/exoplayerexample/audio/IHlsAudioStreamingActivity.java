package com.example.exoplayerexample.audio;

import android.support.v4.media.MediaMetadataCompat;

import com.example.exoplayerexample.MyApplication;
import com.example.exoplayerexample.MyPreferenceManager;
import com.example.exoplayerexample.model.Artist;

public interface IHlsAudioStreamingActivity {

    void hideProgressBar();

    void showPrgressBar();

    void onCategorySelected(String category);

    void onArtistSelected(String category, Artist artist);

    void setActionBarTitle(String title);

    void playPause();

    MyApplication getMyApplicationInstance();

    void onMediaSelected(String playlistId, MediaMetadataCompat mediaItem, int position);

    MyPreferenceManager getMyPreferenceManager();
}
