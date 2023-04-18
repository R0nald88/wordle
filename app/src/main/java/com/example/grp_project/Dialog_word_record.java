package com.example.grp_project;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.grp_project.Storage.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Dialog_word_record extends AppCompatActivity {

    //private final Dialog dialog;
    ListView lv_WordRecord;

    String[] testString = {"test", "testing", "test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test",};

    ArrayList<String> testArray = new ArrayList<String>(Arrays.asList(testString));
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_word_record);
        lv_WordRecord = findViewById(R.id.lv_WordRecord);
        WordListAdapter adapter = new WordListAdapter(this, testArray);
        lv_WordRecord.setAdapter(adapter);
        set_background();
        InitiateBackgroundMusic();
    }
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
    public void set_background() {
        LinearLayout LL3 = findViewById(R.id.LL4);
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        if (sharedPreferences.contains("CustomUriKey")) {
            Drawable d = Drawable.createFromPath(sharedPreferences.getString("CustomUriKey", ""));
            d.setAlpha(200);
            LL3.setBackground(d);
        }
        if (sharedPreferences.contains("nightmodeKey")){
            if(sharedPreferences.getBoolean("nightmodeKey",false)){
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }}
    /*public Dialog_word_record(Dialog dialog) {
        this.dialog = dialog;
    }*/
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
}

