package com.cxf.PhoneIdentification;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cxf.adapter.AutoCompleteAdapter;
import com.cxf.adapter.ServerAutoCompleteAdapter;
import com.cxf.dao.UserHistoryDao;
import com.cxf.entity.Constant;
import com.cxf.entity.MyApplication;
import com.cxf.entity.User;
import com.cxf.net.handler.LoginThread;
import com.cxf.view.AdvancedAutoCompleteTextView;
import com.cxf.view.ServerAdvancedAutoCompleteTextView;

@SuppressLint({ "ShowToast", "HandlerLeak" })
public class LoginFragment extends Fragment implements Callback,
		OnClickListener {
	private static final String TAG = "LoginFragment";
	Button auto_login;
	Button registerButton;
	// 用户名
	private AdvancedAutoCompleteTextView name;
	// 匹配用户名的弹窗
	private AutoCompleteAdapter adapter;
	// 可选择的用户名列表
	private List<String> mOriginalValues = new ArrayList<String>();
	// 密码
	EditText password;
	// 回显
	TextView msg;
	// 用户名
	private ServerAdvancedAutoCompleteTextView server;
	// 匹配用户名的弹窗
	private ServerAutoCompleteAdapter serverAdapter;
	// 可选择的用户名列表
	private List<String> serverValues = new ArrayList<String>();
	// 常量
	public final static int LOGIN = 11;
	public final static int SUCCESS = 12;
	public final static int FAILURE = 13;
	public final static int INIT = 14;
	public final static int START_LOGIN = 15;
	public final static int REGISTER_SUCCESS = 16;
	private final static int REFRESH = 17;
	// 回调
	public static Callback parentCallback;
	// 文本改变的监听类
	TextWatcher textWatcher;
	boolean isDemonstrating = false;
	FragmentManager fm;
	MyApplication app;
	UserHistoryDao userHistoryDao;
	User user;
	String serverUrl;
	// 更新主线程UI界面类
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == FAILURE) {
				Bundle data = msg.getData();
				String result = data.getString("result");
				Toast.makeText(LoginFragment.this.getActivity(), result,
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == INIT) {
				if (msg != null) {
				}
				if (name != null) {
					name.setText("");
				}
				if (password != null) {
					password.setText("");
				}
				if (server != null) {
					server.setText(Constant.HOST);
				}
				if (app != null) {
					app.state = 1;
				}
				auto_login.setClickable(true);
			} else if (msg.what == REFRESH) {
				if (LoginFragment.this.getActivity() != null) {
					mOriginalValues = userHistoryDao.queryUsers();
					adapter = new AutoCompleteAdapter(
							LoginFragment.this.getActivity(), mOriginalValues,
							10);
					name.setAdapter(adapter);

					serverValues = userHistoryDao.queryServers();
					serverAdapter = new ServerAutoCompleteAdapter(
							LoginFragment.this.getActivity(), serverValues, 10);
					server.setAdapter(serverAdapter);
				}
			}
		};
	};

	public static LoginFragment newInstance(Callback callback) {
		LoginFragment newFragment = new LoginFragment();
		parentCallback = callback;
		return newFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity() != null) {
			app = (MyApplication) getActivity().getApplication();
			userHistoryDao = UserHistoryDao.Instance(this.getActivity());
		}
	}

	@Override
	public void onResume() {
		msg.setFocusable(true);
		msg.setFocusableInTouchMode(true);
		msg.requestFocus();
		super.onResume();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;

		view = inflater.inflate(R.layout.fragment_login_layout, container,
				false);
		// 初始化组件
		name = (AdvancedAutoCompleteTextView) view
				.findViewById(R.id.login_name);
		password = (EditText) view.findViewById(R.id.password);
		server = (ServerAdvancedAutoCompleteTextView) view
				.findViewById(R.id.server);
		msg = (TextView) view.findViewById(R.id.msg);
		auto_login = (Button) view.findViewById(R.id.auto_login);
		registerButton = (Button) view.findViewById(R.id.register);
		registerButton.setOnClickListener(this);
		// 初始化变量，增加事件监听

		mOriginalValues = userHistoryDao.queryUsers();
		name.setThreshold(0);
		adapter = new AutoCompleteAdapter(this.getActivity(), mOriginalValues,
				10);
		name.setAdapter(adapter);
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					if (s.length() > 11) {
						String s2 = s.toString().substring(0, 11);
						name.setText(s2);
						name.setSelection(11);
					} else {
						String pattern = "^\\d*$";
						if (!s.toString().matches(pattern)) {
							int length = s.length() - count;
							String s2 = s.toString().substring(0, length);
							name.setText(s2);
							name.setSelection(length);
							String str = LoginFragment.this
									.getString(R.string.number_tip);

							msg.setText(str);
						} else {
							msg.setText("");
						}

					}
				} catch (Exception e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		name.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (TextUtils.isEmpty(name.getText().toString())) {
					name.showDropDown();
				}
				return false;
			}

		});
		serverValues = userHistoryDao.queryServers();
		if (serverValues.size() == 0) {
			serverValues.add(Constant.HOST);
			userHistoryDao.insert(null, Constant.HOST);
			userHistoryDao.insert(null, Constant.HOST2);
			server.setText(Constant.HOST2);
			app.url = Constant.HOST2;
		} else {
			server.setText(Constant.HOST2);
			app.url = Constant.HOST2;
		}
		serverAdapter = new ServerAutoCompleteAdapter(this.getActivity(),
				serverValues, 10);
		server.setAdapter(serverAdapter);
		server.setThreshold(0);
		server.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 50) {
					String s2 = s.toString().substring(0, 50);
					server.setText(s2);
					server.setSelection(50);
				}

				else {
					if ("".equals(s.toString())) {
						String str = LoginFragment.this
								.getString(R.string.server_tip);

						msg.setText(str);
						name.showDropDown();
					} else {
						msg.setText("");

					}
					app.url = s.toString();
				}
			}
		});

		auto_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isDemonstrating = true;
				v.setClickable(false);
				// 启动演示模式提示框
				Message tipMessage = new Message();
				tipMessage.what = MainActivity.DEMONSTRATE_SHOWUP;
				parentCallback.handleMessage(tipMessage);

				// 记录当前的模式状态
				if (app != null) {
					app.state = 2;

					// 自动登录
					name.setText(Constant.TELEPHONE);
					password.setText(Constant.PASSWORD);
				}

			}
		});
		return view;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg != null && msg.what == LOGIN && name != null
				&& name.getText() != null && password != null
				&& password.getText() != null) {
			String nameStr = name.getText().toString();
			String passwordStr = password.getText().toString();
			String serverStr = server.getText().toString();
			Bundle data = msg.getData();
			String lat = data.getString("lat");
			String lon = data.getString("lon");
			String ip = data.getString("ip");
			user = new User();

			if (!("".equals(nameStr) || "".equals(passwordStr) || ""
					.equals(serverStr))) {
				if (app != null) {
					app.url = serverStr;
					app.domain = getDomain(serverStr);
				}

				user.telephone = nameStr;
				user.password = passwordStr;
				serverUrl = app.url;
				new Thread(new LoginThread(LoginFragment.this.getActivity(),
						LoginFragment.this, nameStr, passwordStr, lat, lon, ip))
						.start();
				Message m = new Message();
				m.what = START_LOGIN;
				parentCallback.handleMessage(m);
			} else if ("".equals(nameStr) || "".equals(passwordStr)) {
				String str = LoginFragment.this
						.getString(R.string.login_input_null);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message m = new Message();
				m.what = FAILURE;
				Bundle loginData = new Bundle();
				loginData.putString("result", str);
				m.setData(loginData);
				parentCallback.handleMessage(m);
				Toast.makeText(this.getActivity(), str, 1000).show();
			} else if ("".equals(serverStr)) {
				String str = LoginFragment.this.getString(R.string.server_tip);
				Toast.makeText(this.getActivity(), str, 1000).show();
				Message m = new Message();
				m.what = FAILURE;
				Bundle loginData = new Bundle();
				loginData.putString("result", str);
				m.setData(loginData);
				parentCallback.handleMessage(m);
			}
		} else if (name == null || password == null) {
			Message m = new Message();
			m.what = FAILURE;
			Bundle loginData = msg.getData();
			m.setData(loginData);
			parentCallback.handleMessage(m);
		} else if (msg.what == SUCCESS) {
			userHistoryDao.insert(user, serverUrl);
			user.telephone = "";
			user.password = "";
			serverUrl = "";
			Message refreshMsg = new Message();
			refreshMsg.what = REFRESH;
			handler.sendMessage(refreshMsg);

			Bundle data = msg.getData();
			Message m = new Message();
			m.what = SUCCESS;
			m.setData(data);
			parentCallback.handleMessage(m);
		} else if (msg.what == FAILURE) {
			handler.sendMessage(msg);
			Message m = new Message();
			m.what = FAILURE;
			Bundle loginData = msg.getData();
			m.setData(loginData);
			parentCallback.handleMessage(m);
		} else if (msg.what == INIT) {
			handler.sendMessage(msg);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			if (this.getActivity() != null) {
				Intent intent = new Intent();
				// 设置Intent对象要启动的Activity
				intent.setClass(this.getActivity(), RegisterActivity.class);
				// 通过Intent对象启动另外一个Activity
				startActivityForResult(intent, 0);

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {

			mOriginalValues = userHistoryDao.queryUsers();
			adapter = new AutoCompleteAdapter(this.getActivity(),
					mOriginalValues, 10);
			name.setAdapter(adapter);
			String str = null;
			if (data != null) {
				str = data.getStringExtra("result");
			}
			if (str != null) {
				Message m = new Message();
				m.what = REGISTER_SUCCESS;
				Bundle bundle = new Bundle();
				bundle.putString("result", str);
				m.setData(bundle);
				parentCallback.handleMessage(m);
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getDomain(String url) {
		String sub = "";
		String result = "";
		if (url != null && url.startsWith("http://")) {
			int l = url.lastIndexOf(":");
			if (l == -1 || l < 7) {
				sub = url.substring(7);
				l = sub.indexOf("/");
				if (l == -1) {
					result = sub;
				} else {
					result = sub.substring(0, l);
				}
			} else {
				result = url.substring(7, l);
			}
		}
		return result;
	}

}
