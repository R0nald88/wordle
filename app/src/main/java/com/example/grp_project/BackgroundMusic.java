package com.example.grp_project;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileDescriptor;

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