package com.cxf.safe_android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.MyAplication;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.ConnectionHandler;
import com.cxf.handler.SecurityGuardHandler;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

public class WelcomeActivity extends FragmentActivity {
	UserHandler userHandler;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;
	MyAplication app;
	HistoryCarRecordDao historyCarAlarmDao;
	HistorySecurityRecordDao historySecurityRecordDao;
	int pageSize = 5;
	ConnectionHandler connHandler;
	SharedPreferences sp;

	@Override
	protected void onResume() {
		if (connHandler.isConnectingToInternet()) {
			new Thread() {
				public void run() {
					goHome();
				};
			}.start();
		} else {
			Builder build = new AlertDialog.Builder(this).setTitle("网络连接失败！");
			build.setPositiveButton("设置", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
			}).setNegativeButton("浏览历史记录", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Editor edit = sp.edit();
					if (sp.getString("name", null) == null) {
						edit.putString("state", "未登录").commit();
					} else {
						edit.putString("state", "离线登录").commit();
					}
					app = (MyAplication) WelcomeActivity.this.getApplication();
					app.carAlarms = historyCarAlarmDao.query(1, pageSize, 0);
					app.securityAlarms = historySecurityRecordDao.query(1,
							pageSize, -1);
					app.carAlarmsChanged = true;
					app.securityAlarmItemChanges = true;
					Intent intent = new Intent(WelcomeActivity.this,
							MainActivity.class);
					WelcomeActivity.this.startActivity(intent);
					WelcomeActivity.this.finish();
				}
			});
			build.create().show();

		}
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch);
		userHandler = new UserHandler(this);
		carGuardHandler = new CarGuardHandler(this);
		historyCarAlarmDao = HistoryCarRecordDao.Instance(this);
		securityGuardHandler = new SecurityGuardHandler(this);
		historySecurityRecordDao = HistorySecurityRecordDao.Instance(this);
		connHandler = new ConnectionHandler(this);
		sp = WelcomeActivity.this.getSharedPreferences("sys_setting", 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		historyCarAlarmDao = null;
		securityGuardHandler = null;
		carGuardHandler = null;
		userHandler = null;
		historySecurityRecordDao = null;
	}

	private void goHome() {

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

			app = (MyAplication) WelcomeActivity.this.getApplication();
			app.carAlarms = historyCarAlarmDao.query(1, pageSize, 0);
			app.securityAlarms = historySecurityRecordDao
					.query(1, pageSize, -1);
			app.carAlarmsChanged = true;
			app.securityAlarmItemChanges = true;

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
