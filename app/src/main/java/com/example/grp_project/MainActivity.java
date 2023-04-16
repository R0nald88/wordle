package com.example.grp_project;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.core.view.ViewCompat;

import com.example.grp_project.Storage.Record;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


   AppCompatButton btn_start,btn_settings,btn_record,btn_quit;
    Intent next_activity,bacgroud_music;
    SharedPreferences sharedPreferences;

    ServiceConnection musicConnection;

    String[] permisssions={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int permissioncode =1;
    //Key lists
    public static final String sharedPerferenceKey="sharedPerferenceKey";
    public static final String customurikey="CustomUriKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED) {
                requestPermissions(permisssions,permissioncode);
                Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show();
        }

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
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            InitiateButtons();
            InitiateSharedPreferences();
            InitiateBackgroundMusic();
            bindService(bacgroud_music, musicConnection,0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==permissioncode){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission","All permission granted");
                startActivity(new Intent(MainActivity.this,MainActivity.class));}
        }
    }

    private void putExitdialoge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to quit?");
        builder.setTitle("Quit Now?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopService(bacgroud_music);
                MainActivity.this.finishAffinity();
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
        btn_start=findViewById(R.id.btn_main_start);
        btn_start.setOnClickListener(this);
        btn_start.animate().rotationXBy(-360).setStartDelay(1000).setDuration(500).withStartAction(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setBackgroundTintList(btn_start, ContextCompat.getColorStateList(MainActivity.this, R.color.wordle_green));
            }
        });
        btn_record=findViewById(R.id.btn_main_record);
        btn_record.setOnClickListener(this);
        btn_record.animate().rotationXBy(-360).setStartDelay(1500).setDuration(500).withStartAction(new Runnable() {
            @Override
            public void run() {
                ViewCompat.setBackgroundTintList(btn_record, ContextCompat.getColorStateList(MainActivity.this , R.color.wordle_yellow));
            }
        });
        btn_settings=findViewById(R.id.btn_main_settings);
        btn_settings.setOnClickListener(this);
        btn_settings.animate().rotationXBy(-360).setStartDelay(2000).setDuration(500);
        btn_quit=findViewById(R.id.btn_main_quit);
        btn_quit.setOnClickListener(this);
        btn_quit.animate().rotationXBy(-360).setStartDelay(2500).setDuration(500);
    }
    private void InitiateSharedPreferences() {
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        if (sharedPreferences.contains("nightmodeKey")){
            if(sharedPreferences.getBoolean("nightmodeKey",false)){
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }
        if (sharedPreferences.contains("CustomUriKey")){
            LinearLayout LL1 = findViewById(R.id.LL1);
            Drawable d = Drawable.createFromPath(sharedPreferences.getString("CustomUriKey", ""));
            d.setAlpha(200);
            LL1.setBackground(d);
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_quit:
                putExitdialoge();
                break;
            case R.id.btn_main_start:
                next_activity=new Intent(MainActivity.this,LevelActivity.class);
                if (Record.readCurrentRecord(getApplicationContext())!=null){
                    AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Reset Record?");
                    builder.setMessage("Unsolved game found, do you want to reset your progress?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Record.clearCurrentRecord(getApplicationContext());
                            startActivity(next_activity);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(next_activity);
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
                next_activity=new Intent(MainActivity.this, Records.class);
                startActivity(next_activity);
                break;
            case R.id.btn_main_settings:
                next_activity=new Intent(MainActivity.this,Settings.class);
                startActivity(next_activity);
                break;
        }
    }

}



