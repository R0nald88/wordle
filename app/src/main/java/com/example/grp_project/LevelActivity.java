package com.rcheung.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rcheung.wordle.Dialog.LevelDialog;
import com.rcheung.wordle.Item.Booster;
import com.rcheung.wordle.Item.Token;
import com.rcheung.wordle.Storage.Level;

public class LevelActivity extends AppCompatActivity {
	private LevelPathView viewLevel;
	private ImageView btnShop, btnPlay, btnEndless, btnRecord, btnDaily;
	private TextView txtCoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		Booster.read(this);
		Token.read(this);

		initView();

		Log.d("letter", Level.getCurrentLevel(LevelActivity.this) + "");

		txtCoin.setText(Token.COIN.readFile(this).getNumber() + "");

		viewLevel.setCurrentLevel(Level.getCurrentLevel(this));
		viewLevel.setLevelClickListener(level -> new LevelDialog(LevelActivity.this, level).show());

		//TODO start record act
		btnShop.setOnClickListener(v -> {
			StoreActivity.setListener(v1 -> {
				if (txtCoin != null) txtCoin.setText(Token.COIN.getNumber() + "");
			});
			Intent i = new Intent(LevelActivity.this, StoreActivity.class);
			i.putExtra(StoreActivity.SCROLL_PRODUCT, -1);
			startActivity(i);
		});
		txtCoin.setOnClickListener(v -> btnShop.callOnClick());

		btnEndless.setOnClickListener(v -> new LevelDialog(LevelActivity.this, Level.ENDLESS).show());
		btnDaily.setOnClickListener(v -> {
			if (Level.isNewDate(LevelActivity.this))
				new LevelDialog(LevelActivity.this, Level.DAILY).show();
			else
				Toast.makeText(LevelActivity.this,
						"Daily mode is already played. Please wait for " + Level.waitFor(LevelActivity.this) + " for the next round",
						Toast.LENGTH_LONG).show();
		});
		btnPlay.setOnClickListener(v -> new LevelDialog(LevelActivity.this, Level.getCurrentLevel(LevelActivity.this) - 1).show());
	}

	private void initView() {
		viewLevel = findViewById(R.id.view_level);
		btnDaily = findViewById(R.id.btn_daily);
		btnShop = findViewById(R.id.btn_store);
		btnPlay = findViewById(R.id.btn_play);
		btnRecord = findViewById(R.id.btn_record);
		btnEndless = findViewById(R.id.btn_endless);
		txtCoin = findViewById(R.id.txt_coin);
	}
}