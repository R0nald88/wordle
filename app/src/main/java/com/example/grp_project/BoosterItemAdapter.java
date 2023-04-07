package com.example.grp_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grp_project.Item.Booster;

import java.util.ArrayList;
import java.util.List;

public class BoosterItemAdapter extends RecyclerView.Adapter<BoosterItemAdapter.BoosterViewHolder> {
	private final List<Booster> boosters;
	private final Activity activity;

	public BoosterItemAdapter(Activity c) {
		boosters = new ArrayList<>();
		activity = c;
	}

	@NonNull
	@Override
	public BoosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new BoosterViewHolder(
				LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booster, parent, false)
		);
	}

	@Override
	public void onBindViewHolder(@NonNull BoosterViewHolder holder, int position) {
		holder.setView(Booster.values()[position]);
	}

	@Override
	public int getItemCount() {
		return Booster.values().length;
	}

	public int getBoosters() {
		return Booster.listToInt(boosters);
	}

	public class BoosterViewHolder extends RecyclerView.ViewHolder {
		private final ImageView imgBooster;
		private final TextView txtNumber;
		private boolean isClicked, isEnough;

		public BoosterViewHolder(@NonNull View itemView) {
			super(itemView);
			imgBooster = itemView.findViewById(R.id.img_booster);
			txtNumber = itemView.findViewById(R.id.txt_number);
			isClicked = false;
		}

		private void setView(Booster booster) {
			imgBooster.setImageResource(booster.getDrawableInt());
			isEnough = booster.isEnough(1);
			txtNumber.setText(booster.getNumber() + "");

			itemView.setOnLongClickListener(v -> {
				AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext())
						.setIcon(booster.getDrawableInt())
						.setCancelable(true)
						.setTitle(booster.getName())
						.setMessage(booster.describe() + "\nYou now have " + booster.getNumber())
						.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
						.create();
				alertDialog.show();
				return true;
			});

			itemView.setOnClickListener(v -> {
				if (!isClicked && isEnough) {
					imgBooster.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.green));
					txtNumber.setVisibility(View.GONE);
					booster.decreaseBy(itemView.getContext(), 1);
					boosters.add(booster);
					isClicked = !isClicked;
				} else if (isClicked) {
					imgBooster.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.white));
					txtNumber.setVisibility(View.VISIBLE);
					booster.increaseBy(imgBooster.getContext(), 1);
					boosters.remove(booster);
					isClicked = !isClicked;
				} else {
					Toast.makeText(itemView.getContext(), booster.getNotEnoughString(), Toast.LENGTH_SHORT).show();
					StoreActivity.setListener(v1 -> {
						txtNumber.setText(booster.getNumber() + "");
					});
					Intent i = new Intent(activity, StoreActivity.class);
					i.putExtra(StoreActivity.SCROLL_PRODUCT, booster.index());
					activity.startActivity(i);
				}

				imgBooster.setScaleX(0.75f);
				imgBooster.setScaleX(0.75f);
				imgBooster.animate().scaleY(1).scaleX(1).setInterpolator(new BounceInterpolator()).start();
			});
		}
	}
}
