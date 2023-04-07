package com.rcheung.wordle.Item;

import android.content.Context;
import android.content.SharedPreferences;

import com.rcheung.wordle.R;
import com.rcheung.wordle.Storage.Record;
import com.rcheung.wordle.WordleGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static com.rcheung.wordle.Storage.Record.SHARED_PREF;

public enum Booster implements Product {
	ADD_CHANCE() {
		@Override
		public String describe() {
			return "Increase a chance or 10s for guessing the word.";
		}

		@Override
		public String getName() {
			return "Second Chance";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_add_chance;
		}

		@Override
		public int getColorInt() {
			return R.color.green;
		}

		@Override
		public int getPrice() {
			return 50;
		}

		@Override
		public void boostAtInit(WordleGame wordleGame) {
			wordleGame.setMaxStep(wordleGame.getTimer() == null ?
					wordleGame.getMaxStep() + 1 : wordleGame.getMaxStep() + 10);
		}

		@Override
		public void boostAfterInput(WordleGame wordleGame, String w) {}

		@Override
		public int index() {
			return 0;
		}
	}, REVEAL_WORD() {
		@Override
		public String describe() {
			return "Reveal the position of a random letter.";
		}

		@Override
		public String getName() {
			return "Reveal Word";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_reveal_word;
		}

		@Override
		public int getColorInt() {
			return R.color.yellow;
		}

		@Override
		public int getPrice() {
			return 50;
		}

		@Override
		public void boostAtInit(WordleGame wordleGame) {
			String c = wordleGame.getWordleState().getRecord().getCorrectInput();
			wordleGame.getWordleState().addRevealedLetter(c.charAt(new Random().nextInt(5)));
		}

		@Override
		public void boostAfterInput(WordleGame wordleGame, String w) {}

		@Override
		public int index() {
			return 1;
		}
	}, REVEAL_POSITION() {
		@Override
		public String describe() {
			return "Whenever a letter guessed correctly but in different position, have 25% chance reveal and place it in the correct position.";
		}

		@Override
		public String getName() {
			return "Reveal Position";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_reveal_position;
		}

		@Override
		public int getColorInt() {
			return R.color.red;
		}

		@Override
		public int getPrice() {
			return 50;
		}

		@Override
		public void boostAtInit(WordleGame wordleGame) {}

		@Override
		public void boostAfterInput(WordleGame wordleGame, String w) {
			if (new Random().nextInt(4) != 0) return;

			String c = wordleGame.getWordleState().getRecord().getCorrectInput();
			for (int a = 0; a < c.length(); a++)
				if (w.contains(c.charAt(a) + "") && w.charAt(a) != c.charAt(a)) {
					wordleGame.getWordleState().addRevealedLetter(c.charAt(a));
					break;
				}
		}

		@Override
		public int index() {
			return 2;
		}
	}, AUTO_CORRECT() {
		@Override
		public String describe() {
			return "Have 10% chance correct a wrong letter automatically after each attempts.";
		}

		@Override
		public String getName() {
			return "Auto Correction";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_auto_correct;
		}

		@Override
		public int getColorInt() {
			return R.color.green;
		}

		@Override
		public int getPrice() {
			return 50;
		}

		@Override
		public void boostAtInit(WordleGame wordleGame) {}

		@Override
		public void boostAfterInput(WordleGame wordleGame, String w) {
			if (new Random().nextInt(10) != 0) return;

			String c = wordleGame.getWordleState().getRecord().getCorrectInput();
			for (int a = 0; a < c.length(); a++)
				if (!c.contains(w.charAt(a) + "")) {
					wordleGame.getWordleState().addRevealedPosition(a);
					break;
				}
		}

		@Override
		public int index() {
			return 3;
		}
	};

	public static final String BOOSTER_FILE = "b";
	public static boolean IS_READ = false;
	private int number = 0;

	public abstract void boostAtInit(WordleGame wordleGame);
	public abstract void boostAfterInput(WordleGame wordleGame, String w);
	public abstract int index();

	public void setNumber(int n) {number = n;}
	public int getNumber() {return number;}
	public void writeFile(Context context) {write(context);}
	public Booster readFile(Context context) {read(context); return this;}

	public static List<Booster> intToList(int a) {
		Booster[] items = Booster.values();
		List<Booster> o = new ArrayList<>();
		for (Booster item : items) if ((a / (int) Math.pow(2, item.index())) % 2 == 1) o.add(item);
		return o;
	}

	public static int listToInt(List<Booster> o) {
		int r = 0;
		for (Booster b : o) r += Math.pow(2, b.index());
		return r;
	}

	public static List<Booster> read(Context context) {
		if (IS_READ) return Arrays.asList(Booster.values());

		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		String[] file = sharedPreferences.getString(BOOSTER_FILE, "").split(" ");

		for (Booster b : Booster.values()) if (b.index() < file.length)
			try {
				b.setNumber(Integer.parseInt(file[b.index()]));
			} catch (NumberFormatException e) {
				continue;
			}
		IS_READ = true;
		return Arrays.asList(Booster.values());
	}

	public static String getString() {
		String s = "";
		for (Booster b : Booster.values()) s += b.getNumber() + " ";
		return s;
	}

	public static void write(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(BOOSTER_FILE, getString());
		editor.apply();
	}
}
