package com.cxf.entity;

import android.app.Application;

public class MyApplication extends Application {

	public int state = 1;
	public String url = Constant.HOST;
	public String domain = Constant.IP;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
}
