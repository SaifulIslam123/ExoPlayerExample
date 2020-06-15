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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.exoplayerexample.R;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import static com.example.exoplayerexample.Utils.AUDIO_SINGLETON_OBJ;
import static com.example.exoplayerexample.Utils.PREF_NAME;
import static com.example.exoplayerexample.audio.Samples.SAMPLES;

public class AudioStreamingActivity extends Activity {

    private PlayerView playerView;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        playerView = findViewById(R.id.video_view);

        /*//if (pref.contains(AUDIO_SINGLETON_OBJ)) {
        Gson gson = new Gson();
        String json = pref.getString(AUDIO_SINGLETON_OBJ, "");
        SingletonAudio singletonAudio = gson.fromJson(json, SingletonAudio.class);

        // } else {
        Intent intent = new Intent(this, AudioPlayerService.class);
        Util.startForegroundService(this, intent);

        //}
*/

        Intent intent = new Intent(this, AudioPlayerService.class);
        Util.startForegroundService(this, intent);

        playerView.setPlayer(SingletonAudio.getSingletonAudioInstance(getApplicationContext()).getSimpleExoPlayer());

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SAMPLES));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProgressiveDownloadAction action = new ProgressiveDownloadAction(
                        SAMPLES[position].uri, false, null, null);
                AudioDownloadService.startWithAction(
                        AudioStreamingActivity.this,
                        AudioDownloadService.class,
                        action,
                        false);
            }
        });
    }

}
