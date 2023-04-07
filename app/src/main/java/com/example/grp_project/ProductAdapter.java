package com.example.grp_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grp_project.Item.Booster;
import com.example.grp_project.Item.Product;
import com.example.grp_project.Item.Token;

import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemView> {
	private final List<Product> products;
	private final View.OnClickListener listener;

	public ProductAdapter(View.OnClickListener l) {
		products = Arrays.asList(Booster.values());
		listener = l;
	}

	@NonNull
	@Override
	public ProductItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ProductItemView(
				LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false)
		);
	}

	@Override
	public void onBindViewHolder(@NonNull ProductAdapter.ProductItemView holder, int position) {
		holder.setView(products.get(position), listener);
	}

	@Override
	public int getItemCount() {
		return products.size();
	}

	public int getProductIndex(Product product) {
		return products.indexOf(product);
	}

	public static class ProductItemView extends RecyclerView.ViewHolder {
		private final ImageView imgProduct;
		private final TextView txtProductName, txtProductDescription;
		private final Button btnBuy;

		public ProductItemView(@NonNull View itemView) {
			super(itemView);

			imgProduct = itemView.findViewById(R.id.img_product);
			txtProductName = itemView.findViewById(R.id.txt_product_name);
			txtProductDescription = itemView.findViewById(R.id.txt_product_describe);
			btnBuy = itemView.findViewById(R.id.btn_buy);
		}

		public void setView(Product product, View.OnClickListener l) {
			btnBuy.setText(product.getPrice() + "");
			btnBuy.setOnClickListener(v -> {
				if (Token.COIN.decreaseBy(itemView.getContext(), product.getPrice())) { {
					product.increaseBy(itemView.getContext(), 1);
						Toast.makeText(itemView.getContext(), "Successfully purchased " + product.getName(),
								Toast.LENGTH_SHORT).show();
					}
					if (l != null) l.onClick(v);
				}
			});

			txtProductName.setText(product.getName());
			txtProductDescription.setText(product.describe());
			imgProduct.setImageResource(product.getDrawableInt());
		}
	}
}
