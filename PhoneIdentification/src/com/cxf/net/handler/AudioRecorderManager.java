package com.cxf.net.handler;

import java.io.File;
import java.io.IOException;

import com.cxf.PhoneIdentification.RecorderFragment;
import com.cxf.entity.Constant;
import com.cxf.entity.MyApplication;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

public final class AudioRecorderManager implements Callback {

	private static final String TAG = AudioRecorderManager.class
			.getSimpleName();
	MediaRecorder recorder;
	Context context;
	Callback callback;
	String filePath;
	String fileNamePath;
	String fileName;
	private boolean started = false;
	public final static int AUDIO_TYPE = 2;
	public final static int START_FAILURE = 666;
	public final static int STOP = -666;
	MyApplication app;

	public AudioRecorderManager(Context context, Callback cb) {
		Log.i(TAG, "create MediaRecorderManager object.");
		this.context = context;
		recorder = new MediaRecorder();
		callback = cb;
		fileNamePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/";
		fileName = "myAudio.amr";
		this.filePath = fileNamePath + fileName;
		app=(MyApplication) context.getApplicationContext();
	}

	public void startRecord(SurfaceHolder surfaceHolder) {
		File file = new File(filePath);

		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}

			/* ①Initial：实例化MediaRecorder对象 */
			recorder = new MediaRecorder();
			/* ②setAudioSource/setVedioSource */
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
			/*
			 * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
			 * THREE_GPP(3gp格式，H263视频
			 * /ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
			 */
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			/* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			/* ②设置输出文件的路径 */
			recorder.setOutputFile(file.getAbsolutePath());
			/* ③准备 */
			recorder.prepare();
			/* ④开始 */
			recorder.start();
			started = true;
			/* 按钮状态 */
		} catch (IOException e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = START_FAILURE;
			callback.handleMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = START_FAILURE;
			callback.handleMessage(msg);
		}
	}

	public boolean isRecording() {
		return started;
	}

	public void stop() {
		if (recorder != null && started == true) {
			try{
			recorder.stop();
			}catch(Exception e)
			{}
			started = false;
		}
	}

	public void release() {
		if (recorder != null) {
			recorder.release();
		}

		recorder = null;
	}

	public void upload(String phoneNum, String uniqueToken) {
		new Thread(new UploadFileRequestThread(context, this, uniqueToken,
				phoneNum, AUDIO_TYPE)).start();
	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {

		case RecorderFragment.UPLOAD_REQUEST_SUCCEED:
			String result = data.getString("result");
			String ftp_name = data.getString("ftp_name");
			String ftp_password = data.getString("ftp_password");
			String url="";
			if(app!=null)
			{
				url=app.domain;
			}
			if(ftp_name!=null&&ftp_password!=null&&url!=null)
			{
			new Thread(new UploadFileThread(context, AudioRecorderManager.this,
					url, Constant.PORT, ftp_name, ftp_password, result,
					fileNamePath, fileName, AUDIO_TYPE)).start();
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
			break;
		case STOP:
			stop();
			break;
		}
		return false;
	}

	
}
