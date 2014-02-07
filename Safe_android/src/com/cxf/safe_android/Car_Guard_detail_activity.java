package com.cxf.safe_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cxf.dao.HistoryCarRecordDao;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.MyAplication;
import com.cxf.handler.ImageLoadHandler;

public class Car_Guard_detail_activity extends Activity implements
		View.OnClickListener {
	MyAplication app;
	// 初始化时间
	TextView time;
	// 初始化地点
	TextView place;
	// 车牌号
	TextView carNumber;
	// 方向
	TextView direction;
	// 图片
	ImageView img;
	CarAlarm ca;
	// 返回按钮
	Button back;
	HistoryCarRecordDao historyCarRecordDao;
	Bitmap bigImage;
	ProgressBar progress_bar;
	// WebView wv;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Bundle data = msg.getData();
				final String url = data.getString("url");
				// wv.loadUrl(url);
				new Thread() {
					public void run() {
						try {
							bigImage = new ImageLoadHandler(
									Car_Guard_detail_activity.this)
									.getImage(url);
							Message updateMsg = new Message();
							updateMsg.what = 2;
							handler.sendMessage(updateMsg);

						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();

			} else if (msg.what == 2) {
				Car_Guard_detail_activity.this.img.setImageBitmap(bigImage);
				Car_Guard_detail_activity.this.progress_bar
						.setVisibility(View.GONE);
			}
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activiy_car_guard_detail);

		this.app = (MyAplication) getApplication();
		this.time = (TextView) findViewById(R.id.absTime);
		this.place = (TextView) findViewById(R.id.location);
		this.carNumber = (TextView) findViewById(R.id.number);
		this.direction = (TextView) findViewById(R.id.direction);
		this.img = (ImageView) findViewById(R.id.shortImage);
		this.img.setOnClickListener(this);
		// this.wv=(WebView)findViewById(R.id.wv);
		// this.wv.getSettings().setJavaScriptEnabled(true);
		// this.wv.getSettings().setLoadsImagesAutomatically(true);
		// this.wv.getSettings().setBuiltInZoomControls(true);
		// this.wv.getSettings().setUseWideViewPort(true);
		// this.wv.getSettings().setLoadWithOverviewMode(true);
		// this.wv.getSettings().setBlockNetworkImage(true);

		this.progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
		progress_bar.setVisibility(View.VISIBLE);

		this.back = (Button) findViewById(R.id.button_left);
		historyCarRecordDao = HistoryCarRecordDao.Instance(this);
		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);

		this.ca = app.carAlarmItem;
		if (this.ca != null) {

			final String bigImageUrl = ca.bigImageUrl;
			if (ca.bigImageUrl != null) {
				Message msg = new Message();
				msg.what = 1;
				Bundle data = new Bundle();
				data.putString("url", bigImageUrl);
				msg.setData(data);
				handler.sendMessage(msg);

			}
			this.time.setText(ca.absTime);
			this.place.setText(ca.location);
			this.carNumber.setText(ca.LPNumber);
			if (ca.dir == 0) {
				this.direction.setText("出");
			} else if (ca.dir == 1) {
				this.direction.setText("进");
			}
			ca.isNew = false;
			historyCarRecordDao.update(ca);
			for (CarAlarm c : app.carAlarms) {
				if (ca.id == c.id) {
					c.isNew = false;
				}
			}
			app.carAlarmItemChanged = true;

		}
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		historyCarRecordDao = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.shortImage:
			Uri uri = Uri.parse(ca.bigImageUrl);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;

		default:
			break;
		}
	};
}