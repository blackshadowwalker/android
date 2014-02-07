package com.cxf.safe_android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.cxf.adapter.CarAlarmAdapter;
import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.Constant;
import com.cxf.entity.MyAplication;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.ConnectionHandler;

@SuppressLint("HandlerLeak")
public class FragmentPage_car_alarm extends Fragment implements
		View.OnClickListener, OnItemClickListener, OnScrollListener {
	final String TAG = FragmentPage_car_alarm.class.getName();
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
	Button charts;
	// 定时器
	Timer timer;
	SharedPreferences sp;
	TextView user;
	TextView if_receive;
	// 声音提示
	int dir = -1;
	boolean receiveRemote = true;
	private SoundPool soundPool;
	private int pageSize = 6;
	private int pageNumber = 1;
	@SuppressLint("UseSparseArrays")
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	HistoryCarRecordDao historyCarAlarmDao;
	List<CarAlarm> listTemp;
	boolean isLogin = false;
	boolean isConnectNet = false;
	ConnectionHandler connectionHandler;
	// 处理器
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (app.carAlarmsChanged || app.carAlarmItemChanged
						|| app.carAlarmsIfInit) {

					listTemp = historyCarAlarmDao.query(1, pageSize, dir);
					app.carAlarms = listTemp;
					if (listTemp != null && receiveRemote) {
						app.carAlarms = listTemp;
						list.removeAll(list);
						list.addAll(listTemp);
						Collections.sort(list);
						carAdapter.notifyDataSetChanged();
						Log.i(TAG, "更新listview");
						if (FragmentPage_car_alarm.this.getActivity() != null) {

							if (app.carAlarmItemChanged) {
								app.carAlarmItemChanged = false;
							}
							if (app.carAlarmsIfInit) {
								app.carAlarmsIfInit = false;
							}
							if (app.carAlarmsChanged) {
								app.carAlarmsChanged = false;

								if (sp.getBoolean("sys_sound_prompt", true)
										&& app.startSoundCar) {
									Log.i(TAG, "声音提示");
									// 播放声音
									soundPool.play(soundMap.get(1), 1, 1, 0, 0,
											1);
									app.startSoundCar = false;
								}

								if (sp.getBoolean("sys_vibrate_prompt", true)) {
									Log.i(TAG, "震动提示");
									// 震动
									Vibrator vibrator = (Vibrator) FragmentPage_car_alarm.this
											.getActivity().getSystemService(
													Context.VIBRATOR_SERVICE);
									// if(vibrator.hasVibrator())
									// {
									vibrator.vibrate(1000);
									vibrator.cancel();
									// }
								}
							}
							System.out.println("dd");

						}

					}

				}
			} else if (msg.what == 2) {
				Bundle data = new Bundle();
				data = msg.getData();
				long id = data.getLong("id");
				Log.i(TAG, "从数据库查询历史数据");
				List<CarAlarm> tempList = historyCarAlarmDao.query(pageNumber,
						pageSize, dir, id);
				list.addAll(tempList);
				carAdapter.notifyDataSetChanged();
				receiveRemote = true;
			}
		};
	};

	// 定时器

	// 开启定时器
	public void startTimer() {
		Log.i(TAG, "启动定时器");
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (handler != null || list.size() < 5) {
					if (list.size() < 5) {
						app.carAlarmsIfInit = true;
					}
					handler.sendEmptyMessage(1);
				}

			}
		};

		timer.schedule(task, 0, 2000);
	}

	public void stopTimer() {
		Log.i(TAG, "关闭定时器");
		timer = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "创建视图");
		View view = inflater.inflate(R.layout.fragment_car_alarm, null);
		this.contentTitle = (TextView) view.findViewById(R.id.content_title);
		this.content = (ListView) view.findViewById(R.id.content);
		this.setting = (Button) view.findViewById(R.id.button_right);
		this.charts = (Button) view.findViewById(R.id.button_left);
		this.user = (TextView) view.findViewById(R.id.user);
		this.charts.setOnClickListener(this);
		this.content.setOnItemClickListener(this);
		this.content.setOnScrollListener(this);
		this.charts.setVisibility(View.VISIBLE);
		this.setting.setVisibility(View.VISIBLE);
		this.setting.setOnClickListener(this);
		this.charts.setText("统计图");
		this.contentTitle.setText("车辆告警信息");
		this.connectionHandler = new ConnectionHandler(this.getActivity());
		this.setting.setText("设置");
		this.if_receive = (TextView) view.findViewById(R.id.if_receive);
		if (list != null) {
			this.carAdapter = new CarAlarmAdapter(
					FragmentPage_car_alarm.this.getActivity(), list);
			this.content.setAdapter(carAdapter);
		}

		return view;
	}

	@Override
	public void onResume() {
		
		this.user.setText(sp.getString("nameuser", "未登录"));
		isLogin = sp.getBoolean("is_login", false);
		boolean car_in = sp.getBoolean(
				"receive_car_in", true);
		boolean car_out = sp.getBoolean(
				"receive_car_out", true);
		if(!car_in&&!car_out)
		{
			this.if_receive.setText("不接收数据");
		}
		else
		{
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
		Log.i(TAG, "创建对象");
		app = (MyAplication) getActivity().getApplication();
		list = app.carAlarms;
		carGuardHandler = new CarGuardHandler(this.getActivity());
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundMap.put(1, soundPool.load(this.getActivity(), R.raw.prompt, 1));
		historyCarAlarmDao = HistoryCarRecordDao.Instance(this.getActivity());
		connectionHandler = new ConnectionHandler(this.getActivity());
		startTimer();
		sp = FragmentPage_car_alarm.this.getActivity().getSharedPreferences(
				"sys_setting", 0);

		super.onCreate(savedInstanceState);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (timer == null) {
			startTimer();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.carGuardHandler = null;
		this.handler = null;
		Log.i(TAG, "停止定时器");
		stopTimer();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_right:
			Intent intent = new Intent();
			intent.setClass(getActivity(), Car_alarm_setting_activity.class);
			getActivity().startActivity(intent);
			Log.i(TAG, "启动设置界面");
			break;
		case R.id.button_left:
			String host = sp.getString("host", "");
			Constant.init(host);
			Uri uri = Uri.parse(Constant.CAR_GUARD_CHART_URL);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			Log.i(TAG, "查看统计图");
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Log.i(TAG, "选择单个listview条目");
		app.carAlarmItem = list.get(position);
		Intent intent = new Intent();
		intent.setClass(getActivity(), Car_Guard_detail_activity.class);
		getActivity().startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		System.out.println(view.getLastVisiblePosition());
		int position = view.getLastVisiblePosition();
		CarAlarm c = (CarAlarm) view.getAdapter().getItem(position);
		final long id = c.id;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			Log.i(TAG, "加载历史数据");
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