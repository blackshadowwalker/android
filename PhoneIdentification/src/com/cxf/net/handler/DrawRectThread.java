package com.cxf.net.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cxf.view.FaceView;

public class DrawRectThread implements Runnable {
	byte[] data;
	FaceView faceView;
	Context context;
	Handler handler;
	public DrawRectThread(Context context, Handler handler, byte[] data,
			FaceView faceView) {
		this.data = data;
		this.faceView = faceView;
		this.handler = handler;
	}

	@Override
	public void run() {
		Looper.prepare();//
		if (faceView != null) {
			boolean hasFace = faceView.draw(data);
			if (hasFace) {
				faceView.hasFace = true;
			}
		}
		Looper.loop();
	}
	
	

}
