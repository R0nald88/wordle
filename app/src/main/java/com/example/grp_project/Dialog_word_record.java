package com.example.grp_project;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Dialog_word_record extends AppCompatActivity {
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    SharedPreferences sharedPreferences;
    //private final Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_word_record);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        set_background();
        InitiateBackgroundMusic();

    }


    /*public Dialog_word_record(Dialog dialog) {
        this.dialog = dialog;
    }*/
    public void set_background() {
        LinearLayout LL4 = findViewById(R.id.LL4);
        if (sharedPreferences.contains("CustomUriKey")) {
            Drawable d = Drawable.createFromPath(sharedPreferences.getString("CustomUriKey", ""));
            d.setAlpha(200);
            LL4.setBackground(d);
        }
        if (sharedPreferences.contains("nightmodeKey")){
            if(sharedPreferences.getBoolean("nightmodeKey",false)){
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }
    }
    private void InitiateBackgroundMusic() {
        bacgroud_music=new Intent(getApplicationContext(),BackgroundMusic.class);
        musicConnection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        startService(bacgroud_music);
        bindService(bacgroud_music, musicConnection,0);}

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(musicConnection);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bindService(bacgroud_music, musicConnection,0);
    }
}

