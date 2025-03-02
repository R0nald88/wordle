package com.example.wordle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wordle.Dialog.PauseDialog;
import com.example.wordle.Item.Booster;
import com.example.wordle.Storage.Level;
import com.example.wordle.Storage.Record;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class WordleGame extends AppCompatActivity {
	public static final int DEFAULT_STEP = 5;
	public static final int DEFAULT_TIME = 50;

	private int maxStep, currentStep;
	private WordsItemAdapter state;
	private LinearLayout wordLayout;
	private TextView[] keyAlphabets;
	private int passState = UNFINISHED;
	private TextView txtStep;
	private CountDownTimer timer;
	private ProgressBar progressStep;
	private Intent bacgroud_music;
	private ServiceConnection musicConnection;
	private boolean serviceBound = false;
	public static final String BOOSTER = "b", LEVEL = "l";
	public static final int PASSED = 2, LOSS = 1, UNFINISHED = 0, PAUSED = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordle_game);
		txtStep = findViewById(R.id.txt_step);
		progressStep = findViewById(R.id.progress_step);

		onInit();
		setKeyboard();
		setPauseButton();
		InitiateBackgroundMusic();
	}

	private void onInit() {
		maxStep = DEFAULT_STEP;
		currentStep = 0;

		Record record;

		if (Record.readCurrentRecord(this) != null)
			record = setUpResumeRecord();
		else {
			record = new Record(Level.getRandomWord(WordleGame.this),
					getIntent().getIntExtra(BOOSTER, 0),
					getIntent().getIntExtra(LEVEL, 0));
		}

		wordLayout = findViewById(R.id.list_words);

		state = new WordsItemAdapter(wordLayout, record, maxStep);

		RelativeLayout layout_wordle = findViewById(R.id.layout_wordle);
		layout_wordle.setBackground(record.getLevel().getGameMode().getBackground(WordleGame.this));

		record.getLevel().getGameMode().onGameInit(this);
		for (Booster b : record.getBoosterUsed()) b.boostAtInit(this);
	}

	private Record setUpResumeRecord() {
		Record record = Record.readCurrentRecord(this);
		currentStep = record.getGuessRecord().size();
		return record;
	}

	private void setKeyboard() {
		// set alphabets
		keyAlphabets = new TextView[26];
		RelativeLayout layout = findViewById(R.id.layout_wordle);

		for (char a = 'A'; a <= 'Z'; a++) {
			keyAlphabets[(int) (a - 'A')] = layout.findViewWithTag("key_" + a);
			keyAlphabets[(int) (a - 'A')].setOnClickListener(v ->
					state.input(currentStep, ((TextView) v).getText().toString().toUpperCase().charAt(0)));
		}

		//set clear
		ImageButton keyClear = findViewById(R.id.key_clear);
		keyClear.setOnClickListener(v ->
				state.clear(currentStep));

		//set backspace
		ImageButton keyBackspace = findViewById(R.id.key_backspace);
		keyBackspace.setOnClickListener(v -> state.backspace(currentStep));

		//set enter
		TextView keyEnter = findViewById(R.id.key_enter);
		keyEnter.setOnClickListener(v -> {
			state.setEnterListener((correct, word) -> {
				if (correct) {
					state.getRecord().getLevel().getGameMode().onInput(this, word);
					for (Booster b : state.getRecord().getBoosterUsed()) b.boostAfterInput(this, word);

					Log.d("letter", state.getRecord().getGuessRecord().toString());
					Log.d("letter", state.getRecord().isPassed() + "");

					// passed
					if (state.getRecord().isPassed()) {
						passState = PASSED;
						if (timer != null) timer.cancel();
						getWordleState().getRecord().getLevel().getGameMode().onFinished(WordleGame.this);
						return;
					}

					// lost
					if (currentStep + 1 >= maxStep) {
						passState = LOSS;
						if (timer != null) timer.cancel();
						getWordleState().getRecord().getLevel().getGameMode().onFinished(WordleGame.this);
						return;
					}

					currentStep += 1;
				}
			});

			state.enter(currentStep);
		});
	}

	private void setPauseButton() {
		findViewById(R.id.btn_pause).setOnClickListener(v -> {
			if (passState == UNFINISHED) {
				state.getRecord().writeCurrentRecord(this);
				new PauseDialog(this, this::onRestart).show();
				passState = PAUSED;
			}

			if (timer != null) timer.cancel();
		});
	}

	public WordsItemAdapter getWordleState() {
		return state;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (passState == PAUSED) passState = UNFINISHED;
		if (timer != null) timer.start();
		serviceBound = bindService(bacgroud_music, musicConnection,0);

	}
	@Override
	protected void onPause() {
		super.onPause();
		if (passState == UNFINISHED) {
			state.getRecord().writeCurrentRecord(this);
			new PauseDialog(this, this::onRestart).show();
			passState = PAUSED;
		}

		if (timer != null) timer.cancel();

		if (serviceBound && musicConnection != null) {
			unbindService(musicConnection);
			serviceBound = false;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (passState == UNFINISHED) {
				state.getRecord().writeCurrentRecord(this);
				new PauseDialog(this, this::onRestart).show();
				passState = PAUSED;
			}
			return true;
		}else {
			return super.onKeyDown(keyCode,event);
		}
	}
	//testing code
	@Override
	protected void onStart() {
		super.onStart();
		serviceBound = bindService(bacgroud_music, musicConnection,0);
	}
	public int getMaxStep() {
		return maxStep;
	}

	public void setMaxStep(int maxStep) {
		this.maxStep = maxStep;
		state.setMaxStep(this.maxStep);
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}

	public int getPassState() {
		return passState;
	}

	public void setPassState(int passState) {
		this.passState = passState;
	}

	public CountDownTimer getTimer() {
		return timer;
	}

	public void setTimer(CountDownTimer timer) {
		this.timer = timer;
	}

	public void setStepText(String s) {
		txtStep.setText(s);
	}

	public void setProgress(int a, int max) {
		progressStep.setProgress(a * progressStep.getMax() / max, true);
	}

	private void InitiateBackgroundMusic() {
		bacgroud_music=new Intent(getApplicationContext(),BackgroundMusic.class);
		musicConnection =new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {

			}
		};
		startService(bacgroud_music);
		serviceBound = bindService(bacgroud_music, musicConnection,0);

	}
}
