package com.cxf.safe_android;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.Constant;
import com.cxf.entity.MyAplication;
import com.cxf.handler.ConnectionHandler;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

public class MainActivity extends FragmentActivity {
	MyAplication app;
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	// 定义一个布局
	private LayoutInflater layoutInflater;
	// 定义数组来存放Fragment界面
	// 顶部工具条右边按钮
	public Button buttonLeft;
	SharedPreferences sp;
	Timer timer;
	UserHandler userHandler;
	// 当前界面标志
	ConnectionHandler connectionHandler;
	boolean startAutoLogin = true;
	String TAG = MainActivity.class.getName();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				new Thread() {
					public void run() {
						autoLogin();
					};
				}.start();

			}
		};
	};
	@SuppressWarnings("rawtypes")
	private Class fragmentArray[] = { FragmentPage_car_alarm.class,
			FragmentPage_guard.class, FragmentPage_setting.class,
			FragmentPage_more.class };

	// 定义数组来存放按钮图片
	private int mImageViewArray[] = { R.drawable.tab_carguard_btn,
			R.drawable.tab_guard_btn, R.drawable.tab_setting_btn,
			R.drawable.tab_square_btn, };

	// Tab选项卡的文字
	private String mTextviewArray[] = { "车辆告警", "保卫安防告警", "系统设置", "更多" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_tab_layout);
		connectionHandler = new ConnectionHandler(this);
		sp = getSharedPreferences("sys_setting", 0);
		app = (MyAplication) getApplication();
		userHandler = new UserHandler(this);
		initView();
		startTimer();

	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart()");
		if (timer == null) {
			Log.i(TAG, "onRestart()->startTimer()");
			Log.i(TAG, "timer=null");
			startTimer();
		}
		super.onRestart();
	}

	@Override
	protected void onStart() {
		if (timer == null) {
			Log.i(TAG, "onStart()->startTimer()");
			Log.i(TAG, "timer=null");
			startTimer();
		}
		super.onStart();
	}

	@Override
	public void onResume() {

		if (!connectionHandler.isConnectingToInternet()) {
			sp.edit().putString("start_receive", "不接收数据").commit();

		}

		int pageSize = 6;
		if (app.carAlarms.size() == 0) {
			app.securityAlarms = HistorySecurityRecordDao.Instance(this).query(
					1, pageSize, -1);
			app.securityAlarmItemChanges = true;
		}
		if (app.carAlarms.size() == 0) {
			app.carAlarms = HistoryCarRecordDao.Instance(this).query(1,
					pageSize, 0);
			app.carAlarmsChanged = true;
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			stopTimer();
		}
		Log.i(TAG, " onDestroy()->stopTimer()");
		handler = null;
		super.onDestroy();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化布局对象
		layoutInflater = LayoutInflater.from(this);

		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// 得到fragment的个数
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);

		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);

		return view;
	}

	public void startTimer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Log.i(TAG, "startTimer()");

				boolean isconnect = connectionHandler.isConnectingToInternet();
				if (isconnect) {
					Editor edit = sp.edit();
					edit.putBoolean("isConnectNet", true);

					boolean checkName = sp.getString("name", null) != null;
					boolean checkState = "离线登录".equals(sp.getString("state",
							"离线登录"));
					boolean receive = sp.getBoolean("recieve_infos", true);
					Log.i(TAG, "checkName->" + checkName + ",checkState->"
							+ checkState + ",isconnect->" + isconnect
							+ ",receive->" + receive);
					if (checkName && checkState && isconnect && receive
							&& startAutoLogin) {
						Log.i(TAG, "startAutoLogin");

						startAutoLogin = false;
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						edit.putString("start_receive", "正在接收数据");
						edit.putBoolean("connect_net", true);

					}
					edit.commit();
				} else {
					Editor edit = sp.edit();
					edit.putBoolean("isConnectNet", false);
					if (sp.getString("name", null) != null) {
						edit.putString("state", "离线登录");
					}
					edit.commit();
				}

			}
		};

		timer.schedule(task, 0, 500);
	}

	public void stopTimer() {
		this.timer = null;
	}

	private void autoLogin() {
		Log.i(TAG, "autoLogin()");

		String nameStr = sp.getString("name", null);
		String passStr = sp.getString("password", null);
		String ssid = null;
		String[] hosts = new String[] { Constant.HOST, Constant.DEFAULT_HOST };
		for (int i = 0; i < hosts.length; i++) {

			Editor edit = sp.edit();
			edit.putString("host", hosts[i]);
			edit.commit();
			ssid = userHandler.login(nameStr, passStr);
			if (ssid != null)
				break;
		}
		Log.i(TAG, "ssid:" + ssid);
		if (ssid != null) {
			Log.i(TAG, "autoLogin() ->true");
			Editor edit = sp.edit();
			edit.putString("state", "在线登录");
			edit.putString("ssid", ssid);
			edit.commit();
			startAutoLogin = true;
			Intent intent_service = new Intent(this, RefreshDataService.class);
			intent_service.setAction("REFRESH_DATA");
			startService(intent_service);

			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
		}

	}

}
