package com.cxf.safe_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.SecurityGuardHandler;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

public class LoginActivity extends Activity implements View.OnClickListener,
		OnCheckedChangeListener {
	// 用户名
	EditText name;
	// 密码
	EditText password;
	// 登录按钮
	Button login;
	// 保存用户信息复选框
	CheckBox cb;
	// 用户请求处理类
	UserHandler userHandler;
	// 是否保存用户信息
	boolean keep_info = false;
//	反馈信息
	TextView msg;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		this.userHandler = new UserHandler(this);
		carGuardHandler=new CarGuardHandler(this);
		securityGuardHandler=new SecurityGuardHandler(this);
		this.name = (EditText) findViewById(R.id.login_name);
		this.password = (EditText) findViewById(R.id.password);
		this.login = (Button) findViewById(R.id.login);
		this.cb = (CheckBox) findViewById(R.id.keep_info);
		this.msg=(TextView)findViewById(R.id.msg);

		this.login.setOnClickListener(this);
		this.cb.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.userHandler = null;
		this.carGuardHandler=null;
		this.securityGuardHandler=null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			String nameStr = name.getText().toString();
			String passStr = password.getText().toString();
			if (!("".equals(nameStr) || "".equals(passStr))) {
				msg.setText("正在登录，请耐心等待……");
				String ssid = userHandler.login(nameStr, passStr);
				
				if (ssid != null) {
					msg.setText("登录成功！");
					SharedPreferences sp = (LoginActivity.this)
							.getSharedPreferences("sys_setting", 0);
					Editor edit = sp.edit();
					edit.putString("name", nameStr);
					edit.putString("password", passStr);
					edit.putBoolean("keep_info", keep_info);
					edit.putString("ssid", ssid);
					edit.commit();

					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(intent);
					
					
					Intent intent_service = new Intent(LoginActivity.this,
							RefreshDataService.class);
					intent_service.setAction("REFRESH_DATA");
					LoginActivity.this.startService(intent_service);
					
					LoginActivity.this.finish();
				}
				else
				{
					msg.setText("登录失败，请重新登录！");
				}
				
			}
			break;
		case R.id.keep_info:

			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		keep_info = isChecked;
	}
}
