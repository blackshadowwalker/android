/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cxf.entity;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;

public final class MyPreviewCallback implements Camera.PreviewCallback {

	public static final int DRAW_RECT = 300;
	private final CameraConfiguration configManager;
	private Handler previewHandler;
	int width;
	int height;

	MyPreviewCallback(CameraConfiguration configManager, Handler callback) {
		this.configManager = configManager;
		this.previewHandler = callback;

	}

	public void setWidthAndHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Point cameraResolution = configManager.getCameraResolution();
		Camera.Size size = camera.getParameters().getPreviewSize();
		if (cameraResolution != null && size != null && data != null) {
			Message msg = new Message();
			msg.what = DRAW_RECT;
			Bundle bundle = new Bundle();
			bundle.putByteArray("data", data);
			bundle.putInt("width", size.width);
			bundle.putInt("height", size.height);
			msg.setData(bundle);
			previewHandler.sendMessage(msg);
		}
	}

}
