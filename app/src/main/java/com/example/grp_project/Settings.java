package com.example.grp_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class Settings extends AppCompatActivity {
    SwitchCompat sw_night_mode;

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