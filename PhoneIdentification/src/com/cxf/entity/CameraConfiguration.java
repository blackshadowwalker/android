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

import java.util.Collection;
import java.util.List;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */
public final class CameraConfiguration {

	private static final String TAG = "CameraConfiguration";

	// private static final int MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
	// private static final int MAX_PREVIEW_PIXELS = 1280 * 800;
	//
	// private static final int MIN_PICTURE_PIXELS = 480 * 320; // normal screen
	// private static final int MAX_PICTURE_PIXELS = 700 * 500;

	private final Context context;
	private Point screenResolution;
	private Point cameraResolution;
	private Point pictureResolution;
	private final int MAX_PIXS = 800000;

	CameraConfiguration(Context context) {
		this.context = context;
	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	@SuppressWarnings("deprecation")
	void initFromCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		if (width < height) {
			Log.i(TAG,
					"Display reports portrait orientation; assuming this is incorrect");
			int temp = width;
			width = height;
			height = temp;
		}
		screenResolution = new Point(width, height);
		Log.i(TAG, "Screen resolution: " + screenResolution);
		cameraResolution = findBestPreviewSizeValue(parameters,
				screenResolution);
		pictureResolution = findBestPictureResolution(parameters,
				screenResolution);
		Log.i(TAG, "Camera resolution: " + screenResolution);
	}

	void setDesiredCameraParameters(Camera camera, boolean safeMode) {
		Camera.Parameters parameters = camera.getParameters();

		if (parameters == null) {
			Log.w(TAG,
					"Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

		if (safeMode) {
			Log.w(TAG,
					"In camera config safe mode -- most settings will not be honored");
		}

		String focusMode = null;
		focusMode = findSettableValue(parameters.getSupportedFocusModes(),
				Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
		if (focusMode != null) {
			parameters.setFocusMode(focusMode);
		}
		parameters.setRotation(270);
		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		parameters.setPreviewFpsRange(5, 10);
		parameters.setPictureFormat(ImageFormat.NV21);
		parameters.setPictureSize(pictureResolution.x, pictureResolution.y);
		camera.setParameters(parameters);
	}

	Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	boolean getTorchState(Camera camera) {
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			if (parameters != null) {
				String flashMode = camera.getParameters().getFlashMode();
				return flashMode != null
						&& (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH
								.equals(flashMode));
			}
		}
		return false;
	}

	void setTorch(Camera camera, boolean newSetting) {
		Camera.Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting, false);
		camera.setParameters(parameters);
	}

	private void doSetTorch(Camera.Parameters parameters, boolean newSetting,
			boolean safeMode) {
		String flashMode;
		if (newSetting) {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(),
					Camera.Parameters.FLASH_MODE_TORCH,
					Camera.Parameters.FLASH_MODE_ON);
		} else {
			flashMode = findSettableValue(parameters.getSupportedFlashModes(),
					Camera.

					Parameters.FLASH_MODE_OFF);
		}
		if (flashMode != null) {
			parameters.setFlashMode(flashMode);
		}

	}

	private Point findBestPreviewSizeValue(Camera.Parameters parameters,
			Point screenResolution) {

		int w = 640;
		int h = 480;

		List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE
					|| (size.width * size.height) > (w * h))
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff
						&& (size.width * size.height) < (w * h)) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		Point p = new Point(optimalSize.width, optimalSize.height);
		return p;
	}

	private Point findBestPictureResolution(Camera.Parameters parameters,
			Point screenResolution) {

		int w = screenResolution.x;
		int h = screenResolution.y;
		double r = 1;
		Point bestSize = new Point();
		if (w * h >= MAX_PIXS)
			r = Math.pow((w * h) / MAX_PIXS, 0.5);
		bestSize.x = (int) (w / r);
		bestSize.y = (int) (h / r);
		return bestSize;
	}

	public Point getPictureResolution() {
		return pictureResolution;
	}

	private static String findSettableValue(Collection<String> supportedValues,
			String... desiredValues) {
		Log.i(TAG, "Supported values: " + supportedValues);
		String result = null;
		if (supportedValues != null) {
			for (String desiredValue : desiredValues) {
				if (supportedValues.contains(desiredValue)) {
					result = desiredValue;
					break;
				}
			}
		}
		Log.i(TAG, "Settable value: " + result);
		return result;
	}

}
