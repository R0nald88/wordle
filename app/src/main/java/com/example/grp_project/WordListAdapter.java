package com.example.grp_project;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.grp_project.R;
import com.example.grp_project.Storage.Record;

import java.util.ArrayList;
import java.util.List;

public class WordListAdapter {
    private final Activity context;
    private final List<Record> word_list;
    private Button ib_Search;


    public WordListAdapter(Activity context, List<Record> word_list) {
        this.context = context;
        this.word_list = word_list;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_word_record, null, true);
        TextView tv_WordDisplay = rowView.findViewById(R.id.tv_WordDisplay);
        ib_Search = rowView.findViewById(R.id.ib_Search);
        ib_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://dictionary.cambridge.org/dictionary/english/" + tv_WordDisplay.getText()));
            }
        });
        return rowView;
    }
}


