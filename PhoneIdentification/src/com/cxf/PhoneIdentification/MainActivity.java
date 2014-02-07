package com.cxf.PhoneIdentification;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cxf.adapter.MyFragmentPagerAdapter;
import com.cxf.entity.MyApplication;
import com.cxf.entity.Verify;
import com.cxf.net.handler.LocationHandler;
import com.cxf.net.handler.LogoffThread;
import com.cxf.net.handler.VerifyThread;
import com.cxf.net.handler.VideoRecorderManager;
import com.cxf.receiver.MyReceiver;
import com.cxf.service.MyService;
import com.cxf.view.CameraPromptDialog;
import com.cxf.view.MoreDialog;
import com.cxf.view.MyProgressDialog;
import com.cxf.view.PromptDialog;
import com.cxf.view.ResultPromptDialog;
import com.cxf.view.TipDialog;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnClickListener, OnTouchListener, Callback,
		OnPreDrawListener, BDLocationListener {
	MyApplication app;
	boolean hasMeasured = false;
	final String TAG = "MainActivity";
	LocationClient locationClient;
	LocationHandler locHandler;
	boolean afterOnSave = false;
	// 标题的宽带
	float titleWide = 0f;
	// 广播接收器
	private MyReceiver receiver;
	boolean isExit = false;
	boolean isKeyEvent = false;
	int systemTime = 0;
	boolean isAlive = true;
	// 组建组
	private RadioGroup mRadioGroup;
	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	private RadioButton mRadioButton3;
	private RadioButton mRadioButton4;
	private Button buttom_right_button;
	MoreDialog selectDialog;
	VerifyThread verifyThread = null;
	final int TOTAL_VERIFY_COUNT = 5;
	// 提示对话框
	// PromptDialog promptDialog;
	CameraPromptDialog cameraPromptDialog;
	// 身份认证帮助提示对话框
	TipDialog tipDialog;
	// 操作的最长时长
	int TIME_TOTAL = 60;
	private TimeThread timeThread;
	// 经纬度
	Double lon = 0.0;
	Double lat = 0.0;
	private String ip;
	// 中间的按钮
	Button centerButton;
	boolean clicked = false;

	int veififyCount = 0;
	boolean isReshow = false;
	int recount = 0;
	// 图片组件
	private ImageView mImageView;
	// 当前被选中的RadioButton距离左侧的距离
	private float mCurrentCheckedRadioLeft;
	// 上面的水平滚动控件
	private HorizontalScrollView mHorizontalScrollView;
	// 下方的可横向拖动的控件
	private ViewPager mViewPager;
	// 用来存放下方滚动的不同视图
	private ArrayList<Fragment> fragmentsList;
	// 判断每个页面是否得到焦点
	private SparseBooleanArray fragmentFocus = new SparseBooleanArray();
	// 判断页面中业务是否处理完成
	private SparseBooleanArray fragmentHasBeenDone = new SparseBooleanArray();
	private int currentPageIndex = 0;
	// 判断当前是进行问题验证还是音视频验证，1为问题验证，2为音视频验证
	private int identityStep = 1;
	// 装载fragment的适配器
	MyFragmentPagerAdapter adapter;
	// 菜单按钮
	ImageView moreOptions;
	LocationHandler locationHandler;
	String provider;
	// 父窗口
	View parent;
	Verify verify;
	public final static int UPDATE_TIME = 10;
	public final static int TIME_OVER = -10;
	public final static int SHOW_TIPDIALOG = 11;
	public final static int DEMONSTRATE_SHOWUP = 112;
	public final static int DEMONSTRATE_SHOWDOWN = -112;
	public final static int RESET = 113;
	public final static int NEXT = 0;
	public final static int FAILURE = -1;
	public final static int LOGIN = 1;
	public final static int LOGIN_SUCCESS = 2;
	public final static int LOGIN_FAILURE = -2;
	public final static int BEGIN_VERIFY = 3;
	public final static int ANSWER_FAILURE = -3;
	public final static int VERIFY = 4;
	public final static int VERIFY_SUCCESS = 5;
	public final static int STOP_VERIFY = 6;
	public static final int VERIFY_FAILURE2 = -7;
	public static final int VERIFY_FAILURE = 7;
	public static final int LOGOFF_SUCCESS = 8;
	public static final int LOGOFF_FAILURE = -8;
	public static final int LOGOFF_OFFLINE = 9;
	public static final int EXIT_RESET = -9;
	public static final int NET_DISCONNECTION = -4;
	private boolean isRecording = false;
	private boolean isAnswerTrue = false;
	private boolean isPromptDialogShow = false;
	private boolean isDemonstrate = false;
	private boolean istiming = false;
	private boolean isTipDialogShow = false;
	private boolean isInit = false;
	// private boolean logoff = false;
	// 计时器
	boolean startTime = false;
	int timeCount = TIME_TOTAL;
	TextView timeTip;
	MyHandler handler;

	private void initAll() {
		centerButton.setClickable(true);
		isInit = true;
		if (currentPageIndex >= 2) {
			// updateFragments(2, false);
			verify = null;
			timeTip.setText("");
			buttom_right_button.setVisibility(View.VISIBLE);
		}

		dissmissProgress("progress");
		dissmissProgress("prompt");
		dissmissProgress("tip");
		dissmissProgress("register_success");
//		dissmissProgress("network");
//		dissmissProgress("upload_result");
//		dissmissProgress("verify_result");

		timeTip = (TextView) findViewById(R.id.tip);
		clicked = false;
		// 判断每个页面是否得到焦点
		fragmentFocus = new SparseBooleanArray();
		// 判断页面中业务是否处理完成
		fragmentHasBeenDone = new SparseBooleanArray();
		fragmentFocus.put(0, true);
		fragmentFocus.put(1, false);
		fragmentFocus.put(2, false);
		fragmentFocus.put(3, false);
		fragmentFocus.put(4, false);

		fragmentHasBeenDone.put(0, false);
		fragmentHasBeenDone.put(1, false);
		fragmentHasBeenDone.put(2, false);
		fragmentHasBeenDone.put(3, false);
		mRadioButton1.setVisibility(View.VISIBLE);
		mRadioButton2.setVisibility(View.INVISIBLE);
		mRadioButton3.setVisibility(View.INVISIBLE);
		mRadioButton4.setVisibility(View.INVISIBLE);

		currentPageIndex = 0;
		// 判断当前是进行问题验证还是音视频验证，1为问题验证，2为音视频验证

		identityStep = 1;
		isRecording = false;
		isAnswerTrue = false;
		isPromptDialogShow = false;
		isDemonstrate = false;
		istiming = false;
		isTipDialogShow = false;
		// 计时器
		startTime = false;
		buttom_right_button.setVisibility(View.VISIBLE);
		buttom_right_button.setClickable(true);
		timeCount = TIME_TOTAL;
		mViewPager.setCurrentItem(0);
		timeTip.setVisibility(View.INVISIBLE);
		if (timeThread != null) {
			timeThread.setFlag(false);
		}
		// 初始化
		Message msg = new Message();
		msg.what = LoginFragment.INIT;
		((LoginFragment) fragmentsList.get(0)).handleMessage(msg);

		Message msg2 = new Message();
		msg2.what = LoginFragment.INIT;
		((RecorderFragment) fragmentsList.get(3)).handleMessage(msg2);

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.main);
		handler = new MyHandler(this);
		if (this.getIntent() != null) {
			/* 获取Intent中的Bundle对象 */
			Bundle bundle = this.getIntent().getExtras();

			/* 获取Bundle中的数据，注意类型和key */
			if (bundle != null) {
				String result = bundle.getString("result");
				if (result != null && "register_success".equals(result)) {
					showResultDialog(result, "register_result");
				}
			}
		}
		app = (MyApplication) getApplication();
		iniController();
		iniListener();
		iniVariable();
		printPhoneInfo();

	}

	private void iniVariable() {
		if (timeThread != null) {
			timeThread.setFlag(false);
			timeThread.interrupt();
			try {
				timeThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			timeThread = null;
		}
		timeTip.setVisibility(View.INVISIBLE);
		receiver = new MyReceiver(handler);
		fragmentHasBeenDone = new SparseBooleanArray();
		currentPageIndex = 0;
		// 判断当前是进行问题验证还是音视频验证，1为问题验证，2为音视频验证
		identityStep = 1;
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		filter.addAction("android.intent.action.VERIFY_RECEIVER");
		filter.addAction("android.intent.action.SCREEN_OFF");
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		// 注册广播接收器
		getApplicationContext().registerReceiver(receiver, filter);
		fragmentsList = new ArrayList<Fragment>();
		fragmentFocus = new SparseBooleanArray();
		Fragment loginFragment = LoginFragment.newInstance(this);
		Fragment tradeDetailFragment = TradeDetailFragment.newInstance();
		Fragment answerQuestionFragment = AnswerQuestionFragment
				.newInstance(this);
		Fragment recorderFragment = RecorderFragment.newInstance(this);
		Fragment tradeResultFragment = TradeResultFragment.newInstance();

		fragmentsList.add(loginFragment);
		fragmentsList.add(tradeDetailFragment);
		fragmentsList.add(answerQuestionFragment);
		fragmentsList.add(recorderFragment);
		fragmentsList.add(tradeResultFragment);

		fragmentFocus.put(0, true);
		fragmentFocus.put(1, false);
		fragmentFocus.put(2, false);
		fragmentFocus.put(3, false);

		fragmentHasBeenDone.put(0, false);
		fragmentHasBeenDone.put(1, false);
		fragmentHasBeenDone.put(2, false);
		fragmentHasBeenDone.put(3, false);

		// float tLenght1 = getLength(getString(R.string.login)) * 3.5f;
		// float tLenght2 = getLength(getString(R.string.trade_detail)) * 3.5f;
		// float tLenght3 = getLength(getString(R.string.user_verify)) * 3.5f;
		// float tLenght4 = getLength(getString(R.string.trade_sucess)) * 3.5f;
		// titleWide = Math.max(tLenght1, tLenght2);
		// titleWide = Math.max(titleWide, tLenght3);
		// titleWide = Math.max(titleWide, tLenght4);
		// mRadioButton1.setWidth((int) titleWide);
		// mRadioButton2.setWidth((int) titleWide);
		// mRadioButton3.setWidth((int) titleWide);
		// mRadioButton4.setWidth((int) titleWide);

		ViewGroup.LayoutParams para2;
		para2 = mImageView.getLayoutParams();
		para2.width = (int) titleWide;
		para2.height = 4;
		mImageView.setLayoutParams(para2);

		mRadioButton1.setVisibility(View.VISIBLE);
		mRadioButton2.setVisibility(View.INVISIBLE);
		mRadioButton3.setVisibility(View.INVISIBLE);
		mRadioButton4.setVisibility(View.INVISIBLE);
		// 设置不可点击
		mRadioButton1.setClickable(false);
		mRadioButton2.setClickable(false);
		mRadioButton3.setClickable(false);
		mRadioButton4.setClickable(false);

		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
				fragmentsList);
		mViewPager.setAdapter(adapter);// 设置ViewPager的适配器

		locHandler = new LocationHandler(this);
		this.ip = locHandler.getHostIp();

		locationClient = new LocationClient(this);
		locationClient.setAK("6c8d83da633da1da6e75dc18660221a6");
		locationClient.registerLocationListener(this);
		setLocationOption(locationClient);
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.requestLocation();
		} else {
			locationClient.start();
		}

		mRadioButton1.setChecked(true);
		mViewPager.setCurrentItem(0);
		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
		identityStep = 1;
		isRecording = false;
		isAnswerTrue = false;
		isPromptDialogShow = false;
		verify = null;
		clicked = false;

	}

	/**
	 * RadioGroup点击CheckedChanged监听
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		AnimationSet _AnimationSet = new AnimationSet(true);
		TranslateAnimation _TranslateAnimation;

		Log.i("zj", "checkedid=" + checkedId);
		if (checkedId == R.id.btn1) {

			_TranslateAnimation = new TranslateAnimation(
					-(int) getRadioWide(1), 0, 0f, 0f);
			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);
			// 开始上面蓝色横条图片的动画切换
			mImageView.startAnimation(_AnimationSet);
			// mImageView.setLayoutParams(_LayoutParams1);
			mViewPager.setCurrentItem(0);// 让下方ViewPager跟随上面的HorizontalScrollView切换

		} else if (checkedId == R.id.btn2) {
			_TranslateAnimation = new TranslateAnimation(0, getRadioWide(1),
					0f, 0f);

			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);

			// mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			mViewPager.setCurrentItem(1);
			mRadioButton2.setVisibility(View.VISIBLE);
		} else if (checkedId == R.id.btn3) {
			_TranslateAnimation = new TranslateAnimation((int) getRadioWide(1),
					getRadioWide(2), 0f, 0f);

			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);

			// mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			if (identityStep == 1)
				mViewPager.setCurrentItem(2);
			mRadioButton3.setVisibility(View.VISIBLE);
			String str2 = MainActivity.this.getString(R.string.time);
			timeTip.setText(str2 + ":" + timeCount + "s");

		} else if (checkedId == R.id.btn4) {
			_TranslateAnimation = new TranslateAnimation((int) getRadioWide(2),
					getRadioWide(3), 0f, 0f);

			_AnimationSet.addAnimation(_TranslateAnimation);
			_AnimationSet.setFillBefore(false);
			_AnimationSet.setFillAfter(true);
			_AnimationSet.setDuration(100);

			// mImageView.bringToFront();
			mImageView.startAnimation(_AnimationSet);
			mViewPager.setCurrentItem(4);
			mRadioButton4.setVisibility(View.VISIBLE);
			timeTip.setVisibility(View.INVISIBLE);
		}

		mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();// 更新当前蓝色横条距离左边的距离

		mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft
				- (int) getRadioWide(1), 0);
	}

	/**
	 * 获得当前被选中的RadioButton距离左侧的距离
	 */
	private float getCurrentCheckedRadioLeft() {

		if (mRadioButton1.isChecked()) {
			return titleWide;
		} else if (mRadioButton2.isChecked()) {
			return titleWide * 2;
		} else if (mRadioButton3.isChecked()) {
			return titleWide * 3;
		} else if (mRadioButton4.isChecked()) {
			return titleWide * 4;
		}
		return 0f;
	}

	private float getRadioWide(int index) {

		if (index == 1) {
			return titleWide;
		} else if (index == 2) {
			return titleWide * 2;
		} else if (index == 3) {
			return titleWide * 3;
		} else if (index == 4) {
			return titleWide * 4;
		}
		return 0f;
	}

	private void iniListener() {

		mRadioGroup.setOnCheckedChangeListener(this);

		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
		mViewPager.setOnTouchListener(this);
		buttom_right_button.setOnClickListener(this);
		moreOptions.setOnClickListener(this);
		this.centerButton.setOnClickListener(this);

	}

	private void iniController() {
		parent = findViewById(R.id.parent);
		String tip = getString(R.string.tip);
		String tipContent = getString(R.string.tip_content);

		tipDialog = TipDialog.newInstance(tip, tipContent,
				getSupportFragmentManager(), this);
		timeTip = (TextView) findViewById(R.id.tip);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRadioButton1 = (RadioButton) findViewById(R.id.btn1);
		mRadioButton2 = (RadioButton) findViewById(R.id.btn2);
		mRadioButton3 = (RadioButton) findViewById(R.id.btn3);
		mRadioButton4 = (RadioButton) findViewById(R.id.btn4);
		buttom_right_button = (Button) findViewById(R.id.button_right);
		moreOptions = (ImageView) findViewById(R.id.more);
		this.centerButton = (Button) findViewById(R.id.button_center);
		String loginStr = getString(R.string.login);
		centerButton.setText(loginStr);
		centerButton.setClickable(true);
		mImageView = (ImageView) findViewById(R.id.stick_title);

		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);

		mViewPager = (ViewPager) findViewById(R.id.pager);

	}

	public void initForVerify() {
		identityStep = 1;
		clicked = false;
		String submitStr = getString(R.string.submit_questions);
		centerButton.setText(submitStr);
		if (timeThread != null) {
			timeThread.setFlag(false);
			try {
				timeThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		timeThread = null;
		// startTime = false;
		timeCount = TIME_TOTAL;
		String timeStr = getString(R.string.time);
		timeTip.setText(timeStr + ":" + timeCount + "s");
	}

	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int checkedId) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			clicked = false;
			int posi = position;
			if (posi < 3) {
				if (!fragmentFocus.get(position)
						&& (currentPageIndex + 1) == position) {
					fragmentFocus.put(position, true);
				}
			} else if (posi == 4) {
				fragmentFocus.put(3, true);
			}

			switch (position) {
			case 0:
				mRadioButton1.performClick();
				String loginStr = MainActivity.this.getString(R.string.login);
				centerButton.setText(loginStr);
				break;
			case 1:
				mRadioButton2.performClick();
				String comfirmStr = MainActivity.this
						.getString(R.string.confirm);
				centerButton.setText(comfirmStr);
				if (fragmentFocus.get(1)) {
					mRadioButton2.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mRadioButton3.performClick();
				if (identityStep == 1) {

					String tipContent = getString(R.string.tip_content);
					showTipDialog(tipContent, "tip");
					isTipDialogShow = true;
				}

				if (fragmentFocus.get(2)) {
					mRadioButton3.setVisibility(View.VISIBLE);
				}

				break;
			case 3:

				break;
			case 4:
				mRadioButton4.performClick();
				String closeStr = MainActivity.this.getString(R.string.close);
				centerButton.setText(closeStr);
				buttom_right_button.setVisibility(View.INVISIBLE);
				buttom_right_button.setClickable(false);
				if (fragmentFocus.get(3)) {
					mRadioButton4.setVisibility(View.VISIBLE);
				}
				break;

			}
			currentPageIndex = position;

		}

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.button_center:
			if (clicked) {
				clicked = false;
			} else {
				clicked = true;
			}
			switch (currentPageIndex) {

			case 0:
				// 登录

				Message mLogin = new Message();
				mLogin.what = LoginFragment.LOGIN;
				handleMessage(mLogin);
				centerButton.setClickable(false);
				break;
			case 1:

				fragmentHasBeenDone.put(1, true);
				mViewPager.setCurrentItem(currentPageIndex + 1);
				Message m = new Message();
				m.what = AnswerQuestionFragment.INIT_QUESTION;
				Bundle initData = new Bundle();
				initData.putSerializable("verify", verify);
				m.setData(initData);
				((AnswerQuestionFragment) fragmentsList.get(2))
						.handleMessage(m);
				break;
			case 2:
				if (identityStep == 1 && clicked) {
					Message verifyMsg = new Message();
					verifyMsg.what = AnswerQuestionFragment.VERIFY;
					((AnswerQuestionFragment) fragmentsList.get(2))
							.handleMessage(verifyMsg);
					clicked = false;
					centerButton.setClickable(false);

				}
				break;
			case 3:
				if (identityStep == 2 && clicked && isAnswerTrue
						&& !isPromptDialogShow) {
					Message msg = new Message();
					msg.what = RecorderFragment.START_RECORDER;

					((RecorderFragment) fragmentsList.get(3))
							.handleMessage(msg);
					String completeString = MainActivity.this
							.getString(R.string.complete);
					centerButton.setText(completeString);
					isRecording = true;

				} else if (identityStep == 2 && !clicked && isRecording
						&& isAnswerTrue && !isPromptDialogShow) {
					String readString = MainActivity.this
							.getString(R.string.read);
					centerButton.setText(readString);
					centerButton.setClickable(false);
					isPromptDialogShow = true;
					isRecording = false;

					Message msg = new Message();
					msg.what = RecorderFragment.STOP_RECORED;
					((RecorderFragment) fragmentsList.get(3))
							.handleMessage(msg);

				}

				break;
			case 4:
				Message msg = new Message();
				msg.what = 0;
				((TradeResultFragment) fragmentsList.get(4)).handleMessage(msg);
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			}
			break;
		case R.id.more:
			if (selectDialog == null) {
				selectDialog = new MoreDialog(this, this, R.style.dialog,
						getSupportFragmentManager());

				Window win = selectDialog.getWindow();
				LayoutParams params = new LayoutParams();

				Rect rect = new Rect();
				MainActivity.this.getWindow().getDecorView()
						.getWindowVisibleDisplayFrame(rect);
				View view2 = MainActivity.this.getWindow().findViewById(
						Window.ID_ANDROID_CONTENT);
				int topS = rect.top;// 状态栏高度
				int topT = rect.height() - view2.getHeight();

				int[] position = new int[2];
				this.moreOptions.getLocationInWindow(position);
				int height = moreOptions.getHeight();
				int newX = position[0] - this.parent.getWidth() + 15;
				int newY = position[1] - this.parent.getHeight() / 2 - height
						- topS - topT - 5;

				params.x = newX;// 设置x坐标
				params.y = newY;// 设置y坐标
				win.setAttributes(params);
			}
			selectDialog.show();

			break;
		case R.id.button_right:
			isAlive = false;
			Message msg = new Message();
			msg.what = LOGOFF_OFFLINE;
			handler.sendMessage(msg);

			break;

		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		Fragment f = getSupportFragmentManager().findFragmentByTag("progress");
		if (f != null) {
			DialogFragment df = (DialogFragment) f;
			df.dismiss();
			getSupportFragmentManager().beginTransaction().remove(f).commit();
		}
		if (istiming && timeThread == null && !isTipDialogShow) {
			timeThread = new TimeThread();
			timeThread.setFlag(true);
			timeThread.start();
		}
		if (istiming && timeThread != null) {
			timeThread.setFlag(true);
		}
		if (isDemonstrate) {
			Intent intent = new Intent(MainActivity.this, MyService.class);
			intent.putExtra("what", "reshow_tip");
			startService(intent);
		}
		if (identityStep == 2 && clicked && isRecording) {
			Message msg = new Message();
			msg.what = RecorderFragment.RESTART_RECORDER;

			((RecorderFragment) fragmentsList.get(3)).handleMessage(msg);
			String completeStr = MainActivity.this.getString(R.string.complete);
			centerButton.setText(completeStr);
			isRecording = true;
		}
		try {
			locationClient.start();
		} catch (Exception e) {

		}

	}

	@Override
	protected void onPause() {
		if (istiming && timeThread != null) {
			timeThread.setFlag(false);
			try {
				timeThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeThread = null;
		}
		// if (isprogressShow) {
		// progressDialog.dismiss();
		// isprogressShow = false;
		// }
		if (isDemonstrate) {
			Intent intent = new Intent(MainActivity.this, MyService.class);
			intent.putExtra("what", "hide_tip");
			startService(intent);
		}
		if (identityStep == 2 && !clicked && isRecording) {
			Message msg = new Message();
			msg.what = RecorderFragment.STOP_RECORED;
			((RecorderFragment) fragmentsList.get(3)).handleMessage(msg);
			String readStr = MainActivity.this.getString(R.string.read);
			centerButton.setText(readStr);
			clicked = true;
		}
		locationClient.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		getApplicationContext().unregisterReceiver(receiver);
		SharedPreferences sp = getSharedPreferences("sys", 0);
		Editor editor = sp.edit();
		editor.putInt("state", 1);
		editor.commit();
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Boolean slipNext = fragmentHasBeenDone.get(currentPageIndex);

		if (currentPageIndex < 3 && slipNext) {
			return false;
		}

		return true;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {
		case MainActivity.NEXT:
			// 更新到第二步验证
			// updateFragments(2, true);
			replaceFragment(2, 3);
			break;
		case LoginFragment.LOGIN:

			Message msg1 = new Message();
			msg1.what = LoginFragment.LOGIN;
			Bundle logdata = new Bundle();
			DecimalFormat a = new DecimalFormat("##0.0#####");
			String latStr = a.format(lat);
			String lonStr = a.format(lon);
			logdata.putString("lat", latStr);
			logdata.putString("lon", lonStr);
			logdata.putString("ip", this.ip);
			msg1.setData(logdata);
			((LoginFragment) fragmentsList.get(0)).handleMessage(msg1);

			msg.what = LOGIN;
			break;
		case LoginFragment.SUCCESS:
			this.verify = (Verify) data.getSerializable("verify");

			Message nextMsg = new Message();
			nextMsg.what = LOGIN_SUCCESS;
			handler.sendMessage(nextMsg);
			isInit = false;
			break;
		case LoginFragment.FAILURE:
			Message logMsg = msg;
			logMsg.what = LOGIN_FAILURE;
			handler.sendMessage(logMsg);
			break;
		case AnswerQuestionFragment.SUCCESS:
			isAnswerTrue = true;
			// updateFragments(2, true);
			replaceFragment(2, 3);
			Message m3 = new Message();
			m3.what = BEGIN_VERIFY;
			handler.sendMessage(m3);

			break;
		case AnswerQuestionFragment.FAILURE:
			Message answerFailure = new Message();
			answerFailure.what = ANSWER_FAILURE;
			handler.sendMessage(answerFailure);
			break;
		case RecorderFragment.HAS_FACE:
			handler.sendMessage(msg);
			break;
		case RecorderFragment.HAS_NO_FACE:
			handler.sendMessage(msg);
			break;
		case PromptDialog.OK:
			centerButton.setClickable(false);
			timeTip.setText("");
			recount = timeCount;
			if (timeThread != null) {
				timeThread.setFlag(false);
				istiming = false;
				try {
					timeThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isPromptDialogShow = false;
			if (verify != null) {
				Message uploadMsg = new Message();
				uploadMsg.what = RecorderFragment.START_UPLOAD;
				Bundle uploadData = new Bundle();
				uploadData.putString("uniqueToken", verify.uniqueToken);
				uploadData.putString("phoneNum", verify.phoneNum);
				uploadMsg.setData(uploadData);
				((RecorderFragment) fragmentsList.get(3))
						.handleMessage(uploadMsg);
				handler.sendMessage(msg);
			}

			break;
		case PromptDialog.CANCEL:
			handler.sendMessage(msg);
			break;
		case PromptDialog.SHOWUP:
			handler.sendMessage(msg);
			break;
		case RecorderFragment.UPLOAD_SUCCESS:
			Message uploadSuccessMsg = new Message();
			uploadSuccessMsg.what = VERIFY;
			handler.sendMessage(uploadSuccessMsg);

			if (timeThread != null) {
				timeThread.setFlag(false);
				try {
					timeThread.interrupt();
					timeThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Intent it = new Intent(MainActivity.this, MyService.class);
			// Bundle toServiceData = new Bundle();
			// toServiceData.putString("what", "do_verify");
			// toServiceData.putSerializable("verify", verify);
			// it.putExtras(toServiceData);
			// startService(it);
			verifyThread = new VerifyThread(this, this, verify.phoneNum,
					verify.uniqueToken);
			verifyThread.start();
			break;
		case RecorderFragment.UPLOAD_FAILURE:
		case RecorderFragment.UPLOAD_REQUEST_FAILURE:
			handler.sendMessage(msg);
			break;
		case MainActivity.VERIFY_SUCCESS:
			Message verifySuccessMsg = new Message();
			verifySuccessMsg.what = VERIFY_SUCCESS;
			handler.sendMessage(verifySuccessMsg);
			break;
		case MainActivity.VERIFY_FAILURE:
			handler.sendMessage(msg);
			break;
		case MainActivity.LOGOFF_SUCCESS:
			Message logoffSuccessMsg = new Message();
			logoffSuccessMsg.what = LOGOFF_SUCCESS;
			handler.sendMessage(logoffSuccessMsg);
			break;
		case MainActivity.LOGOFF_FAILURE:
			handler.sendMessage(msg);
			break;
		case VideoRecorderManager.CAMERA_CONNECT_FAILURE:
			handler.sendMessage(msg);
		case TipDialog.OK:
			handler.handleMessage(msg);
			break;
		case TipDialog.DISMISS:
			handler.handleMessage(msg);
			break;
		case MainActivity.TIME_OVER:
			if (identityStep == 2) {
				replaceFragment(3, 2);
				Message m2 = new Message();
				m2.what = AnswerQuestionFragment.INIT_QUESTION;
				Bundle initData = new Bundle();
				initData.putSerializable("verify", verify);
				m2.setData(initData);
				((AnswerQuestionFragment) fragmentsList.get(2))
						.handleMessage(m2);
			}
			Message m = new Message();
			m.what = MainActivity.SHOW_TIPDIALOG;
			handler.sendMessage(m);
			break;
		case MainActivity.DEMONSTRATE_SHOWUP:
			isDemonstrate = true;
			Intent intent = new Intent(MainActivity.this, MyService.class);
			intent.putExtra("what", "show_tip");
			startService(intent);
			break;
		case MyProgressDialog.DISMISS:
			handler.sendMessage(msg);
			break;
		case LoginFragment.START_LOGIN:
			String str = MainActivity.this.getString(R.string.logining);
			showProgressDialog(str, "progress");
			break;
		case LoginFragment.REGISTER_SUCCESS:
			Bundle bundle = msg.getData();

			/* 获取Bundle中的数据，注意类型和key */
			if (bundle != null) {
				String result = bundle.getString("result");
				if (result != null) {
					showResultDialog(result, "register_result");
				}
			}
			break;
		}

		return false;
	}

	public void nextStep() {
		fragmentHasBeenDone.put(currentPageIndex, true);
		mViewPager.setCurrentItem(currentPageIndex + 1);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		this.lat = location.getLatitude();
		this.lon = location.getLongitude();
	}

	public void onReceivePoi(BDLocation poiLocation) {
		if (poiLocation == null) {
			return;
		}

	}

	private void setLocationOption(LocationClient locationClient) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setAddrType("all");
		option.setScanSpan(3000);

		if (locHandler.hasGPSDevice() && locHandler.isGPSOpen()) {
			option.setOpenGps(true);
			option.setPriority(LocationClientOption.GpsFirst);

		} else if (locHandler.hasNetworkDevice()) {
			option.setPriority(LocationClientOption.NetWorkFirst);
		}

		option.setPoiNumber(10);
		option.disableCache(true);
		locationClient.setLocOption(option);
	}

	public class TimeThread extends Thread {
		volatile boolean flag = false;

		@Override
		public void run() {
			Looper.prepare();
			while (flag) {
				try {
					if (timeCount < 0) {
						flag = false;
						break;
					}
					Message message = new Message();
					message.what = UPDATE_TIME;
					handler.sendMessage(message);
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

		}

		public void setFlag(boolean flag) {
			this.flag = flag;
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

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public int getLength(String value) {
		Paint paint = new Paint();
		Rect rect = new Rect();
		paint.getTextBounds(value, 0, value.length(), rect);
		return rect.width();
	}

	public void printPhoneInfo() {
		System.out.println(android.os.Build.BOARD);
		System.out.println(android.os.Build.BOOTLOADER);// :获取设备引导程序版本号
		System.out.println(android.os.Build.CPU_ABI);// ：获取设备指令集名称（CPU的类型）
		System.out.println(android.os.Build.CPU_ABI2);// ：获取第二个指令集名称
		System.out.println(android.os.Build.MANUFACTURER);// :获取设备制造商

	}

	public void replaceFragment(int index1, int index2) {
		if (index1 == 2) {
			mViewPager.setCurrentItem(index2);

		} else if (index1 == 3) {
			mViewPager.setCurrentItem(index2);
			initForVerify();
		}
	}

	@Override
	public boolean onPreDraw() {
		if (hasMeasured == false) {

			int width1 = mRadioButton1.getMeasuredWidth();
			int width2 = mRadioButton2.getMeasuredWidth();
			int width3 = mRadioButton3.getMeasuredWidth();
			int width4 = mRadioButton4.getMeasuredWidth();
			titleWide = Math.max(width1, width2);
			titleWide = Math.max(titleWide, width3);
			titleWide = Math.max(titleWide, width4);
			mRadioButton1.setWidth((int) titleWide);
			mRadioButton2.setWidth((int) titleWide);
			mRadioButton3.setWidth((int) titleWide);
			mRadioButton4.setWidth((int) titleWide);

			hasMeasured = true;

		}
		return true;
	}

	public void showDialog(String msg, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		MyProgressDialog dialog = MyProgressDialog.newInstance(
				getSupportFragmentManager(), this, msg);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void showProgressDialog(String msg, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		MyProgressDialog dialog = MyProgressDialog.newInstance(
				getSupportFragmentManager(), this, msg);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void dissmissProgress(String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}

		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	public void showResultDialog(String msg, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		ResultPromptDialog dialog = ResultPromptDialog.newInstance("", msg,
				MainActivity.this.getSupportFragmentManager(),
				MainActivity.this);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void showPromptDialog(String msg, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		PromptDialog dialog = PromptDialog.newInstance("", msg,
				MainActivity.this.getSupportFragmentManager(),
				MainActivity.this);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void showTipDialog(String msg, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);

		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		// Create and show the dialog.
		TipDialog dialog = TipDialog.newInstance("", msg,
				MainActivity.this.getSupportFragmentManager(),
				MainActivity.this);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void dissmissDialog(String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}

		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			handler.sendEmptyMessageDelayed(EXIT_RESET, 2000);
		} else {
			// 关闭服务端的验证
			int mode = 1;
			if (app != null) {
				mode = app.state;
			}
			if (verify != null && mode == 1) {
				new Thread(new LogoffThread(MainActivity.this,
						MainActivity.this, verify.phoneNum, verify.uniqueToken))
						.start();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private TimeThread creatTimeThread() {
		return new TimeThread();
	}

	static class MyHandler extends Handler {
		// 注意下面的“PopupActivity”类是MyHandler类所在的外部类，即所在的activity
		WeakReference<MainActivity> mActivity;

		MyHandler(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity theActivity = mActivity.get();

			if (msg.what == NEXT) {
				theActivity.nextStep();
			} else if (msg.what == LOGIN) {

			} else if (msg.what == LOGIN_SUCCESS || msg.what == LOGIN_FAILURE) {

				theActivity.centerButton.setClickable(true);
				theActivity.dissmissProgress("progress");
				if (msg.what == LOGIN_SUCCESS) {
					theActivity.nextStep();
				}
			} else if (msg.what == ANSWER_FAILURE) {
				theActivity.centerButton.setClickable(true);
			} else if (msg.what == RecorderFragment.HAS_FACE) {
				String queryStr = theActivity.getString(R.string.if_upload);
				theActivity.showPromptDialog(queryStr, "prompt");
			} else if (msg.what == RecorderFragment.HAS_NO_FACE) {
				theActivity.centerButton.setClickable(true);
				theActivity.isPromptDialogShow = false;
				String str = theActivity.getString(R.string.no_face);
				Toast.makeText(theActivity, str, Toast.LENGTH_LONG).show();
			}

			else if (msg.what == BEGIN_VERIFY) {
				theActivity.identityStep = 2;
				String str = theActivity.getString(R.string.read);
				theActivity.centerButton.setText(str);
				theActivity.centerButton.setClickable(true);
			} else if (msg.what == VERIFY) {
				String str = theActivity.getString(R.string.verifying);
				theActivity.showProgressDialog(str, "progress");
			} else if (msg.what == VERIFY_SUCCESS) {
				theActivity.dissmissProgress("progress");
				String str = theActivity.getString(R.string.verify_success);
				// showResultDialog(str, "verify_result");
				Toast.makeText(theActivity, str, Toast.LENGTH_LONG).show();
				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}
				if (theActivity.verify != null && mode == 1) {
					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
				}
				theActivity.centerButton.setClickable(true);
				Message nextMsg = new Message();
				nextMsg.what = NEXT;
				this.sendMessage(nextMsg);
			} else if (msg.what == VERIFY_FAILURE) {
				theActivity.dissmissProgress("progress");
				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}
				if (theActivity.verify != null && mode == 1) {

					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
				}
				Bundle data = msg.getData();
				if (data != null) {
					String result = data.getString("result");
					if (result != null && !"".equals(result)) {
						theActivity.showResultDialog(result, "verify_result");

					}
				}
				Message msgReset = new Message();
				msgReset.what = RESET;
				this.sendMessage(msgReset);

			} else if (msg.what == PromptDialog.CANCEL) {
				theActivity.centerButton.setClickable(true);
				theActivity.isPromptDialogShow = false;
			} else if (msg.what == PromptDialog.OK) {
				theActivity.centerButton.setClickable(false);
				String str = theActivity.getString(R.string.uploading);
				theActivity.showProgressDialog(str, "progress");
			} else if (msg.what == PromptDialog.SHOWUP) {
				theActivity.centerButton.setClickable(false);
				theActivity.isPromptDialogShow = true;
			} else if (msg.what == LOGOFF_SUCCESS) {
				if (!theActivity.isAlive) {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			} else if (msg.what == RecorderFragment.UPLOAD_FAILURE
					|| msg.what == RecorderFragment.UPLOAD_REQUEST_FAILURE) {
				theActivity.dissmissProgress("progress");
				Message msgReset = new Message();
				msgReset.what = RESET;
				this.sendMessage(msgReset);
				String text = theActivity.getString(R.string.upload_failure);

				theActivity.showResultDialog(text, "upload_result");

				theActivity.centerButton.setClickable(true);
			} else if (MainActivity.LOGOFF_FAILURE == msg.what) {
				if (!theActivity.isAlive) {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			} else if (MainActivity.LOGOFF_OFFLINE == msg.what) {

				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}

				if (theActivity.isServiceRunning(theActivity,
						MyService.class.getName())) {
					Intent intent = new Intent(theActivity, MyService.class);
					theActivity.stopService(intent);
					theActivity.isDemonstrate = true;
				}
				if (theActivity.verify != null && mode == 1) {
					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
				} else {
					android.os.Process.killProcess(android.os.Process.myPid());
				}

			} else if (VideoRecorderManager.CAMERA_CONNECT_FAILURE == msg.what) {
				Bundle data = msg.getData();
				String result = data.getString("result");
				if (result != null) {
					String str = theActivity
							.getString(R.string.connect_camera_failure);
					theActivity.cameraPromptDialog = CameraPromptDialog
							.newInstance("", str,
									theActivity.getSupportFragmentManager(),
									theActivity);
					theActivity.cameraPromptDialog.show();
				}
				theActivity.centerButton.setClickable(false);
			} else if (MainActivity.UPDATE_TIME == msg.what) {
				theActivity.timeCount--;
				if (theActivity.timeCount >= 0) {
					String str = theActivity.getString(R.string.time);
					theActivity.timeTip.setText(str + ":"
							+ theActivity.timeCount + "s");

				} else {
					String str = theActivity.getString(R.string.timeout_tip);
					theActivity.showResultDialog(str, "timeout");

					Message m = new Message();
					m.what = RESET;
					this.sendMessage(m);

				}
			} else if (TipDialog.OK == msg.what) {
			} else if (TipDialog.DISMISS == msg.what) {
				if (!theActivity.isInit) {
					theActivity.isTipDialogShow = false;
					theActivity.timeTip.setVisibility(View.VISIBLE);
					if (theActivity.timeThread == null) {
						theActivity.timeThread = theActivity.creatTimeThread();
						theActivity.istiming = true;
						theActivity.timeThread.start();
					}
					theActivity.timeThread.setFlag(true);
				}

			} else if (MainActivity.SHOW_TIPDIALOG == msg.what) {
				String tipContent = theActivity.getString(R.string.tip_content);
				theActivity.showTipDialog(tipContent, "tip");
				theActivity.isTipDialogShow = true;
			} else if (MainActivity.RESET == msg.what) {

				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}
				if (mode == 2) {
					theActivity.isDemonstrate = false;
					Intent intent = new Intent(theActivity, MyService.class);
					intent.putExtra("what", "hide_tip");
					theActivity.startService(intent);
				}
				if (theActivity.verify != null && mode == 1) {

					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
				}

				if (theActivity.timeThread != null) {
					theActivity.timeThread.setFlag(false);
					try {
						theActivity.timeThread.interrupt();
						theActivity.timeThread.join();
						theActivity.timeThread = null;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (theActivity.isPromptDialogShow) {
					theActivity.dissmissDialog("prompt");
					theActivity.isPromptDialogShow = false;
				}
				theActivity.isDemonstrate = false;
				if (theActivity.isTipDialogShow == true
						&& theActivity.tipDialog != null) {
					theActivity.tipDialog.dismiss();
					theActivity.isTipDialogShow = false;
				}
				theActivity.initAll();

			}
			// 锁屏状态
			else if (10000 == msg.what) {
				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}
				if (mode == 2) {
					theActivity.isDemonstrate = false;
					Intent intent = new Intent(theActivity, MyService.class);
					intent.putExtra("what", "hide_tip");
					theActivity.startService(intent);
				}
				if (theActivity.verify != null && mode == 1) {

					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
				}
				theActivity.initAll();
			} else if (EXIT_RESET == msg.what) {
				theActivity.isExit = false;
			} else if (NET_DISCONNECTION == msg.what) {
				int mode = 1;
				if (theActivity.app != null) {
					mode = theActivity.app.state;
				}

				if (theActivity.verify != null && mode == 1) {

					new Thread(new LogoffThread(theActivity, theActivity,
							theActivity.verify.phoneNum,
							theActivity.verify.uniqueToken)).start();
					theActivity.initAll();
					String str = theActivity.getString(R.string.net_disconnect);
					theActivity.showResultDialog(str, "network");
				}
			}

		}
	}
}