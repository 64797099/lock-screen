package sggs.android.lockview;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import sggs.android.lockview.UnlockBar.OnUnlockListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LockViewActivity extends Activity {

	private static final boolean DBG = true;
	private static final String TAG = "Fantastic";
	private View unlocl_bg;
	
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DBG)
			Log.d(TAG, "onCreate()");
		setContentView(R.layout.main);
		startService(new Intent(LockViewActivity.this,
				FantasticLockService.class));

		UnlockBar unlockBar = (UnlockBar) findViewById(R.id.unlock);
		unlockBar.setOnUnlockListener(new OnUnlockListener() {
			@Override
			public void onUnlock() {
				finish();
			}

			@Override
			public void onDownLoad() {
				startActivity(new Intent(LockViewActivity.this,
						InfoActivity.class));
				finish();
			}
		});

		final VerticalViewPager vPager = (VerticalViewPager) findViewById(R.id.viewpager);
		vPager.setAdapter(new MyAdapter());
		
		unlocl_bg = findViewById(R.id.unlocl_bg);
		
		final View arrow_down = findViewById(R.id.arrow_down);
		final View arrow_up = findViewById(R.id.arrow_up);
		vPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if(arg0 == 0){
					arrow_up.setVisibility(View.GONE);
				}else{
					arrow_up.setVisibility(View.VISIBLE);
				}
				
				if(arg0 == vPager.getCurrentItem() - 1){
					arrow_down.setVisibility(View.GONE);
				}else{
					arrow_down.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if(arg0 != VerticalViewPager.SCROLL_STATE_IDLE){
					unlocl_bg.setVisibility(View.GONE);
				}else{
					unlocl_bg.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (DBG)
			Log.d(TAG, "onDetachedFromWindow()");
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private final class MyAdapter extends PagerAdapter {
		private final int[] RES = { R.drawable.p01, R.drawable.p02,
				R.drawable.p03, R.drawable.p04, R.drawable.p05, R.drawable.p06,
				R.drawable.p07, R.drawable.p08, R.drawable.p09, R.drawable.p10,
				R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14,
				R.drawable.p15, R.drawable.p16 };

		private View[] views;
		
		public MyAdapter(){
			views = new View[RES.length];
		}
		
		@Override
		public int getCount() {
			return RES.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if(views[position] == null){
				views[position] = View.inflate(LockViewActivity.this, R.layout.pager_item, null);
				//views[position].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				ImageView imageView = (ImageView) views[position].findViewById(R.id.image);
				imageView.setBackgroundResource(RES[position]);
			}
			container.addView(views[position]);
			return views[position];
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views[position]);
		}
		
	}
}

