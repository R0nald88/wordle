package com.example.wordle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wordle.Storage.Level;
import com.example.wordle.Storage.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordsItemAdapter {

	private final List<Integer> revealedPosition;
	private final Record record;
	private OnEnterListener listener;
	private final List<WordsItemLayout> childs;
	private final LinearLayout parent;

	public WordsItemAdapter(LinearLayout parent, Record record, int step) {
		this.childs = new ArrayList<>();
		this.parent = parent;
		this.record = record;
		revealedPosition = new ArrayList<>();

		for (int a = 0; a < step; a++) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words, parent, false);
			parent.addView(v);
			childs.add(setLayout(a, v));
		}
	}

	public void setMaxStep(int step) {
		for (int a = 0; a < step - childs.size(); a++) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_words, parent, false);
			parent.addView(v);
			childs.add(setLayout(a + childs.size(), v));
		}
	}

	private WordsItemLayout setLayout(int a, View v) {
		WordsItemLayout w;

		if (a < record.getGuessRecord().size())
			w = new WordsItemLayout(v, record.getGuessRecord().get(a), record.getCorrectInput());
		else
			w = new WordsItemLayout(v);

		return w;
	}

	public Record getRecord() {
		return record;
	}

	public void addRevealedPosition(int revealedPosition) {
		this.revealedPosition.add(revealedPosition);

		for (WordsItemLayout w : childs)
			w.setRevealedPosition(record.getCorrectInput(), this.revealedPosition);
	}

	public void addRevealedLetter(char letter) {
		for (int a = 0; a < record.getCorrectInput().length(); a++)
			if (record.getCorrectInput().charAt(a) == letter) revealedPosition.add(a);

		for (WordsItemLayout w : childs)
			w.setRevealedPosition(record.getCorrectInput(), revealedPosition);
	}

	public void resetState() {
		for (WordsItemLayout w : childs) {
			w.onInit();
		}
	}

	public void input(int pos, char a) {
		childs.get(pos).onInput(a);
	}

	public void enter(int pos) {
		String g = childs.get(pos).onEnter(record.getCorrectInput());
		listener.onEnter(g != null, g);
	}

	public void backspace(int pos) {
		childs.get(pos).onBackspace();
	}

	public void clear(int pos) {
		childs.get(pos).onClear();
	}

	public void setEnterListener(OnEnterListener listener) {
		this.listener = listener;
	}

	public interface OnEnterListener {
		void onEnter(boolean isInputValid, String guess);
	}

	public static class WordsItemLayout {
		private final List<TextView> words;
		private List<Character> letters;
		private List<Integer> indicesUnknown;
		private final View itemView;

		private WordsItemLayout(@NonNull View itemView) {
			this.itemView = itemView;
			words = new ArrayList<>();
			letters = new ArrayList<>();
			indicesUnknown = Arrays.asList(0, 1, 2, 3, 4);

			for (int a = 1; a <= 5; a++)
				words.add(itemView.findViewWithTag("word_" + a));
		}

		private WordsItemLayout(@NonNull View itemView, String i, String correct) {
			this(itemView);
			for (char c : i.toCharArray()) onInput(c);
			onEnter(correct);
		}

		public synchronized void onInit() {
			onClear();
			for (TextView t : words) setDefaultWord(t);
		}

		public synchronized void onInput(char a) {
			if (letters.size() >= indicesUnknown.size())
				Toast.makeText(itemView.getContext(), "Reached Max Number of Letter Input!", Toast.LENGTH_SHORT).show();
			else {
				words.get(indicesUnknown.get(letters.size())).setText(String.valueOf(a).toUpperCase());
				letters.add(a);
			}
		}

		public synchronized void onBackspace() {
			if (letters.size() <= 0)
				Toast.makeText(itemView.getContext(), "No Letter Can Be Deleted!", Toast.LENGTH_SHORT).show();
			else {
				words.get(indicesUnknown.get(letters.size() - 1)).setText("");
				letters.remove(letters.size() - 1);
			}
		}

		public synchronized void onClear() {
			for (int a : indicesUnknown)
				words.get(a).setText("");
			letters = new ArrayList<>();
		}

		public synchronized void setRevealedPosition(String correctWord, List<Integer> revealed) {
			if (!letters.isEmpty()) return;

			onClear();
			for (int a : revealed) {
				words.get(a).setText(String.valueOf(correctWord.charAt(a)).toUpperCase());
				setCorrectPositionWord(words.get(a));
			}
			indicesUnknown = new ArrayList<>();

			for (int a = 0; a < 5; a++)
				if (!revealed.contains(a)) indicesUnknown.add(a);
		}

		public synchronized String onEnter(String correctWord) {
			if (letters.size() != indicesUnknown.size()){
				Toast.makeText(itemView.getContext(), "Input Not Finished!", Toast.LENGTH_SHORT).show();
				return null;
			}

			String output = getGuess(correctWord);

			if (!Level.isSpellingCorrect(itemView.getContext(), output.trim().toLowerCase())) {
				Toast.makeText(itemView.getContext(), "Incorrect Spelling!", Toast.LENGTH_SHORT).show();
				return null;
			}

			correctWord = correctWord.toUpperCase();
			for (int a = 0; a < correctWord.length(); a++) {
				if (correctWord.charAt(a) == output.toUpperCase().charAt(a)) {
					setCorrectPositionWord(words.get(a));
				} else if (correctWord.contains(output.toUpperCase().charAt(a) + "")) {
					setCorrectWord(words.get(a));
				} else {
					setWrongWord(words.get(a));
				}
			}

			return output;
		}

		public synchronized String getGuess(String correctWord) {
			String output = "";
			for (int a = 0; a < 5; a++)
				if (indicesUnknown.contains(a))
					output += letters.get(indicesUnknown.indexOf(a)).toString().toUpperCase();
				else output += correctWord.charAt(a);

			return output.toUpperCase();
		}

		private void bounceWord(TextView word) {
			word.setScaleX(0.75f);
			word.setScaleY(0.75f);
			word.animate().scaleX(1).scaleY(1).setInterpolator(new BounceInterpolator()).start();
		}

		public void setDefaultWord(TextView word) {
			word.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.word_bg_color));
		}

		public void setWrongWord(TextView word) {
			word.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.word_wrong_bg));
			bounceWord(word);
		}

		public void setCorrectPositionWord(TextView word) {
			word.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.word_correct_position_bg));
			bounceWord(word);
		}

		public void setCorrectWord(TextView word) {
			word.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.word_correct_word_bg));
			bounceWord(word);
		}
	}
}
