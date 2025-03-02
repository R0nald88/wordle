package com.example.wordle.Dialog;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.wordle.Item.Token;
import com.example.wordle.R;
import com.example.wordle.WordleGame;

public class LostDialog extends ResultDialog {

	public LostDialog(WordleGame w) {
		super(w);
	}

	@Override
	protected void setResultView() {
		imgResult.setImageResource(R.drawable.ic_lost);
		txtTitle.setText("You Lost...");
		txtCorrectWord.setText("\tWord: -");
		layoutResult.setBackgroundTintList(wordleGame.getColorStateList(R.color.red));

		setRewardViews(null);
		btnNextLevel.setText("Play On (60 coins)");
		btnNextLevel.setOnClickListener(v -> {
					if (Token.COIN.decreaseBy(wordleGame, 60))
						layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
								.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(dialog::dismiss
						).start();
				}
		);

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
