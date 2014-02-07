package com.cxf.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cxf.PhoneIdentification.R;

public class MyProgressDialog extends DialogFragment implements OnKeyListener
{
	public static FragmentManager fm;
	private static Callback callback;
	public static final int OK = 41;
	public static final int CANCEL = -41;
	public static final int DISMISS = -42;
	Dialog dialog;
	View view;
	private int mBackStackId = 0;;
	static String msg = null;

	public static interface DialogClickListener {
		public void doPositiveClick();

		public void doNegativeClick();
	}

	public static MyProgressDialog newInstance(FragmentManager fm, Callback cb,
			String m) {
		MyProgressDialog frag = new MyProgressDialog();
		MyProgressDialog.fm = fm;
		callback = cb;
		msg = m;
		frag.setRetainInstance(true);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (dialog == null) {
			dialog = new Dialog(getActivity());
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.dialog_progress, null, false);
			TextView text = (TextView) view.findViewById(R.id.progress_prompt);
			text.setText(msg);
			dialog.setContentView(view);
			dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
			dialog.setOnKeyListener(this);
		}

		return dialog;
	}

	public void setPromptMessage(String prompt) {
		Dialog view = getDialog();
		if (view != null) {
			TextView msg = (TextView) view.findViewById(R.id.progress_prompt);
			msg.setText(prompt);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (callback != null) {
			Message msg = new Message();
			msg.what = DISMISS;
			callback.handleMessage(msg);
		}
		super.onDismiss(dialog);
	}

	public int show(FragmentTransaction transaction, String tag) {
		return show(transaction, tag, false);
	}

	public int show(FragmentTransaction transaction, String tag,
			boolean allowStateLoss) {
		transaction.add(this, tag);
		mBackStackId = allowStateLoss ? transaction.commitAllowingStateLoss()
				: transaction.commit();
		return mBackStackId;
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

			return true;
		}
		return false;
	}

}
