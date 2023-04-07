package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grp_project.Dialog.LevelDialog;
import com.example.grp_project.Item.Booster;
import com.example.grp_project.Item.Token;
import com.example.grp_project.Storage.Level;

public class LevelActivity extends AppCompatActivity {
	private LevelPathView viewLevel;
	private ImageView btnShop, btnPlay, btnEndless, btnRecord, btnDaily;
	private TextView txtCoin;

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		Booster.read(this);
		Token.read(this);
		sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
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
		if (sharedPreferences.contains("CustomUriKey")){
			viewLevel.setBackground(Drawable.createFromPath(sharedPreferences.getString("CustomUriKey","")));

		}
		btnDaily = findViewById(R.id.btn_daily);
		btnShop = findViewById(R.id.btn_store);
		btnPlay = findViewById(R.id.btn_play);
		btnRecord = findViewById(R.id.btn_record);
		btnEndless = findViewById(R.id.btn_endless);
		txtCoin = findViewById(R.id.txt_coin);
	}
}
