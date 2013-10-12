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

import com.cxf.adapter.CarAlarmAdapter;
import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.MyAplication;
import com.cxf.handler.CarGuardHandler;

@SuppressLint("HandlerLeak")
public class FragmentPage_car_alarm extends Fragment implements
		View.OnClickListener, OnItemClickListener {

	MyAplication app;
	// 数据源
	List<CarAlarm> list = new ArrayList<CarAlarm>();
	// 适配器
	CarAlarmAdapter carAdapter;
	// ListView
	ListView content;
	// 内容的标题
	TextView contentTitle;
	// 用户的ssid
	String ssid;
	// 车辆告警处理类
	CarGuardHandler carGuardHandler;
	// 设置按钮
	Button setting;
	// 声音提示
	private SoundPool soundPool;
	HistoryCarRecordDao historyCarAlarmDao;
	// 处理器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (app.carAlarmsChanged) {
					app.carAlarms = historyCarAlarmDao.query(0, 5, 1);
					if (app.carAlarms != null && app.carAlarms.size() > 0) {
						list.removeAll(list);
						list.addAll(app.carAlarms);
						Collections.sort(list);
						carAdapter.notifyDataSetChanged();

						SharedPreferences sp=FragmentPage_car_alarm.this.getActivity().getSharedPreferences("sys_setting", 0);
						if(sp.getBoolean("sys_sound_prompt", false))
						{
						// 播放声音
						soundPool.load(
								FragmentPage_car_alarm.this.getActivity(),
								R.raw.prompt, 1);
						soundPool.play(1, 1, 1, 0, 0, 1);
						}
						
						if(sp.getBoolean("sys_vibrate_prompt", false))
						{
						// 震动
							Vibrator vibrator = (Vibrator)FragmentPage_car_alarm.this.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//							if(vibrator.hasVibrator())
//							{
								vibrator.vibrate(1000); 
								vibrator.cancel(); 
//							}
						}
						System.out.println("dd");
						
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

		View view = inflater.inflate(R.layout.fragment_car_alarm, null);
		this.contentTitle = (TextView) view.findViewById(R.id.content_title);
		this.content = (ListView) view.findViewById(R.id.content);
		this.setting = (Button) view.findViewById(R.id.button_right);

		this.content.setOnItemClickListener(this);
		this.setting.setVisibility(View.VISIBLE);
		this.setting.setText("设置");
		this.setting.setOnClickListener(this);
		if (list != null) {
			this.carAdapter = new CarAlarmAdapter(
					FragmentPage_car_alarm.this.getActivity(), list);
			this.content.setAdapter(carAdapter);
		}
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (MyAplication) getActivity().getApplication();
		list = app.carAlarms;
		carGuardHandler = new CarGuardHandler(this.getActivity());
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		historyCarAlarmDao=new HistoryCarRecordDao(this.getActivity());
		
		startTimer();

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.carGuardHandler = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_right:
			Intent intent = new Intent();
			intent.setClass(getActivity(), Car_alarm_setting_activity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		app.carAlarmItem = list.get(position);
		Intent intent = new Intent();
		intent.setClass(getActivity(), Car_Guard_detail_activity.class);
		getActivity().startActivity(intent);
	}
}