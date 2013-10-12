package com.cxf.adapter;

import java.util.List;

import com.cxf.entity.SecurityAlarm;
import com.cxf.safe_android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SecurityAlarmAdapter extends BaseAdapter {
	// 数据源
	private List<SecurityAlarm> list;
	// 上下文
	private Context context;
	// 布局填充类
	private LayoutInflater inflater;
	// 资源文件
	private int layoutId;

	public SecurityAlarmAdapter(Context context, List<SecurityAlarm> list) {
		super();
		this.list = list;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutId = R.layout.item_layout;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < list.size()) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final SecurityAlarm securityAlarm = (SecurityAlarm) getItem(position);
		DataWrapper viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(layoutId, null);

			viewHolder = new DataWrapper(convertView);
			viewHolder.time = (TextView) convertView.findViewById(R.id.tx1);
			viewHolder.place = (TextView) convertView.findViewById(R.id.tx3);
			viewHolder.level = (TextView) convertView.findViewById(R.id.tx2);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.eventName = (TextView) convertView
					.findViewById(R.id.tx4);
			viewHolder.prompt = (TextView) convertView
					.findViewById(R.id.prompt);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (DataWrapper) convertView.getTag();
		}

		viewHolder.time.setText(String.valueOf(securityAlarm.absTime));
		viewHolder.place.setText(String.valueOf(securityAlarm.location));
		viewHolder.level.setText(String.valueOf(securityAlarm.level));
		viewHolder.eventName.setText(securityAlarm.eventName);
		if (securityAlarm.shortImage != null) {
			viewHolder.img.setImageBitmap(securityAlarm.shortImage);
		}
		if (securityAlarm.isNew) {
			viewHolder.prompt.setText("新");
		} else {
			viewHolder.prompt.setText("");
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount();
	}

	/**
	 * 组装类的实体类，方便操作
	 * 
	 * @author Administrator
	 * 
	 */
	private class DataWrapper {
		// 初始化时间
		public TextView time;
		// 初始化地点
		public TextView place;
		// 警告级别
		public TextView level;
		// 事件名称
		public TextView eventName;
		// 图片
		public TextView prompt;
		public ImageView img;
		private TextView timeTitle;
		private TextView levelTitle;
		private TextView placeTitle;

		public DataWrapper(View view) {
			this.timeTitle = (TextView) view.findViewById(R.id.tx1_text);
			this.levelTitle = (TextView) view.findViewById(R.id.tx2_text);
			this.placeTitle = (TextView) view.findViewById(R.id.tx3_text);

			this.timeTitle.setText("时间：");
			this.levelTitle.setText("警告警告级别：");
			this.placeTitle.setText("地点：");
		}

	}

}
