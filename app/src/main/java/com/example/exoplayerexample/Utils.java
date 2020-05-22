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
package com.example.exoplayerexample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class Utils {

    public static final String PLAYBACK_CHANNEL_ID = "playback_channel";
    public static final int PLAYBACK_NOTIFICATION_ID = 1;
    public static final String MEDIA_SESSION_TAG = "audio_demo";
    public static final String DOWNLOAD_CHANNEL_ID = "download_channel";
    public static final int DOWNLOAD_NOTIFICATION_ID = 2;
    public static final String PREF_NAME = "pref";
    public static final String AUDIO_SINGLETON_OBJ = "audio obj";

    public static String convertMiliSecToMinutes(long millis) {

        try {
            DateFormat formatter = new SimpleDateFormat("hh:mm:ss", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(new Date(millis));
        } catch (Exception e) {
            return null;
        }
    }

    public static int getHourFromMillisSec(long millis){
       return  (int) ((millis / 1000) / 3600);
       //return  (int)( millis / (1000*60*60));
    }

    public static int getMinutesFromMillisSec(long millis){
            return (int) (((millis / 1000) / 60) % 60);
       //     return (int)(millis % (1000*60*60)) / (1000*60);
    }
    public static int getSecFromMillisSec(long millis){
        return (int) ((millis / 1000) % 60);
        //return (int) ((millis % (1000*60*60)) % (1000*60) / 1000);
    }

    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) ((milliseconds / 1000) / 3600);
        int minutes = (int) (((milliseconds / 1000) / 60) % 60);;
        int seconds =  (int) ((milliseconds / 1000) % 60);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

}
