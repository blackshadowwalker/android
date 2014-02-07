/*
 * Copyright (C) 2008 ZXing authors
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

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraManager {

	private static final String TAG = CameraManager.class.getSimpleName();

	private static final int MIN_FRAME_WIDTH = 240;
	private static final int MIN_FRAME_HEIGHT = 240;
	private static final int MAX_FRAME_WIDTH = 960; // = 1920/2
	private static final int MAX_FRAME_HEIGHT = 540; // = 1080/2
	Handler callback;
	private final Context context;
	private final CameraConfiguration configManager;
	private Camera camera;
	private Rect framingRect;
	private boolean initialized = false;
	private int requestedFramingRectWidth;
	private int requestedFramingRectHeight;
	CameraInfo info;
	int cameraId = 0;
	private final MyPreviewCallback previewCallback;
	private boolean previewing = false;
	private Point cameraResolution;

	/**
	 * Preview frames are delivered here, which we pass on to the registered
	 * handler. Make sure to clear the handler so it will only receive one
	 * message.
	 */

	public CameraManager(Context context, Handler callback) {
		this.context = context;
		this.configManager = new CameraConfiguration(context);
		this.callback = callback;
		previewCallback = new MyPreviewCallback(configManager, callback);
	}

	public Camera getCamera() throws IOException {
		info = null;
		Camera c = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
				info = new CameraInfo();
				Camera.getCameraInfo(i, info);
				cameraId = i;
				if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {// 这就是前置摄像头，亲。
					try {
						c = Camera.open(i);
						break;
					} catch (Exception e) {
						e.printStackTrace();
						if (camera != null) {
							if (previewing) {
								camera.stopPreview();
							}
							camera.lock();
							camera.release();
							camera = null;
						}
						throw new IOException();
					}
				}
			}
		}

		if (c == null) {
			c = Camera.open();
		}

		return c;
	}

	public int getCameraId() {
		return cameraId;
	}

	public void setCameraDisplayOrientation(Camera c) {
		Camera.getCameraInfo(cameraId, info);
		int rotation = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		c.setDisplayOrientation(result);
	}

	public synchronized Camera openCamera(SurfaceHolder surfaceHolder)
			throws Exception {

		this.camera = getCamera();
		if (camera != null) {
			setCameraDisplayOrientation(camera);
			camera.setPreviewDisplay(surfaceHolder);
			camera.setPreviewCallback(previewCallback);
		}

		if (camera != null) {
			configManager.initFromCameraParameters(camera);
			if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
				setManualFramingRect(requestedFramingRectWidth,
						requestedFramingRectHeight);
				requestedFramingRectWidth = 0;
				requestedFramingRectHeight = 0;
			}

			Camera.Parameters parameters = camera.getParameters();
			String parametersFlattened = parameters == null ? null : parameters
					.flatten(); // Save these, temporarily
			try {
				configManager.setDesiredCameraParameters(camera, false);
			} catch (RuntimeException re) {
				// Driver failed
				Log.w(TAG,
						"Camera rejected parameters. Setting only minimal safe-mode parameters");
				Log.i(TAG, "Resetting to saved camera params: "
						+ parametersFlattened);
				// Reset:
				if (parametersFlattened != null) {
					parameters = camera.getParameters();
					parameters.unflatten(parametersFlattened);
					parameters.setRotation(0);
					try {
						camera.setParameters(parameters);
						configManager.setDesiredCameraParameters(camera, true);
					} catch (RuntimeException re2) {
						// Well, darn. Give up
						Log.w(TAG,
								"Camera rejected even safe-mode parameters! No configuration");
					}
				}
			}

			cameraResolution = configManager.getCameraResolution();
			previewCallback.setWidthAndHeight(cameraResolution.x,
					cameraResolution.y);
		}

		return this.camera;

	}

	public Point getPictureResolution() {
		return configManager.getPictureResolution();
	}

	public Point getScreenResolution() {
		return configManager.getScreenResolution();
	}

	public synchronized Rect getFramingRect() {
		if (framingRect == null) {
			if (camera == null) {
				return null;
			}
			Point screenResolution = configManager.getScreenResolution();
			if (screenResolution == null) {
				return null;
			}

			int width = findDesiredDimensionInRange(screenResolution.x,
					MIN_FRAME_WIDTH, MAX_FRAME_WIDTH);
			int height = findDesiredDimensionInRange(screenResolution.y,
					MIN_FRAME_HEIGHT, MAX_FRAME_HEIGHT);

			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
					topOffset + height);
			Log.d(TAG, "Calculated framing rect: " + framingRect);
		}
		return framingRect;
	}

	private static int findDesiredDimensionInRange(int resolution, int hardMin,
			int hardMax) {
		int dim = resolution / 2; // Target 50% of each dimension
		if (dim < hardMin) {
			return hardMin;
		}
		if (dim > hardMax) {
			return hardMax;
		}
		return dim;
	}

	/**
	 * Allows third party apps to specify the scanning rectangle dimensions,
	 * rather than determine them automatically based on screen resolution.
	 * 
	 * @param width
	 *            The width in pixels to scan.
	 * @param height
	 *            The height in pixels to scan.
	 */
	public synchronized void setManualFramingRect(int width, int height) {
		if (!initialized) {
			Point screenResolution = configManager.getScreenResolution();
			if (width > screenResolution.x) {
				width = screenResolution.x;
			}
			if (height > screenResolution.y) {
				height = screenResolution.y;
			}
			framingRect = new Rect(0, 0, width, height);
			Log.d(TAG, "Calculated manual framing rect: " + framingRect);

		} else {
			requestedFramingRectWidth = width;
			requestedFramingRectHeight = height;

			initialized = true;
		}
	}

	public synchronized void stopPreview() {
		if (camera != null && previewing) {
			camera.stopPreview();
			previewing = false;
		}
	}

	public synchronized void startPreview() {
		Camera theCamera = camera;
		if (theCamera != null && !previewing) {
			theCamera.startPreview();
			previewing = true;
		}
	}

	public void initPreviewCallback(Camera c) {
		c.setPreviewCallback(previewCallback);
	}

}
