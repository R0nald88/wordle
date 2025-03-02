package com.example.wordle;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wordle.Dialog.WordRecordDialog;
import com.example.wordle.Item.GameMode;
import com.example.wordle.Storage.Level;
import com.example.wordle.Storage.Record;

import java.util.List;

public class RecordActivity extends AppCompatActivity {
    Intent bacgroud_music;
    ServiceConnection musicConnection;
    boolean serviceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        InitiateBackgroundMusic();

        setStatLayout(findViewById(R.id.layout_step), GameMode.STEP_MODE);
        setStatLayout(findViewById(R.id.layout_time), GameMode.TIME_MODE);
        setStatLayout(findViewById(R.id.layout_endless), GameMode.ENDLESS_MODE);
        setStatLayout(findViewById(R.id.layout_daily), GameMode.DAILY_MODE);

        findViewById(R.id.btn_return).setOnClickListener(v -> finish());
        findViewById(R.id.btn_word).setOnClickListener(v -> startActivity(new Intent(RecordActivity.this, WordRecordDialog.class)));
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (serviceBound && musicConnection != null) {
            unbindService(musicConnection);
            serviceBound = false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        serviceBound = bindService(bacgroud_music, musicConnection,0);
    }

    private void setStatLayout(RelativeLayout layout, GameMode mode) {
        ImageView img_mode = layout.findViewById(R.id.img_result);
        TextView txt_mode = layout.findViewById(R.id.txt_correct_word);
        TextView txt_detail = layout.findViewById(R.id.txt_detail);
        ImageView btn_search = layout.findViewById(R.id.btn_search);
        btn_search.setVisibility(View.GONE);

        txt_mode.setText(mode.describe());

        img_mode.setImageResource(mode.getDrawableInt());

        List<Record> list = Record.readMode(getApplicationContext(), mode, true);
        int roundPlay = list.size();
        int averageGuess = 0;
        int correctGuess = 0;

        for (Record r : list) {
            if (r.isPassed()) correctGuess++;
            averageGuess += r.getGuessRecord().size();
        }

        if (roundPlay != 0) averageGuess /= roundPlay;
        int correctPerc = roundPlay == 0 ? 0 : correctGuess * 100 / roundPlay;

        txt_detail.setText(
            "Round Played: " + roundPlay + "\n" +
            "Correct Guess: " + correctGuess + " (" + correctPerc + "%)\n" +
            "Average Guess per Word: " + averageGuess + " time(s)"
        );
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
        serviceBound = bindService(bacgroud_music, musicConnection,0);
    }
}