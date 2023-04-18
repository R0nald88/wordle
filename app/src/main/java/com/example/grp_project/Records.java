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

import com.example.grp_project.Storage.Level;
import com.example.grp_project.Storage.Record;

import java.util.List;

public class Records extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    TextView tv_EndlessStep, tv_EndlessCompleted, tv_DailyStep, tv_DailyCompleted, tv_JourneyStep, tv_JourneyCompleted;
    AppCompatButton btn_ViewWord;

    List<Record> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);

        records = Record.read(getApplicationContext());
        initializeButton();
        initializeTextView();
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
        tv_DailyCompleted = findViewById(R.id.tv_DailyWord);
        tv_EndlessStep = findViewById(R.id.tv_EndlessStep);
        tv_EndlessCompleted = findViewById(R.id.tv_EndlessCompleted);
        tv_JourneyStep = findViewById(R.id.tv_JourneyStep);
        tv_JourneyCompleted = findViewById(R.id.tv_JourneyCompleted);

        // ENDLESS: No. of words solved:
        tv_EndlessCompleted.setText(null);
        // ENDLESS: Average Steps Used for each game:
        tv_EndlessStep.setText(null);
        // DAILY: No. of Daily Challenge solved:
        tv_DailyCompleted.setText(null);
        // DAILY: Average Steps Used for each game:
        tv_DailyStep.setText(null);
        //LEVEL: Average Steps Used for each game:
        tv_JourneyStep.setText(null);

        tv_JourneyCompleted.setText(""+Level.getCurrentLevel(getApplicationContext()));
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