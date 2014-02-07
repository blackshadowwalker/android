package com.cxf.view;

import com.cxf.PhoneIdentification.R;
import com.cxf.adapter.AutoCompleteAdapter;
import com.cxf.net.handler.DisplayUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class AdvancedAutoCompleteTextView extends RelativeLayout {

	private Context context;
	private MyAutoCompleteTextView tv;
	String hint = "";

	public AdvancedAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.autoview);
		hint = arr.getString(R.styleable.autoview_my_hint);
		arr.recycle();
		initViews();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	private void initViews() {
		final float scale = context.getResources().getDisplayMetrics().density;
		int height = (int) (35 * scale + 0.5f);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		tv = new MyAutoCompleteTextView(context);
		tv.setBackgroundResource(R.drawable.edit_bg);
		tv.setPadding((int) (10 * scale + 0.5f), 0, 40, 0);
		tv.setTextSize(12);
		tv.setMaxEms(11);
		tv.setSingleLine();
		tv.setLayoutParams(params);
		setHint();

		int w=DisplayUtil.dip2px(context, 45);
		int h=DisplayUtil.dip2px(context, 20);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, h);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		p.rightMargin = 12;
		
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(p);
		iv.setPadding(0, DisplayUtil.dip2px(context, 2), 0, DisplayUtil.dip2px(context, 2));
		iv.setScaleType(ScaleType.FIT_CENTER);
		iv.setImageResource(R.drawable.delete);
		iv.setClickable(true);
		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv.setText("");
			}
		});
		RelativeLayout.LayoutParams parent = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, height);
		this.setLayoutParams(parent);
		this.setPadding(0, 0, 0, 0);
		this.addView(tv, params);
		this.addView(iv, p);

	}

	public void setAdapter(AutoCompleteAdapter adapter) {
		tv.setAdapter(adapter);
	}

	public void setThreshold(int threshold) {
		tv.setThreshold(threshold);
	}

	public AutoCompleteTextView getAutoCompleteTextView() {
		return tv;
	}

	public void setText(String s) {
		tv.setText(s);
	}

	public String getText() {
		String s = tv.getText().toString();
		return s;
	}

	public void addTextChangedListener(TextWatcher tw) {
		tv.addTextChangedListener(tw);

	}

	public void setSelection(int index) {
		tv.setSelection(index);
	}

	public void setHint() {
		tv.setHint(hint);
	}

	public void showDropDown() {
		tv.showDropDown();
	}
}
