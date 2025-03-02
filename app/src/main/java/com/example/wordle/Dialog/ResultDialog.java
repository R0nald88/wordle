package com.example.wordle.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.wordle.Item.Product;
import com.example.wordle.Item.Token;
import com.example.wordle.LevelActivity;
import com.example.wordle.R;
import com.example.wordle.WordleGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ResultDialog {
	protected final WordleGame wordleGame;
	protected final Dialog dialog;
	protected TextView txtLevel, txtDate, txtCorrectWord, txtStep, txtTitle, txtCoin;
	protected TextView[] txtReward;
	protected Button btnNextLevel, btnRecord, btnExit;
	protected ImageView imgResult;
	protected LinearLayout layoutRewards, layoutResult, layoutTransition;

	public ResultDialog(WordleGame w) {
		wordleGame = w;
		w.getWordleState().getRecord().write(w);

		dialog = new Dialog(w);
		dialog.setContentView(R.layout.dialog_pass);
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		initView();
		setResultView();
		setBasicViews();
	}

	private void initView() {
		txtDate = dialog.findViewById(R.id.txt_date);
		txtStep = dialog.findViewById(R.id.txt_step);
		txtCorrectWord = dialog.findViewById(R.id.txt_correct_word);
		txtLevel = dialog.findViewById(R.id.txt_level);
		txtTitle = dialog.findViewById(R.id.txt_title);
		btnNextLevel = dialog.findViewById(R.id.btn_next_level);
		btnRecord = dialog.findViewById(R.id.btn_record);
		btnExit = dialog.findViewById(R.id.btn_exit);
		imgResult = dialog.findViewById(R.id.img_result);
		layoutRewards = dialog.findViewById(R.id.layout_rewards);
		layoutResult = dialog.findViewById(R.id.layout_result);
		layoutTransition = dialog.findViewById(R.id.layout_transition);

		txtReward = new TextView[4];
		txtReward[0] = dialog.findViewById(R.id.txt_reward_1);
		txtReward[1] = dialog.findViewById(R.id.txt_reward_3);
		txtReward[2] = dialog.findViewById(R.id.txt_reward_2);
		txtReward[3] = dialog.findViewById(R.id.txt_reward_4);
		txtCoin = dialog.findViewById(R.id.txt_coin);
	}

	private void setBasicViews() {
		txtDate.setText("\tDate: " + wordleGame.getWordleState().getRecord().getDate());
		txtStep.setText("\tStep: " + wordleGame.getWordleState().getRecord().getGuessRecord().size() + "");
		txtLevel.setText("\t" +
				wordleGame.getWordleState().getRecord().getLevel().getLevelString(
						wordleGame.getWordleState().getRecord().getLevelInt()
				));

		btnExit.setOnClickListener(v ->
			layoutResult.animate().translationY(300).alpha(0).setDuration(1000)
					.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
						dialog.dismiss();
						Intent i = new Intent(wordleGame, LevelActivity.class);
						wordleGame.startActivity(i);
						wordleGame.finish();
					}
			).start()
		);

		txtCoin.setText(Token.COIN.getNumber() + "");
	}

	protected void setRewardViews(Map<Product, Integer> r) {
		if (r == null) {
			layoutRewards.setVisibility(View.GONE);
			return;
		}

		List<Map.Entry<Product, Integer>> rewards = new ArrayList<>(r.entrySet());
		for (int a = 0; a < txtReward.length; a++) {
			if (a < rewards.size()) {
				txtReward[a].setText("\t" + rewards.get(a).getKey().getName() + ": " + rewards.get(a).getValue());
				txtReward[a].setCompoundDrawablesRelativeWithIntrinsicBounds(
						wordleGame.getDrawable(rewards.get(a).getKey().getDrawableInt()),
						null, null, null);
			} else txtReward[a].setVisibility(View.GONE);
		}
	}

	public void show() {
		dialog.show();
		layoutTransition.setTranslationY(300);
		layoutTransition.setAlpha(0);
		layoutResult.setAlpha(0);
		layoutResult.setScaleX(1.25f);
		layoutResult.setScaleY(1.25f);

		layoutResult.animate().scaleX(1).scaleY(1).alpha(1)
				.setDuration(1000)
				.setInterpolator(new FastOutSlowInInterpolator())
				.withEndAction(() ->
						layoutTransition.animate()
								.alpha(1).translationY(0)
								.setInterpolator(new FastOutSlowInInterpolator())
								.setDuration(1000)
								.start()
				).start();
	}

	protected abstract void setResultView();
}
