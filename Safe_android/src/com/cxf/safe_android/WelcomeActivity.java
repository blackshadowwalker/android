package com.cxf.safe_android;

import com.cxf.entity.MyAplication;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.SecurityGuardHandler;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class WelcomeActivity extends FragmentActivity {
	UserHandler userHandler;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;
	MyAplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch);
		userHandler = new UserHandler(this);
		carGuardHandler = new CarGuardHandler(this);
		securityGuardHandler = new SecurityGuardHandler(this);
		new Thread() {
			public void run() {
				goHome();
			};
		}.start();

	}

	private void goHome() {
		SharedPreferences sp = WelcomeActivity.this.getSharedPreferences(
				"sys_setting", 0);
		// 如果保存了登录信息，直接登录，否则弹出登录界面
		if (sp.getBoolean("keep_info", false) == true) {
			autoLogin();
		} else {
			Intent intent = new Intent(WelcomeActivity.this,
					LoginActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}
	}

	private void autoLogin() {
		SharedPreferences sp = WelcomeActivity.this.getSharedPreferences(
				"sys_setting", 0);
		String nameStr = sp.getString("name", null);
		String passStr = sp.getString("password", null);
		String ssid = userHandler.login(nameStr, passStr);
		if (ssid != null) {
			sp.edit().putString("ssid", ssid).commit();
//			app=(MyAplication) WelcomeActivity.this.getApplication();
//    		app.carAlarms = carGuardHandler.requestCarAlarms(ssid);
//    		app.securityAlarms=securityGuardHandler.requestSecurityAlarms(ssid);
//    		
    		Intent intent_service = new Intent(WelcomeActivity.this,
					RefreshDataService.class);
			intent_service.setAction("REFRESH_DATA");
			startService(intent_service);
    		
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		} else {
			Intent intent = new Intent(WelcomeActivity.this,
					LoginActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}

	}

}
