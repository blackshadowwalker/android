package com.cxf.safe_android;

import com.cxf.entity.CarAlarm;
import com.cxf.entity.MyAplication;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Car_Guard_detail_activity extends Activity implements
		View.OnClickListener {
	MyAplication app;
	// 初始化时间
	TextView time;
	// 初始化地点
	TextView place;
	// 车牌号
	TextView carNumber;
	// 方向
	TextView direction;
	// 图片
	ImageView img;
	CarAlarm ca;
	// 返回按钮
	Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_car_guard_detail);
		this.app = (MyAplication) getApplication();
		this.time = (TextView) findViewById(R.id.absTime);
		this.place = (TextView) findViewById(R.id.location);
		this.carNumber = (TextView) findViewById(R.id.number);
		this.direction = (TextView) findViewById(R.id.direction);
		this.img = (ImageView) findViewById(R.id.shortImage);
		this.back = (Button) findViewById(R.id.button_left);

		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);

		this.ca = app.carAlarmItem;
		if (this.ca != null) {
			this.img.setImageBitmap(ca.shortImageA);
			this.time.setText(ca.absTime);
			this.place.setText(ca.location);
			this.carNumber.setText(ca.LPNumber);
			this.direction.setText(String.valueOf(ca.dir));
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