package com.cxf.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cxf.net.handler.DisplayUtil;
import com.cxf.net.handler.ImageTools;
//import android.graphics.PorterDuff.Mode;
//import android.graphics.PorterDuffXfermode;

public class FaceView extends SurfaceView implements SurfaceHolder.Callback {

	private int imageWidth, imageHeight;
	private int numberOfFace = 1;
	private FaceDetector myFaceDetect;
	private FaceDetector.Face[] myFace;
	float myEyesDistance;
	int numberOfFaceDetected;
	Bitmap myBitmap;
	Context context;
	String TAG = "FaceView";
	protected SurfaceHolder surfaceHolder;
	int count = 0;
	Point screenResolution;
	Canvas canvas;
	Paint myPaint;
	public boolean hasFace=false;
	public static final int HAS_NO_FACE=1111;

	public FaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		screenResolution = DisplayUtil.getScreenResolution(context);
		myPaint = new Paint();
		myPaint.setColor(Color.GREEN);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(3);
		myPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		myPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	}

	public FaceView(Context context) {
		super(context);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		surfaceHolder = holder;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceHolder = holder;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceHolder = null;
	}

	// void clearDraw() {
	// canvas = surfaceHolder.lockCanvas();
	// canvas.drawColor(Color.BLUE);
	// surfaceHolder.unlockCanvasAndPost(canvas);
	// }

	public synchronized boolean draw(byte[] data) {
		boolean flag = false;
		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap myBitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
				BitmapFactoryOptionsbfo);

		float scale = 1;
		imageWidth = myBitmap.getWidth();
		imageHeight = myBitmap.getHeight();

		if ((imageHeight * imageHeight) < (screenResolution.y * screenResolution.x)) {
			scale = (float) Math.pow((screenResolution.x * screenResolution.y)
					/ (imageWidth * imageHeight), 0.5);
		}
		myBitmap = ImageTools.zoomBitmap(myBitmap, scale);
		myBitmap = ImageTools.convertHorizontalBitmap(myBitmap);
		imageWidth = myBitmap.getWidth();
		imageHeight = myBitmap.getHeight();
		myFace = new FaceDetector.Face[numberOfFace];
		myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
		numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace);
		if(numberOfFaceDetected>0)
		{
			flag=true;
		}
		myBitmap.recycle();
		myBitmap = null;
		canvas = surfaceHolder.lockCanvas();
		
		if (canvas != null) {
			// Paint vPaint = new Paint();
			// vPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			// vPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
			// vPaint.setAlpha(80); // Bitmap透明度(0 ~ 100)
			// canvas.drawBitmap(myBitmap, 0, 0, vPaint);
			Paint p = new Paint();
	        //清屏
	        p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
	        canvas.drawPaint(p);
	        p.setXfermode(new PorterDuffXfermode(Mode.SRC));

			for (int i = 0; i < numberOfFaceDetected; i++) {
				Face face = myFace[i];
				PointF myMidPoint = new PointF();
				face.getMidPoint(myMidPoint);
				myEyesDistance = face.eyesDistance();
				canvas.drawRect((int) (int) (myMidPoint.x - myEyesDistance),
						(int) (myMidPoint.y - myEyesDistance),
						(int) (myMidPoint.x + myEyesDistance),
						(int) (myMidPoint.y + myEyesDistance), myPaint);
				Log.i(TAG, "face:" + i);
			}
			surfaceHolder.unlockCanvasAndPost(canvas);
		}

		return flag;
	}
}
