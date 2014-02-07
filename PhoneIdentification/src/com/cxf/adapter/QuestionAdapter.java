package com.cxf.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cxf.PhoneIdentification.R;
import com.cxf.entity.VerifyItem;

public class QuestionAdapter extends BaseAdapter {
	private List<VerifyItem> list = new ArrayList<VerifyItem>();
	HashMap<Integer,Boolean> ifTrue = new HashMap<Integer,Boolean>();
	private Context context = null;

	/**
	 * 自定义构造方法
	 * 
	 * @param list
	 * 
	 * @param activity
	 */
	public QuestionAdapter(Context context, List<VerifyItem> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			// 下拉项布局
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_question, null);
			holder.question = (TextView) convertView
					.findViewById(R.id.question);
			holder.answer = (EditText) convertView.findViewById(R.id.answer);

			convertView.setTag(holder);
			final String question = list.get(position).question;
			holder.question.setText(question);

			TextWatcher textWatcher = new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					final String answerO = s.toString();
					if (answerO.equals(list.get(position).answer)) {
						ifTrue.put(position,true);
					}
					else
					{
						ifTrue.put(position,false);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int lengthBefore, int lengthAfter) {

				}
			};
			holder.answer.addTextChangedListener(textWatcher);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;

	}

	class ViewHolder {
		TextView question;
		EditText answer;
	}

	public boolean checkIfTrue() {
		Boolean result = true;

		if (ifTrue.size() == 0) {
			result = false;
			return false;
		}
		for (int i = 0; i < ifTrue.size(); i++) {
			if ((ifTrue.get(i) != null && !ifTrue.get(i))
					|| ifTrue.get(i) == null) {
				return false;
			}
		}
		return result;
	}
}