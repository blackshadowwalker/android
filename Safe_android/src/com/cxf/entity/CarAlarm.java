package com.cxf.entity;

import android.graphics.Bitmap;

public class CarAlarm implements Comparable<CarAlarm> {
	// id
	public Long id;
	// 车牌号
	public String LPNumber;
	// 方向
	public int dir;
	// 地址
	public String location;
	// 时间
	public String absTime;
	// 图片
	public Bitmap shortImageA;
	public boolean isNew=false;

	// 构造方法
	
	@Override
	public int compareTo(CarAlarm another) {
		return -absTime.compareTo(another.absTime);
	}

	public CarAlarm(Long id, String lPNumber, int dir, String location,
			String absTime, Bitmap shortImageA, boolean isNew) {
		super();
		this.id = id;
		LPNumber = lPNumber;
		this.dir = dir;
		this.location = location;
		this.absTime = absTime;
		this.shortImageA = shortImageA;
		this.isNew = isNew;
	}

}
