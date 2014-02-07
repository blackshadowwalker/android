package com.cxf.net.handler;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import com.cxf.PhoneIdentification.RecorderFragment;
import com.cxf.entity.CameraManager;
import com.cxf.entity.Constant;
import com.cxf.entity.MyApplication;

public final class VideoRecorderManager3 implements Callback, OnErrorListener {

	private static final String TAG = VideoRecorderManager3.class
			.getSimpleName();
	Camera camera;
	Callback callback;
	MediaRecorder mediarecorder;
	CameraManager cameraManager;
	Context context;
	private String uploadPath;
	private String fileNamePath = "";
	private String fileName = "";
	int recorderDegree = 0;
	boolean started = false;
	@SuppressWarnings("unused")
	private String filePath;
	public final static int VIDEO_TYPE = 1;
	public final static int CAMERA_CONNECT_FAILURE = -100;
	Handler handler;
	MyApplication app;
	LocalSocket receiver, sender;
	LocalServerSocket lss;

	public VideoRecorderManager3(Context context, Callback cb) {
		Log.i(TAG, "create MediaRecorderManager object.");
		this.context = context;
		this.mediarecorder = new MediaRecorder();
		handler = new Handler(callback);
		this.cameraManager = new CameraManager(context, handler);
		this.fileNamePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/";
		this.fileName = "myVideo.mp4";
		this.filePath = this.fileNamePath + this.fileName;
		this.callback = cb;
		app = (MyApplication) context.getApplicationContext();

	}

	public void initSocket() {
		receiver = new LocalSocket();
		try {
			lss = new LocalServerSocket("VideoCamera");
			receiver.connect(new LocalSocketAddress("VideoCamera"));
			receiver.setReceiveBufferSize(500000);
			receiver.setSendBufferSize(500000);
			sender = lss.accept();
			sender.setReceiveBufferSize(500000);
			sender.setSendBufferSize(500000);
		} catch (IOException e) {
			return;
		}
	}

	public boolean isRecording() {
		return started;
	}

	public void startRecord(SurfaceHolder surfaceHolder) {
		started = true;
		initSocket();
		if (surfaceHolder != null) {
			try {
				if (camera == null) {
					camera = cameraManager.openCamera(surfaceHolder);
				}

			} catch (IOException e1) {
				e1.printStackTrace();
				started = false;
				Message msg = new Message();
				msg.what = CAMERA_CONNECT_FAILURE;
				Bundle data = new Bundle();
				data.putString("result", "无法连接到摄像头！");
				msg.setData(data);
				callback.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (camera != null && mediarecorder != null) {
				camera.unlock();
				mediarecorder.setCamera(camera);
				int cameraId = cameraManager.getCameraId();
				setMediaRecorderDisplayOrientation(mediarecorder, cameraId,
						camera);
				try {
					// 设置录制视频源为Camera(相机)
					mediarecorder
							.setVideoSource(MediaRecorder.VideoSource.CAMERA);

					// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
					mediarecorder
							.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					// 设置录制的视频编码h263 h264
					mediarecorder
							.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
					// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错

					// if ("samsung".equals(android.os.Build.MANUFACTURER)) {
					// // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
					// mediarecorder.setVideoFrameRate(12);
					// }

					mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
					// 设置视频文件输出的路径
					mediarecorder.setOutputFile(sender.getFileDescriptor());
					mediarecorder.setOnErrorListener(this);

					// 准备录制
					mediarecorder.prepare();
					// 开始录制
					mediarecorder.start();
					new Thread() {
						public void run() {
							while (isRecording()) {
								InputStream is;
								try {
									is = receiver.getInputStream();
									Log.i("vedio", is.toString());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						};
					}.start();

				} catch (IllegalStateException e) {
					stop();
					e.printStackTrace();
				} catch (IOException e) {
					stop();
					e.printStackTrace();
				}
			}
		}
	}

	public void stop() {
		if (mediarecorder != null && started == true) {

			mediarecorder.stop();
			camera.stopPreview();
			camera.lock();
			camera.release();
			camera = null;
			started = false;

			try {
				lss.close();
				receiver.close();
				sender.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void release() {
		if (mediarecorder != null) {
			mediarecorder.release();
		}

		mediarecorder = null;
	}

	public void setMediaRecorderDisplayOrientation(MediaRecorder recorder,
			int cameraId, Camera c) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int mOrientation = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getRotation();
		int rotation = 0;
		if (mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				rotation = (info.orientation - mOrientation + 360) % 360;
			} else { // back-facing camera
				rotation = (info.orientation + mOrientation) % 360;
			}
		} else {
			// Get the right original orientation
			rotation = info.orientation;
		}
		// mMediaRecorder.setOrientationHint(rotation);

		if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {
			if (rotation == 270 || rotation == 90 || rotation == 180) {
				recorder.setOrientationHint(180);
			} else {
				recorder.setOrientationHint(0);
			}
		} else {
			if (rotation == 180) {
				recorder.setOrientationHint(180);
			} else {
				recorder.setOrientationHint(0);
			}
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
			new Thread(new UploadFileThread(context,
					VideoRecorderManager3.this, url, Constant.PORT, ftp_name,
					ftp_password, uploadPath, fileNamePath, fileName,
					VIDEO_TYPE)).start();
			break;
		case RecorderFragment.UPLOAD_REQUEST_FAILURE:
			callback.handleMessage(msg);
			break;
		case RecorderFragment.UPLOAD_SUCCESS:
			callback.handleMessage(msg);
			break;
		case RecorderFragment.UPLOAD_FAILURE:
			callback.handleMessage(msg);
			break;
		}
		return false;
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		if (mediarecorder != null) {
			mediarecorder.release();
		}

	}
}
