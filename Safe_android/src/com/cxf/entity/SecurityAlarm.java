package com.cxf.entity;

import com.cxf.safe_android.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SecurityAlarm implements Comparable<SecurityAlarm>{
	// id
	public Long id;
	// 时间
	public String absTime="";
	// 地址
	public String location="";
	// 告警级别
	public int level;
	// 事件名称
	public String eventName="";
	// 管理人员
	public String personInCharge="";
	// 事件描述
	public String desp="";
	// 图片
	public Bitmap shortImage;
	public boolean isNew=false;
	public String bigImageUrl="";

	

	public SecurityAlarm(Long id, String absTime, String location,
			int level, String eventName, String personInCharge, String desp,
			Bitmap shortImage, boolean isNew,String bigImageUrl) {
		super();
		this.id = id;
		
		
		if(absTime!=null)
		{
			this.absTime = absTime;
		}
		if(location!=null)
		{
			this.location = location;
		}
		this.level = level;
		if(eventName!=null)
		{
		this.eventName = eventName;
		}
		if(personInCharge!=null)
		{
		this.personInCharge = personInCharge;
		}
		if(desp!=null)
		{
		this.desp = desp;
		}
		if(shortImage!=null)
		{
		this.shortImage = shortImage;
		}
		else{
			Bitmap bm=BitmapFactory.decodeResource(null, R.drawable.item_logo);
			this.shortImage=bm;
		}
		this.shortImage = shortImage;
		this.isNew = isNew;
		if(bigImageUrl!=null)
		{
			this.bigImageUrl=bigImageUrl;
		}
	}



	@Override
	public int compareTo(SecurityAlarm another) {
		return -absTime.compareTo(another.absTime);
	}

	// 构造方法

}
