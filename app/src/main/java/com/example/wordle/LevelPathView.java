package com.example.wordle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class LevelPathView extends View {
	protected final float[][] intervalButtonPath = {
	 	{0.2944444f, 0.962114726f}, {0.3574f, 0.94178f},
		{0.475925f, 0.93f}, {0.598148f, 0.915025f}, {0.7388889f, 0.899186643f},
		{0.787037f, 0.88291952f}, {0.7388889f, 0.86536815f}, {0.598148f, 0.8491f},
		{0.475925f, 0.834546232f}, {0.3574f, 0.821275684f},
	};
	protected List<List<Float>> btnPath;
	protected final float[] startBtnPos = {0.3574f, 0.98095f};
	protected final float mapAspectRatio = 8.6518518519f;
	protected final float heightInterval = 0.98095f - 0.821275684f;
	protected final int pathRepeat = 6;

	protected final byte MAX_LEVEL_NO = (byte) (intervalButtonPath.length * pathRepeat);
	protected boolean isInitialized = false;
	protected boolean isClicked = false;
	protected boolean isScrolled = false;
	protected Paint paint;
	protected final float buttonRadius = 50f;
	protected RectF rectF;
	protected float actionDownX = 0;
	protected float actionDownY = 0;
	protected LevelClickListener levelClickListener = (level, region) -> Toast.makeText(getContext(), "Level " + level, Toast.LENGTH_SHORT).show();

	protected short minLevelNo = 1, currentLevel = 1;
	protected int viewHeight = 0;
	protected long clickMillis = 0L;
	public final static long MAX_CLICK_MILLIS = 100;

	public LevelPathView(Context context) {
		super(context);
		if (!isInitialized) {
			initialise();
		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		if (!isInitialized) {
			initialise();
		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!isInitialized) {
			initialise();
		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (!isInitialized) {
			initialise();
		}
	}

	protected void initialise() {
		paint = new Paint();
		paint.setAntiAlias(true);
		rectF = new RectF();
		isInitialized = true;
		btnPath = getButtonPath();
	}

	public void scrollToLevel(int level) {
		float y = Math.max(Math.min(
			btnPath.get(level % MAX_LEVEL_NO).get(1) * getMapHeight() - viewHeight / 2f,
			getMapHeight() - viewHeight), 0
		);

		scrollTo(0, (int) y);
		isScrolled = true;
	}

	private void setTextPaint(int b) {
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				ResourcesCompat.getFloat(getContext().getResources(), R.dimen.subtitle_float),
				getContext().getResources().getDisplayMetrics()));
		paint.setColor(getContext().getColor(R.color.white));

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			paint.setTypeface(getContext().getResources().getFont(R.font.title));
//		} else {
//			paint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.title));
//		}
	}

	private float getFontWidth(String text) {
		return paint.measureText(text);
	}

	private float getFontHeight() {
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		return fontMetrics.bottom - fontMetrics.top + fontMetrics.leading;
	}

	private List<List<Float>> getButtonPath() {
		List<List<Float>> res = new ArrayList<>();
		List<Float> pos = new ArrayList<>();
		pos.add(startBtnPos[0]);
		pos.add(startBtnPos[1]);
		res.add(pos);

		for (int i = 0; i < pathRepeat; i++) {
			float dh = i * heightInterval;
			for (float[] f : intervalButtonPath) {
				List<Float> posi = new ArrayList<>();
				posi.add(f[0]);
				posi.add(f[1] - dh);
				res.add(posi);
			}
		}

		return res;
	}

	private float getMapWidth() {
		return getWidth();
	}

	private float getMapHeight() {
		return getMapWidth() * mapAspectRatio;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		viewHeight = getHeight();
		Drawable d = AppCompatResources.getDrawable(getContext(), R.drawable.route_map);
		assert d != null;
		d.setBounds(
			getLeft(), getTop(), getRight(),
			(int) (getTop() + getMapHeight())
		);
		d.draw(canvas);

		int b = minLevelNo;
		for (List<Float> f : btnPath) {
			paint.setColor(getContext().getColor(R.color.white));
			canvas.drawCircle(f.get(0) * getMapWidth(), f.get(1) * getMapHeight(), buttonRadius + 10f, paint);

			if (b >= minLevelNo + MAX_LEVEL_NO) break;
			paint.setColor(getContext().getColor(currentLevel > b ? R.color.green :
					currentLevel == b ? R.color.yellow : R.color.grey));
			canvas.drawCircle(f.get(0) * getMapWidth(), f.get(1) * getMapHeight(), buttonRadius, paint);
			b++;
		}

		for (int a = 0; a < MAX_LEVEL_NO; a++) {
			setTextPaint(a);
			float fontWidth = btnPath.get(a).get(0) * getMapWidth() - getFontWidth((a + minLevelNo + 1) + "") * 0.5f;
			float fontHeight = btnPath.get(a).get(1) * getMapHeight() + getFontHeight() * 0.25f;
			canvas.drawText((a + minLevelNo + 1) + "", fontWidth, fontHeight, paint);
		}

		Drawable nextRegion = AppCompatResources.getDrawable(getContext(), R.drawable.ic_next_region);
		assert nextRegion != null;
		float length = buttonRadius * 0.75f;
		nextRegion.setBounds(
			(int) (btnPath.get(btnPath.size() - 1).get(0) * getMapWidth() - length),
			(int) (btnPath.get(btnPath.size() - 1).get(1) * getMapHeight() - length),
			(int) (btnPath.get(btnPath.size() - 1).get(0) * getMapWidth() + length),
			(int) (btnPath.get(btnPath.size() - 1).get(1) * getMapHeight() + length)
		);
		nextRegion.draw(canvas);

		if (!isScrolled) scrollToLevel(currentLevel);
	}

	public interface LevelClickListener {
		void onLevelClick(int level, boolean isNextRegion);
	}

	public void setLevelClickListener(@NonNull LevelClickListener levelClickListener) {
		this.levelClickListener = levelClickListener;
	}

	public void setCurrentLevel(int level) {
		minLevelNo = (short) (level - level % MAX_LEVEL_NO);
		currentLevel = (short) (level - 1);
		isScrolled = false;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isClicked = true;
				actionDownY = event.getY();
				actionDownX = event.getX();
				clickMillis = System.currentTimeMillis();
				return true;
			case MotionEvent.ACTION_UP:
				if (!isClicked || levelClickListener == null) {
					isClicked = false;
					return false;
				}

				short i = 0;
				for (List<Float> f : btnPath) {
					rectF.set((f.get(0) * getMapWidth() - buttonRadius) - getScrollX(),
							(f.get(1) * getMapHeight() - buttonRadius) - getScrollY(),
							(f.get(0) * getMapWidth() + buttonRadius) - getScrollX(),
							(f.get(1) * getMapHeight() + buttonRadius) - getScrollY());

					if (!rectF.contains(event.getX(), event.getY())) {
						i++;
						continue;
					}

					if (i >= MAX_LEVEL_NO) {
						if (currentLevel >= i + minLevelNo) {
							levelClickListener.onLevelClick(0, true);
						} else {
							Toast.makeText(getContext(), "Pass all level in this region before going to next region", Toast.LENGTH_SHORT).show();
						}
					}
					else if (currentLevel >= i + minLevelNo) {
						levelClickListener.onLevelClick(i + minLevelNo, false);
					} else {
						Toast.makeText(getContext(), "You have not passed the previous level.", Toast.LENGTH_SHORT).show();
					}
					isClicked = false;
					return true;
				}
				isClicked = false;
				return true;
			case MotionEvent.ACTION_MOVE:
				float y = Math.max(getScrollY() - (event.getY() - actionDownY), 0);
				y = Math.min(y, getMapHeight() - viewHeight);

				scrollTo(0, (int) y);

				isClicked = System.currentTimeMillis() - clickMillis <= MAX_CLICK_MILLIS;
				return true;
		}
		return false;
	}
}
