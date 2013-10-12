package com.cxf.safe_android;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);

		this.receiveInfosCheckBox = (CheckBox) view.findViewById(R.id.opt1);
		this.saveLoginInfosCheckBox = (CheckBox) view.findViewById(R.id.opt2);
		this.soundPromptCheckBox = (CheckBox) view.findViewById(R.id.sys_sound_prompt);
		this.vibratePromptCheckBox = (CheckBox) view.findViewById(R.id.sys_vibrate_prompt);

		this.receiveInfosCheckBox.setOnCheckedChangeListener(this);
		this.saveLoginInfosCheckBox.setOnCheckedChangeListener(this);
		this.soundPromptCheckBox.setOnCheckedChangeListener(this);
		this.vibratePromptCheckBox.setOnCheckedChangeListener(this);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("sys_setting", 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		init();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor edit = sp.edit();
		switch (buttonView.getId()) {
		case R.id.opt1:

			if (sp != null) {

				edit.putBoolean("keep_info", isChecked);
				edit.commit();
			}
			break;
		case R.id.opt2:
			edit.putBoolean("recieve_infos", isChecked);
			edit.commit();
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
		receiveInfosCheckBox.setChecked(sp.getBoolean("recieve_infos", false));
		saveLoginInfosCheckBox.setChecked(sp.getBoolean("keep_info", false));
		this.soundPromptCheckBox.setChecked(sp.getBoolean("sys_sound_prompt", false));
		this.vibratePromptCheckBox.setChecked(sp.getBoolean("sys_vibrate_prompt", false));
	}

}