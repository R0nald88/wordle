package com.example.grp_project;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grp_project.Item.Token;

public class StoreActivity extends AppCompatActivity {
	private ImageView btnReturn;
	private TextView txtCoin;
	private RecyclerView listProduct;
	private Intent bacgroud_music;
	private SharedPreferences sharedPreferences;
	private ServiceConnection musicConnection;
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
		InitiateBackground();
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
		bindService(bacgroud_music, musicConnection,0);

	}

	private void InitiateBackground() {
		sharedPreferences=getSharedPreferences("sharedPerferenceKey",MODE_PRIVATE);
		if (sharedPreferences.contains("nightmodeKey")){
			if(sharedPreferences.getBoolean("nightmodeKey",false)){
				getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
			}else {
				getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

			}
			}
			if (sharedPreferences.contains("CustomUriKey")){
				LinearLayout ll = findViewById(R.id.store_ll);
				Drawable d = Drawable.createFromPath(sharedPreferences.getString("CustomUriKey", ""));
				d.setAlpha(200);
				ll.setBackground(d);
			}

	}
}
