package com.cxf.safe_android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cxf.adapter.OptionsAdapter;
import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.dao.HistorySettingDao;
import com.cxf.entity.Constant;
import com.cxf.entity.MyAplication;
import com.cxf.entity.RequestSetting;
import com.cxf.handler.CarGuardHandler;
import com.cxf.handler.ConnectionHandler;
import com.cxf.handler.SecurityGuardHandler;
import com.cxf.handler.SystemXMLHandler;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements View.OnClickListener,
		OnCheckedChangeListener, Callback {
	MyAplication app;
	SharedPreferences sp;
	// 用户名
	EditText name;
	// 密码
	EditText password;
	// 登录按钮
	Button login;
	// 离线登录按钮
	Button login_offline;
	// 保存用户信息复选框
	CheckBox cb;
	// 用户请求处理类
	UserHandler userHandler;
	// 是否保存用户信息
	boolean keep_info = false;
	boolean connectionNet = true;
	SystemXMLHandler xml;
	List<RequestSetting> rslist = new ArrayList<RequestSetting>();
	// 反馈信息
	TextView msgText;
	// 服务器的配置信息
	List<RequestSetting> lp = new ArrayList<RequestSetting>();
	String ssid;
	String nameStr;
	String passStr;
	String host;
	CarGuardHandler carGuardHandler;
	SecurityGuardHandler securityGuardHandler;
	// 数据库处理类
	HistoryCarRecordDao historyCarAlarmDao;
	HistorySecurityRecordDao historySecurityRecordDao;
	ConnectionHandler connHandler;
	HistorySettingDao historySettingDao;

	int pageSize = 5;

	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private OptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<String> datas = new ArrayList<String>();;
	// 下拉框依附组件
	private LinearLayout parent;
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// 文本框
	private EditText service;
	// 下拉箭头图片组件
	private Button selectService;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 用来处理选中或者删除下拉项消息
	private Handler handler;
	// 是否初始化完成标志
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		this.app = (MyAplication) getApplication();
		this.sp = getSharedPreferences("sys_setting", 0);
		this.userHandler = new UserHandler(this);
		this.handler = new Handler(LoginActivity.this);
		this.carGuardHandler = new CarGuardHandler(this);
		this.securityGuardHandler = new SecurityGuardHandler(this);
		this.historySettingDao = HistorySettingDao.Instance(this);
		this.name = (EditText) findViewById(R.id.login_name);
		this.password = (EditText) findViewById(R.id.password);
		this.parent = (LinearLayout) findViewById(R.id.parent);
		this.service = (EditText) findViewById(R.id.edittext);
		this.selectService = (Button) findViewById(R.id.btn_select);
		this.login = (Button) findViewById(R.id.login);
		this.login_offline = (Button) findViewById(R.id.enter_direct);
		this.cb = (CheckBox) findViewById(R.id.keep_info);
		this.msgText = (TextView) findViewById(R.id.msg);
		this.connHandler = new ConnectionHandler(this);
		this.login.setOnClickListener(this);
		this.login_offline.setOnClickListener(this);
		this.cb.setOnCheckedChangeListener(this);
		this.historyCarAlarmDao = HistoryCarRecordDao.Instance(this);
		this.historySecurityRecordDao = HistorySecurityRecordDao.Instance(this);
		if (!connHandler.isConnectingToInternet()) {
			Toast.makeText(this, "网络已经断开！", Toast.LENGTH_SHORT).show();
			if (!sp.getString("name", "未登录").equals("未登录")) {
				this.login.setText("离线登录");
				Editor edit = sp.edit();
				edit.putBoolean("connect_net", false);
				edit.putString("state", "离线登录");
				edit.commit();
			}
			connectionNet = false;
			this.login.setText("离线登录");

		} else {
			Editor edit = sp.edit();
			edit.putBoolean("connect_net", true);
			edit.commit();
		}
		new Thread() {
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			};
		}.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.userHandler = null;
		this.carGuardHandler = null;
		this.securityGuardHandler = null;
		this.historyCarAlarmDao = null;
		this.historySecurityRecordDao = null;
		handler = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:

			if (connectionNet) {
				service.setEnabled(true);
				selectService.setEnabled(true);
				final String nameStr = name.getText().toString();
				final String passStr = password.getText().toString();
				Editor edit = sp.edit();
				edit.putString("host", service.getText().toString());
				edit.commit();

				new Thread() {
					@Override
					public void run() {
						super.run();

						Message msg = new Message();
						msg.what = 1;
						Bundle data = new Bundle();
						data.putString("nameStr", nameStr);
						data.putString("passStr", passStr);
						msg.setData(data);
						handler.sendMessage(msg);
					}
				}.start();
			} else {

				final String nameStr = name.getText().toString();
				final String passStr = password.getText().toString();
				SharedPreferences sp = (LoginActivity.this)
						.getSharedPreferences("sys_setting", 0);
				Editor edit = sp.edit();
				edit.putString("name", nameStr);
				edit.putString("password", passStr);
				edit.putBoolean("keep_info", keep_info);
				edit.putString("ssid", null);
				edit.putString("state", "离线登录");
				edit.putString("start_receive", "不接收数据");
				edit.putBoolean("connect_net", false);
				edit.commit();

				app.securityAlarms = historySecurityRecordDao.query(1,
						pageSize, -1);
				app.securityAlarmsChanges = true;
				app.carAlarms = historyCarAlarmDao.query(1,
						pageSize, -1);
				app.securityAlarmsChanges = true;

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}

			break;
		case R.id.btn_select:
			if (flag) {
				// 显示PopupWindow窗口
				popupWindwShowing();
			}
			break;
		case R.id.enter_direct:

			app.securityAlarms = historySecurityRecordDao
					.query(1, pageSize, -1);
			app.securityAlarmsChanges = true;
			app.carAlarms = historyCarAlarmDao
					.query(1, pageSize, -1);
			app.carAlarmsChanged = true;

			if (sp.getString("name", null) != null
					&& !sp.getString("name", "未登录").equals("未登录")
					&& !connectionNet) {
				sp.edit().putString("state", "离线登录").commit();

			} else if (connectionNet && sp.getString("name", null) != null) {
				new Thread() {
					public void run() {
						String nameStr = sp.getString("name", null);
						String passStr = sp.getString("password", null);
						String ssid = userHandler.login(nameStr, passStr);
						sp.edit().putString("state", "在线登录").commit();
						if (ssid != null) {
							sp.edit().putString("ssid", ssid).commit();

							Intent intent_service = new Intent(
									LoginActivity.this,
									RefreshDataService.class);
							intent_service.setAction("REFRESH_DATA");
							startService(intent_service);

						}
					};
				}.start();

			}
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();

			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		keep_info = isChecked;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!flag) {
			initWedget();
			flag = true;
		}
	}

	/**
	 * 初始化界面控件
	 */
	private void initWedget() {
		// 初始化Handler,用来处理消息

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		selectService.setOnClickListener(this);

		// 初始化PopupWindow
		initPopuWindow();
	}

	/**
	 * 初始化填充Adapter所用List数据
	 */
	private void initDatas() {

		this.rslist = this.historySettingDao.query();
		for (int i = 0; i < rslist.size(); i++) {
			datas.add(rslist.get(i).host);
		}

		if (datas != null && datas.size() == 0) {
			datas.add(Constant.DEFAULT_HOST);
			datas.add(Constant.HOST);
			RequestSetting rs = new RequestSetting();
			rs.host = Constant.DEFAULT_HOST;
			this.historySettingDao.insert(rs);
			RequestSetting rs2 = new RequestSetting();
			rs2.host = Constant.HOST;
			this.historySettingDao.insert(rs2);
		}
	}

	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {

		initDatas();

		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);

		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		Drawable bg = getResources().getDrawable(R.drawable.content_bg);
		selectPopupWindow.setBackgroundDrawable(bg);
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		selectPopupWindow.showAsDropDown(parent, 0, -3);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	/**
	 * 处理Hander消息
	 */
	@Override
	public boolean handleMessage(Message message) {
		Bundle data = message.getData();

		switch (message.what) {
		case 0:
			init();
			break;
		case 1:
			nameStr = data.getString("nameStr");
			passStr = data.getString("passStr");
			if (!("".equals(nameStr) || "".equals(passStr))) {
				msgText.setText("正在登录，请耐心等待……");
				SharedPreferences sp = (LoginActivity.this)
						.getSharedPreferences("sys_setting", 0);
				Editor edit = sp.edit();
				edit.putString("name", nameStr);
				edit.commit();
				new Thread() {
					public void run() {
						ssid = userHandler.login(nameStr, passStr);
						if (ssid != null) {
							Message msg2 = new Message();
							msg2.what = 2;
							handler.sendMessage(msg2);
						} else {
							Message msg4 = new Message();
							msg4.what = 4;
							handler.sendMessage(msg4);
						}
					};
				}.start();

			}

			break;
		case 2:

			if (ssid != null) {
				msgText.setText("登录成功！");
				System.out.println(ssid);
				new Thread() {
					@Override
					public void run() {
						super.run();
						SharedPreferences sp = (LoginActivity.this)
								.getSharedPreferences("sys_setting", 0);
						Editor edit = sp.edit();
						edit.putString("name", nameStr);
						edit.putString("password", passStr);
						edit.putBoolean("keep_info", keep_info);
						edit.putString("ssid", ssid);
						edit.putString("host", service.getText().toString());
						edit.putBoolean("connect_net", true);
						edit.putString("state", "在线登录");
						edit.commit();

						RequestSetting rs = new RequestSetting();
						rs.host = service.getText().toString();
						historySettingDao.insert(rs);
						if (!isServiceRunning(LoginActivity.this,
								RefreshDataService.class.getName())) {
							Intent intent_service = new Intent(
									LoginActivity.this,
									RefreshDataService.class);
							intent_service.setAction("REFRESH_DATA");
							LoginActivity.this.startService(intent_service);
						}
						Message m = new Message();
						m.what = 3;
						handler.sendMessage(m);

					}
				}.start();

			} else {
				msgText.setText("登录失败，请重新登录！");
			}

			break;
		case 3:
			new Thread() {
				@Override
				public void run() {
					super.run();
					app.securityAlarms = historySecurityRecordDao.query(1,
							pageSize, -1);
					app.securityAlarmsChanges = true;
					app.carAlarms=historyCarAlarmDao.query(1,
							pageSize, -1);
					app.carAlarmsChanged=true;
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				}
			}.start();

			break;
		case 4:
			msgText.setText("登录失败，请重新登录！");
			break;
		case 5:
			// 选中下拉项，下拉框消失
			int selIndex = data.getInt("selIndex");
			service.setText(datas.get(selIndex));

			dismiss();
			break;
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	public boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public void init() {

		if (sp.getString("name", null) != null) {
			this.name.setText(sp.getString("name", ""));
		}
		if (sp.getString("password", null) != null) {
			password.setText(sp.getString("password", null));
		}

		if (sp.getString("host", null) != null) {
			service.setText(new String(sp.getString("host", null)));
		} else {
			service.setText(Constant.DEFAULT_HOST);
		}
		if (!connHandler.isConnectingToInternet()) {
			service.setEnabled(false);
			selectService.setEnabled(false);
		}

	}

}
