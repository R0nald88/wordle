package com.example.grp_project;

import android.app.Dialog;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grp_project.Storage.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Dialog_word_record extends AppCompatActivity {

    //private final Dialog dialog;
    ListView lv_WordRecord;

    String[] testString = {"test", "testing", "test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test","test", "test",};

    ArrayList<String> testArray = new ArrayList<String>(Arrays.asList(testString));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_word_record);
        lv_WordRecord = findViewById(R.id.lv_WordRecord);
        WordListAdapter adapter = new WordListAdapter(this, testArray);
        lv_WordRecord.setAdapter(adapter);
    }


    /*public Dialog_word_record(Dialog dialog) {
        this.dialog = dialog;
    }*/
}

