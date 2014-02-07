package com.cxf.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.handler.ExceptionHandler;

import android.app.Application;

public class MyAplication extends Application {
	public List<CarAlarm> carAlarms = new ArrayList<CarAlarm>();
	public List<SecurityAlarm> securityAlarms = new ArrayList<SecurityAlarm>();
	public SecurityAlarm securityAlarmItem;
	public CarAlarm carAlarmItem;
	public boolean carAlarmsChanged = false;
	public boolean carAlarmItemChanged = false;
	public boolean securityAlarmsChanges = false;
	public boolean securityAlarmItemChanges = false;
	public String host = null;
	public static final String PATH_ERROR_LOG = File.separator + "data"
			+ File.separator + "data" + File.separator + "com.cxf.safe_android"
			+ File.separator + "files" + File.separator + "error.log";
	private boolean need2Exit;
	/** 异常处理类。 */
	private ExceptionHandler ueHandler;
	public boolean carAlarmsIfInit=false;
	public boolean securityIfInit=false;
	public boolean startSoundCar=false;
	public boolean startSoundSecurity=false;

	public void onCreate() {
		need2Exit = false;
		ueHandler = new ExceptionHandler(this);
		// 设置异常处理实例
		Thread.setDefaultUncaughtExceptionHandler(ueHandler);
		
	}

	public void setNeed2Exit(boolean bool) {
		need2Exit = bool;
	}

	public boolean need2Exit() {
		return need2Exit;
	}
}
