package com.cxf.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.SecurityGuardHandler;

public class RefreshDataService extends Service {
	// 声明定时器，用于创建新的进程来计时
	private Timer timer;
	private TimerTask task;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;
	boolean startReceiveCarGuards = true;
	boolean startReceiveSecurityGuards = true;
	MyAplication app;
	SharedPreferences sp;
	final int RECORDSIZE = 5;
	HistoryCarRecordDao historyCarAlarmDao;
	HistorySecurityRecordDao historySecurityRecordDao;

	// 继承Service必须要重写的方法onBind
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* Service的生命周期一：onCreate、onStartCommand、onDestroy方法构成 */
	// onCreate方法，启动服务，每次启动仅仅调用一次
	@Override
	public void onCreate() {
		carGuardHandler = new CarGuardHandler(this);
		securityGuardHandler = new SecurityGuardHandler(this);
		app = (MyAplication) getApplication();
		this.historyCarAlarmDao = new HistoryCarRecordDao(this);
		this.historySecurityRecordDao = new HistorySecurityRecordDao(this);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startTimer();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopTimer();
		this.carGuardHandler = null;
		this.securityGuardHandler = null;
		this.historyCarAlarmDao = null;
		this.historySecurityRecordDao = null;
		super.onDestroy();
	}

	// 开启定时器
	public void startTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {

				SharedPreferences sp = RefreshDataService.this
						.getSharedPreferences("sys_setting", 0);
				if (sp.getBoolean("recieve_infos", false)) {
					app = (MyAplication) RefreshDataService.this
							.getApplication();
					String ssid = sp.getString("ssid", null);
					if (ssid != null && startReceiveCarGuards) {

						System.out.println(ssid);
						sp = getSharedPreferences("car_alarm_setting", 0);

						boolean car_in = sp.getBoolean("receive_car_in", false);
						boolean car_out = sp.getBoolean("receive_car_out",
								false);
						List<CarAlarm> listCarAlarm = null;
						if (car_in && car_out) {
							listCarAlarm = carGuardHandler.requestCarAlarms(
									ssid, RECORDSIZE, -1);
						} else if (car_in && !car_out) {
							listCarAlarm = carGuardHandler.requestCarAlarms(
									ssid, RECORDSIZE, 1);
						} else if (!car_in && car_out) {
							listCarAlarm = carGuardHandler.requestCarAlarms(
									ssid, RECORDSIZE, 0);

						}

						if (listCarAlarm != null && listCarAlarm.size() > 0) {
							app.carAlarmsChanged = true;
							startReceiveCarGuards = false;
							
							// 保存到手机数据库
							historyCarAlarmDao.insert(listCarAlarm);
							
							// 返回数据给服务端
							String jsonIds = carGuardHandler
									.idsJson(listCarAlarm);
							int returnCode = carGuardHandler.receiveds(ssid,
									jsonIds);
							if (returnCode == 0) {
								startReceiveCarGuards = true;
							}
						}
					}

					if (ssid != null && startReceiveSecurityGuards) {
						sp = getSharedPreferences("security_alarm_setting", 0);
						List<SecurityAlarm> listSecurityAlarms=null;
						if(sp.getBoolean("receive_security_alarm", false))
						{
						listSecurityAlarms = securityGuardHandler
								.requestSecurityAlarms(ssid, 5, -1);
						}

						if (listSecurityAlarms != null
								&& listSecurityAlarms.size() > 0) {
							app.securityAlarmsChanges = true;
							startReceiveSecurityGuards = false;
							// 保存到手机数据库
							historySecurityRecordDao.insert(listSecurityAlarms);
							// 返回数据给服务端
							String jsonIds = securityGuardHandler
									.idsJson(listSecurityAlarms);
							int returnCode = securityGuardHandler.receiveds(
									ssid, jsonIds);
							if (returnCode == 0) {
								startReceiveSecurityGuards = true;
							}
						}
					}
				}
			}

		};
		timer.schedule(task, 0, 5000);
	}

	// 停止定时器
	public void stopTimer() {
		timer.cancel();
	}
}
