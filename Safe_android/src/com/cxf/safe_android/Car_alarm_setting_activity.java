package com.cxf.safe_android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Car_alarm_setting_activity extends Activity implements
		OnCheckedChangeListener, View.OnClickListener {

	// 设置是否接收驶入车辆数据
	CheckBox receiveCarInCheckbox;
	//
	CheckBox receiveCarOutCheckbox;
	// 设置是否接收车辆驶出数据
	SharedPreferences sp;
	// 返回按钮
	Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_guard_setting);

		this.receiveCarInCheckbox = (CheckBox) findViewById(R.id.opt1_car_in);
		this.receiveCarOutCheckbox = (CheckBox) findViewById(R.id.opt1_car_out);
		this.back = (Button) findViewById(R.id.button_left);

		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);

		this.receiveCarInCheckbox.setOnCheckedChangeListener(this);
		this.receiveCarOutCheckbox.setOnCheckedChangeListener(this);

		sp = getSharedPreferences("sys_setting", 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@SuppressWarnings("unused")
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {
		case R.id.opt1_car_in:

			if (sp != null) {
				Editor edit = sp.edit();
				edit.putBoolean("receive_car_in", isChecked);
				edit.commit();

				if (isChecked == true && !sp.getBoolean("recieve_infos", false)) {
					Editor sysEditor = sp.edit();
					sysEditor.putBoolean("recieve_infos", true);
					sysEditor.commit();
				}
				if (isChecked = false
						&& sp.getBoolean("receive_car_out", false)
						&& sp.getBoolean("receive_security_alarm", false)) {
					Editor sysEditor = sp.edit();
					sysEditor.putBoolean("recieve_infos", false);
					sysEditor.commit();
				}
			}
			break;
		case R.id.opt1_car_out:
			Editor edit = sp.edit();
			edit.putBoolean("receive_car_out", isChecked);
			edit.commit();
			if (isChecked == true && !sp.getBoolean("recieve_infos", false)) {
				Editor sysEditor = sp.edit();
				sysEditor.putBoolean("recieve_infos", true);
				sysEditor.commit();
			}
			if (isChecked = false && sp.getBoolean("receive_car_in", false)
					&& sp.getBoolean("receive_security_alarm", false)) {
				Editor sysEditor = sp.edit();
				sysEditor.putBoolean("recieve_infos", false);
				sysEditor.commit();
			}

			break;

		default:
			break;
		}
	}

	public void init() {
		this.receiveCarInCheckbox.setChecked(sp.getBoolean("receive_car_in",
				true));
		this.receiveCarOutCheckbox.setChecked(sp.getBoolean("receive_car_out",
				true));
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