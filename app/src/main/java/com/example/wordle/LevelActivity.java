package com.example.wordle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordle.Dialog.LevelDialog;
import com.example.wordle.Item.Booster;
import com.example.wordle.Item.Token;
import com.example.wordle.Storage.Level;
import com.example.wordle.Storage.Record;

public class LevelActivity extends AppCompatActivity {
	private LevelPathView viewLevel;
	private ImageView btnShop, btnPlay, btnEndless, btnRecord, btnDaily;
	private TextView txtCoin;
	private Intent bacgroud_music;
	private boolean serviceBound = false;
	private ServiceConnection musicConnection;

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
		viewLevel.setLevelClickListener((level, region) -> {
			if (region) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LevelActivity.this);
				builder.setTitle("Next Region");
				builder.setMessage("Congratulation! You have completed this region!\nReady for next region?");
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						startActivity(new Intent(LevelActivity.this, LevelActivity.class));
						dialogInterface.dismiss();
						LevelActivity.this.finish();
					}
				});

				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				AlertDialog alertDialog=builder.create();
				alertDialog.show();
				return;
			}
			new LevelDialog(LevelActivity.this, level).show();
		});

		btnRecord.setOnClickListener(v -> {
			Intent i = new Intent(LevelActivity.this, RecordActivity.class);
			startActivity(i);
		});

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
		InitiateBackgroundMusic();
	}
	protected void onPause() {
		super.onPause();

		if (serviceBound && musicConnection != null) {
			unbindService(musicConnection);
			serviceBound = false;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		serviceBound = bindService(bacgroud_music, musicConnection,0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		serviceBound = bindService(bacgroud_music, musicConnection,0);
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

	private void InitiateBackgroundMusic() {
		bacgroud_music=new Intent(getApplicationContext(),BackgroundMusic.class);
		musicConnection =new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {

			}
		};
		startService(bacgroud_music);
		serviceBound = bindService(bacgroud_music, musicConnection,0);

	}
}
