package com.cxf.net.handler;

import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import com.cxf.PhoneIdentification.RecorderFragment;
import com.cxf.entity.CameraManager;
import com.cxf.entity.Constant;
import com.cxf.entity.MyApplication;

public final class VideoRecorderManager2 implements Callback {

	private static final String TAG = VideoRecorderManager2.class
			.getSimpleName();
	Camera camera;
	Callback callback;
	CameraManager cameraManager;
	Context context;
	private String uploadPath;
	private String fileNamePath = "";
	private String fileName = "";
	int recorderDegree = 0;
	boolean started = false;
	private String filePath;
	public final static int VIDEO_TYPE = 1;
	public final static int CAMERA_CONNECT_FAILURE = -100;
	Handler handler;
	MyApplication app;

	public VideoRecorderManager2(Context context, Callback cb) {
		Log.i(TAG, "create MediaRecorderManager object.");
		this.context = context;
		this.callback = cb;
		handler = new Handler(callback);
		this.cameraManager = new CameraManager(context, handler);
		this.fileNamePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/";
		this.fileName = "myVideo.avi";
		this.filePath = this.fileNamePath + this.fileName;
		app = (MyApplication) context.getApplicationContext();

	}

	public boolean isRecording() {
		return started;
	}

	public void startRecord(SurfaceHolder surfaceHolder) {
		started = true;
		if (surfaceHolder != null) {
			try {
				if (camera == null) {
					camera = cameraManager.openCamera(surfaceHolder);

				}
				if (camera != null) {
					camera.startPreview();
				}

			} catch (IOException e1) {
				started = false;
				e1.printStackTrace();
				Message msg = new Message();
				msg.what = CAMERA_CONNECT_FAILURE;
				Bundle data = new Bundle();
				data.putString("result", "无法连接到摄像头！");
				msg.setData(data);
				callback.handleMessage(msg);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public void stop() {
		if (started == true) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.lock();
			camera.release();
			camera = null;
			started = false;

		}
	}

	public void upload(String phoneNum, String uniqueToken) {
		new Thread(new UploadFileRequestThread(context, this, uniqueToken,
				phoneNum, VIDEO_TYPE)).start();

	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {

		case RecorderFragment.UPLOAD_REQUEST_SUCCEED:
			String result = data.getString("result");
			uploadPath = result;
			String ftp_name = data.getString("ftp_name");
			String ftp_password = data.getString("ftp_password");
			String url = app.domain;
			if (ftp_name != null && ftp_password != null && url != null) {
				new Thread(new UploadFileThread(context,
						VideoRecorderManager2.this, url, Constant.PORT,
						ftp_name, ftp_password, uploadPath, fileNamePath,
						fileName, VIDEO_TYPE)).start();
			}
			break;
		case RecorderFragment.UPLOAD_REQUEST_FAILURE:
			callback.handleMessage(msg);
			break;
		case RecorderFragment.UPLOAD_SUCCESS:
			File file = new File(filePath);
			FileTools.deleteFoder(file);
			callback.handleMessage(msg);
			break;
		case RecorderFragment.UPLOAD_FAILURE:
			callback.handleMessage(msg);
			break;
		}
		return false;
	}

}
