package com.example.grp_project;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.io.File;

public class Settings extends AppCompatActivity {
    SwitchCompat sw_night_mode;
    Button btn_photo,btn_reset;
    SeekBar sb_volume;
    AudioManager audioManager;
    LinearLayout LL4;
    SharedPreferences sharedPreferences;
    ServiceConnection musicConnection;
    Intent bacgroud_music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        LL4 =findViewById(R.id.LL4);
        set_background();
        InitiateSeekbars();
        InitiateNightModeSwitch();
        InitiateButtons();
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

    @Override
    protected void onStart() {
        super.onStart();
        bindService(bacgroud_music, musicConnection,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if ( resultCode == RESULT_OK && null != data && requestCode==1){
                String picturePath=null;
                Uri uri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                try {
                    Cursor c = getContentResolver().query(uri, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    if(picturePath==null) {
                        picturePath=uri.getPath();
                    }
                    //Use picturePath for setting bitmap  or to crop
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("CustomUriKey",picturePath);
                editor.apply();
                Log.d("SharedPref","Storing Path:"+picturePath);
                set_background();
            }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent i = new Intent(Settings.this,MainActivity.class);
            startActivity(i);
            return true;
        }else {
            return super.onKeyDown(keyCode,event);
        }
    }
    private void InitiateButtons() {
        btn_photo=findViewById(R.id.btn_background);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);}
        });

        btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setMessage("Do you want to reset?");
                builder.setTitle("Reset Now?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        sw_night_mode.setChecked(false);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,5,0);
                        sb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                        Intent intent = new Intent(Settings.this,Settings.class);
                        startActivity(intent);
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
        });


    }
    private void InitiateSeekbars() {
        sb_volume=findViewById(R.id.sb_volume);
        audioManager= (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        int max= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb_volume.setMax(max);
        sb_volume.setProgress(current);
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void InitiateNightModeSwitch() {
        sw_night_mode=findViewById(R.id.sw_night_mode);
        if (sharedPreferences.getBoolean("nightmodeKey",false)){
            sw_night_mode.setChecked(true);
        }

        sw_night_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sw_night_mode.setChecked(true);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putBoolean("nightmodeKey",true);
                    editor.commit();

                }else {
                    sw_night_mode.setChecked(false);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putBoolean("nightmodeKey",false);
                    editor.commit();

                }
                set_background();
            }
        });
    }
    public void set_background() {
        if (sharedPreferences.contains("CustomUriKey")) {
            Drawable d =Drawable.createFromPath(sharedPreferences.getString("CustomUriKey",""));
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
        bindService(bacgroud_music, musicConnection,0);

    }
}