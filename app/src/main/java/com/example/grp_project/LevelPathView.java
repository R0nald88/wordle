package com.rcheung.wordle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

public class LevelPathView extends View {
	protected final float[][] buttonPath = {{77, 97.5f}, {62, 97.5f}, {47, 97.5f}, {32, 97.5f},
			{22.5f, 94.5f}, {41f, 86.5f}, {67.5f, 75}, {75.5f, 67},
			{54, 67}, {21.5f, 67}, {11.5f, 60.75f}, {11.5f, 53.25f},
			{11.5f, 45.75f}, {37, 44.75f}, {52, 44.75f}, {62.25f, 48.25f},
			{76.75f, 48.25f}, {95, 47.5f}, {95.75f, 40}, {95.75f, 32.5f},
			{82.5f, 31}, {68.75f, 29}, {91, 27}, {95.25f, 21.25f}, {95.25f, 13.75f},
			{95.25f, 6.25f}, {85.5f, 3}, {70.5f, 3}, {59.5f, 3.75f}, {43.25f, 10},
			{24.5f, 17}, {9.5f, 19.75f}};

	protected final byte MAX_LEVEL_NO = (byte) buttonPath.length;
	protected boolean ifInitialsed = false;
	protected boolean ifClicked = false;
	protected boolean isScrolled = false;
	protected Paint paint;
	protected final float buttonRadius = 37.5f;
	protected RectF rectF;
	protected float actionDownX = 0;
	protected float actionDownY = 0;
	protected LevelClickListener levelClickListener = level -> Toast.makeText(getContext(), "Level " + level, Toast.LENGTH_SHORT).show();

	protected short minLevelNo = 1, currentLevel = 1;

	public LevelPathView(Context context) {
		super(context);
		if (!ifInitialsed) {
			initialise();
		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		if (!ifInitialsed) {
			initialise();

		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (!ifInitialsed) {
			initialise();
		}
	}

	public LevelPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		if (!ifInitialsed) {
			initialise();
		}
	}

	protected void initialise() {
		paint = new Paint();
		paint.setAntiAlias(true);
		rectF = new RectF();
		ifInitialsed = true;
	}

	public void scrollToLevel(int level) {
		float x = Math.max(Math.min((buttonPath[level % MAX_LEVEL_NO][0] * getWidth() * 0.01f)
				- getWidth() / 3f - getWidth() / 6f
				, getWidth() / 3f), - getWidth() / 3f);
		float y = Math.max(Math.min(buttonPath[level % MAX_LEVEL_NO][1] * getHeight() * 0.01f
				- getHeight() / 3f - getHeight() / 6f
				, getHeight() / 3f), -getHeight() / 3f);

		Log.d("letter", x + ", " + y + ", " + getWidth() + ", " + getHeight() + ", " + level);
		scrollTo((int) x, (int) y);
		isScrolled = true;
	}

	private void setTextPaint(int b) {
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				ResourcesCompat.getFloat(getContext().getResources(), R.dimen.subtitle_float),
				getContext().getResources().getDisplayMetrics()));
		paint.setColor(getContext().getColor(currentLevel > b ? R.color.white :
				currentLevel == b ? R.color.grey : R.color.grey));

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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Drawable d = AppCompatResources.getDrawable(getContext(), R.drawable.city_map);
		assert d != null;
		d.setBounds(getLeft(), getTop(), getRight(), getBottom());
		d.draw(canvas);

		int b = minLevelNo;
		for (float[] f : buttonPath) {
			paint.setColor(getContext().getColor(currentLevel > b ? R.color.green :
					currentLevel == b ? R.color.yellow : R.color.white));
			canvas.drawCircle(f[0] * getWidth() * 0.01f, f[1] * getHeight() * 0.01f, buttonRadius, paint);
			b++;
		}

		for (int a = 0; a < MAX_LEVEL_NO; a++) {
			setTextPaint(a);
			float fontWidth = buttonPath[a][0] * getWidth() * 0.01f - getFontWidth((a + minLevelNo + 1) + "") * 0.5f;
			float fontHeight = buttonPath[a][1] * getHeight() * 0.01f + getFontHeight() * 0.25f;
			canvas.drawText((a + minLevelNo + 1) + "", fontWidth, fontHeight, paint);
		}

		if (!isScrolled) scrollToLevel(currentLevel);
	}

	public interface LevelClickListener {
		void onLevelClick(int level);
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
				ifClicked = true;
				actionDownY = event.getY();
				actionDownX = event.getX();
				return true;
			case MotionEvent.ACTION_UP:
				if (!ifClicked || levelClickListener == null) {
					ifClicked = false;
					return false;
				}

				short i = minLevelNo;
				for (float[] f : buttonPath) {
					rectF.set((f[0] * getWidth() * 0.01f - buttonRadius) - getScrollX(),
							(f[1] * getHeight() * 0.01f - buttonRadius) - getScrollY(),
							(f[0] * getWidth() * 0.01f + buttonRadius) - getScrollX(),
							(f[1] * getHeight() * 0.01f + buttonRadius) - getScrollY());

					if (i == currentLevel)
						Log.d("letter1",
								((f[0] * getWidth() * 0.01f - buttonRadius) - getScrollX()) + ", "+
										((f[1] * getHeight() * 0.01f - buttonRadius) - getScrollY()));

					if (rectF.contains(event.getX(), event.getY()) &&
					currentLevel >= i) {
						levelClickListener.onLevelClick(i);
						ifClicked = false;
						return true;
					} else if (rectF.contains(event.getX(), event.getY())) {
						Toast.makeText(getContext(), "You have not passed the previous level.", Toast.LENGTH_SHORT).show();
						ifClicked = false;
						return true;
					}
					i++;
				}
				ifClicked = false;
				return true;
			case MotionEvent.ACTION_MOVE:
				int x = (getScrollX() - (event.getX() - actionDownX) >= getX() - getWidth() / 3f &&
						getScrollX() - (event.getX() - actionDownX) <= getX() + getWidth() / 3f) ?
						(int) (getScrollX() - (event.getX() - actionDownX)) :
						getScrollX();
				int y = (getScrollY() - (event.getY() - actionDownY) >= getY() - getHeight() / 3f &&
						getScrollY() - (event.getY() - actionDownY) <= getY() + getHeight() / 3f) ?
						(int) (getScrollY() - (event.getY() - actionDownY)) :
						getScrollY();

				scrollTo(x, y);
				ifClicked = false;
				return true;
		}
		return false;
	}
}
