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

public class Security_alarm_setting_activity extends Activity implements
		OnCheckedChangeListener, View.OnClickListener {
	SharedPreferences sp;
	// 返回按钮
	Button back;
	// 设置是否接收驶出数据
	CheckBox receiveSecurityCheckbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_setting);

		this.receiveSecurityCheckbox = (CheckBox) findViewById(R.id.receive_security_alarm);
		this.receiveSecurityCheckbox.setOnCheckedChangeListener(this);

		this.back = (Button) findViewById(R.id.button_left);
		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);
		sp = getSharedPreferences("security_alarm_setting", 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {
		case R.id.receive_security_alarm:

			if (sp != null) {
				Editor edit = sp.edit();
				edit.putBoolean("receive_security_alarm", isChecked);
				edit.commit();
			}
			break;

		default:
			break;
		}
	}

	public void init() {
		this.receiveSecurityCheckbox.setChecked(sp.getBoolean(
				"receive_security_alarm", false));
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