package com.example.wordle.Dialog;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordle.BackgroundMusic;
import com.example.wordle.R;
import com.example.wordle.Storage.Record;
import com.example.wordle.WordResultAdapter;

import java.util.List;

public class WordRecordDialog extends AppCompatActivity {
    Intent background_music;
    ServiceConnection musicConnection;
    boolean serviceBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_word_record);
        setWordList();

        ImageView btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(v -> finish());
    }

    private void setWordList() {
        RecyclerView list_result = findViewById(R.id.list_result);
        LinearLayout layout_empty = findViewById(R.id.layout_empty);

        if (Record.read(this).isEmpty()) {
            list_result.setVisibility(View.GONE);
            layout_empty.setVisibility(View.VISIBLE);
        } else {
            list_result.setVisibility(View.VISIBLE);
            layout_empty.setVisibility(View.GONE);
            WordResultAdapter adapter = new WordResultAdapter(WordRecordDialog.this);
            list_result.setAdapter(adapter);
            list_result.setItemAnimator(new DefaultItemAnimator());
            list_result.setHasFixedSize(true);
            list_result.setLayoutManager(new LinearLayoutManager(WordRecordDialog.this));
        }

    }

    private void initiateBackgroundMusic() {
        background_music =new Intent(getApplicationContext(), BackgroundMusic.class);
        musicConnection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        startService(background_music);
        serviceBound = bindService(background_music, musicConnection,0);}

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
        serviceBound = bindService(background_music, musicConnection,0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initiateBackgroundMusic();
        serviceBound = bindService(background_music, musicConnection,0);
    }
}

