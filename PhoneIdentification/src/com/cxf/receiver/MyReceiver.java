package com.cxf.receiver;

import com.cxf.PhoneIdentification.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyReceiver extends BroadcastReceiver {
	Handler handler;
	State wifiState = null;
	State mobileState = null;

	public void onReceive(android.content.Context context,
			android.content.Intent intent) {
		// 监听自定义模式变化
		if (intent.getAction().equals("android.intent.action.MY_RECEIVER")) {
			Bundle bundle = intent.getExtras();
			int what = bundle.getInt("what");
			Message msg = new Message();
			msg.what = what;
			handler.sendMessage(msg);
		} else if (intent.getAction().equals(
				"android.intent.action.VERIFY_RECEIVER")) {
			Bundle bundle = intent.getExtras();
			int what = bundle.getInt("what");
			Message msg = new Message();
			msg.what = what;
			handler.sendMessage(msg);
			// 监听休眠
		} else if (intent.getAction()
				.equals("android.intent.action.SCREEN_OFF")) {
			Message msg = new Message();
			msg.what = 10000;
			handler.sendMessage(msg);
		} else if (intent.getAction()
				.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			Message msg = new Message();
			msg.what = 10000;
			handler.sendMessage(msg);
		} else if (intent.getAction().equals(
				"android.net.conn.CONNECTIVITY_CHANGE")) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState();

			if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {

				Message msg = new Message();
				msg.what = MainActivity.NET_DISCONNECTION;
				handler.sendMessage(msg);
			}
		}
	};

	public MyReceiver(Handler handler) {
		this.handler = handler;
	}
}
