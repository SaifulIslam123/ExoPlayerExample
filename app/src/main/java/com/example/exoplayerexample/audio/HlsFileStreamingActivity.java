package com.example.exoplayerexample.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoplayerexample.MediaControllerFragment;
import com.example.exoplayerexample.MyApplication;
import com.example.exoplayerexample.MyPreferenceManager;
import com.example.exoplayerexample.R;
import com.example.exoplayerexample.mediaUtils.MediaBrowserHelper;
import com.example.exoplayerexample.mediaUtils.MediaBrowserHelperCallback;
import com.example.exoplayerexample.model.Artist;
import com.example.exoplayerexample.model.MediaDocument;
import com.example.exoplayerexample.services.MediaService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.exoplayerexample.Constants.MEDIA_QUEUE_POSITION;
import static com.example.exoplayerexample.Constants.QUEUE_NEW_PLAYLIST;
import static com.example.exoplayerexample.Constants.SEEK_BAR_MAX;
import static com.example.exoplayerexample.Constants.SEEK_BAR_PROGRESS;

public class HlsFileStreamingActivity extends AppCompatActivity implements IHlsAudioStreamingActivity, MediaBrowserHelperCallback, PlaylistRecyclerAdapter.IMediaSelector {

    private static final String TAG = "HlsFileStreamingActivit";

    //UI Components
    private ProgressBar mProgressBar;

    // Vars
    private MediaBrowserHelper mMediaBrowserHelper;
    private MyApplication mMyApplication;
    private MyPreferenceManager mMyPrefManager;
    private boolean mIsPlaying;
    private SeekBarBroadcastReceiver mSeekbarBroadcastReceiver;
    private UpdateUIBroadcastReceiver mUpdateUIBroadcastReceiver;
    private boolean mOnAppOpen;
    private boolean mWasConfigurationChange = false;

    private RecyclerView mRecyclerView;
    private PlaylistRecyclerAdapter mAdapter;
    private ArrayList<MediaMetadataCompat> mainMediaDocumentArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_hls_streaming);

        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaylistRecyclerAdapter(this, mainMediaDocumentArrayList, this);
        mRecyclerView.setAdapter(mAdapter);

        mMyApplication = MyApplication.getInstance();
        mMyPrefManager = new MyPreferenceManager(this);

        mMediaBrowserHelper = new MediaBrowserHelper(this, MediaService.class);
        mMediaBrowserHelper.setMediaBrowserHelperCallback(this);

        prepareMediaData();

        if (savedInstanceState == null) {

            Artist artist = new Artist();
            artist.setTitle("Music");
            artist.setArtist_id("DmkWVyfXHkc3GC7nIfRh");
            artist.setImage("https://i.imgur.com/DvpvklR.png");
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        //  mIMainActivity.hideProgressBar();
        mAdapter.notifyDataSetChanged();

    }


    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*String newMediaId = intent.getStringExtra(getString(R.string.broadcast_new_media_id));
            Log.d(TAG, "onReceive: CALLED: " + newMediaId);
            if (getPlaylistFragment() != null) {
                Log.d(TAG, "onReceive: " + mMyApplication.getMediaItem(newMediaId).getDescription().getMediaId());
                getPlaylistFragment().updateUI(mMyApplication.getMediaItem(newMediaId));
            }*/
        }
    }

    //Recyclerview item click handle method
    @Override
    public void onMediaSelected(int position) {

        //TODO: start playing music

        getMyApplicationInstance().setMediaItems(mainMediaDocumentArrayList);
        mAdapter.setSelectedIndex(position);

        onMediaSelected(
                mainMediaDocumentArrayList.get(position).getDescription().getMediaId(), // playlist_id = media_id
                mainMediaDocumentArrayList.get(position),
                position);
        saveLastPlayedSongProperties(mainMediaDocumentArrayList.get(position));

    }

    public void updateUI(MediaMetadataCompat mediaItem) {

    }

    private void saveLastPlayedSongProperties(MediaMetadataCompat mediaItem) {
        // NOTE: Normally you'd do this with a cache

        getMyPreferenceManager().savePlaylistId(mediaItem.getDescription().getMediaId()); // playlist id is same as media id
        getMyPreferenceManager().saveLastPlayedArtist(mediaItem.getDescription().getMediaId());
        getMyPreferenceManager().saveLastPlayedArtistImage(mediaItem.getDescription().getIconUri().toString());
        getMyPreferenceManager().saveLastPlayedMedia(mediaItem.getDescription().getMediaId());

    }

    public void prepareMediaData() {

        MediaDocument mediaDocument = new MediaDocument();
        mediaDocument.setFieldArtist("Andy Largo");
        mediaDocument.setFieldArtistId("largoo_007");
        mediaDocument.setFieldDateAdded(new Date());
        mediaDocument.setFieldDescription("Andy Largo L.O.T.S. Lechfeld, Bavaria 31-10-2018");
        mediaDocument.setFieldMediaId("FJYw5g2JgWuoXHoiEeyi");
        mediaDocument.setFieldMediaUrl("https://bitmovin-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8");
        mediaDocument.setFieldTitle("Andy Largo L.O.T.S. Lechfeld, Bavaria");
        mediaDocument.setFieldMediaImage("https://i.imgur.com/DvpvklR.png");

        mainMediaDocumentArrayList.add(addToMediaList(mediaDocument));


    }
    /**
     * Translate the Firestore data into something the MediaBrowserService can deal with (MediaMetaDataCompat objects)
     *
     * @param document
     */
    private MediaMetadataCompat addToMediaList(MediaDocument document) {

        MediaMetadataCompat media = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, document.getFieldMediaId())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, document.getFieldArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, document.getFieldTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, document.getFieldMediaUrl())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, document.getFieldDescription())
                .putString(MediaMetadataCompat.METADATA_KEY_DATE, document.getFieldDateAdded().toString())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, document.getFieldMediaImage())
                .build();

        return media;
    }


    private void initUpdateUIBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.broadcast_update_ui));
        mUpdateUIBroadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(mUpdateUIBroadcastReceiver, intentFilter);
    }

    @Override
    public void onMediaControllerConnected(MediaControllerCompat mediaController) {
        getMediaControllerFragment().getMediaSeekBar().setMediaController(mediaController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSeekBarBroadcastReceiver();
        initUpdateUIBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSeekbarBroadcastReceiver != null) {
            unregisterReceiver(mSeekbarBroadcastReceiver);
        }
        if (mUpdateUIBroadcastReceiver != null) {
            unregisterReceiver(mUpdateUIBroadcastReceiver);
        }
    }

    private class SeekBarBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long seekProgress = intent.getLongExtra(SEEK_BAR_PROGRESS, 0);
            long seekMax = intent.getLongExtra(SEEK_BAR_MAX, 0);
            if (!getMediaControllerFragment().getMediaSeekBar().isTracking()) {
                getMediaControllerFragment().getMediaSeekBar().setProgress((int) seekProgress);
                getMediaControllerFragment().getMediaSeekBar().setMax((int) seekMax);
            }
        }
    }

    private void initSeekBarBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.broadcast_seekbar_update));
        mSeekbarBroadcastReceiver = new SeekBarBroadcastReceiver();
        registerReceiver(mSeekbarBroadcastReceiver, intentFilter);
    }


    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        Log.d(TAG, "onMetadataChanged: called");
        if (metadata == null) {
            return;
        }

        // Do stuff with new Metadata
        getMediaControllerFragment().setMediaTitle(metadata);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        Log.d(TAG, "onPlaybackStateChanged: called.");
        mIsPlaying = state != null &&
                state.getState() == PlaybackStateCompat.STATE_PLAYING;

        // update UI
        if (getMediaControllerFragment() != null) {
            getMediaControllerFragment().setIsPlaying(mIsPlaying);
        }
    }


    private MediaControllerFragment getMediaControllerFragment() {
        MediaControllerFragment mediaControllerFragment = (MediaControllerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bottom_media_controller);
        if (mediaControllerFragment != null) {
            return mediaControllerFragment;
        }
        return null;
    }

    @Override
    public void onMediaSelected(String playlistId, MediaMetadataCompat mediaItem, int queuePosition) {
        if (mediaItem != null) {
            Log.d(TAG, "onMediaSelected: CALLED: " + mediaItem.getDescription().getMediaId());

            String currentPlaylistId = getMyPreferenceManager().getPlaylistId();

            Bundle bundle = new Bundle();
            bundle.putInt(MEDIA_QUEUE_POSITION, queuePosition);
            if (playlistId.equals(currentPlaylistId)) {
                mMediaBrowserHelper.getTransportControls().playFromMediaId(mediaItem.getDescription().getMediaId(), bundle);
            } else {
                bundle.putBoolean(QUEUE_NEW_PLAYLIST, true); // let the player know this is a new playlist
                mMediaBrowserHelper.subscribeToNewPlaylist(currentPlaylistId, playlistId);
                mMediaBrowserHelper.getTransportControls().playFromMediaId(mediaItem.getDescription().getMediaId(), bundle);
            }

            mOnAppOpen = true;
        } else {
            Toast.makeText(this, "select something to play", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public MyPreferenceManager getMyPreferenceManager() {
        return mMyPrefManager;
    }

    @Override
    public MyApplication getMyApplicationInstance() {
        return mMyApplication;
    }


    @Override
    public void playPause() {
        if (mOnAppOpen) {
            if (mIsPlaying) {
                mMediaBrowserHelper.getTransportControls().pause();
            } else {
                mMediaBrowserHelper.getTransportControls().play();
            }
        } else {
            if (!getMyPreferenceManager().getPlaylistId().equals("")) {
                onMediaSelected(
                        getMyPreferenceManager().getPlaylistId(),
                        mMyApplication.getMediaItem(getMyPreferenceManager().getLastPlayedMedia()),
                        getMyPreferenceManager().getQueuePosition()
                );
            } else {
                Toast.makeText(this, "select something to play", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWasConfigurationChange = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!getMyPreferenceManager().getPlaylistId().equals("")) {
            prepareLastPlayedMedia();
        } else {
            mMediaBrowserHelper.onStart(mWasConfigurationChange);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called.");
        mMediaBrowserHelper.onStop();
        getMediaControllerFragment().getMediaSeekBar().disconnectController();
    }

    /**
     * In a production app you'd want to get this data from a cache.
     */
    private void prepareLastPlayedMedia() {
        showPrgressBar();

        //creating dummy data
        final List<MediaMetadataCompat> mediaItems = new ArrayList<>();

        for (MediaMetadataCompat mediaDocument1 : mainMediaDocumentArrayList) {
            mediaItems.add(mediaDocument1);
            if (mediaDocument1.getDescription().getMediaId().equals(getMyPreferenceManager().getLastPlayedMedia())) {
                getMediaControllerFragment().setMediaTitle(mediaDocument1);
            }
        }
        onFinishedGettingPreviousSessionData(mediaItems);
    }

    private void onFinishedGettingPreviousSessionData(List<MediaMetadataCompat> mediaItems) {
        mMyApplication.setMediaItems(mediaItems);
        mMediaBrowserHelper.onStart(mWasConfigurationChange);
        hideProgressBar();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPrgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


}