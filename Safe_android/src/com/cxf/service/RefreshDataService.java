package com.cxf.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.ConnectionHandler;
import com.cxf.handler.HistoryCarRecordHandler;
import com.cxf.handler.SecurityGuardHandler;
import com.cxf.safe_android.LoginActivity;

public class RefreshDataService extends Service {
	// 声明定时器，用于创建新的进程来计时
	private Timer timer;
	private TimerTask task;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;
	Boolean startReceiveCarGuards = true;
	Boolean startReceiveSecurityGuards = true;
	MyAplication app;
	SharedPreferences sp;
	final int CAR_RECORDSIZE = 1;
	final int SECURITY_RECORDSIZE = 1;
	HistoryCarRecordHandler historyCarRecordHandler;
	HistorySecurityRecordDao historySecurityRecordDao;
	String ssid;
	ConnectionHandler connHandler;
	private final String TAG = RefreshDataService.class.getName();

	// 继承Service必须要重写的方法onBind
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* Service的生命周期一：onCreate、onStartCommand、onDestroy方法构成 */
	// onCreate方法，启动服务，每次启动仅仅调用一次
	@Override
	public void onCreate() {
		Log.i(TAG, "RefreshDataService创建");
		carGuardHandler = new CarGuardHandler(this);
		securityGuardHandler = new SecurityGuardHandler(this);
		app = (MyAplication) getApplication();
		this.historyCarRecordHandler = HistoryCarRecordHandler.instance(this);
		this.historySecurityRecordDao = HistorySecurityRecordDao.Instance(this);
		sp = RefreshDataService.this.getSharedPreferences("sys_setting", 0);
		super.onCreate();
		connHandler = new ConnectionHandler(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		if (sp.getBoolean("recieve_infos", true)) {
			startTimer();
			Editor edit = sp.edit();
			edit.putString("start_receive", "正在接收数据");
			edit.commit();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy->stopTimer");
		stopTimer();
		this.carGuardHandler = null;
		this.securityGuardHandler = null;
		this.historyCarRecordHandler = null;
		this.historySecurityRecordDao = null;
		stopSelf();
		Editor edit = sp.edit();
		edit.putString("start_receive", "不接收数据");
		edit.commit();
		super.onDestroy();
	}

	// 开启定时器
	public void startTimer() {
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {

				if (sp.getBoolean("recieve_infos", true)
						&& connHandler.isConnectingToInternet()) {
					app = (MyAplication) RefreshDataService.this
							.getApplication();
					ssid = sp.getString("ssid", null);
					Log.i(TAG, "获得车辆告警信息");

					if (ssid != null && startReceiveCarGuards) {
						startReceiveCarGuards = false;
						new Thread() {
							public void run() {

								System.out.println(ssid);

								boolean car_in = sp.getBoolean(
										"receive_car_in", true);
								boolean car_out = sp.getBoolean(
										"receive_car_out", true);
								List<CarAlarm> listCarAlarm = null;
								if (car_in && car_out) {
									listCarAlarm = carGuardHandler
											.requestCarAlarms(ssid,
													CAR_RECORDSIZE, -1);
								} else if (car_in && !car_out) {
									listCarAlarm = carGuardHandler
											.requestCarAlarms(ssid,
													CAR_RECORDSIZE, 1);
								} else if (!car_in && car_out) {
									listCarAlarm = carGuardHandler
											.requestCarAlarms(ssid,
													CAR_RECORDSIZE, 0);

								}

								if (listCarAlarm != null
										&& listCarAlarm.size() > 0) {

									boolean ifnull = app.carAlarms == null;
									boolean iflistnull = listCarAlarm == null;
									if (ifnull) {
										app.carAlarmsChanged = true;
										app.startSoundCar = true;
									} else if (!ifnull
											&& app.carAlarms.size() == 0) {
										app.carAlarmsChanged = true;
										app.startSoundCar = true;
									} else if (!ifnull && !iflistnull
											&& app.carAlarms.size() > 0) {
										boolean biggerId = listCarAlarm.get(0).id > app.carAlarms
												.get(0).id;
										boolean timeBigger = listCarAlarm
												.get(0).absTime
												.compareTo(app.carAlarms.get(0).absTime) > 0;
										if (biggerId && timeBigger) {
											app.carAlarmsChanged = true;
											app.startSoundCar = true;
										}
									}
									historyCarRecordHandler
											.saveCarAlarms(listCarAlarm);
									Log.i(TAG, "保存获取的车辆告警信息到数据库");

									// 返回数据给服务端
									String jsonIds = carGuardHandler
											.idsJson(listCarAlarm);
									int returnCode = carGuardHandler.receiveds(
											ssid, jsonIds);
									if (returnCode == 0) {
										startReceiveCarGuards = true;
									}
								} else {
									startReceiveCarGuards = true;
								}

							};
						}.start();
					}

					if (ssid != null && startReceiveSecurityGuards) {
						startReceiveSecurityGuards = false;
						new Thread() {
							public void run() {

								Log.i(TAG, "获得安防告警信息");
								List<SecurityAlarm> listSecurityAlarms = null;
								if (sp.getBoolean("receive_security_alarm",
										true)) {
									listSecurityAlarms = securityGuardHandler
											.requestSecurityAlarms(ssid,
													SECURITY_RECORDSIZE, -1);
								}

								if (listSecurityAlarms != null
										&& listSecurityAlarms.size() > 0) {
									boolean ifnull = app.securityAlarms == null;
									boolean iflistnull = listSecurityAlarms == null;
									if (ifnull) {
										app.securityAlarmsChanges = true;
										app.startSoundSecurity=true;
									} else if (!ifnull
											&& app.securityAlarms.size() == 0) {
										app.securityAlarmsChanges = true;
										app.startSoundSecurity=true;
									} else if (!ifnull && !iflistnull
											&& app.securityAlarms.size() > 0) {
										boolean biggerId = listSecurityAlarms
												.get(0).id > app.securityAlarms
												.get(0).id;
										boolean timeBigger = listSecurityAlarms
												.get(0).absTime
												.compareTo(app.securityAlarms
														.get(0).absTime) > 0;
										if (biggerId && timeBigger) {
											app.securityAlarmsChanges = true;
											app.startSoundSecurity=true;
										}
									}

									// 保存到手机数据库
									int size = historySecurityRecordDao
											.getCount();
									if (size > 50) {
										long tempId = historySecurityRecordDao
												.queryId(50, -1);
										historySecurityRecordDao
												.deleteStartFrom(tempId);
										Log.i(TAG, "清理安防告警数据库信息");
									}
									historySecurityRecordDao
											.insert(listSecurityAlarms);
									Log.i(TAG, "保存获取的安防告警信息到数据库");
									// 返回数据给服务端
									String jsonIds = securityGuardHandler
											.idsJson(listSecurityAlarms);
									int returnCode = securityGuardHandler
											.receiveds(ssid, jsonIds);
									if (returnCode == 0) {
										startReceiveSecurityGuards = true;
									}
								} else {
									startReceiveSecurityGuards = true;
								}

							};
						}.start();
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
