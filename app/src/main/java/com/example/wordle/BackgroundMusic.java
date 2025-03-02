package com.example.wordle;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusic extends Service {
    MediaPlayer mediaPlayer;
    IBinder iBinder;

    @Override
    public void onCreate() {
        mediaPlayer=MediaPlayer.create(getApplicationContext(),R.raw.music);
        mediaPlayer.setLooping(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mediaPlayer.start();
        return iBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}