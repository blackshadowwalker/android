package com.cxf.entity;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyAplication extends Application {
	public List<CarAlarm> carAlarms=new ArrayList<CarAlarm>();
	public List<SecurityAlarm> securityAlarms=new ArrayList<SecurityAlarm>();
	public SecurityAlarm securityAlarmItem;
	public CarAlarm carAlarmItem;
	public boolean carAlarmsChanged=false;
	public boolean securityAlarmsChanges=false;
}
