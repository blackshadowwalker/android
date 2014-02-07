package com.cxf.net.handler;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.cxf.PhoneIdentification.MainActivity;
import com.cxf.PhoneIdentification.R;
import com.cxf.entity.MyApplication;

public class VerifyThread extends Thread {
	UserHandler userHandler;
	Context context;
	Callback handCallback;
	String phoneNum;
	String uniqueToken;
	MyApplication app;

	public VerifyThread(Context context, Callback haCallback, String phoneNum,
			String uniqueToken) {
		this.context = context;
		this.handCallback = haCallback;
		this.userHandler = new UserHandler(context);
		this.phoneNum = phoneNum;
		this.uniqueToken = uniqueToken;
		this.app = (MyApplication) context.getApplicationContext();
	}

	@Override
	public void run() {
		Looper.prepare();//
		int mode = app.state;
		if (mode == 2) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				Message msg = new Message();
				msg.what = MainActivity.VERIFY_SUCCESS;
				Bundle data = new Bundle();
				msg.setData(data);
				handCallback.handleMessage(msg);
			}

		} else {
			try {

				int resultCode = userHandler.verify(phoneNum, uniqueToken);
				if (resultCode == 200) {
					Message msg = new Message();
					msg.what = MainActivity.VERIFY_SUCCESS;
					Bundle data = new Bundle();
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (resultCode == 703) {
					Message msg = new Message();
					msg.what = MainActivity.VERIFY_FAILURE;
					Bundle data = new Bundle();
					String str = context
							.getString(R.string.authentication_timeout);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (resultCode == 744) {
					Message msg = new Message();
					msg.what = MainActivity.VERIFY_FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.no_face);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (resultCode == 603) {
					Message msg = new Message();
					msg.what = MainActivity.VERIFY_FAILURE;
					Bundle data = new Bundle();
					String str = context
							.getString(R.string.server_load_toolarge);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (resultCode == 702) {
					Message msg = new Message();
					msg.what = MainActivity.VERIFY_FAILURE;
					Bundle data = new Bundle();
					String str = context
							.getString(R.string.server_load_toolarge);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = MainActivity.VERIFY_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				handCallback.handleMessage(msg);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = MainActivity.VERIFY_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				handCallback.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = MainActivity.VERIFY_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				handCallback.handleMessage(msg);
			}
		}

	}

}
