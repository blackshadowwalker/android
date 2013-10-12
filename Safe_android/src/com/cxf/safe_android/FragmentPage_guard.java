package com.cxf.safe_android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.cxf.adapter.SecurityAlarmAdapter;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;
import com.cxf.handler.SecurityGuardHandler;

@SuppressLint("HandlerLeak")
public class FragmentPage_guard extends Fragment implements
		View.OnClickListener {
	MyAplication app;
	// 数据源
	List<SecurityAlarm> list = new ArrayList<SecurityAlarm>();
	// 适配器
	SecurityAlarmAdapter securityAlarmAdapter;
	// ListView
	ListView content;
	// 内容的标题
	TextView contentTitle;
	// 用户的ssid
	String ssid;
	// 安防告警处理类
	// 设置按钮
	Button setting;
	// 声音提示
	private SoundPool soundPool;
	SecurityGuardHandler securityGuardHandler;
	HistorySecurityRecordDao historySecurityRecordDao;

	// 处理器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (app.securityAlarmsChanges) {
					app.securityAlarms = historySecurityRecordDao
							.query(1, 5, -1);
					if (app.securityAlarms != null
							&& app.securityAlarms.size() > 0) {
						list.removeAll(list);
						list.addAll(app.securityAlarms);
						Collections.sort(list);
						securityAlarmAdapter.notifyDataSetChanged();

						SharedPreferences sp = FragmentPage_guard.this
								.getActivity().getSharedPreferences(
										"sys_setting", 0);
						if (sp.getBoolean("sys_sound_prompt", false)) {
							// 播放声音
							soundPool.load(
									FragmentPage_guard.this.getActivity(),
									R.raw.prompt, 1);
							soundPool.play(1, 1, 1, 0, 0, 1);
						}

						if (sp.getBoolean("sys_vibrate_prompt", false)) {
							// 震动
							Vibrator vibrator = (Vibrator) FragmentPage_guard.this
									.getActivity().getSystemService(
											Context.VIBRATOR_SERVICE);
							// if (vibrator.hasVibrator()) {
							vibrator.vibrate(1000);
							vibrator.cancel();
							// }
						}
						System.out.println("ss");
					}
				}
			}
		};
	};

	// 定时器

	// 开启定时器
	public void startTimer() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		};

		timer.schedule(task, 0, 2000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_security, null);
		this.contentTitle = (TextView) view.findViewById(R.id.content_title);
		this.contentTitle.setText("保卫防控告警信息");
		this.content = (ListView) view.findViewById(R.id.content);
		this.setting = (Button) view.findViewById(R.id.button_right);

		// 设置事件监听
		this.content.setOnItemClickListener(new OnItemClickListenerImpl());
		this.setting.setVisibility(View.VISIBLE);
		this.setting.setText("设置");
		this.setting.setOnClickListener(this);

		if (list != null) {
			this.securityAlarmAdapter = new SecurityAlarmAdapter(
					FragmentPage_guard.this.getActivity(), list);
			this.content.setAdapter(securityAlarmAdapter);
		}

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.securityGuardHandler = new SecurityGuardHandler(this.getActivity());
		app = (MyAplication) getActivity().getApplication();
		list = app.securityAlarms;
		this.soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		historySecurityRecordDao = new HistorySecurityRecordDao(
				this.getActivity());
		startTimer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.securityGuardHandler = null;
		historySecurityRecordDao = null;
	}

	public class OnItemClickListenerImpl implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			app.securityAlarmItem = list.get(position);
			Intent intent = new Intent();
			intent.setClass(getActivity(), Security_detail_activity.class);
			getActivity().startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_right:
			Intent intent = new Intent();
			intent.setClass(getActivity(),
					Security_alarm_setting_activity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
