package com.cxf.safe_android;

import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Security_detail_activity extends Activity implements
		View.OnClickListener {
	MyAplication app;
	// 拍摄的图片
	ImageView shortImage;
	// 时间
	TextView absTime;
	// 地址
	TextView location;
	// 告警级别
	TextView level;
	// 事件名称
	TextView eventName;
	// 管理人员
	TextView personInCharge;
	// 事件描述
	TextView desp;
	SecurityAlarm sa;
	// 返回按钮
	Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_security_detail);
		this.app = (MyAplication) getApplication();
		this.absTime = (TextView) findViewById(R.id.absTime);
		this.desp = (TextView) findViewById(R.id.desp);
		this.eventName = (TextView) findViewById(R.id.eventName);
		this.level = (TextView) findViewById(R.id.level);
		this.location = (TextView) findViewById(R.id.location);
		this.personInCharge = (TextView) findViewById(R.id.personInCharge);
		this.shortImage = (ImageView) findViewById(R.id.shortImage);
		this.back = (Button) findViewById(R.id.button_left);

		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);

		this.sa = app.securityAlarmItem;
		if (this.sa != null) {
			this.shortImage.setImageBitmap(sa.shortImage);
			this.absTime.setText(sa.absTime);
			this.desp.setText(sa.desp);
			this.eventName.setText(sa.eventName);
			this.level.setText(String.valueOf(sa.level));
			this.location.setText(sa.location);
			this.personInCharge.setText(sa.personInCharge);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;

		default:
			break;
		}
	};
}