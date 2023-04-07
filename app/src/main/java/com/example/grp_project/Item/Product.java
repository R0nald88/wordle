package com.example.grp_project.Item;

import android.content.Context;
import android.widget.Toast;

public interface Product extends GameItem{
	int getPrice();
	void setNumber(int n);
	int getNumber();
	void writeFile(Context context);

	default void increaseBy(Context context, int n) {
		setNumber(getNumber() + n);
		writeFile(context);
	}
	default boolean decreaseBy(Context context, int n) {
		if (getNumber() - n >= 0) {
			setNumber(getNumber() - n);
			writeFile(context);
			return true;
		} else {
			Toast.makeText(context, getName() + " is not enough. Buy some in store!", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	default boolean isEnough(int n) {return getNumber() - n >= 0;}
	default String getNotEnoughString() {return getName() + " is not enough. Buy some in store!";}
}
