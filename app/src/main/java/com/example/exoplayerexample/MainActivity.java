package com.example.exoplayerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.exoplayerexample.audio.AudioStreamingActivity;
import com.example.exoplayerexample.audio.HlsFileStreamingActivity;
import com.example.exoplayerexample.video.DashVideoPlayerActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.dashVideoPlayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DashVideoPlayerActivity.class));
            }
        });

        findViewById(R.id.audioPlayBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, AudioStreamingActivity.class));
                startActivity(new Intent(MainActivity.this, HlsFileStreamingActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });


    }


}
