package com.cxf.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cxf.PhoneIdentification.R;
import com.cxf.entity.Option;
import com.cxf.net.handler.DisplayUtil;
import com.cxf.view.ItemFeedbackPopWindow;
import com.cxf.view.ItemHelpPopWindow;
import com.cxf.view.ItemVersionPopWindow;

public class OptionsAdapter extends BaseAdapter {
	private List<Option> list = new ArrayList<Option>();
	private Context context = null;
	View parent;
	Callback callback;
	FragmentManager fm;
	public static final int SHUTDOWN_PAREBT = 300;
	PopupWindow pop = null;

	/**
	 * 自定义构造方法
	 * 
	 * @param list
	 * 
	 * @param activity
	 */
	public OptionsAdapter(Context context, Callback cb, FragmentManager fm) {
		this.context = context;
		this.callback = cb;
		this.fm = fm;
		initData();
		parent = LayoutInflater.from(context).inflate(R.layout.main, null);
	}

	public void initData() {
		Option op1 = new Option();
		String str1 = context.getString(R.string.help);
		String str2 = context.getString(R.string.version_info);
		String str3 = context.getString(R.string.feedback_info);
		op1.name = str1;
		op1.ImageResourceId = R.drawable.item_logo;
		op1.optionClassName = ItemHelpPopWindow.class.getName();
		list.add(op1);

		Option op2 = new Option();
		op2.name = str2;
		op2.ImageResourceId = R.drawable.item_logo;
		op2.optionClassName = ItemVersionPopWindow.class.getName();
		list.add(op2);

		Option op3 = new Option();
		op3.name = str3;
		op3.ImageResourceId = R.drawable.item_logo;
		op3.optionClassName = ItemFeedbackPopWindow.class.getName();
		list.add(op3);
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
					R.layout.item_option, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.item_text);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.delImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(list.get(position).name);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (ItemHelpPopWindow.class.getName().equals(
						list.get(position).optionClassName)) {
					pop = new ItemHelpPopWindow(context);
				} else if (ItemVersionPopWindow.class.getName().equals(
						list.get(position).optionClassName)) {
					pop = new ItemVersionPopWindow(context);
				} else if (ItemFeedbackPopWindow.class.getName().equals(
						list.get(position).optionClassName)) {
					pop = new ItemFeedbackPopWindow(context, fm);
				}
				View top = OptionsAdapter.this.parent
						.findViewById(R.id.horizontalScrollView);
				if (pop != null) {
					
					pop.showAsDropDown(top, 0, DisplayUtil.dip2px(context, 120));
				}
				Message m = new Message();
				m.what = SHUTDOWN_PAREBT;
				callback.handleMessage(m);

			}
		});

		return convertView;
	}
}

class ViewHolder {
	TextView textView;
	ImageView imageView;
}
