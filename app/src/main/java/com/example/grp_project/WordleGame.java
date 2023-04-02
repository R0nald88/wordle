package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WordleGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordle_game);
        getSupportActionBar().hide();

    }
}