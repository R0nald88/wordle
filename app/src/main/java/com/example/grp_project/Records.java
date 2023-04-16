package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grp_project.Storage.Record;

import java.util.List;

public class Records extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    List<Record> record_list;
    TextView tv_EndlessStep, tv_EndlessWord, tv_DailyStep, tv_DailyWord, tv_JourneyStep, tv_JourneyCompleted;
    AppCompatButton btn_ViewWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        initializeButton();
        initializeTextView();
        set_background();
        InitiateBackgroundMusic();
        record_list = Record.read(getApplicationContext());
    }

    private void initializeButton() {
        btn_ViewWord = findViewById(R.id.btn_ViewWord);
        btn_ViewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Records.this, Dialog_word_record.class);
                startActivity(i);
            }
        });
    }

    private void initializeTextView() {
        tv_DailyStep = findViewById(R.id.tv_DailyStep);
        tv_DailyWord = findViewById(R.id.tv_DailyWord);
        tv_EndlessStep = findViewById(R.id.tv_EndlessStep);
        tv_EndlessWord = findViewById(R.id.tv_EndlessWord);
        tv_JourneyStep = findViewById(R.id.tv_JourneyStep);
        tv_JourneyCompleted = findViewById(R.id.tv_JourneyCompleted);
    }

    public void set_background() {
        LinearLayout LL3 = findViewById(R.id.LL3);
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
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(Records.this,MainActivity.class);
            startActivity(i);
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
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
}