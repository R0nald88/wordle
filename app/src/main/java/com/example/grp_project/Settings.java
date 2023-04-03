package com.example.grp_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.io.File;
import java.net.URI;

public class Settings extends AppCompatActivity {
    SwitchCompat sw_night_mode;
    Button btn_background,btn_reset;
    LinearLayout LL4;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        getSupportActionBar().hide();
        InitiateNightModeSwitch();
        InitiateButtons();
        LL4 =findViewById(R.id.LL4);
        set_background();
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
        btn_background=findViewById(R.id.btn_background);
        btn_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Title"),1);
            }


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
                        editor.remove("nightmodeKey");
                        editor.clear();
                        editor.apply();
                        editor.remove("CustomUriKey");
                        editor.clear();
                        editor.apply();
                        sw_night_mode.setChecked(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
                Uri uri = data.getData();
                SharedPreferences.Editor editor= sharedPreferences.edit();
                String path=getRealPathFromURI(getApplicationContext(),uri);
                editor.putString("CustomUriKey",path);
                editor.commit();
                LL4.setBackground(Drawable.createFromPath(sharedPreferences.getString("CustomUriKey","")));

        }
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public void set_background() {
        if (sharedPreferences.contains("CustomUriKey")) {
            LL4.setBackground(Drawable.createFromPath(sharedPreferences.getString("CustomUriKey", "")));
            LL4.setAlpha(0.7F);
        }
        if (sharedPreferences.contains("nightmodeKey")){
            if(sharedPreferences.getBoolean("nightmodeKey",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }

    }
}
