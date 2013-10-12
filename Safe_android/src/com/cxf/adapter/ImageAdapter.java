package com.cxf.adapter;

import com.cxf.safe_android.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;

	public ImageAdapter(Context context) {
		this.context = context;
	}

	private Integer[] images = {
			// 九宫格图片的设置
			R.drawable.item_logo, R.drawable.item_logo, };

	private String[] texts = {
			// 九宫格图片下方文字的设置
			"切换用户", "退出" };

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ImgTextWrapper wrapper;
		if (view == null) {
			wrapper = new ImgTextWrapper();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.tab_item_view, null);
			view.setTag(wrapper);
			view.setPadding(15, 15, 15, 15); // 每格的间距
		} else {
			wrapper = (ImgTextWrapper) view.getTag();
		}

		wrapper.imageView = (ImageView) view.findViewById(R.id.imageview);
		wrapper.imageView.setBackgroundResource(images[position]);
		LayoutParams para;
		para = wrapper.imageView.getLayoutParams();
		para.height = 50;
		para.width = 50;
		wrapper.imageView.setLayoutParams(para);
		wrapper.textView = (TextView) view.findViewById(R.id.textview);
		wrapper.textView.setText(texts[position]);
		wrapper.textView.setTextColor(Color.BLACK);
		wrapper.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

		return view;
	}
}

class ImgTextWrapper {
	ImageView imageView;
	TextView textView;

}
