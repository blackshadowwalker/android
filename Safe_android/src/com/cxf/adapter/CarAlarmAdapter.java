package com.cxf.adapter;

import java.util.List;

import com.cxf.entity.CarAlarm;
import com.cxf.safe_android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CarAlarmAdapter extends BaseAdapter {
	// 数据源
	private List<CarAlarm> list;
	// 上下文
	private Context context;
	// 布局填充类
	private LayoutInflater inflater;
	// 资源文件
	private int layoutId;

	public CarAlarmAdapter(Context context, List<CarAlarm> list) {
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
		final CarAlarm carAlarm = (CarAlarm) getItem(position);
		DataWrapper viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(layoutId, null);

			viewHolder = new DataWrapper();
			viewHolder.time = (TextView) convertView.findViewById(R.id.tx1);
			viewHolder.place = (TextView) convertView.findViewById(R.id.tx3);
			viewHolder.carNumber = (TextView) convertView
					.findViewById(R.id.tx2);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.direction=(TextView)convertView.findViewById(R.id.tx4);
			viewHolder.prompt=(TextView)convertView.findViewById(R.id.prompt);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (DataWrapper) convertView.getTag();
		}

		viewHolder.time.setText(String.valueOf(carAlarm.absTime));
		viewHolder.place.setText(String.valueOf(carAlarm.location));
		viewHolder.carNumber.setText(carAlarm.LPNumber);
		if(carAlarm.shortImageA!=null)
		{
		viewHolder.img.setImageBitmap(carAlarm.shortImageA);
		}
		
		if(carAlarm.dir==1)
		{
			viewHolder.direction.setText("进");
		}
		else
		{
			viewHolder.direction.setText("出");
		}
		
		if(carAlarm.isNew)
		{
			viewHolder.prompt.setText("新");
		}
		else{
			viewHolder.prompt.setText("");
		}

		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
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
		// 车牌号
		public TextView carNumber;
		// 方向
		public TextView direction;
		// 图片
		public TextView prompt;
		
		public ImageView img;
		private TextView timeTitle;
		private TextView carNumberTitle;
		private TextView placeTitle;

		public DataWrapper() {
			View view =inflater.inflate(layoutId, null);
			this.timeTitle = (TextView) view.findViewById(R.id.tx1_text);
			this.carNumberTitle = (TextView) view.findViewById(R.id.tx2_text);
			this.placeTitle = (TextView) view.findViewById(R.id.tx3_text);

			this.timeTitle.setText("时间：");
			this.carNumberTitle.setText("车牌号：");
			this.placeTitle.setText("地点：");
			
		}

	}

}
