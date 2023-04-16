package com.example.grp_project;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grp_project.Storage.Record;

public class Dialog_word_record extends AppCompatActivity {

    //private final Dialog dialog;
    ListView lv_WordRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_word_record);
        WordListAdapter adapter = new WordListAdapter(this, Record.read(getApplicationContext()));


    }


    /*public Dialog_word_record(Dialog dialog) {
        this.dialog = dialog;
    }*/
}

