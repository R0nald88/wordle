package com.example.wordle.Item;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wordle.R;

import java.util.Arrays;
import java.util.List;

import static com.example.wordle.Storage.Record.SHARED_PREF;

public enum Token implements Product {
	COIN() {
		@Override
		public int index() {
			return 0;
		}

		@Override
		public String describe() {
			return "Currency in game.";
		}

		@Override
		public String getName() {
			return "Coin";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_coin;
		}

		@Override
		public int getColorInt() {
			return R.color.yellow;
		}

		@Override
		public int getPrice() {
			return 0;
		}
	};

	public static final String TOKEN_FILE = "T";
	private static boolean IS_READ = false;
	private int number = 0;

	public abstract int index();
	@Override
	public int getNumber() { return number; }
	@Override
	public void setNumber(int number) { this.number = number; }
	public void writeFile(Context context) {write(context);}
	public Token readFile(Context context) {read(context); return this;}

	public static List<Token> read(Context context) {
		if (IS_READ) return Arrays.asList(Token.values());

		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		String[] file = sharedPreferences.getString(TOKEN_FILE, "").split(" ");

		for (Token b : Token.values()) if (b.index() < file.length)
			try {
				b.setNumber(Integer.parseInt(file[b.index()]));
			} catch (NumberFormatException e) {
				continue;
			}

		IS_READ = true;
		return Arrays.asList(Token.values());
	}

	public static String getString() {
		String s = "";
		for (Token b : Token.values()) s += b.getNumber() + " ";
		return s;
	}

	public static void write(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(TOKEN_FILE, getString());
		editor.apply();
	}
}
