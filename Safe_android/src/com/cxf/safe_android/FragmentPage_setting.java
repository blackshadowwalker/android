package com.cxf.safe_android;

import com.cxf.handler.ConnectionHandler;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentPage_setting extends Fragment implements
		OnCheckedChangeListener {

	// 接收服务器端消息推送
	CheckBox receiveInfosCheckBox;
	// 保存登录信息
	CheckBox saveLoginInfosCheckBox;
	// 保存登录信息
	CheckBox soundPromptCheckBox;
	// 保存登录信息
	CheckBox vibratePromptCheckBox;
	// 系统配置文件
	SharedPreferences sp;
	TextView user;
	ConnectionHandler connHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);

		this.receiveInfosCheckBox = (CheckBox) view.findViewById(R.id.opt2);
		this.saveLoginInfosCheckBox = (CheckBox) view
				.findViewById(R.id.keep_info);
		this.user = (TextView) view.findViewById(R.id.user);
		this.soundPromptCheckBox = (CheckBox) view
				.findViewById(R.id.sys_sound_prompt);
		this.vibratePromptCheckBox = (CheckBox) view
				.findViewById(R.id.sys_vibrate_prompt);

		this.receiveInfosCheckBox.setOnCheckedChangeListener(this);
		this.saveLoginInfosCheckBox.setOnCheckedChangeListener(this);
		this.soundPromptCheckBox.setOnCheckedChangeListener(this);
		this.vibratePromptCheckBox.setOnCheckedChangeListener(this);
		this.user.setText(sp.getString("nameuser", "未登录"));
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		sp = getActivity().getSharedPreferences("sys_setting", 0);
		connHandler = new ConnectionHandler(this.getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		init();
		super.onResume();

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor edit = sp.edit();
		switch (buttonView.getId()) {
		case R.id.keep_info:

			if (sp != null) {

				edit.putBoolean("keep_info", isChecked);
				edit.commit();

			}
			break;
		case R.id.opt2:
			if (isChecked && sp.getString("name", null) != null
					&& connHandler.isConnectingToInternet()&&sp.getBoolean("connect_net", false)) {
				edit.putString("start_receive", "正在接收数据");
			} else {
				edit.putString("start_receive", "不接收数据");
			}

			edit.putBoolean("recieve_infos", isChecked);
			edit.commit();

			// 更改子配置
			if (sp != null) {
				if (isChecked == false) {

					Editor editor = sp.edit();
					editor.putBoolean("receive_car_in", isChecked);
					editor.putBoolean("receive_car_out", isChecked);
					editor.putBoolean("receive_security_alarm", isChecked);
					editor.commit();
				} else if (!sp.getBoolean("receive_car_in", false)
						&& !sp.getBoolean("receive_car_out", false)
						&& !sp.getBoolean("receive_security_alarm", false)) {
					Editor editor = sp.edit();
					editor.putBoolean("receive_car_in", true);
					editor.putBoolean("receive_car_out", true);
					editor.putBoolean("receive_security_alarm", true);
					editor.commit();
				}
			}
			break;
		case R.id.sys_sound_prompt:
			edit.putBoolean("sys_sound_prompt", isChecked);
			edit.commit();
			break;
		case R.id.sys_vibrate_prompt:
			edit.putBoolean("sys_vibrate_prompt", isChecked);
			edit.commit();
			break;

		default:
			break;
		}
	}

	public void init() {
		receiveInfosCheckBox.setChecked(sp.getBoolean("recieve_infos", true));
		saveLoginInfosCheckBox.setChecked(sp.getBoolean("keep_info", false));
		this.soundPromptCheckBox.setChecked(sp.getBoolean("sys_sound_prompt",
				true));
		this.vibratePromptCheckBox.setChecked(sp.getBoolean(
				"sys_vibrate_prompt", false));
	}

}