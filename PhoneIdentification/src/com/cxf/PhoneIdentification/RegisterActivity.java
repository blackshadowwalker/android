package com.cxf.PhoneIdentification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxf.dao.UserHistoryDao;
import com.cxf.entity.Constant;
import com.cxf.entity.User;
import com.cxf.entity.VerifyItem;
import com.cxf.net.handler.FileTools;
import com.cxf.net.handler.ImageTools;
import com.cxf.net.handler.RegisterThread;
import com.cxf.view.MyProgressDialog;
import com.cxf.view.ResultPromptDialog;

public class RegisterActivity extends FragmentActivity implements Callback,
		OnClickListener, OnKeyListener {
	FragmentManager fm;
	EditText name;
	EditText password;
	Button setPictureButton;
	View setPictureLayout;
	Button selectPicButton;
	Button takePicButton;
	ImageView img;
	Button setQuestionButton;
	View questionListLayout;
	EditText question;
	EditText answer;
	Button save;
	Button reset;
	Button cancel;
	Button showAllQuestion;
	Button register;
	Button back;
	TextView msg;
	Bitmap bitmap;
	String path;
	boolean hasDestroy = false;
	ArrayList<VerifyItem> questionsAndAnswers = new ArrayList<VerifyItem>();
	final String TAG = "RegisterActivity";
	public final static int REGISTER_SUCCESS = 911;
	public final static int REGISTER_FAILURE = -911;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	int degree;
	UserHistoryDao userHistoryDao;
	MyHandler handler;

	protected void onStop() {
		super.onStop();
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		handler = new MyHandler(this);
		path = Environment.getExternalStorageDirectory() + "/image.jpg";
		userHistoryDao = UserHistoryDao.Instance(this);
		init();
	}

	@Override
	public void onDetachedFromWindow() {
		hasDestroy = true;
		super.onDetachedFromWindow();
	}

	private void init() {
		fm = getSupportFragmentManager();
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.password);
		setPictureButton = (Button) findViewById(R.id.picture_set);
		setPictureLayout = findViewById(R.id.picture_set_detail);
		selectPicButton = (Button) findViewById(R.id.select_pic);
		takePicButton = (Button) findViewById(R.id.take_pic);
		img = (ImageView) findViewById(R.id.img);
		setQuestionButton = (Button) findViewById(R.id.question_set);
		questionListLayout = findViewById(R.id.question_list_set);
		question = (EditText) findViewById(R.id.question);
		answer = (EditText) findViewById(R.id.answer);
		save = (Button) findViewById(R.id.save);
		reset = (Button) findViewById(R.id.reset);
		cancel = (Button) findViewById(R.id.cancel);
		showAllQuestion = (Button) findViewById(R.id.show_question);
		register = (Button) findViewById(R.id.register);
		back = (Button) findViewById(R.id.back);
		msg = (TextView) findViewById(R.id.msg);

		setPictureButton.setOnClickListener(this);
		selectPicButton.setOnClickListener(this);
		takePicButton.setOnClickListener(this);
		setQuestionButton.setOnClickListener(this);
		save.setOnClickListener(this);
		reset.setOnClickListener(this);
		cancel.setOnClickListener(this);
		showAllQuestion.setOnClickListener(this);
		register.setOnClickListener(this);
		back.setOnClickListener(this);

		String qust = getString(R.string.question);
		String answ = getString(R.string.answer);
		if (qust != null && answ != null && question != null && answer != null) {
			int count = questionsAndAnswers.size() + 1;
			question.setHint(qust + count);
			answer.setHint(answ + count);
		}

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
							String str = RegisterActivity.this
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
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler = null;
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 设置图片
		case R.id.picture_set:
			setPictureLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.select_pic:
			Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
			openAlbumIntent.setType("image/*");
			startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
			break;
		case R.id.take_pic:
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			if (openCameraIntent.resolveActivity(getPackageManager()) != null) {

				Uri imageUri = Uri.fromFile(new File(path));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
			break;
		// 设置问题
		case R.id.question_set:
			questionListLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.save:
			if (question != null && answer != null
					&& questionsAndAnswers != null
					&& questionsAndAnswers.size() < 7) {
				String questionStr = question.getText().toString();
				String answerStr = answer.getText().toString();
				if (!"".equals(questionStr) && !"".equals(answerStr)) {
					VerifyItem item = new VerifyItem();
					item.question = questionStr;
					item.answer = answerStr;
					questionsAndAnswers.add(item);
					String qust = getString(R.string.question);
					String answ = getString(R.string.answer);
					if (qust != null && answ != null && question != null
							&& answer != null && questionsAndAnswers.size() < 6) {
						int count = questionsAndAnswers.size() + 1;
						question.setText("");
						answer.setText("");
						question.setHint(qust + count);
						answer.setHint(answ + count);
					} else if (qust != null && answ != null && question != null
							&& answer != null
							&& questionsAndAnswers.size() == 6) {
						question.setText("");
						answer.setText("");
						question.setHint(qust + 6);
						answer.setHint(answ + 6);
						question.setClickable(false);
						answer.setClickable(false);
						String str = getString(R.string.max_question_number);
						Toast.makeText(this, str, Toast.LENGTH_LONG).show();
					}

				}
			} else if (questionsAndAnswers != null
					&& questionsAndAnswers.size() >= 7) {
				String str = getString(R.string.max_question_number);
				Toast.makeText(this, str, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.reset:
			if (question != null && answer != null) {
				question.setText("");
				answer.setText("");
			}
			break;
		case R.id.cancel:
			break;
		case R.id.show_question:
			break;
		// 注册按钮
		case R.id.register:

			if (name != null && password != null && questionsAndAnswers != null) {
				String nameStr = name.getText().toString();
				String passStr = password.getText().toString();
				if (!("".equals(passStr) || "".equals(nameStr))
						&& name.length() == 11 && password.length() >= 6
						&& questionsAndAnswers.size() > 0 && bitmap != null) {
					register.setClickable(false);
					String str = getString(R.string.register);
					showProgressDialog(str, "register_progress");
					new Thread(new RegisterThread(this, this, nameStr, passStr,
							bitmap, "123456", 6, questionsAndAnswers)).start();
				} else if (name.length() < 11) {
					String str = getString(R.string.number_length_tip);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				} else if (password.length() < 6) {
					String str = getString(R.string.password_length_tip);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				}

				else if ("".equals(nameStr) || "".equals(passStr)) {
					String str = getString(R.string.login_input_null);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				} else if (bitmap == null) {
					String str = getString(R.string.picture_enpty);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				} else if (questionsAndAnswers.size() < 1) {
					String str = getString(R.string.at_least_onequestion__tip);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
				}
			}
			break;
		// 返回按钮点击事件
		case R.id.back:
			RegisterActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, String.valueOf(resultCode));
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case TAKE_PICTURE:
				degree = ImageTools.readPictureDegree(path);
				// 将保存在本地的图片取出并缩小后显示在界面上
				bitmap = ImageTools.getSmallBitmap(path);
				if (FileTools.checkResolution(bitmap.getWidth(),
						bitmap.getHeight())) {
					bitmap = ImageTools.rotaingImageView(degree, bitmap);
					Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, 100, 100);
					// 将处理过的图片显示在界面上，并保存到本地
					img.setImageBitmap(newBitmap);
				} else {
					String str = getString(R.string.take_pic_toosmall);
					Toast.makeText(this, str, Toast.LENGTH_LONG).show();
					bitmap.recycle();
					bitmap = null;
				}
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					bitmap = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if ("samsung".equals(android.os.Build.MANUFACTURER)) 
					{
						bitmap=ImageTools.rotaingImageView(270, bitmap);
					}
					if (FileTools.checkResolution(bitmap.getWidth(),
							bitmap.getHeight())) {
						degree = ImageTools.readPictureDegree(originalUri
								.getPath());
						bitmap = ImageTools.rotaingImageView(degree, bitmap);
						bitmap = ImageTools.zoomBitmap(bitmap, 480, 640);

						if (bitmap != null) {
							// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap,
									100, 100);
							// 释放原始图片占用的内存，防止out of memory异常发生
							img.setImageBitmap(smallBitmap);
						}
					} else {
						String str = getString(R.string.pic_toosmall);
						Toast.makeText(this, str, Toast.LENGTH_LONG).show();
						bitmap.recycle();
						bitmap = null;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
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
				RegisterActivity.this.getSupportFragmentManager(),
				RegisterActivity.this);
		// adding the fragment to it with the given tag, and committing it.
		dialog.show(ft, tag);
	}

	public void dissmissProgress(String tag) {
		if (!hasDestroy) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
			if (prev != null) {
				ft.remove(prev);
			}

			ft.addToBackStack(null);
			ft.commitAllowingStateLoss();
		}
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

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// 设置Intent对象要启动的Activity
			intent.setClass(RegisterActivity.this, MainActivity.class);
			// 通过Intent对象启动另外一个Activity
			startActivity(intent);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RegisterActivity.this.finish();

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	static class MyHandler extends Handler {
		// 注意下面的“PopupActivity”类是MyHandler类所在的外部类，即所在的activity
		WeakReference<RegisterActivity> mActivity;

		MyHandler(RegisterActivity activity) {
			mActivity = new WeakReference<RegisterActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			RegisterActivity theActivity = mActivity.get();

			switch (msg.what) {
			case REGISTER_SUCCESS:
				File file = new File(theActivity.path);
				FileTools.deleteFoder(file);
				theActivity.register.setClickable(true);
				theActivity.dissmissProgress("register_progress");
				String str = theActivity.getString(R.string.register_success);

				if (str != null) {
					User user = new User();
					String nameStr = theActivity.name.getText().toString();
					String passStr = theActivity.password.getText().toString();
					String url = Constant.HOST;
					user.telephone = nameStr;
					user.password = passStr;

					theActivity.userHistoryDao.insert(user, url);

					theActivity.name.setText("");
					theActivity.password.setText("");
					theActivity.question.setText("");
					theActivity.answer.setText("");
					theActivity.questionsAndAnswers = new ArrayList<VerifyItem>();
					String qust = theActivity.getString(R.string.question);
					String answ = theActivity.getString(R.string.answer);
					theActivity.question.setHint(qust + 1);
					theActivity.answer.setHint(answ + 1);
					// showResultDialog(str, "register_result");

					Intent intent = new Intent(theActivity, MainActivity.class);
					intent.putExtra("result", str);
					theActivity.setResult(0, intent);

					theActivity.finish();
				}

				break;
			case REGISTER_FAILURE:
				File file2 = new File(theActivity.path);
				FileTools.deleteFoder(file2);
				theActivity.register.setClickable(true);
				theActivity.dissmissProgress("register_progress");
				String str2 = theActivity.getString(R.string.register_failure);
				if (str2 != null) {
					theActivity.showResultDialog(str2, "register_result");
				}
			default:
				break;
			}
		}
	}

}
