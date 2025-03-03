package com.example.wordle.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.wordle.LevelActivity;
import com.example.wordle.MainActivity;
import com.example.wordle.R;
import com.example.wordle.WordleGame;

public class PauseDialog {
	private final Dialog pauseDialog;
	private ImageButton btnContinue, btnExit, btnRestart;
	private final OnRestartListener listener;
	private final WordleGame wordle;

	public PauseDialog(WordleGame context, @NonNull OnRestartListener l) {
		listener = l;
		wordle = context;

		pauseDialog = new Dialog(context);
		pauseDialog.setContentView(R.layout.dialog_pause);
		pauseDialog.setCancelable(true);
		pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pauseDialog.setOnCancelListener(i -> onContinue());

		initView();
	}

	private void initView() {
		btnContinue = pauseDialog.findViewById(R.id.btn_continue);
		btnContinue.setOnClickListener(v -> onContinue());

		btnExit = pauseDialog.findViewById(R.id.btn_exit);
		btnExit.setOnClickListener(v -> onExit());

		btnRestart = pauseDialog.findViewById(R.id.btn_restart);
		btnRestart.setOnClickListener(v -> onRestart());
	}

	private void onContinue() {
		pauseDialog.dismiss();
		listener.onRestart();
	}

	private void onExit() {
		wordle.getWordleState().getRecord().write(wordle);
		wordle.startActivity(new Intent(wordle, LevelActivity.class));
		pauseDialog.dismiss();
		wordle.finish();
	}

	private void onRestart() {
		wordle.getWordleState().getRecord().write(wordle);

		Intent i = new Intent(wordle, LevelActivity.class);
		i.putExtra(WordleGame.BOOSTER, 0);
		i.putExtra(WordleGame.LEVEL, wordle.getWordleState().getRecord().getLevelInt());

		pauseDialog.dismiss();
		wordle.startActivity(i);
		wordle.finish();
	}

	public void show() {
		pauseDialog.show();
	}

	public interface OnRestartListener{
		void onRestart();
	}
}
