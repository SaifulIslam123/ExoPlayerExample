/*
 * Copyright (C) 2018 The Android Open Source Project
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

import androidx.annotation.Nullable;

import com.example.exoplayerexample.R;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationUtil;

import static com.example.exoplayerexample.audio.C.DOWNLOAD_CHANNEL_ID;
import static com.example.exoplayerexample.audio.C.DOWNLOAD_NOTIFICATION_ID;


public class AudioDownloadService extends DownloadService {

    public AudioDownloadService() {
        super(
                DOWNLOAD_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                DOWNLOAD_CHANNEL_ID,
                R.string.download_channel_name);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return DownloadUtil.getDownloadManager(this);
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return null;
    }

    @Override
    protected Notification getForegroundNotification(TaskState[] taskStates) {
        return DownloadNotificationUtil.buildProgressNotification(
                this,
                R.drawable.exo_icon_play,
                DOWNLOAD_CHANNEL_ID,
                null,
                null,
                taskStates);
    }

}
