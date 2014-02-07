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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cxf.dao.HistorySecurityRecordDao;
import com.cxf.entity.MyAplication;
import com.cxf.entity.SecurityAlarm;
import com.cxf.handler.ImageLoadHandler;

public class Security_detail_activity extends Activity implements
		View.OnClickListener {
	MyAplication app;
	// 拍摄的图片
	ImageView img;
	// 时间
	TextView absTime;
	// 地址
	TextView location;
	// 告警级别
	TextView level;
	// 事件名称
	TextView eventName;
	// 管理人员
	TextView personInCharge;
	// 事件描述
	TextView desp;
	SecurityAlarm sa;
	
	  ProgressBar	progress_bar;
	// 返回按钮
	Button back;
//	大图片
	Bitmap bigImage;
//	数据库类
	HistorySecurityRecordDao historySecurityRecordDao;
  
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Bundle data = msg.getData();
				final String url = data.getString("url");
				System.out.println(url);
				new Thread() {
					public void run() {
						try {
							bigImage =new ImageLoadHandler(Security_detail_activity.this).getImage(url);
							if (bigImage != null) {
								Message updateMsg = new Message();
								updateMsg.what = 2;
								handler.sendMessage(updateMsg);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();

			} else if (msg.what == 2) {
				Security_detail_activity.this.img.setImageBitmap(bigImage);
				Security_detail_activity.this.progress_bar.setVisibility(View.GONE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activiy_security_detail);
		historySecurityRecordDao = HistorySecurityRecordDao.Instance(this);
		this.app = (MyAplication) getApplication();
		this.absTime = (TextView) findViewById(R.id.absTime);
		this.desp = (TextView) findViewById(R.id.desp);
		this.eventName = (TextView) findViewById(R.id.eventName);
		this.level = (TextView) findViewById(R.id.level);
		this.location = (TextView) findViewById(R.id.location);
		this.personInCharge = (TextView) findViewById(R.id.personInCharge);
		this.back = (Button) findViewById(R.id.button_left);
		this.img = (ImageView) findViewById(R.id.shortImage);
		this.img.setOnClickListener(this);
		this.back.setVisibility(View.VISIBLE);
		this.back.setText("返回");
		this.back.setOnClickListener(this);
		this.progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
		progress_bar.setVisibility(View.VISIBLE);

		this.sa = app.securityAlarmItem;
		if (this.sa != null) {
			if (sa.bigImageUrl != null) {
				Message msg = new Message();
				msg.what = 1;
				Bundle data = new Bundle();
				data.putString("url", sa.bigImageUrl);
				msg.setData(data);
				handler.sendMessage(msg);
			}
			this.absTime.setText(sa.absTime);
			this.desp.setText(sa.desp);
			this.eventName.setText(sa.eventName);
			this.level.setText(String.valueOf(sa.level));
			this.location.setText(sa.location);
			this.personInCharge.setText(sa.personInCharge);
			this.sa.isNew = false;

			historySecurityRecordDao.update(sa);
			app.securityAlarmItemChanges = true;
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		historySecurityRecordDao = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.shortImage:
			 Uri uri = Uri.parse(sa.bigImageUrl);
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 startActivity(intent);
			break;
		default:
			break;
		}
	};
	
	  public Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight,int bigger) {

	        int originWidth  = bitmap.getWidth();
	        int originHeight = bitmap.getHeight();

	        if (originWidth < maxWidth && originHeight < maxHeight) {
	            return bitmap;
	        }

	        int width  = originWidth;
	        int height = originHeight;

	        // 若图片过宽, 则保持长宽比缩放图片
	        if (originWidth > maxWidth) {
	            width = maxWidth;

	            double i = originWidth * 1.0 / maxWidth;
	            height = (int) Math.floor(originHeight / i);

	            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	        }

	        // 若图片过长, 则从上端截取
	        if (height > maxHeight) {
	            height = maxHeight;
	            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
	        }

//	        Log.i(TAG, width + " width");
//	        Log.i(TAG, height + " height");

	        return bitmap;
	    }
}