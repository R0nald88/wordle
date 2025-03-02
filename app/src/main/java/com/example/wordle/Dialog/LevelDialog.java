package com.example.wordle.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordle.BoosterItemAdapter;
import com.example.wordle.Item.Booster;
import com.example.wordle.Item.Product;
import com.example.wordle.LevelActivity;
import com.example.wordle.R;
import com.example.wordle.RecordActivity;
import com.example.wordle.Storage.Level;
import com.example.wordle.WordleGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelDialog {
	private final Dialog dialog;
	private final Level level;
	private TextView[] txtReward;
	private TextView txtLevel;
	private LinearLayout layoutLevel;
	private final int levelInt;
	private ImageView imgGameMode;
	private RecyclerView listBooster;
	private Button btnStart, btnRecord, btnExit;
	private BoosterItemAdapter adapter;
	private final Activity activity;

	public LevelDialog(Activity context, int l) {
		this.dialog = new Dialog(context);

		dialog.setContentView(R.layout.dialog_level);
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		level = Level.getLevel(l);
		levelInt = l;
		activity = context;

		initView();
		setBoosterList();
		setViews();
		setRewardViews(level.getRewards());
	}

	private void setViews() {
		txtLevel.setText(level.getLevelString(levelInt));
		imgGameMode.setImageResource(level.getGameMode().getDrawableInt());
		layoutLevel.setBackgroundTintList(
			AppCompatResources.getColorStateList(dialog.getContext(), level.getGameMode().getColorInt())
		);

		btnExit.setOnClickListener(v -> layoutLevel.animate().translationY(300).alpha(0).setDuration(1000)
				.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(() -> {
							dialog.dismiss();
					if (!(activity instanceof LevelActivity)) {
						Intent i = new Intent(activity, LevelActivity.class);
						activity.startActivity(i);
						activity.finish();
					}
				}
				).start());

		btnStart.setOnClickListener(v -> {
			Intent i = new Intent(activity, WordleGame.class);
			i.putExtra(WordleGame.LEVEL, levelInt);
			i.putExtra(WordleGame.BOOSTER, adapter.getBoosters());
			Booster.write(activity);
			layoutLevel.animate().translationY(300).alpha(0).setDuration(1000)
					.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(
					() -> {
						if (levelInt == Level.DAILY) Level.setLastPlayDate(activity);
						activity.startActivity(i);
						activity.finish();
						dialog.dismiss();
					}
			).start();
		});

		//TODO start record act
		btnRecord.setOnClickListener(v -> {
			layoutLevel.animate().translationY(300).alpha(0).setDuration(1000)
					.setInterpolator(new FastOutSlowInInterpolator()).withEndAction(
						() -> {
							dialog.dismiss();
							Intent i = new Intent(activity, RecordActivity.class);
							activity.startActivity(i);
							activity.finish();

						}
					).start();
		});
	}

	private void setBoosterList() {
		adapter = new BoosterItemAdapter(activity);

		listBooster.setHasFixedSize(true);
		listBooster.setAdapter(adapter);
		listBooster.setLayoutManager(new LinearLayoutManager(dialog.getContext(), LinearLayoutManager.HORIZONTAL, false));
		listBooster.setItemAnimator(new DefaultItemAnimator());
	}

	private void initView() {
		txtReward = new TextView[4];
		txtReward[0] = dialog.findViewById(R.id.txt_reward_1);
		txtReward[1] = dialog.findViewById(R.id.txt_reward_3);
		txtReward[2] = dialog.findViewById(R.id.txt_reward_2);
		txtReward[3] = dialog.findViewById(R.id.txt_reward_4);

		btnStart = dialog.findViewById(R.id.btn_next_level);
		btnRecord = dialog.findViewById(R.id.btn_record);
		btnExit = dialog.findViewById(R.id.btn_exit);

		txtLevel = dialog.findViewById(R.id.txt_level);
		imgGameMode = dialog.findViewById(R.id.img_game_mode);
		listBooster = dialog.findViewById(R.id.list_booster);
		layoutLevel = dialog.findViewById(R.id.layout_level);
	}

	private void setRewardViews(Map<Product, Integer> r) {
		List<Map.Entry<Product, Integer>> rewards = new ArrayList<>(r.entrySet());
		for (int a = 0; a < txtReward.length; a++) {
			if (a < rewards.size()) {
				txtReward[a].setText("\t" + rewards.get(a).getKey().getName() + ": " + rewards.get(a).getValue());
				txtReward[a].setCompoundDrawablesRelativeWithIntrinsicBounds(
						dialog.getContext().getDrawable(rewards.get(a).getKey().getDrawableInt()),
						null, null, null);
			} else txtReward[a].setVisibility(View.GONE);
		}
	}

	public void show() {
		dialog.show();
		layoutLevel.setScaleX(0.5f);
		layoutLevel.setScaleY(0.5f);
		layoutLevel.animate().scaleX(1).scaleY(1)
				.setDuration(1000)
				.setInterpolator(new BounceInterpolator()).start();
	}
}
