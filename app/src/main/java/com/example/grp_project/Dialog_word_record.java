package com.example.grp_project;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;

import com.example.grp_project.Storage.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Dialog_word_record extends AppCompatActivity {

    //private final Dialog dialog;
    ListView lv_WordRecord;
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    ArrayList<String> arraylist;
    List<Record> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_word_record);
        lv_WordRecord = findViewById(R.id.lv_WordRecord);
        set_background();
        arraylist = new ArrayList<>();
        list = Record.read(getApplicationContext());
        for (int i=0; i<list.size();i++) {
            arraylist.add(list.get(i).getCorrectInput());
        }
        WordListAdapter adapter = new WordListAdapter(this, arraylist);
        lv_WordRecord.setAdapter(adapter);

    }




    private void InitiateBackgroundMusic() {
        bacgroud_music =new Intent(getApplicationContext(),BackgroundMusic.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(Dialog_word_record.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Dialog_word_record.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            InitiateBackgroundMusic();
            bindService(bacgroud_music, musicConnection,0);
        }
        arraylist = new ArrayList<>();
        list = Record.read(getApplicationContext());
        for (int i=0; i<list.size();i++) {
            arraylist.add(list.get(i).getCorrectInput());
        }
        WordListAdapter adapter = new WordListAdapter(this, arraylist);
        lv_WordRecord.setAdapter(adapter);

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
}

