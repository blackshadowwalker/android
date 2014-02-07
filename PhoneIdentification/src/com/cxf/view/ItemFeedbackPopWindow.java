package com.cxf.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.cxf.PhoneIdentification.R;
import com.cxf.net.handler.SendEmailHandler;
import com.cxf.net.handler.SendEmailThread;

public class ItemFeedbackPopWindow extends PopupWindow implements
		OnClickListener, Callback {
	private View mMenuView;
	TextView feedback;
	Context context;
	// 反馈内容
	EditText content;
	// 反馈地址
	EditText address;
	// 提交按钮
	TextView submit;
	// 取消按钮
	TextView cancel;
	FragmentManager fm;
	// 进度条
	MyProgressDialog myProgressDialog;
	private final int UPDATE_PROGRESS_UI = 600;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_PROGRESS_UI:
				String str = context.getString(R.string.emailing);
				myProgressDialog.setPromptMessage(str);
				break;

			default:
				break;
			}
		};
	};

	public ItemFeedbackPopWindow() {
	}

	public ItemFeedbackPopWindow(Context context) {
	}

	public ItemFeedbackPopWindow(Context context, FragmentManager fm) {
		super(context);
		this.context = context;
		this.fm = fm;
		myProgressDialog = MyProgressDialog.newInstance(fm, this,"");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popwindow_feedback, null);

		// 尺寸伸缩动画效果 scale
		Animation animation_scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.5f);
		animation_scale.setDuration(500);// 设置时间持续时间为 5000毫秒
		AnimationSet animationSet = new AnimationSet(true);

		animationSet.addAnimation(animation_scale);// 尺寸伸缩
		mMenuView.setAnimation(animationSet);

		feedback = (TextView) mMenuView.findViewById(R.id.feedback);
		content = (EditText) mMenuView.findViewById(R.id.feedback_text);
		address = (EditText) mMenuView.findViewById(R.id.feedback_address);
		submit = (TextView) mMenuView.findViewById(R.id.submit);
		cancel = (TextView) mMenuView.findViewById(R.id.cancel);

		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
				R.color.transparent));
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.parent).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if (content != null && submit != null && address != null) {
				String text = content.getText().toString();
				if (!"".equals(text)) {

					String url = address.getText().toString();
					if (url == null) {
						url = "";
					}
					new Thread(new SendEmailThread(context, this, text, url))
							.start();

					Message m = new Message();
					m.what = UPDATE_PROGRESS_UI;
					handleMessage(m);
				}
			}
			break;
		case R.id.cancel:
			dismiss();
			break;
		}

	}

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case UPDATE_PROGRESS_UI:
			myProgressDialog.show(fm,"feedback_progress");
			handler.sendMessage(msg);
			break;
		case SendEmailHandler.SENDEMAIL_SUCCESS:
			myProgressDialog.dismiss();
			this.dismiss();
			String strSuccess = context.getString(R.string.send_successfully);
			Toast.makeText(context, strSuccess, Toast.LENGTH_SHORT).show();
			break;
		case SendEmailHandler.SENDEMAIL_FAILURE:
			myProgressDialog.dismiss();
			String strFailure = context.getString(R.string.send_failure);
			Toast.makeText(context, strFailure, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return false;
	}
}
