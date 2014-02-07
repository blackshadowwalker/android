package com.cxf.safe_android;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cxf.handler.UserHandler;

@SuppressLint("ShowToast")
public class User_detail_activity extends Activity implements
		View.OnClickListener, Callback {

	SharedPreferences sp;
	// 返回按钮
	Button back;
	TextView name;
	TextView state;
	Button changePassButton;
	TextView receive_state;
	UserHandler userHandle;
	TextView msgText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("sys_setting", 0);
		setContentView(R.layout.user_detail);
		this.back = (Button) findViewById(R.id.button_left);
		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);
		name = (TextView) findViewById(R.id.name);
		state = (TextView) findViewById(R.id.state);
		msgText = (TextView) findViewById(R.id.msg);

		changePassButton = (Button) findViewById(R.id.change_password);
		changePassButton.setOnClickListener(this);
		receive_state = (TextView) findViewById(R.id.receive_state);
		userHandle = new UserHandler(this);
	}

	@Override
	protected void onResume() {
		name.setText(sp.getString("name", "没有用户"));
		state.setText(sp.getString("state", "未登录"));
		receive_state.setText(sp.getString("start_receive", "不接收数据"));
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.change_password:
			Builder build = new AlertDialog.Builder(User_detail_activity.this)
					.setTitle("修改密码");
			View view = LayoutInflater.from(User_detail_activity.this).inflate(
					R.layout.modify_password, null);
			final TextView msg = (TextView) view.findViewById(R.id.msg);
			final EditText oldEdit = (EditText) view.findViewById(R.id.old);
			final EditText newEdit = (EditText) view
					.findViewById(R.id.newpassword);
			final EditText comfirmEdit = (EditText) view
					.findViewById(R.id.comfirmpassword);
			final String password = sp.getString("password", "admin");
			oldEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View arg0, boolean hasFocus) {
					if (!hasFocus) {
						String oldPass = oldEdit.getText().toString();
						if (!oldPass.equals(password)) {
							msg.setText("您的旧密码输入错误！");
						}
						else
						{
							msg.setText("");
						}
					}

				}
			});

			AlertDialog dialog = build
					.setPositiveButton("修改", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String oldPass = oldEdit.getText().toString();
							String newPass = newEdit.getText().toString();
							String comfirmPass = comfirmEdit.getText()
									.toString();

							String ssid = sp.getString("ssid", "");
							if (!oldPass.equals(password)
									|| !comfirmPass.equals(newPass)) {

								try {
									Field field = arg0.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									// 设置mShowing值，欺骗android系统
									field.set(arg0, false);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (!oldPass.equals(password)) {
									msg.setText("您的旧密码输入错误！");
								} else {
									msg.setText("两次输入的密码不一致");
								}

							} else if (!("".equals(oldPass)
									|| "".equals(newPass) || ""
										.equals(comfirmPass))
									&& newPass.equals(comfirmPass)) {
								try {
									Field field = arg0.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									// 设置mShowing值，欺骗android系统
									field.set(arg0, true);
								} catch (Exception e) {
									e.printStackTrace();
								}
								msg.setText("正在修改密码");
								msgText.setText("正在修改密码");
								new Thread(new ModifyPassThread(
										User_detail_activity.this, ssid,
										oldPass, newPass)).start();
							}

						}
					}).setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								// 设置mShowing值，欺骗android系统
								field.set(dialog, true);
							} catch (Exception e) {
								e.printStackTrace();
							}
							dialog.cancel();

						}
					}).setView(view).create();
			dialog.show();
			break;
		default:
			break;
		}
	};

	class ModifyPassThread implements Runnable {
		String ssid;
		String oldPass;
		String newPass;
		Callback handler;

		public ModifyPassThread(Callback handler, String ssid, String oldPass,
				String newPass) {
			this.handler = handler;
			this.ssid = ssid;
			this.oldPass = oldPass;
			this.newPass = newPass;
		}

		@Override
		public void run() {
			String result = userHandle.modifyPassword(ssid, oldPass, newPass);
			Message msg = new Message();
			msg.what = 1;
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			handler.handleMessage(msg);
		}
	}

	@Override
	protected void onDestroy() {
		userHandle = null;
		super.onDestroy();
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 1) {
			Bundle data = msg.getData();
			String result = data.getString("result");
			User_detail_activity.this.msgText.setText(result);
		}
		return false;
	}
}