package com.cxf.net.handler;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import com.cxf.PhoneIdentification.MainActivity;
import com.cxf.PhoneIdentification.R;
import com.cxf.entity.MyApplication;
import android.content.Context;
import android.os.Handler.Callback;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

public class LogoffThread implements Runnable {
	UserHandler userHandler;
	Context context;
	Callback handCallback;
	String phoneNum;
	String uniqueToken;
MyApplication app;
	public LogoffThread(Context context, Callback haCallback, String phoneNum,
			String uniqueToken) {
		this.context = context;
		this.handCallback = haCallback;
		this.userHandler = new UserHandler(context);
		this.phoneNum = phoneNum;
		this.uniqueToken = uniqueToken;
		app=(MyApplication) context.getApplicationContext();
	}

	@Override
	public void run() {
		Looper.prepare();//
		int mode =app.state;
		if (mode == 2) {
			Message msg = new Message();
			msg.what = MainActivity.LOGOFF_SUCCESS;
			Bundle data = new Bundle();
			String str = context.getString(R.string.exit_success);
			data.putString("result", str);
			msg.setData(data);
			handCallback.handleMessage(msg);
		} else {
			try {

				int resultCode = userHandler.logff(phoneNum, uniqueToken);
				if (resultCode != 200) {
					Message msg = new Message();
					Bundle data = new Bundle();
					String str = context.getString(R.string.exit_failure);
					data.putString("result", str);
					msg.what = MainActivity.LOGOFF_FAILURE;
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = MainActivity.LOGOFF_SUCCESS;
					Bundle data = new Bundle();
					String str = context.getString(R.string.exit_success);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);

				}
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.what = MainActivity.LOGOFF_FAILURE;
				msg.setData(data);
				handCallback.handleMessage(msg);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.what = MainActivity.LOGOFF_FAILURE;
				msg.setData(data);
				handCallback.handleMessage(msg);
			}catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.what = MainActivity.LOGOFF_FAILURE;
				msg.setData(data);
				handCallback.handleMessage(msg);
			}
		}

	}
}
