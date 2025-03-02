package com.example.wordle.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.wordle.Item.Booster;
import com.example.wordle.Item.GameMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Record {
	public static final String SHARED_PREF = "S";
	public static final String RECORD_FILE = "R";
	public static final String CURRENT_RECORD_FILE = "CR";
	private static List<Record> RECORDS;
	private static Set<String> RECORDS_STRING;

	private final List<List<String>> guessRecord;
	private final List<String> correctInput;
	private final long date;
	private final int level;
	private int timeUsed;
	private final List<Booster> boosterUsed;
	private boolean isWrite = false;

	private Record(List<String> guessRecord, String correctInput, long date, int level, int timeUsed, List<Booster> boosterUsed) {
		this.guessRecord = new ArrayList<>();
		this.guessRecord.add(guessRecord);

		this.correctInput = new ArrayList<>();
		this.correctInput.add(correctInput);

		this.date = date;
		this.level = level;
		this.timeUsed = timeUsed;
		this.boosterUsed = boosterUsed;
		this.isWrite = true;
	}

	public Record(String correctInput, int boosterUsed, int level) {
		date = Level.milliToDate();
		this.boosterUsed = Booster.intToList(boosterUsed);
		this.correctInput = new ArrayList<>();
		this.correctInput.add(correctInput);
		guessRecord = new ArrayList<>();
		guessRecord.add(new ArrayList<>());
		this.level = level;
		timeUsed = 0;
	}

	private Record(String s) {
		List<Booster> boosterUsed1;
		long date1;
		int level1;

		String[] records = s.split(" ");
		try {
			date1 = Long.parseLong(records[0]);
		} catch (NumberFormatException e) {
			date1 = Level.milliToDate();
		}

		date = date1;
		isWrite = true;

		try {
			boosterUsed1 = Booster.intToList(Integer.parseInt(records[1]));
		} catch (NumberFormatException e) {
			boosterUsed1 = new ArrayList<>();
		}


		boosterUsed = boosterUsed1;
		try {
			level1 = Integer.parseInt(records[4]);
		} catch (NumberFormatException e) {
			level1 = 1;
		}
		level = level1;
		correctInput = Arrays.asList(records[2].split(";"));

		guessRecord = new ArrayList<>();
		if (!records[3].equals("") && !records[3].equals(";")) {
			String[] g = records[3].split(";");
			for (String a : g)
				guessRecord.add(new ArrayList<>(Arrays.asList(a.split(","))));
		} else {
			guessRecord.add(new ArrayList<>());
		}

		try {
			timeUsed = Integer.parseInt(records[5]);
		} catch (NumberFormatException e) {
			timeUsed = 0;
		}
	}

	public static List<Record> read(Context context){
		if (RECORDS != null) return RECORDS;

		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		RECORDS_STRING = sharedPreferences.getStringSet(RECORD_FILE, new HashSet<>());

		RECORDS = new ArrayList<>();
		for (String s : RECORDS_STRING) RECORDS.add(new Record(s));
		return RECORDS;
	}

	public static List<Record> readMode(Context context, GameMode mode, boolean flatten) {
		List<Record> list = read(context);
		if (flatten && mode == GameMode.ENDLESS_MODE) {
			list = readeFlatten(context);
		}

		List<Record> result = new ArrayList<>();

		for (Record r : list) {
			if (r.getLevel().getGameMode() == mode) {
				result.add(r);
			}
		}

		return result;
	}

	public static List<Record> readeFlatten(Context context) {
		List<Record> list = read(context);
		List<Record> result = new ArrayList<>();

		for (Record r : list) {
			result.addAll(r.flattenEndlessRecord());
		}

		return result;
	}

	public static Record readCurrentRecord(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		String s = sharedPreferences.getString(CURRENT_RECORD_FILE, null);
		return s == null ? null : new Record(s);
	}

	public void write(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		if (isWrite) {
			editor.putString(CURRENT_RECORD_FILE, null);
			editor.apply();
			return;
		}

		List<Record> records = read(context);
		records.add(this);

		RECORDS_STRING.add(toString());
		editor.putStringSet(RECORD_FILE, RECORDS_STRING);
		editor.putString(CURRENT_RECORD_FILE, null);
		editor.apply();
		isWrite = true;
	}

	public static void clearCurrentRecord(Context c) {
		SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(CURRENT_RECORD_FILE, null);
		editor.apply();
	}

	public void writeCurrentRecord(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(CURRENT_RECORD_FILE, toString());
		editor.apply();
	}

	public List<Record> flattenEndlessRecord() {
		ArrayList<Record> list = new ArrayList<>();
		if (!isEndless() || getEndlessRecordRound() == 1) {
			list.add(this);
			return list;
		}

		for (int i = 0; i < getEndlessRecordRound(); i++) {
			Record r = new Record(
				getEndlessGuessRecord(i),
				getEndlessCorrectInput(i),
				date,
				Level.ENDLESS,
				getTimeUsed(),
				getBoosterUsed()
			);
			list.add(r);
		}

		return list;
	}

	@NonNull
	public List<String> getGuessRecord() {
		return guessRecord.get(guessRecord.size() - 1);
	}

	public int getEndlessRecordRound() { return guessRecord.size(); }

	public String getCorrectInput() {
		return correctInput.get(correctInput.size() - 1).toUpperCase();
	}

	public String getEndlessCorrectInput(int a) {
		if (getLevel().getGameMode() == GameMode.ENDLESS_MODE) return correctInput.get(a);
		return correctInput.get(0);
	}

	@NonNull
	public List<String> getEndlessGuessRecord(int a) {
		if (getLevel().getGameMode() == GameMode.ENDLESS_MODE) return guessRecord.get(a);
		return guessRecord.get(0);
	}

	public List<String> getEndlessCorrectInput() {
		return correctInput;
	}

	public boolean isEndless() {
		return level == Level.ENDLESS;
	}

	public boolean isDaily() {
		return level == Level.DAILY;
	}

	@NonNull
	public List<List<String>> getEndlessGuessRecord() {
		return guessRecord;
	}

	public String getDate() {
		return Level.toDate(date);
	}

	public int getLevelInt() {
		return level;
	}

	public Level getLevel() {
		return Level.getLevel(level);
	}

	public List<Booster> getBoosterUsed() {
		return boosterUsed;
	}

	public void addGuessRecord(String a) {guessRecord.get(guessRecord.size() - 1).add(a);}

	public void addEndlessGuessRecord(int i, String a) {guessRecord.get(i).add(a);}

	public void addEndlessRound(String c) {
		guessRecord.add(new ArrayList<>());
		correctInput.add(c);
	}

	public boolean isPassed() {
		List<String> l = getGuessRecord();
		if (l.size() <= 0) return false;
		return getCorrectInput().equalsIgnoreCase(l.get(l.size() - 1));
	}

	public int getTimeUsed() {
		return timeUsed;
	}

	public void setTimeUsed(int timeUsed) {
		this.timeUsed = timeUsed;
	}

	@NonNull
	public String toString() {
		String c = "", g = "";
		if (getLevel().getGameMode() != GameMode.ENDLESS_MODE) {
			c = correctInput.get(0);

			for (int a = 0; a < guessRecord.get(0).size(); a++) {
				if (a == guessRecord.size() - 1)
					g += guessRecord.get(0).get(a);
				else
					g += guessRecord.get(0).get(a) + ',';
			}
		} else {
			for (int a = 0; a < correctInput.size(); a++) {
				if (a == correctInput.size() - 1)
					c += correctInput.get(a);
				else c += correctInput.get(a) + ';';
			}

			for (int a = 0; a < guessRecord.size(); a++) {
				for (int b = 0; b < guessRecord.get(a).size(); b++) {
					if (b == guessRecord.get(a).size() - 1) g += guessRecord.get(a).get(b);
					else g += guessRecord.get(a).get(b) + ',';
				}

				if (a < guessRecord.size() - 1) g += ';';
			}
		}

		return date + " " + Booster.listToInt(boosterUsed) + " " + c + " " + g + " " + level + " " + timeUsed;
	}
}