package com.cxf.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cxf.PhoneIdentification.R;

public class NumberAdapter extends BaseAdapter {
	private List<String> list = new ArrayList<String>();
	private Context context = null;

	/**
	 * 自定义构造方法
	 * 
	 * @param list
	 * 
	 * @param activity
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NumberAdapter(Context context, List list) {
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
					R.layout.item_number, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.item_text);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(list.get(position));

		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}
}
