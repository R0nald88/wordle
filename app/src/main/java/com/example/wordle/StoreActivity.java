package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordle.Item.Token;

public class StoreActivity extends AppCompatActivity {
	private ImageView btnReturn;
	private TextView txtCoin;
	private RecyclerView listProduct;
	private Intent bacgroud_music;
	private SharedPreferences sharedPreferences;
	private ServiceConnection musicConnection;
	private boolean serviceBound = false;
	private static View.OnClickListener listener;
	public static final String SCROLL_PRODUCT = "s";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);

		initView();
		txtCoin.setText(Token.COIN.getNumber() + "");
		btnReturn.setOnClickListener(v -> finish());
		setListProduct();
		if (getIntent().hasExtra(SCROLL_PRODUCT) &&
				getIntent().getIntExtra(SCROLL_PRODUCT, -1) >= 0)
			listProduct.scrollToPosition(getIntent().getIntExtra(SCROLL_PRODUCT, -1));
		InitiateBackgroundMusic();
	}

	private void initView() {
		btnReturn = findViewById(R.id.btn_return);
		txtCoin = findViewById(R.id.txt_coin);
		listProduct = findViewById(R.id.list_product);
	}

	private void setListProduct() {
		listProduct.setItemAnimator(new DefaultItemAnimator());
		listProduct.setLayoutManager(new LinearLayoutManager(this));
		listProduct.setHasFixedSize(true);
		listProduct.setAdapter(new ProductAdapter(v -> {
			txtCoin.setText(Token.COIN.getNumber() + "");
			if (listener != null) listener.onClick(v);
		}));
	}

	public static void setListener(View.OnClickListener listener) {
		StoreActivity.listener = listener;
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

	@Override
	protected void onPause() {
		super.onPause();
		if (serviceBound && musicConnection != null) {
			unbindService(musicConnection);
			serviceBound = false;
		}
	}
}
