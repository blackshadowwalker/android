package com.cxf.safe_android;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cxf.adapter.GridAdapter;
import com.cxf.handler.UserHandler;
import com.cxf.service.RefreshDataService;

public class FragmentPage_more extends Fragment implements OnItemClickListener,
		Callback {
	GridView gridview;
	GridAdapter adapter;
	TextView user;
	SharedPreferences sp;
	UserHandler userHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, null);
		this.adapter = new GridAdapter(getActivity());
		this.gridview = (GridView) view.findViewById(R.id.more_grid);
		this.user = (TextView) view.findViewById(R.id.user);

		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		this.user.setText(sp.getString("nameuser", "未登录"));
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		sp = FragmentPage_more.this.getActivity().getSharedPreferences(
				"sys_setting", 0);
		userHandler = new UserHandler(this.getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		userHandler = null;
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
		// TODO Auto-generated method stub
		switch (idx) {
		case 0:
			getActivity().startActivity(
					new Intent(getActivity(), LoginActivity.class));// 启动另一个Activity
			break;
		case 1:
			getActivity().startActivity(
					new Intent(getActivity(), User_detail_activity.class));// 启动另一个Activity
			break;
		case 2:
			if (isServiceRunning(FragmentPage_more.this.getActivity(),
					RefreshDataService.class.getName())) {
				Intent intent = new Intent(
						FragmentPage_more.this.getActivity(),
						RefreshDataService.class);
				FragmentPage_more.this.getActivity().stopService(intent);
			}

			Editor edit = sp.edit();
			edit.putString("state", "未登录");
			edit.putString("start_receive", "不接收数据");
			edit.commit();
			String ssid = sp.getString("ssid", null);
			new Thread(new LogoffThread(ssid, FragmentPage_more.this,
					userHandler)) {
			}.start();

			break;

		default:

			break;
		}
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

	class LogoffThread implements Runnable {
		String ssid;
		Callback handCallback;
		UserHandler userHandler;

		public LogoffThread(String ssid, Callback handCallback,
				UserHandler userHandler) {
			this.ssid = ssid;
			this.handCallback = handCallback;
			this.userHandler = userHandler;

		}

		@Override
		public void run() {
			String result = userHandler.logoff(ssid);
			Message msg = new Message();
			msg.what = 1;
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			handCallback.handleMessage(msg);
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 1) {
			Bundle data = msg.getData();
			String result = data.getString("result");
			System.exit(0);
		}
		return false;
	}

}