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

import com.example.grp_project.Item.GameMode;
import com.example.grp_project.Storage.Level;
import com.example.grp_project.Storage.Record;

import java.util.List;

public class Records extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    TextView tv_EndlessStep, tv_EndlessCompleted, tv_DailyStep, tv_DailyCompleted, tv_JourneyStep, tv_JourneyCompleted;
    AppCompatButton btn_ViewWord;
    Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);


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
        List<Record> list = Record.read(getApplicationContext());
        int endlessSum = 0, endlessCount=0;
        int dailySum = 0, dailyCount=0;
        int levelSum = 0, levelCount=0;
        for (int index = 0; index < list.size(); index++){
            record = list.get(index);
            if (record.getLevel().getGameMode() == GameMode.ENDLESS_MODE) {
                endlessCount += record.getEndlessRecordRound();
                for (int a = 0; a<record.getEndlessGuessRecord().size(); a++ ){
                    endlessSum += record.getEndlessGuessRecord().get(a).size();
                }
            }
            if (record.getLevel().getGameMode() == GameMode.DAILY_MODE && record.isPassed()) {
                dailySum += record.getGuessRecord().size();
                dailyCount++;
            }
            if (record.getLevel().getGameMode() == GameMode.STEP_MODE || record.getLevel().getGameMode() == GameMode.TIME_MODE){
                levelSum += record.getGuessRecord().size();
                levelCount++;
            }
        }

        double endlessAvg = 0;
        try {endlessAvg = endlessSum/endlessCount;}
        catch (Exception e){
            endlessAvg = 0;
        }

        // ENDLESS: No. of words solved:
        tv_EndlessCompleted.setText(""+ endlessCount);
        // ENDLESS: Average Steps Used for each game:
        tv_EndlessStep.setText(""+ endlessAvg);

        double dailyAvg = 0;
        try {dailyAvg = dailySum/ dailyCount;}
        catch (Exception e){
            dailyAvg = 0;
        }

        // DAILY: No. of Daily Challenge solved:
        tv_DailyCompleted.setText(""+ dailyCount);
        // DAILY: Average Steps Used for each game:
        tv_DailyStep.setText(""+ dailyAvg);

        double levelAvg = 0;
        try{levelAvg=levelSum/ levelCount;}
        catch(Exception e){
            levelAvg=0;
        }
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


        List<Record> list = Record.read(getApplicationContext());
        int endlessSum = 0, endlessCount=0;
        int dailySum = 0, dailyCount=0;
        int levelSum = 0, levelCount=0;
        for (int index = 0; index < list.size(); index++){
            record = list.get(index);
            if (record.getLevel().getGameMode() == GameMode.ENDLESS_MODE) {
                endlessCount += record.getEndlessRecordRound();
                for (int a = 0; a<record.getEndlessGuessRecord().size(); a++ ){
                    endlessSum += record.getEndlessGuessRecord().get(a).size();
                }
            }
            if (record.getLevel().getGameMode() == GameMode.DAILY_MODE && record.isPassed()) {
                dailySum += record.getGuessRecord().size();
                dailyCount++;
            }
            if (record.getLevel().getGameMode() == GameMode.STEP_MODE || record.getLevel().getGameMode() == GameMode.TIME_MODE){
                levelSum += record.getGuessRecord().size();
                levelCount++;
            }
        }

        double endlessAvg = 0;
        try {endlessAvg = endlessSum/endlessCount;}
        catch (Exception e){
            endlessAvg = 0;
        }

        // ENDLESS: No. of words solved:
        tv_EndlessCompleted.setText(""+ endlessCount);
        // ENDLESS: Average Steps Used for each game:
        tv_EndlessStep.setText(""+ endlessAvg);

        double dailyAvg = 0;
        try {dailyAvg = dailySum/ dailyCount;}
        catch (Exception e){
            dailyAvg = 0;
        }

        // DAILY: No. of Daily Challenge solved:
        tv_DailyCompleted.setText(""+ dailyCount);
        // DAILY: Average Steps Used for each game:
        tv_DailyStep.setText(""+ dailyAvg);

        double levelAvg = 0;
        try{levelAvg=levelSum/ levelCount;}
        catch(Exception e){
            levelAvg=0;
        }

        tv_JourneyCompleted.setText(""+Level.getCurrentLevel(getApplicationContext()));
        //LEVEL: Average Steps Used for each game:
        tv_JourneyStep.setText(""+ levelAvg);
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