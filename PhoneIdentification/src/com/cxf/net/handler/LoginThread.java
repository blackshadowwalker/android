package com.cxf.net.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.cxf.PhoneIdentification.LoginFragment;
import com.cxf.PhoneIdentification.R;
import com.cxf.entity.MyApplication;
import com.cxf.entity.Verify;
import com.cxf.entity.VerifyItem;

public class LoginThread implements Runnable {
	String name;
	String password;
	UserHandler userHandler;
	Context context;
	Callback handCallback;
	String longitude;
	String latitude;
	String ip;
	MyApplication app;

	public LoginThread(Context context, Callback haCallback, String name,
			String password, String lat, String lon, String ip) {
		this.name = name;
		this.password = password;
		this.context = context;
		this.handCallback = haCallback;
		this.userHandler = new UserHandler(context);
		this.latitude = lon;
		this.longitude = lat;
		this.ip = ip;
		app = (MyApplication) context.getApplicationContext();
	}

	@Override
	public void run() {
		Looper.prepare();//
		int mode = app.state;
		if (mode == 2) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Verify verify = new Verify();
			List<VerifyItem> list = new ArrayList<VerifyItem>();
			VerifyItem item = new VerifyItem();
			item.question = "1+1=?";
			item.answer = "2";
			list.add(item);
			verify.password = password;
			verify.phoneNum = name;
			verify.questionsAndAnswers = list;
			Message msg = new Message();
			msg.what = LoginFragment.SUCCESS;
			Bundle data = new Bundle();
			data.putSerializable("verify", verify);
			msg.setData(data);
			handCallback.handleMessage(msg);
		} else {
			try {

				Verify verify = userHandler.request(name, password, longitude,
						latitude, ip);
				if (verify == null) {
					Message msg = new Message();
					msg.what = LoginFragment.FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.login_failure);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (verify != null && verify.returnCode == 200) {
					Message msg = new Message();
					msg.what = LoginFragment.SUCCESS;
					Bundle data = new Bundle();
					data.putSerializable("verify", verify);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (verify != null && verify.returnCode == 602) {
					Message msg = new Message();
					msg.what = LoginFragment.FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.no_user);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (verify != null && verify.returnCode == 603) {
					Message msg = new Message();
					msg.what = LoginFragment.FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.relogin);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (verify != null && verify.returnCode == 604) {
					Message msg = new Message();
					msg.what = LoginFragment.FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.password_failure);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = LoginFragment.FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.login_failure);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = LoginFragment.FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.check_server);
				data.putString("result", str);
				msg.setData(data);
				handCallback.handleMessage(msg);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = LoginFragment.FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.setData(data);
				handCallback.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = LoginFragment.FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.setData(data);
				handCallback.handleMessage(msg);
			}

		}

	}

}
