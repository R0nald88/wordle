package com.example.wordle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordle.Storage.Record;

import java.util.ArrayList;
import java.util.List;

public class WordResultAdapter extends RecyclerView.Adapter<WordResultAdapter.ResultItemView> {
    private final List<Record> recordList;

    public WordResultAdapter(Context c) {
        this.recordList = Record.readeFlatten(c);
        Log.d("letter", recordList.toString() + "");
    }

    @NonNull
    @Override
    public ResultItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultItemView(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_record, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ResultItemView holder, int position) {
        holder.setView(recordList.get(position));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class ResultItemView extends RecyclerView.ViewHolder {
        private ImageView img_result, btn_search;
        private TextView txt_correct_word, txt_detail;
        public ResultItemView(@NonNull View itemView) {
            super(itemView);
            img_result = itemView.findViewById(R.id.img_result);
            btn_search = itemView.findViewById(R.id.btn_search);
            txt_correct_word = itemView.findViewById(R.id.txt_correct_word);
            txt_detail = itemView.findViewById(R.id.txt_detail);
        }

        public void setView(Record record) {
            btn_search.setOnClickListener((v) -> {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://dictionary.cambridge.org/dictionary/english/" +
                    record.getCorrectInput().toLowerCase()
                ));
                v.getContext().startActivity(i);
            });
            img_result.setImageResource(
                record.isEndless() ? R.drawable.ic_endless :
                record.isPassed() ? R.drawable.ic_correct : R.drawable.ic_wrong
            );
            img_result.setImageTintList(ColorStateList.valueOf(img_result.getContext().getColor(
                record.isEndless() ? R.color.grey :
                record.isPassed() ? R.color.green : R.color.red
            )));
            txt_correct_word.setText(record.getCorrectInput());
            txt_detail.setText("Step used: " + record.getGuessRecord().size() + "\nTime used: " + record.getTimeUsed());
        }
    }
}


