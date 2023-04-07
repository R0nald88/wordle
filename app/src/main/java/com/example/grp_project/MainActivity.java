package com.example.grp_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button btn_start,btn_settings,btn_record,btn_quit;
    Intent next_activity,bacgroud_music;
    SharedPreferences sharedPreferences;

    //Key lists
    public static final String sharedPerferenceKey="sharedPerferenceKey";
    public static final String nightmodeKey="nightmodeKey";
    public static final String customurikey="CustomUriKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitiateButtons();
        InitiateSharedPreferences();
        InitiateBackgroundMusic();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            putExitdialoge();
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }
    //show exit dialoge for exit
    private void putExitdialoge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to quit?");
        builder.setTitle("Quit Now?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //initiate the buttons
    private void InitiateButtons() {
        btn_start=findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_start.animate().rotationXBy(-360).setStartDelay(1000).setDuration(500).withStartAction(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setBackgroundTintList(btn_start, ContextCompat.getColorStateList(MainActivity.this, R.color.wordle_green));
            }
        });
        btn_record=findViewById(R.id.btn_record);
        btn_record.setOnClickListener(this);
        btn_record.animate().rotationXBy(-360).setStartDelay(1500).setDuration(500).withStartAction(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setBackgroundTintList(btn_record, ContextCompat.getColorStateList(MainActivity.this , R.color.wordle_yellow));
            }
        });
        btn_settings=findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);
        btn_settings.animate().rotationXBy(-360).setStartDelay(2000).setDuration(500);
        btn_quit=findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
        btn_quit.animate().rotationXBy(-360).setStartDelay(2500).setDuration(500);
    }
    private void InitiateSharedPreferences() {
        sharedPreferences=getSharedPreferences(sharedPerferenceKey,MODE_PRIVATE);
        if (sharedPreferences.getBoolean(nightmodeKey,false)){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean(nightmodeKey,true);
            editor.commit();
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putBoolean(nightmodeKey,false);
            editor.commit();
        }
        if (sharedPreferences.contains(customurikey)){
            LinearLayout LL1 = findViewById(R.id.LL1);
            LL1.setBackground(Drawable.createFromPath(sharedPreferences.getString(customurikey,"")));
        }
    }
    private void InitiateBackgroundMusic() {
        bacgroud_music=new Intent(getApplicationContext(),BackgroundMusic.class);
        startService(bacgroud_music);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_quit:
                putExitdialoge();
                break;
            case R.id.btn_start:
                next_activity=new Intent(MainActivity.this,LevelActivity.class);
                startActivity(next_activity);
                break;
            case R.id.btn_record:
                next_activity=new Intent(MainActivity.this, Records.class);
                startActivity(next_activity);
                break;
            case R.id.btn_settings:
                next_activity=new Intent(MainActivity.this,Settings.class);
                startActivity(next_activity);
                break;
        }
    }
}



