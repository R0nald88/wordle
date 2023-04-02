package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    Switch sw_night_mode;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        getSupportActionBar().hide();
        InitiateNightModeSwitch();

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
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sw_night_mode.setChecked(true);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putBoolean("nightmodeKey",true);
                    editor.commit();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sw_night_mode.setChecked(false);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putBoolean("nightmodeKey",false);
                    editor.commit();
                }
            }
        });
    }
}