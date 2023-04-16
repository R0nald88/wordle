package com.example.grp_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class WordListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> word_list;
    private ImageButton ib_Search;


    public WordListAdapter(Activity context, ArrayList<String> word_list) {
        super(context, R.layout.listview_word_record, word_list);
        this.context = context;
        this.word_list = word_list;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_word_record, null, true);

        TextView tv_WordDisplay = rowView.findViewById(R.id.tv_WordDisplay);
        tv_WordDisplay.setText(word_list.get(position));

        ib_Search = rowView.findViewById(R.id.ib_Search);
        ib_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://dictionary.cambridge.org/dictionary/english/" + tv_WordDisplay.getText()));
                context.startActivity(i);
            }
        });
        return rowView;
    }
}


