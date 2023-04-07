package com.example.grp_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grp_project.Item.Token;

public class StoreActivity extends AppCompatActivity {
	private ImageView btnReturn;
	private TextView txtCoin;
	private RecyclerView listProduct;
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
}
