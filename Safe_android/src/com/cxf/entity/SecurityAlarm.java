package com.cxf.entity;

import android.graphics.Bitmap;

public class SecurityAlarm implements Comparable<SecurityAlarm>{
	// id
	public Long id;
	// 时间
	public String absTime;
	// 地址
	public String location;
	// 告警级别
	public int level;
	// 事件名称
	public String eventName;
	// 管理人员
	public String personInCharge;
	// 事件描述
	public String desp;
	// 图片
	public Bitmap shortImage;
	public boolean isNew=false;

	

	public SecurityAlarm(Long id, String absTime, String location,
			int level, String eventName, String personInCharge, String desp,
			Bitmap shortImage, boolean isNew) {
		super();
		this.id = id;
		this.absTime = absTime;
		this.location = location;
		this.level = level;
		this.eventName = eventName;
		this.personInCharge = personInCharge;
		this.desp = desp;
		this.shortImage = shortImage;
		this.isNew = isNew;
	}



	@Override
	public int compareTo(SecurityAlarm another) {
		return -absTime.compareTo(another.absTime);
	}

	// 构造方法

}
