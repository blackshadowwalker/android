package com.cxf.net.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.cxf.PhoneIdentification.R;
import com.cxf.PhoneIdentification.RecorderFragment;
import com.cxf.entity.MyApplication;

public class UploadFileThread implements Runnable {
	FileHandler fileHandler;
	Context context;
	Callback handCallback;
	String url;
	String port;
	String username;
	String password;
	String remotePath;
	String fileNamePath;
	String fileName;
	int type;
	MyApplication app;

	public UploadFileThread(Context context, Callback haCallback, String url,
			String port, String username, String password, String remotePath,
			String fileNamePath, String fileName, int type) {

		this.context = context;
		this.handCallback = haCallback;
		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
		this.remotePath = remotePath;
		this.fileName = fileName;
		this.fileNamePath = fileNamePath;
		this.fileHandler = new FileHandler(context);
		this.type = type;
		app = (MyApplication) context.getApplicationContext();
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
				msg.what = RecorderFragment.UPLOAD_SUCCESS;
				Bundle data = new Bundle();
				data.putInt("type", type);
				msg.setData(data);
				handCallback.handleMessage(msg);
			}

		} else {
			try {

				String result;
				result = fileHandler.ftpUpload(url, port, username, password,
						remotePath, fileNamePath, fileName);
				if (result == null || (result != null && "0".endsWith(result))) {
					Message msg = new Message();
					msg.what = RecorderFragment.UPLOAD_FAILURE;
					Bundle data = new Bundle();
					String str = context.getString(R.string.upload_failure);
					data.putString("result", str);
					data.putInt("type", type);
					msg.setData(data);
					handCallback.handleMessage(msg);
				} else if ("1".equals(result)) {
					Message msg = new Message();
					msg.what = RecorderFragment.UPLOAD_SUCCESS;
					Bundle data = new Bundle();
					data.putInt("type", type);
					msg.setData(data);
					handCallback.handleMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = RecorderFragment.UPLOAD_FAILURE;
				Bundle data = new Bundle();
				String str = context.getString(R.string.upload_failure);
				data.putString("result", str);
				data.putInt("type", type);
				msg.setData(data);
				handCallback.handleMessage(msg);
			}
		}

	}

}
