package com.cxf.PhoneIdentification;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxf.adapter.QuestionAdapter;
import com.cxf.entity.Verify;
import com.cxf.entity.VerifyItem;

@SuppressLint("HandlerLeak")
public class AnswerQuestionFragment extends Fragment implements
		View.OnClickListener, Callback {
	private static final String TAG = "AnswerQuestionFragment";
	public static final int SUCCESS = 21;
	public static final int INIT_QUESTION = 20;
	public static final int VERIFY = 22;
	public static final int FAILURE = -21;
	TextView question;
	EditText answer;
	Verify verify;
	static Callback callback;
	ListView listview;
	List<VerifyItem> list = new ArrayList<VerifyItem>();
	QuestionAdapter adapter;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_QUESTION:
				Bundle data = msg.getData();
				verify = (Verify) data.getSerializable("verify");
				if (verify != null) {
					list = verify.questionsAndAnswers;
					adapter = new QuestionAdapter(
							AnswerQuestionFragment.this.getActivity(), list);
					listview.setAdapter(adapter);
				}
				break;

			default:
				break;
			}
		};
	};

	// Button centerButton;

	public static AnswerQuestionFragment newInstance(Callback callback) {
		AnswerQuestionFragment.callback = callback;
		AnswerQuestionFragment newFragment = new AnswerQuestionFragment();
		return newFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_question_answer,
				container, false);
		question = (TextView) view.findViewById(R.id.question);
		answer = (EditText) view.findViewById(R.id.answer);
		listview = (ListView) view.findViewById(R.id.question_list);
		return view;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case INIT_QUESTION:
			handler.sendMessage(msg);
			break;
		case VERIFY:
			if (adapter != null && adapter.checkIfTrue()) {
				Message m = new Message();
				m.what = SUCCESS;
				callback.handleMessage(m);
			} else if (adapter == null && this.getActivity() != null) {
				String str = AnswerQuestionFragment.this
						.getString(R.string.answer_input_null);
				Toast.makeText(this.getActivity(), str, Toast.LENGTH_SHORT)
						.show();
				Message m = new Message();
				m.what = FAILURE;
				callback.handleMessage(m);
			} else {
				if (this.getActivity() != null) {
					String str = AnswerQuestionFragment.this
							.getString(R.string.answer_failure);
					Toast.makeText(this.getActivity(), str, Toast.LENGTH_SHORT)
							.show();
					Message m = new Message();
					m.what = FAILURE;
					callback.handleMessage(m);
				}
			}

			break;
		case SUCCESS:

			break;
		case FAILURE:

			break;
		default:
			break;
		}
		return false;
	}
}
