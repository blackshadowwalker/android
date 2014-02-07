package com.cxf.safe_android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxf.adapter.SecurityAlarmAdapter;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.Constant;
import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;
import com.cxf.handler.ConnectionHandler;
import com.cxf.handler.SecurityGuardHandler;

@SuppressLint("HandlerLeak")
public class FragmentPage_guard extends Fragment implements
		View.OnClickListener, OnScrollListener {
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
	TextView user;
	// 统计图
	Button charts;
	// 声音提示
	TextView if_receive;
	@SuppressLint("UseSparseArrays")
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	private SoundPool soundPool;
	SecurityGuardHandler securityGuardHandler;
	HistorySecurityRecordDao historySecurityRecordDao;
	boolean receiveRemote = true;
	private int pageSize = 6;
	private int pageNumber = 1;
	private int level = -1;
	Timer timer;
	ConnectionHandler connectionHandler;
	SharedPreferences sp;
	// 处理器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if ((app.securityAlarmsChanges || app.securityAlarmItemChanges || app.securityIfInit)
						&& historySecurityRecordDao != null) {
					List<SecurityAlarm> tempList = historySecurityRecordDao
							.query(pageNumber, pageSize, level);
					app.securityAlarms = tempList;
					if (tempList != null && receiveRemote) {
						list.removeAll(list);
						list.addAll(tempList);
						Collections.sort(list);
						securityAlarmAdapter.notifyDataSetChanged();
						if (app.securityAlarmItemChanges) {
							app.securityAlarmItemChanges = false;
						}
						if (app.securityIfInit) {
							app.securityIfInit = false;
						}
						if (app.securityAlarmsChanges) {
							app.securityAlarmsChanges = false;

							if (sp.getBoolean("sys_sound_prompt", true)
									&& app.startSoundSecurity) {
								// 播放声音
								soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
								app.startSoundSecurity = false;
							}

							if (sp.getBoolean("sys_vibrate_prompt", true)) {
								// 震动
								Vibrator vibrator = (Vibrator) FragmentPage_guard.this
										.getActivity().getSystemService(
												Context.VIBRATOR_SERVICE);
								// if(vibrator.hasVibrator())
								// {
								vibrator.vibrate(1000);
								vibrator.cancel();
								// }
							}
						}

						System.out.println("ss");
					}
				}
			} else if (msg.what == 2) {
				Bundle data = new Bundle();
				data = msg.getData();
				long id = data.getLong("id");
				List<SecurityAlarm> tempList = historySecurityRecordDao.query(
						pageNumber, pageSize, level, id);
				list.addAll(tempList);
				securityAlarmAdapter.notifyDataSetChanged();
				receiveRemote = true;
			}
		};
	};

	// 定时器

	// 开启定时器
	public void startTimer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (handler != null || list.size() < 5) {
					if (list.size() < 5) {
						app.securityIfInit = true;
					}
					handler.sendEmptyMessage(1);
				}
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
		this.charts = (Button) view.findViewById(R.id.button_left);
		this.user = (TextView) view.findViewById(R.id.user);
		this.charts.setOnClickListener(this);
		this.charts.setVisibility(View.VISIBLE);
		this.charts.setText("统计图");
		// 设置事件监听
		this.content.setOnItemClickListener(new OnItemClickListenerImpl());
		this.content.setOnScrollListener(this);
		this.setting.setVisibility(View.VISIBLE);
		this.setting.setText("设置");
		this.setting.setOnClickListener(this);
		this.if_receive = (TextView) view.findViewById(R.id.if_receive);

		if (timer == null) {
			startTimer();
		}

		if (list != null) {
			this.securityAlarmAdapter = new SecurityAlarmAdapter(
					FragmentPage_guard.this.getActivity(), list);
			this.content.setAdapter(securityAlarmAdapter);
		}

		return view;
	}

	@Override
	public void onResume() {
		this.user.setText(sp.getString("nameuser", "未登录"));
		if (!sp.getBoolean("receive_security_alarm", true)) {
			this.if_receive.setText("不接收数据");
		} else {
			this.if_receive.setText(sp.getString("start_receive", ""));
		}
		if (!connectionHandler.isConnectingToInternet()) {
			Toast.makeText(this.getActivity(), "网络已经断开！", Toast.LENGTH_SHORT)
					.show();
		}
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.securityGuardHandler = new SecurityGuardHandler(this.getActivity());
		app = (MyAplication) getActivity().getApplication();
		list = app.securityAlarms;
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundMap.put(1, soundPool.load(this.getActivity(), R.raw.prompt, 1));
		historySecurityRecordDao = HistorySecurityRecordDao.Instance(this
				.getActivity());
		this.connectionHandler = new ConnectionHandler(this.getActivity());
		sp = FragmentPage_guard.this.getActivity().getSharedPreferences(
				"sys_setting", 0);
		startTimer();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.securityGuardHandler = null;
		historySecurityRecordDao = null;
		timer = null;
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
		case R.id.button_left:
			String host = sp.getString("host", "");
			Constant.init(host);
			Uri uri = Uri.parse(Constant.SECURITY_GUARD_CHART_URL);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		System.out.println(view.getLastVisiblePosition());
		int position = view.getLastVisiblePosition();
		SecurityAlarm c = (SecurityAlarm) view.getAdapter().getItem(position);
		final long id = c.id;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			receiveRemote = false;
			new Thread() {
				public void run() {
					receiveRemote = false;
					Message m = new Message();
					Bundle data = new Bundle();
					data.putLong("id", id);
					m.setData(data);
					m.what = 2;
					handler.sendMessage(m);

				};
			}.start();
		}

	}
}
