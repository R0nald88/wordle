package com.rcheung.wordle.Item;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.os.Handler;

import com.rcheung.wordle.Dialog.LostDialog;
import com.rcheung.wordle.Dialog.PassDialog;
import com.rcheung.wordle.R;
import com.rcheung.wordle.WordleGame;

import java.util.Random;

public enum GameMode implements GameItem {
	STEP_MODE() {
		@Override
		public String describe() {
			return "Step Mode";
		}

		@Override
		public String getName() {
			return "Step Mode";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_step;
		}

		@Override
		public int getColorInt() {
			return R.color.red;
		}

		@Override
		public void onGameInit(WordleGame wordleGame) {
			if (wordleGame.getWordleState().getRecord().getGuessRecord().size() > 0) {
				wordleGame.setCurrentStep(wordleGame.getWordleState().getRecord().getGuessRecord().size());
			} else {
				wordleGame.setCurrentStep(0);
				wordleGame.getWordleState().getRecord().setTimeUsed(0);
			}

			wordleGame.setTimer(null);
			wordleGame.setPassState(WordleGame.UNFINISHED);
			wordleGame.setMaxStep(WordleGame.DEFAULT_STEP);
			wordleGame.setStepText("Step(s) Left: " + (wordleGame.getMaxStep() - wordleGame.getCurrentStep()));
			wordleGame.setProgress(1, 1);
		}

		@Override
		public void onInput(WordleGame wordleGame, String w) {
			wordleGame.getWordleState().getRecord().addGuessRecord(w);
			wordleGame.setStepText("Step(s) Left: " + (wordleGame.getMaxStep() - wordleGame.getCurrentStep()));
			wordleGame.setProgress((wordleGame.getMaxStep() - wordleGame.getCurrentStep() - 1), wordleGame.getMaxStep());
		}

		@Override
		public void onFinished(WordleGame wordleGame) {
			if (wordleGame.getPassState() == WordleGame.PASSED)
				new PassDialog(wordleGame).show();
			else if (wordleGame.getPassState() == WordleGame.LOSS)
				new LostDialog(wordleGame).show();

			wordleGame.getWordleState().getRecord().write(wordleGame);
		}
	}, TIME_MODE() {
		@Override
		public String describe() {
			return "Time Mode";
		}

		@Override
		public String getName() {
			return "Time Mode";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_time;
		}

		@Override
		public int getColorInt() {
			return R.color.green;
		}

		@Override
		public void onGameInit(WordleGame wordleGame) {
			wordleGame.setMaxStep(WordleGame.DEFAULT_TIME);

			if (wordleGame.getWordleState().getRecord().getGuessRecord().size() > 0) {
				wordleGame.setCurrentStep(wordleGame.getWordleState().getRecord().getGuessRecord().size());
			} else {
				wordleGame.setCurrentStep(0);
				wordleGame.getWordleState().getRecord().setTimeUsed(0);
			}

			wordleGame.setTimer(new CountDownTimer(wordleGame.getMaxStep() * 1000, 1000) {
				@Override
				public void onTick(long l) {
					wordleGame.getWordleState().getRecord().setTimeUsed(
							wordleGame.getWordleState().getRecord().getTimeUsed() + 1
					);

					wordleGame.setStepText("Time Left: " + (wordleGame.getMaxStep() - wordleGame.getWordleState().getRecord().getTimeUsed()) + 's');
					wordleGame.setProgress((wordleGame.getMaxStep() - wordleGame.getWordleState().getRecord().getTimeUsed()),
							wordleGame.getMaxStep());

					if (wordleGame.getWordleState().getRecord().getTimeUsed() >= wordleGame.getMaxStep()) {
						wordleGame.setPassState(WordleGame.LOSS);
						TIME_MODE.onFinished(wordleGame);
						cancel();
					}
				}

				@Override
				public void onFinish() {
					start();
				}
			});

			wordleGame.setPassState(WordleGame.UNFINISHED);
			wordleGame.setStepText("Time Left: " + (wordleGame.getMaxStep() - wordleGame.getWordleState().getRecord().getTimeUsed()) + 's');
			wordleGame.setProgress(1, 1);
			wordleGame.getTimer().start();
		}

		@Override
		public void onInput(WordleGame wordleGame, String w) {
			wordleGame.getWordleState().getRecord().addGuessRecord(w);
		}

		@Override
		public void onFinished(WordleGame wordleGame) {
			STEP_MODE.onFinished(wordleGame);
		}
	}, ENDLESS_MODE() {
		@Override
		public String describe() {
			return "Endless Mode";
		}

		@Override
		public String getName() {
			return "Endless Mode";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_endless;
		}

		@Override
		public int getColorInt() {
			return R.color.red;
		}

		@Override
		public void onGameInit(WordleGame wordleGame) {
			STEP_MODE.onGameInit(wordleGame);
		}

		@Override
		public void onInput(WordleGame wordleGame, String w) {
			STEP_MODE.onInput(wordleGame, w);
		}

		@Override
		public void onFinished(WordleGame wordleGame) {
			if (wordleGame.getPassState() == WordleGame.LOSS) {
				new LostDialog(wordleGame).show();
				return;
			}

			wordleGame.setPassState(WordleGame.PAUSED);

			ProgressDialog mProgressDialog = new ProgressDialog(wordleGame);
			mProgressDialog.setMax(100);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setTitle("Correct! Preparing Next Round...");

			final Handler handler = new Handler();
			mProgressDialog.show();
			String c = WordleGame.WORD_LIST[new Random().nextInt(WordleGame.WORD_LIST.length)];
			wordleGame.getWordleState().getRecord().addEndlessRound(c);
			onGameInit(wordleGame);
			handler.postDelayed(() -> {
				wordleGame.getWordleState().resetState();
				mProgressDialog.dismiss();
				}, 1000);
		}
	}, DAILY_MODE() {
		@Override
		public String describe() {
			return "Daily Mode";
		}

		@Override
		public String getName() {
			return "Daily Mode";
		}

		@Override
		public int getDrawableInt() {
			return R.drawable.ic_date;
		}

		@Override
		public int getColorInt() {
			return R.color.yellow;
		}

		@Override
		public void onGameInit(WordleGame wordleGame) {
			STEP_MODE.onGameInit(wordleGame);
		}

		@Override
		public void onInput(WordleGame wordleGame, String w) {
			STEP_MODE.onInput(wordleGame, w);
		}

		@Override
		public void onFinished(WordleGame wordleGame) {
			STEP_MODE.onFinished(wordleGame);
		}
	};

	public abstract void onGameInit(WordleGame wordleGame);
	public abstract void onInput(WordleGame wordleGame, String w);
	public abstract void onFinished(WordleGame wordleGame);
}
