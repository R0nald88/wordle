package com.example.grp_project;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusic extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.music);
        mediaPlayer.setLooping(true);


    }

    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer.start();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
    }

}