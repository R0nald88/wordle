package com.example.grp_project.Dialog;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.grp_project.R;
import com.example.grp_project.Storage.Level;
import com.example.grp_project.WordleGame;

public class PassDialog extends ResultDialog {
	public PassDialog(WordleGame w) {
		super(w);
	}

	@Override
	protected void setResultView() {
		wordleGame.getWordleState().getRecord().getLevel().grantReward(wordleGame);
		imgResult.setImageResource(R.drawable.ic_pass);
		imgResult.setImageTintList(wordleGame.getColorStateList(R.color.green));
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
						.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(
						dialog::dismiss
				).start()
		);
	}
}
