package com.cxf.PhoneIdentification;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cxf.entity.MyPreviewCallback;
import com.cxf.net.handler.AudioRecorderManager;
import com.cxf.net.handler.AviWriter;
import com.cxf.net.handler.DrawRectThread;
import com.cxf.net.handler.ImageTools;
import com.cxf.net.handler.VideoRecorderManager;
import com.cxf.net.handler.VideoRecorderManager2;
import com.cxf.view.FaceView;

public class RecorderFragment extends Fragment implements
		SurfaceHolder.Callback, Callback {
	private VideoRecorderManager2 mediaRecorderManager;// 录制视频的类
	private SurfaceView surfaceview;// 显示视频的控件
	ImageView head;
	// 用来显示视频的一个接口，我靠不用还不行，也就是说用mediarecorder录制视频还得给个界面看
	// 想偷偷录视频的同学可以考虑别的办法。。嗯需要实现这个接口的Callback接口
	private SurfaceHolder surfaceHolder;
	public boolean isRecording = false;
	private AudioRecorderManager audioRecorderManager;
	static RecorderFragment newFragment;
	public static final int START_RECORDER = 31;
	public static final int STOP_RECORED = -31;
	public static final int UPLOAD_SUCCESS = 32;
	public static final int UPLOAD_FAILURE = -32;
	public final static int UPLOAD_REQUEST_SUCCEED = 34;
	public final static int UPLOAD_REQUEST_FAILURE = -34;
	public static final int START_UPLOAD = 33;
	public static final int RESTART_RECORDER = 35;
	public static final int START_RECORDER_FAILURE = 36;
	public static final int STOP_RECORED_FAILURE = -36;
	public static final int INIT = 37;
	public static final int HAS_NO_FACE = -38;
	public static final int HAS_FACE = 38;
	private boolean hasUploadVideo = false;
	private boolean hasUploadAudio = false;
	private boolean uploadVideoFailure = false;
	private boolean uploadAudioFailure = false;
	private static Callback callback;
	private boolean restart = false;
	int count = 0;
	int countVerify = 0;
	// 人脸识别异步线程
	FaceView faceView;
	private ExecutorService executorService;

	AviWriter aviWriter;
	Bitmap frame;
	ByteArrayOutputStream os;
	private final int MAX_THREADS = 50;

	public static Fragment newInstance(Callback cb) {
		newFragment = new RecorderFragment();
		callback = cb;
		return newFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recorder, container,
				false);
		surfaceview = (SurfaceView) view.findViewById(R.id.surfaceview);
		head = (ImageView) view.findViewById(R.id.head);
		surfaceview.setBackgroundColor(getResources().getColor(R.color.bg));
		faceView = (FaceView) view.findViewById(R.id.drawview);
		init();
		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaRecorderManager = new VideoRecorderManager2(this.getActivity(),
				this);
		audioRecorderManager = new AudioRecorderManager(this.getActivity(),
				this);

	}

	@SuppressWarnings("deprecation")
	private void init() {
		// 取得holder
		surfaceHolder = surfaceview.getHolder();
		// holder加入回调接口
		surfaceHolder.addCallback(this);
		// setType必须设置，要不出错.
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	class TestVideoListener implements OnClickListener {

		@Override
		public void onClick(View v) {
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		surfaceHolder = holder;
		if (restart) {
			restart = false;
			Message m = new Message();
			m.what = START_RECORDER;
			handleMessage(m);

		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceHolder = holder;
		if (restart) {
			restart = false;
			Message m = new Message();
			m.what = START_RECORDER;
			handleMessage(m);

		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// surfaceDestroyed的时候同时对象设置为null
		surfaceHolder = null;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (isRecording == true) {
			isRecording = false;
			// restart = true;
			mediaRecorderManager.stop();
			audioRecorderManager.stop();
		}
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (surfaceHolder != null && restart == true&&!isInit) {
		// restart = false;
		// Message m = new Message();
		// m.what = START_RECORDER;
		// handleMessage(m);
		// }
	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {
		case START_RECORDER:
			head.setVisibility(View.INVISIBLE);
			if (isRecording == false) {
				isRecording = true;
				executorService = Executors.newFixedThreadPool(MAX_THREADS);

				if (aviWriter == null) {
					File file = new File(
							Environment.getExternalStorageDirectory()
									+ "/myVideo.avi");
					try {
						aviWriter = new AviWriter(file, 2, true);
						aviWriter.setDimensions(480, 640);
						aviWriter.setSamplesPerSecond(5);
						aviWriter.setFramesPerSecond(5, 1);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (surfaceview != null) {

					surfaceview.setBackgroundColor(Color.TRANSPARENT);
				}
				if (surfaceHolder != null) {
					if (!mediaRecorderManager.isRecording())
						mediaRecorderManager.startRecord(surfaceHolder);
					if (!audioRecorderManager.isRecording())
						audioRecorderManager.startRecord(surfaceHolder);
				}

				faceView.setVisibility(View.VISIBLE);
			}
			break;
		case STOP_RECORED:
			head.setVisibility(View.VISIBLE);
			if (isRecording == true) {
				isRecording = false;
				if (surfaceview != null) {
					surfaceview.setBackgroundColor(getResources().getColor(
							R.color.bg));
				}
				if (surfaceHolder != null) {
					if (mediaRecorderManager.isRecording())
						mediaRecorderManager.stop();
					if (audioRecorderManager.isRecording())
						audioRecorderManager.stop();
				}
				try {
					if (aviWriter != null)
						aviWriter.close();
					aviWriter = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (faceView != null) {
					faceView.setVisibility(View.GONE);
					if (faceView.hasFace) {
						Message faceMsg = new Message();
						faceMsg.what = HAS_FACE;
						callback.handleMessage(faceMsg);
					} else {
						Message faceMsg = new Message();
						faceMsg.what = HAS_NO_FACE;
						callback.handleMessage(faceMsg);
					}
					faceView.hasFace = false;
				}

			}
			break;
		case RESTART_RECORDER:
			restart = true;
			break;
		case START_UPLOAD:

			String phoneNum = data.getString("phoneNum");
			String uniqueToken = data.getString("uniqueToken");
			mediaRecorderManager.upload(phoneNum, uniqueToken);
			audioRecorderManager.upload(phoneNum, uniqueToken);
			break;
		case UPLOAD_SUCCESS:
			int type = data.getInt("type");
			if (type == com.cxf.net.handler.AudioRecorderManager.AUDIO_TYPE) {
				this.hasUploadAudio = true;
				countVerify++;
			} else if (type == VideoRecorderManager.VIDEO_TYPE) {
				this.hasUploadVideo = true;
				countVerify++;
			}

			if (this.hasUploadAudio && this.hasUploadVideo && countVerify == 2) {
				countVerify = 0;
				this.hasUploadAudio = false;
				this.hasUploadVideo = false;
				Message uploadSuccessMsg = new Message();
				uploadSuccessMsg.what = UPLOAD_SUCCESS;
				callback.handleMessage(uploadSuccessMsg);

			} else if (countVerify == 2) {
				countVerify = 0;
				this.hasUploadAudio = false;
				this.hasUploadVideo = false;
				Message uploadSuccessMsg = new Message();
				uploadSuccessMsg.what = UPLOAD_FAILURE;
				callback.handleMessage(uploadSuccessMsg);
			}

			break;

		case UPLOAD_REQUEST_FAILURE:
		case UPLOAD_FAILURE:
			if (data != null) {
				int t = data.getInt("type");
				if (t == com.cxf.net.handler.AudioRecorderManager.AUDIO_TYPE) {
					this.uploadAudioFailure = true;
					count=2;
				} else if (t == VideoRecorderManager.VIDEO_TYPE) {
					this.uploadVideoFailure = true;
					count=2;
				}

			}
			if ((this.uploadAudioFailure || this.uploadVideoFailure)
					&& count == 2) {
				count = 0;
				this.uploadAudioFailure = false;
				this.uploadVideoFailure = false;
				callback.handleMessage(msg);
			}

			break;
		case VideoRecorderManager.CAMERA_CONNECT_FAILURE:
			if (audioRecorderManager.isRecording())
				audioRecorderManager.stop();
			callback.handleMessage(msg);
			break;
		case MyPreviewCallback.DRAW_RECT:
			if (isRecording) {
				Bundle bundle = msg.getData();
				byte[] btyes = bundle.getByteArray("data");
				int width = bundle.getInt("width");
				int height = bundle.getInt("height");
				final YuvImage image = new YuvImage(btyes, ImageFormat.NV21,
						width, height, null);
				os = new ByteArrayOutputStream(btyes.length);

				if (image
						.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
					byte[] tmp = os.toByteArray();
					frame = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
					frame = ImageTools.rotaingImageView(270, frame);

				}
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// 人脸检测
				// 压缩mp4

				if (frame != null) {
					ByteArrayOutputStream out2 = new ByteArrayOutputStream();
					frame.compress(Bitmap.CompressFormat.JPEG, 90, out2);
					if (out2 != null) {
						byte[] tmp2 = out2.toByteArray();

						executorService.submit(new DrawRectThread(this
								.getActivity(), null, tmp2, faceView));

						frame.recycle();
						frame = null;
						try {
							aviWriter.writeFrame(tmp2);
							tmp2 = null;
							out2.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

			break;
		case INIT:
			restart = false;
			if (faceView != null) {
				faceView.hasFace = false;
			}
			Message m = new Message();
			m.what = STOP_RECORED;
			this.handleMessage(m);
			break;
		}

		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (frame != null && !frame.isRecycled()) {
			frame.recycle();

		}
	}

}
