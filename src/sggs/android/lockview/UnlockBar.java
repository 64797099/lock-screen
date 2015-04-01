package sggs.android.lockview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UnlockBar extends RelativeLayout {
	private static final int INIT = 0;
	private static final int SLIDING = 1;
	private static final int LOCK_TOUCHED = 2;
	
	private OnUnlockListener listener = null;

	private ImageView img_thumb = null;

	private ImageView img_download = null;
	private ImageView img_unlock = null;
	
	private int sliderPosition = 0;
	int initialSliderPosition = 0;
	float initialSlidingX = 0;
	private int state = INIT;

	public UnlockBar(Context context) {
		super(context);
		init(context, null);
	}

	public UnlockBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public UnlockBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public void setOnUnlockListener(OnUnlockListener listener) {
		this.listener = listener;
	}

	public void reset() {
		img_thumb.setX(getMeasuredWidth() / 2 - img_thumb.getWidth() / 2);
		img_thumb.setImageResource(R.drawable.lock_slide_icon_normal_no_quick_launcher);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.unlock_main, this, true);

		// Retrieve layout elements
		img_thumb = (ImageView) findViewById(R.id.img_thumb);
		img_download = (ImageView) findViewById(R.id.img_download);
		img_unlock = (ImageView) findViewById(R.id.img_unlock);
		
		state = INIT;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (state == INIT && event.getAction() == MotionEvent.ACTION_DOWN) {
			if (event.getX() > img_thumb.getX()
					&& event.getX() < (img_thumb.getX() + img_thumb.getWidth())) {
				state = SLIDING;
				initialSlidingX = event.getX();
				initialSliderPosition = (int) img_thumb.getX();
				img_thumb.setImageResource(R.drawable.lock_slide_icon_pressed);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_OUTSIDE) {
			if (state != INIT && inImg_Unlock()) {
				Log.d("UnlockBar", "onUnlock");
				if (listener != null)
					listener.onUnlock();
			}else if(state != INIT && inImg_Download()){
				Log.d("UnlockBar", "onDownLoad");
				if (listener != null)
					listener.onDownLoad();
			} else {
				sliderPosition = 0;
				reset();
				state = INIT;
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE && state == SLIDING) {
			sliderPosition = (int) (initialSliderPosition + (event.getX() - initialSlidingX));

			if (inImg_Unlock()) {
				//sliderPosition = (int) (getMeasuredWidth() - thumbWidth);
				sliderPosition = (int) img_unlock.getX();
				setState(LOCK_TOUCHED);
			} else if(inImg_Download()) {
				sliderPosition = (int) img_download.getX();
				setState(LOCK_TOUCHED);
			}else{
				setState(SLIDING);
			}
			setMargin(sliderPosition);
		}

		return true;
	}

	private void setState(int newState){
		if(state == newState)
			return;
		state = SLIDING;
		switch (state) {
		case INIT:
			img_thumb.setImageResource(R.drawable.lock_slide_icon_normal_no_quick_launcher);
			break;
		case SLIDING:
			img_thumb.setImageResource(R.drawable.lock_slide_icon_pressed);
			break;
		case LOCK_TOUCHED:
			img_thumb.setImageResource(R.drawable.lock_touched);
			break;
		default:
			break;
		}
	}
	
	private boolean inImg_Download(){
		return sliderPosition < (img_download.getX() + img_download.getWidth());
	}
	
	private boolean inImg_Unlock(){
		return sliderPosition + img_thumb.getWidth() > img_unlock.getX();
	}
	
	private void setMargin(int margin) {
		if (img_thumb == null)
			return;
		img_thumb.setX(margin);
	}

	private int dpToPx(int dp) {
		float density = getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	public static interface OnUnlockListener {
		void onUnlock();
		void onDownLoad();
	}
}
