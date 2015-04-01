package sggs.android.lockview;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class FantasticLockService extends Service {

	protected static final boolean DBG = false;
	private static final String TAG = "FantasticLockService";
	private Intent mFxLockIntent;

	@Override
	public void onCreate() {
		super.onCreate();
		registerComponent();
		mFxLockIntent = new Intent(FantasticLockService.this, LockViewActivity.class);
		mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterComponent();
		// 被销毁时启动自身，保持自身在后台存活
		startService(new Intent(FantasticLockService.this, FantasticLockService.class));
	}

	// 监听来自用户按Power键点亮点暗屏幕的广播
	private BroadcastReceiver mScreenOnOrOffReceiver = new BroadcastReceiver() {

		private KeyguardManager mKeyguardManager;
		private KeyguardLock mKeyguardLock;
		private Intent mFxLockIntent;

		@Override
		public void onReceive(Context context, Intent intent) {

			if (DBG)
				Log.d(TAG, "mScreenOffReceiver-->" + intent.getAction());

			if (intent.getAction().equals("android.intent.action.SCREEN_ON")
					|| intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
				mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("FxLock");
				// 屏蔽手机内置的锁屏
				mKeyguardLock.disableKeyguard();
				// 启动该第三方锁屏
				mFxLockIntent = new Intent(FantasticLockService.this, LockViewActivity.class);
				mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mFxLockIntent);
			}

		}
	};

	// 注册广播监听
	public void registerComponent() {
		if (DBG)
			Log.d(TAG, "registerComponent()");
		IntentFilter mScreenOnOrOffFilter = new IntentFilter();
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_ON");
		mScreenOnOrOffFilter.addAction("android.intent.action.SCREEN_OFF");
		FantasticLockService.this.registerReceiver(mScreenOnOrOffReceiver, mScreenOnOrOffFilter);
	}

	public void unregisterComponent() {
		if (DBG)
			Log.d(TAG, "unregisterComponent()");
		if (mScreenOnOrOffReceiver != null) {
			FantasticLockService.this.unregisterReceiver(mScreenOnOrOffReceiver);
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
