package com.cxf.entity;

import android.app.Application;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class Location extends Application {

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	public Double lat;
	public Double lon;

	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(this);
		/**
		 * ——————————————————————————————————————————————————————————————————
		 * 这里的AK和应用签名包名绑定，如果使用在自己的工程中需要替换为自己申请的Key
		 * ——————————————————————————————————————————————————————————————————
		 */
		mLocationClient.setAK("6c8d83da633da1da6e75dc18660221a6");
		mLocationClient.registerLocationListener(myListener);
		// 位置提醒相关代码
		// mNotifyer = new NotifyLister();
		// mNotifyer.SetNotifyLocation(40.047883,116.312564,3000,"gps");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
		// mLocationClient.registerNotify(mNotifyer);

		super.onCreate();
		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			lat=location.getLatitude();
			lon=location.getLongitude();

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			mVibrator01.vibrate(1000);
		}
	}
}