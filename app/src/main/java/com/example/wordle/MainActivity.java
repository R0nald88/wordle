package com.example.wordle;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

import com.example.wordle.Storage.Record;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    boolean serviceBounded = false;
    AppCompatButton btn_start,btn_record,btn_quit;
    Intent next_activity, backgroundMusic;
    ServiceConnection musicConnection;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] bg = {R.drawable.bg_thumbsup, R.drawable.bg_time, R.drawable.bg_step, R.drawable.bg_stat};
        int rand = bg[new Random().nextInt(bg.length)];

        if (rand == R.drawable.bg_stat || rand == R.drawable.bg_step) {
            AppCompatButton btn = findViewById(R.id.btn_main_start);
            btn.setBackgroundTintList(AppCompatResources.getColorStateList(MainActivity.this, R.color.green));
        }

        RelativeLayout r = findViewById(R.id.layout_bg);
        r.setBackground(AppCompatResources.getDrawable(MainActivity.this, rand));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            putExitDialog();
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serviceBounded && musicConnection != null) {
            unbindService(musicConnection);
            serviceBounded = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        serviceBounded = bindService(backgroundMusic, musicConnection,0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        InitiateButtons();
        InitiateBackgroundMusic();
        serviceBounded = bindService(backgroundMusic, musicConnection,0);
    }

    private void putExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to quit?");
        builder.setTitle("Quit Now?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopService(backgroundMusic);
                MainActivity.this.finish();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void InitiateButtons() {
        btn_start=findViewById(R.id.btn_main_start);
        btn_start.setOnClickListener(this);
        btn_start.animate().rotationXBy(-360).setStartDelay(1000).setDuration(500);
        btn_record=findViewById(R.id.btn_main_record);
        btn_record.setOnClickListener(this);
        btn_record.animate().rotationXBy(-360).setStartDelay(1500).setDuration(500);
        btn_quit=findViewById(R.id.btn_main_quit);
        btn_quit.setOnClickListener(this);
        btn_quit.animate().rotationXBy(-360).setStartDelay(2000).setDuration(500);
    }
    private void InitiateBackgroundMusic() {
        backgroundMusic =new Intent(getApplicationContext(),BackgroundMusic.class);
        musicConnection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        startService(backgroundMusic);
        serviceBounded = bindService(backgroundMusic, musicConnection,0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_quit:
                putExitDialog();
                break;
            case R.id.btn_main_start:
                next_activity = new Intent(MainActivity.this,LevelActivity.class);
                if (Record.readCurrentRecord(getApplicationContext())!=null){
                    AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Reset Record?");
                    builder.setMessage("Unsolved game found, do you want to reset your progress?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Record.clearCurrentRecord(getApplicationContext());
                            startActivity(next_activity);
                            dialogInterface.dismiss();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(next_activity);
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();

                }
                else{
                    startActivity(next_activity);
                }
                break;
            case R.id.btn_main_record:
                next_activity=new Intent(MainActivity.this, RecordActivity.class);
                startActivity(next_activity);
                break;
        }
    }

}



