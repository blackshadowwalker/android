package com.cxf.net.handler;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.cxf.PhoneIdentification.R;
import com.cxf.PhoneIdentification.RecorderFragment;
import com.cxf.entity.MyApplication;

public class UploadFileRequestThread implements Runnable {
	FileHandler fileHandler;
	Context context;
	Callback handCallback;
	String uniqueToken;
	String phoneNum;
	int authMediaType;
	MyApplication app;

	public UploadFileRequestThread(Context context, Callback haCallback,
			String uniqueToken, String phoneNum, int authMediaType) {

		this.context = context;
		this.handCallback = haCallback;
		this.uniqueToken = uniqueToken;
		this.phoneNum = phoneNum;
		this.authMediaType = authMediaType;
		this.fileHandler = new FileHandler(context);
		this.app=(MyApplication) context.getApplicationContext();
	}

	@Override
	public void run() {
		Looper.prepare();//
		int mode = app.state;
		if (mode == 2) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = RecorderFragment.UPLOAD_REQUEST_SUCCEED;
			Bundle data = new Bundle();
			data.putString("result", "/13000000000");
			data.putString("ftp_name", phoneNum);
			data.putString("ftp_password", "");
			msg.setData(data);
			handCallback.handleMessage(msg);
		} else {
			try {

				String result = fileHandler.uploadFile(uniqueToken, phoneNum,
						authMediaType);
				if (result == null) {
					Message msg = new Message();
					msg.what = RecorderFragment.UPLOAD_REQUEST_FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.upload_failure);
					data.putString("result", str);
					data.putInt("type", authMediaType);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = RecorderFragment.UPLOAD_REQUEST_SUCCEED;
					Bundle data = new Bundle();
					data.putString("result", result);
					data.putString("ftp_name", phoneNum);
					data.putString("ftp_password", uniqueToken);
					msg.setData(data);
					handCallback.handleMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = RecorderFragment.UPLOAD_REQUEST_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				data.putInt("type", authMediaType);
				msg.setData(data);
				handCallback.handleMessage(msg);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = RecorderFragment.UPLOAD_REQUEST_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.connection_error);
				data.putString("result", str);
				data.putInt("type", authMediaType);
				msg.setData(data);
				handCallback.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = RecorderFragment.UPLOAD_REQUEST_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.upload_failure);
				data.putString("result", str);
				data.putInt("type", authMediaType);
				msg.setData(data);
				handCallback.handleMessage(msg);
			}
		}

	}

}
