package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Records extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
        set_background();

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
}