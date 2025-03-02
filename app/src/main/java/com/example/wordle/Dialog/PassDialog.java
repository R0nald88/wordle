package com.example.wordle.Dialog;

import android.content.Intent;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.wordle.R;
import com.example.wordle.RecordActivity;
import com.example.wordle.Storage.Level;
import com.example.wordle.WordleGame;

public class PassDialog extends ResultDialog {
	public PassDialog(WordleGame w) {super(w);}

	@Override
	protected void setResultView() {
		wordleGame.getWordleState().getRecord().getLevel().grantReward(wordleGame);
		imgResult.setImageResource(R.drawable.ic_pass);
		txtTitle.setText("You Passed!!!");
		layoutResult.setBackgroundTintList(wordleGame.getColorStateList(R.color.green));
		Level.addCurrentLevel(wordleGame, wordleGame.getWordleState().getRecord().getLevelInt() + 1);
		btnNextLevel.setText("Play On");
		txtCorrectWord.setText("\tWord: " + wordleGame.getWordleState().getRecord().getCorrectInput());
		setRewardViews(wordleGame.getWordleState().getRecord().getLevel().getRewards());

		btnNextLevel.setOnClickListener(v ->
				layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
						.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
							new LevelDialog(wordleGame, Level.getCurrentLevel(wordleGame) - 1).show();
							dialog.dismiss();
						}
				).start()
		);

		// TODO add record act
		btnRecord.setOnClickListener(v ->
				layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
						.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
							dialog.dismiss();
							Intent i = new Intent(wordleGame, RecordActivity.class);
							wordleGame.startActivity(i);
							wordleGame.finish();
						}
				).start()
		);
	}
}
