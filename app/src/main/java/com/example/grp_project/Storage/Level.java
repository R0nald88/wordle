package com.example.grp_project.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.example.grp_project.Item.Booster;
import com.example.grp_project.Item.GameMode;
import com.example.grp_project.Item.Product;
import com.example.grp_project.Item.Token;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.example.grp_project.Storage.Record.SHARED_PREF;

public class Level {
	public static final int ENDLESS = -1, DAILY = -2;
	public static final String LEVEL_FILE = "G", DATE_FILE = "D";
	private static int CURRENT_LEVEL = 1;
	private static long LAST_PLAY_DATE = 0;
	private static boolean IS_READ = false;

	private final GameMode gameMode;
	private final Map<Product, Integer> rewards;

	public static final Level[] LEVELS = new Level[] {
			new Level(GameMode.STEP_MODE, new Product[]{Token.COIN, Booster.ADD_CHANCE}, new int[]{10, 2}),
			new Level(GameMode.STEP_MODE, new Product[]{Token.COIN, Booster.REVEAL_POSITION}, new int[]{10, 1}),
			new Level(GameMode.TIME_MODE, new Product[]{Token.COIN, Booster.REVEAL_WORD}, new int[]{10, 3}),
			new Level(GameMode.STEP_MODE, new Product[]{Token.COIN, Booster.AUTO_CORRECT}, new int[]{10, 1}),
			new Level(GameMode.TIME_MODE, new Product[]{Booster.REVEAL_WORD, Booster.ADD_CHANCE}, new int[]{1, 2}),
			new Level(GameMode.STEP_MODE, new Product[]{Booster.REVEAL_WORD, Booster.REVEAL_POSITION}, new int[]{2, 1}),
			new Level(GameMode.STEP_MODE, new Product[]{Booster.REVEAL_POSITION, Booster.ADD_CHANCE}, new int[]{1, 2}),
			new Level(GameMode.STEP_MODE, new Product[]{Booster.AUTO_CORRECT, Booster.ADD_CHANCE}, new int[]{1, 1}),
			new Level(GameMode.TIME_MODE, new Product[]{Booster.REVEAL_WORD, Booster.AUTO_CORRECT}, new int[]{1, 1}),
			new Level(GameMode.TIME_MODE, new Product[]{Booster.REVEAL_POSITION, Booster.AUTO_CORRECT}, new int[]{2, 1}),
			new Level(GameMode.TIME_MODE, new Product[]{Token.COIN, Booster.AUTO_CORRECT}, new int[]{5, 3}),
			new Level(GameMode.TIME_MODE, new Product[]{Token.COIN, Booster.REVEAL_WORD}, new int[]{5, 5})
	};

	public static Level getLevel(int l) {
		if (l == ENDLESS) {
			Level level = LEVELS[new Random().nextInt(LEVELS.length)];
			return new Level(GameMode.ENDLESS_MODE, level.rewards);
		} else if (l == DAILY) {
			Level level = LEVELS[new Random().nextInt(LEVELS.length)];
			return new Level(GameMode.DAILY_MODE, level.rewards);
		}

		return LEVELS[l % LEVELS.length];
	}

	private Level(GameMode gameMode, Product[] products, int[] number) {
		this.gameMode = gameMode;
		rewards = new HashMap<>();
		for (int a = 0; a < products.length; a++)
			rewards.put(products[a], number[a]);
	}

	private Level(GameMode gameMode, Map<Product, Integer> number) {
		this.gameMode = gameMode;
		rewards = number;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public Map<Product, Integer> getRewards() {
		return rewards;
	}

	public void grantReward(Context c) {
		for (Map.Entry<Product, Integer> e : rewards.entrySet()) {
			e.getKey().increaseBy(c, e.getValue());
		}
	}

	public String getLevelString(int l) {
		if (gameMode == GameMode.ENDLESS_MODE) return "Endless Mode";
		if (gameMode == GameMode.DAILY_MODE) return "Daily Mode";
		return "Level " + ++l;
	}

	public static void read(Context context) {
		if (IS_READ) return;

		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		try {
			LAST_PLAY_DATE = sharedPreferences.getLong(DATE_FILE, milliToDate());
		} catch (Exception e) {
			LAST_PLAY_DATE = sharedPreferences.getInt(DATE_FILE, (int) milliToDate());
		}

		CURRENT_LEVEL = sharedPreferences.getInt(LEVEL_FILE, 1);
		IS_READ = true;
	}

	public static void write(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(DATE_FILE, LAST_PLAY_DATE);
		editor.putInt(LEVEL_FILE, CURRENT_LEVEL);
		editor.apply();
	}

	public static int getCurrentLevel(Context context) {
		read(context);
		return CURRENT_LEVEL;
	}

	public static String getLastPlayDate(Context context) {
		read(context);
		return toDate(LAST_PLAY_DATE);
	}

	public static void addCurrentLevel(Context context, int level) {
		if (level == ENDLESS || level == DAILY) return;
		else if (level < CURRENT_LEVEL) return;
		CURRENT_LEVEL++;
		write(context);
	}

	public static void setLastPlayDate(Context c) {
		LAST_PLAY_DATE = milliToDate();
		write(c);
	}

	public static String toDate(long i) {
		return DateFormat.getDateInstance(DateFormat.SHORT).format(i);
	}

	public static long milliToDate() {
		return System.currentTimeMillis();
	}

	public static boolean isNewDate(Context context) {
		read(context);
		long now = System.currentTimeMillis() / (24 * 60 * 60 * 1000);
		long then = (LAST_PLAY_DATE) / (24 * 60 * 60 * 1000);

		return now > then;
	}

	public static String waitFor(Context context) {
		read(context);
		long time = LAST_PLAY_DATE + 24 * 60 * 60 * 1000 - System.currentTimeMillis();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		Log.d("letter", calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
		return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
	}
}
