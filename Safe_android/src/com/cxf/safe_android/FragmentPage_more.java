package com.cxf.safe_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.cxf.adapter.ImageAdapter;

public class FragmentPage_more extends Fragment implements OnItemClickListener {
	GridView gridview;
	ImageAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more, null);
		this.adapter = new ImageAdapter(getActivity());
		this.gridview = (GridView) view.findViewById(R.id.more_grid);

		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
		// TODO Auto-generated method stub
		switch (idx) {
		case 0:
			getActivity().startActivity(
					new Intent(getActivity(), LoginActivity.class));// 启动另一个Activity
			break;
		case 1:
			System.exit(0);
			break;
		default:
			
			break;
		}
	}
}