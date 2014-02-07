package com.cxf.net.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.commons.net.util.Base64;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.cxf.PhoneIdentification.R;
import com.cxf.PhoneIdentification.RegisterActivity;
import com.cxf.entity.MyApplication;
import com.cxf.entity.VerifyItem;

public class RegisterThread implements Runnable {
	String name;
	String password;
	UserHandler userHandler;
	Context context;
	Callback handCallback;
	String pictureBase64String;
	int pictureLenth;
	String voiceBase64String;
	int VoiceLenth;
	List<VerifyItem> questionsAndAnswers;
	Bitmap bitmap;
	String TAG = "RegisterThread";
	WakeLock wakeLock = null;
	MyApplication app;

	public RegisterThread(Context context, Callback haCallback, String name,
			String password, Bitmap bitmap, String voiceBase64String,
			int VoiceLenth, List<VerifyItem> questionsAndAnswers) {
		this.name = name;
		this.password = password;
		this.context = context;
		this.handCallback = haCallback;
		this.userHandler = new UserHandler(context);
		this.questionsAndAnswers = questionsAndAnswers;
		this.bitmap = bitmap;
		this.voiceBase64String = voiceBase64String;
		this.VoiceLenth = VoiceLenth;
		this.app = (MyApplication) context.getApplicationContext();

	}

	@Override
	public void run() {
		Looper.prepare();//
		int mode = app.state;
		if (mode == 2) {

			Message msg = new Message();
			msg.what = RegisterActivity.REGISTER_SUCCESS;
			Bundle data = new Bundle();
			String str = context.getString(R.string.register_success);
			data.putString("result", str);
			msg.setData(data);
			handCallback.handleMessage(msg);
		} else {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				String pictureString = Base64.encodeBase64String(out
						.toByteArray());
				pictureBase64String = pictureString;
				pictureLenth = pictureBase64String.length();
				Log.i(TAG, "register:name:" + name);
				Log.i(TAG, "register:password:" + password);
				Log.i(TAG, "register:pictureBase64String:"
						+ pictureBase64String);
				Log.i(TAG, "register:pictureLenth:" + pictureLenth);
				Log.i(TAG, "register:voiceBase64String:" + voiceBase64String);
				Log.i(TAG, "register:VoiceLenth:" + VoiceLenth);
				for (int i = 0; i < questionsAndAnswers.size(); i++) {
					Log.i(TAG, "register:questionsAndAnswers->question" + i
							+ ":" + questionsAndAnswers.get(i).question);
					Log.i(TAG, "register:questionsAndAnswers->answer" + i + ":"
							+ questionsAndAnswers.get(i).answer);
				}
				acquireWakeLock();
				int flag = userHandler.register(name, password,
						pictureBase64String, pictureLenth, voiceBase64String,
						VoiceLenth, questionsAndAnswers);
				if (flag == 906) {
					Message msg = new Message();
					msg.what = RegisterActivity.REGISTER_FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.register_success);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if (flag == 200) {
					Message msg = new Message();
					msg.what = RegisterActivity.REGISTER_SUCCESS;
					Bundle data = new Bundle();
					String str = context.getString(R.string.register_success);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = RegisterActivity.REGISTER_FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.register_failure);
					data.putString("result", str);
					msg.setData(data);
					handCallback.handleMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = RegisterActivity.REGISTER_FAILURE;
				Bundle data = new Bundle();
				data.putString("result", e.getLocalizedMessage());
				msg.setData(data);
				handCallback.handleMessage(msg);
				Log.e(TAG, e.getMessage());
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = RegisterActivity.REGISTER_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				msg.setData(data);
				handCallback.handleMessage(msg);

				Log.e(TAG, e.getMessage());
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = RegisterActivity.REGISTER_FAILURE;
				Bundle data = new Bundle();
				data.putString("result", e.getLocalizedMessage());
				msg.setData(data);
				handCallback.handleMessage(msg);
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			} finally {
				releaseWakeLock();
			}

		}

	}

	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "PostLocationService");
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	private void releaseWakeLock() {
		if (null != wakeLock) {
			wakeLock.release();
			wakeLock = null;
		}
	}

}
