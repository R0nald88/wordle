package com.example.wordle.Item;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public interface GameItem {
	String describe();
	String getName();
	@DrawableRes int getDrawableInt();
	@ColorRes int getColorInt();
	default ColorStateList getColor(Context c) {
		return ColorStateList.valueOf(c.getColor(getColorInt()));
	}

	default Drawable getDrawable(Context c) {
		return c.getDrawable(getDrawableInt());
	}
}
