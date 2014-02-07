package com.cxf.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.widget.ListView;

import com.cxf.PhoneIdentification.R;
import com.cxf.adapter.OptionsAdapter;
import com.cxf.entity.Option;

public class MoreDialog extends AlertDialog implements Callback {
	ListView listview;
	OptionsAdapter adapter;
	Context context;
	Callback handler;
	List<Option> list = new ArrayList<Option>();
	FragmentManager fm;

	public MoreDialog(Context context, Callback handler, int theme,
			FragmentManager fm) {
		super(context, theme);
		this.context = context;
		this.handler = handler;
		this.fm = fm;
	}

	public MoreDialog(Context context, Callback handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_options);
		listview = (ListView) findViewById(R.id.list);
		adapter = new OptionsAdapter(context, this, fm);
		listview.setAdapter(adapter);
		this.setCancelable(true);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == OptionsAdapter.SHUTDOWN_PAREBT) {
			this.dismiss();
		}
		return false;
	}

}
