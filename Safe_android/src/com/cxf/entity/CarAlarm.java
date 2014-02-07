package com.cxf.entity;

import android.graphics.Bitmap;

public class CarAlarm implements Comparable<CarAlarm> {
	// id
	public Long id;
	// 车牌号
	public String LPNumber = "";
	// 方向
	public int dir;
	// 地址
	public String location = "";
	// 时间
	public String absTime = "";
	// 图片
	public Bitmap shortImageA;
	public boolean isNew = false;
	public String bigImageUrl = "";

	// 构造方法

	@Override
	public int compareTo(CarAlarm another) {
		return -absTime.compareTo(another.absTime);
	}

	public CarAlarm(Long id, String lPNumber, int dir, String location,
			String absTime, Bitmap shortImageA, boolean isNew,
			String bigImageUrl) {
		super();
		this.id = id;
		if (lPNumber != null) {
			LPNumber = lPNumber;
		}

		this.dir = dir;

		if (location != null) {
			this.location = location;
		}
		if (absTime != null) {
			this.absTime = absTime;
		}
		if (shortImageA != null) {
			this.shortImageA = shortImageA;
		}
		if (bigImageUrl != null) {
			this.bigImageUrl = bigImageUrl;
		}

		this.isNew = isNew;
	}

}
