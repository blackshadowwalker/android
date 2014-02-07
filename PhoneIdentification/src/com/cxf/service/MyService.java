package com.cxf.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.cxf.PhoneIdentification.MainActivity;
import com.cxf.PhoneIdentification.R;
import com.cxf.entity.MyApplication;
import com.cxf.entity.Verify;
import com.cxf.net.handler.VerifyThread;

public class MyService extends Service implements Callback {
	Button floatButton;
	RelativeLayout floatLayout;
	WindowManager.LayoutParams params;
	WindowManager windowManager;
	VerifyThread verifyThread;
	MyApplication app;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String what = intent.getStringExtra("what");
			if ("show_tip".equals(what)) {
				createFloatView();
			} else if ("hide_tip".equals(what)) {
				if (floatLayout != null) {
					windowManager.removeView(floatLayout);
					floatLayout=null;
				}

			} else if ("reshow_tip".equals(what)) {
				if (floatLayout != null) {
					windowManager.addView(floatLayout, params);
				} else {
					createFloatView();
				}
			} else if ("do_verify".equals(what)) {
				Verify verify = (Verify) intent.getSerializableExtra("verify");
				if (verify != null) {
					verifyThread = new VerifyThread(this, MyService.this,
							verify.phoneNum, verify.uniqueToken);
					verifyThread.start();
				}
			}

		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (floatLayout != null) {
			app.state=1;
			windowManager.removeView(floatLayout);
			floatLayout = null;
		}
	}

	private void createFloatView() {

		params = new WindowManager.LayoutParams();
		windowManager = (WindowManager) getApplication().getSystemService(
				Context.WINDOW_SERVICE);
		params.type = LayoutParams.TYPE_PHONE;
		params.format = PixelFormat.RGBA_8888;
		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
		float scale = getResources().getDisplayMetrics().density;
		params.x = 0;
		params.y = (int) (50 * scale + 0.5f);
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		floatLayout = (RelativeLayout) inflater.inflate(R.layout.button_float,
				null);
		floatButton = (Button) floatLayout.findViewById(R.id.change_state);
		windowManager.addView(floatLayout, params);

		floatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		floatButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				floatButton.setClickable(false);
				app.state=1;
				// 发送特定action的广播

				Intent intent = new Intent();
				intent.setAction("android.intent.action.MY_RECEIVER");
				intent.putExtra("what", MainActivity.RESET);
				sendBroadcast(intent);

				if (floatLayout != null) {
					windowManager.removeView(floatLayout);
					floatLayout=null;
				}
			}
		});

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MainActivity.VERIFY_SUCCESS:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VERIFY_RECEIVER");
			intent.putExtra("what", MainActivity.VERIFY_SUCCESS);
			sendBroadcast(intent);
			break;
		case MainActivity.VERIFY_FAILURE:
			Intent intent2 = new Intent();
			intent2.setAction("android.intent.action.VERIFY_RECEIVER");
			intent2.putExtra("what", MainActivity.VERIFY_FAILURE);
			sendBroadcast(intent2);
			break;

		default:
			break;
		}
		return false;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		app=(MyApplication) getApplication();
	}
}
