package com.example.grp_project.Dialog;

import android.widget.Toast;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.grp_project.Item.Token;
import com.example.grp_project.R;
import com.example.grp_project.WordleGame;

public class LostDialog extends ResultDialog {

	public LostDialog(WordleGame w) {
		super(w);
	}

	@Override
	protected void setResultView() {
		imgResult.setImageResource(R.drawable.ic_lost);
		imgResult.setImageTintList(wordleGame.getColorStateList(R.color.red));
		txtTitle.setText("You Lost...");
		txtCorrectWord.setText("\tWord: -");
		layoutResult.setBackgroundTintList(wordleGame.getColorStateList(R.color.red));

		setRewardViews(null);

		btnNextLevel.setCompoundDrawablesRelativeWithIntrinsicBounds(
				null, wordleGame.getDrawable(R.drawable.ic_continue),
				null, null);
		btnNextLevel.setText("Play On\n60 coins");
		btnNextLevel.setOnClickListener(v -> {
					if (Token.COIN.decreaseBy(wordleGame, 60))
						layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
								.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(dialog::dismiss
						).start();
				}
		);

		btnRecord.setCompoundDrawablesRelativeWithIntrinsicBounds(
				null, wordleGame.getDrawable(R.drawable.ic_restart),
				null, null);
		btnRecord.setText("Retry");
		btnRecord.setOnClickListener(v ->
				layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
						.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
							new LevelDialog(wordleGame, wordleGame.getWordleState().getRecord().getLevelInt()).show();
							dialog.dismiss();
						}
				).start()
		);
	}
}
