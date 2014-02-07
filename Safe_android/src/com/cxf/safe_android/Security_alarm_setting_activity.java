package com.cxf.safe_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;

public class Security_alarm_setting_activity extends Activity implements
		OnCheckedChangeListener, View.OnClickListener {
	SharedPreferences sp;
	// 返回按钮
	Button back;
	// 设置是否接收驶出数据
	CheckBox receiveSecurityCheckbox;
	ImageButton imgButoon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_setting);

		this.receiveSecurityCheckbox = (CheckBox) findViewById(R.id.receive_security_alarm);
		this.receiveSecurityCheckbox.setOnCheckedChangeListener(this);
		this.imgButoon = (ImageButton) findViewById(R.id.opt1_level);
		this.back = (Button) findViewById(R.id.button_left);

		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);
		this.imgButoon.setOnClickListener(this);
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
		case R.id.receive_security_alarm:

			if (sp != null) {
				Editor edit = sp.edit();
				edit.putBoolean("receive_security_alarm", isChecked);
				edit.commit();

				if (isChecked == true && !sp.getBoolean("recieve_infos", false)) {
					Editor sysEditor = sp.edit();
					sysEditor.putBoolean("recieve_infos", true);
					sysEditor.commit();
				}
				if (isChecked = false && sp.getBoolean("receive_car_in", false)
						&& sp.getBoolean("receive_car_out", false)) {
					Editor sysEditor = sp.edit();
					sysEditor.putBoolean("recieve_infos", false);
					sysEditor.commit();
				}
			}
			break;

		default:
			break;
		}
	}

	public void init() {
		this.receiveSecurityCheckbox.setChecked(sp.getBoolean(
				"receive_security_alarm", true));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.opt1_level:
			Builder build = new AlertDialog.Builder(
					Security_alarm_setting_activity.this).setTitle("选择警告级别");
			AlertDialog dialog = build
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
			dialog.show();
			break;

		default:
			break;
		}
	};

}