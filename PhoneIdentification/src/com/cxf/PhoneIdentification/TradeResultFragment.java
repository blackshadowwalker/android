package com.cxf.PhoneIdentification;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TradeResultFragment extends Fragment implements Callback{
	private static final String TAG = "TradeResultFragment";
//Button centerButton;
    public static TradeResultFragment newInstance() {
    	TradeResultFragment newFragment = new TradeResultFragment();
        return newFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_result, container, false);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}

}
